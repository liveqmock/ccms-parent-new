package com.yunat.ccms.tradecenter.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.taobao.api.FileItem;
import com.taobao.api.domain.PicUrl;
import com.taobao.api.domain.RefundMessage;
import com.taobao.api.request.RefundMessageAddRequest;
import com.taobao.api.response.RefundMessageAddResponse;
import com.taobao.api.response.RefundMessagesGetResponse;
import com.yunat.base.response.BaseResponse;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.vo.ControlerResult;
import com.yunat.ccms.tradecenter.controller.vo.CaringRequest;
import com.yunat.ccms.tradecenter.controller.vo.RefundHistoryVO;
import com.yunat.ccms.tradecenter.controller.vo.RefundRequest;
import com.yunat.ccms.tradecenter.controller.vo.RefundVO;
import com.yunat.ccms.tradecenter.domain.RefundProofFileDomain;
import com.yunat.ccms.tradecenter.domain.RefundTopContentDomain;
import com.yunat.ccms.tradecenter.service.OrderInteractionService;
import com.yunat.ccms.tradecenter.service.RefundService;
import com.yunat.ccms.tradecenter.service.ShopReasonService;
import com.yunat.ccms.tradecenter.service.TaobaoService;
import com.yunat.ccms.tradecenter.service.queryobject.RefundQuery;
import com.yunat.ccms.tradecenter.support.cons.UserInteractionType;
import com.yunat.ccms.tradecenter.support.taobaoapi.TaobaoRefundManager;

/**
 * 退款事务
 *
 * @author ming.peng
 * @date 2013-7-15
 * @since 4.2.0
 */
@Controller
@RequestMapping(value = "/customerCenter/refund/*")
public class RefundController {

	private static Logger logger = LoggerFactory.getLogger(RefundController.class);

	@Autowired
	private OrderInteractionService ordIntService;

	@Autowired
	private TaobaoService taobaoService;

	@Autowired
	private RefundService refundService;

    @Autowired
    private ShopReasonService shopReasonService;

    @Autowired
	private AppPropertiesQuery appPropertiesQuery;

    @Autowired
    private TaobaoRefundManager taobaoRefundManager;


	/**
	 * 退款凭证调用淘宝接口
	 *
	 * @param reqs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refundProof", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult refundProof(@RequestBody RefundRequest reqs, HttpServletRequest request) {
		if (ObjectUtils.equals(reqs.getFilePath(), null) && reqs.getFilePath().length == 0) {
			return ControlerResult.newError("未选择上传文件！");
		}
		int count = 0;
		for (int i = 0; i < reqs.getFilePath().length; i++) {
			String path = reqs.getFilePath()[i];
			RefundMessageAddRequest req = new RefundMessageAddRequest();
			req.setRefundId(reqs.getRefundId());
			req.setContent(i == 0 ? reqs.getContent() : "  ");
			req.setContent(reqs.getContent());
			FileItem fItem = new FileItem(new File(request.getSession().getServletContext().getRealPath(path)));
//			TODO
			logger.info("淘宝调用凭证上传：path-"+ path+ " 替换后地址-"+ request.getSession().getServletContext().getRealPath(path));
			req.setImage(fItem);
			try {
				RefundMessageAddResponse response = taobaoService.execTaobao(reqs.getDpId(), req);
				if (ObjectUtils.notEqual(response, null) && response.isSuccess()) {
					count++;
				}
			} catch (Exception e) {
				logger.error("RefundId: "+ reqs.getRefundId()+ " 凭证上传失败：", e);
				return ControlerResult.newError("上传失败！");
			}
		}
		return count > 0 ? ControlerResult.newSuccess("凭证上传成功：" + count + "张, 失败：" + (reqs.getFilePath().length - count)
				+ "张!") : ControlerResult.newError("凭证上传失败！");
//		return ControlerResult.newSuccess("上传成功!");
	}

	/**
	 * 上传文件 根据是否是临时来做处理， 是临时文件， 就存放到临时目录，返回文件地址， 如果是作为常用凭证文件， 就存放数据库， 返回当前文件的地址。
	 *
	 * @param myfiles
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ControlerResult uploadFile(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile myfile = multiRequest.getFile("proofFile");
		if (myfile.isEmpty()) {
			return ControlerResult.newError("文件未上传！");
		}
		RefundRequest reqs = new RefundRequest();
		reqs.setDpId(ObjectUtils.toString(request.getParameter("dpId"), "0"));
		reqs.setTemp(Boolean.parseBoolean(request.getParameter("temp")));

		//TODO /uploadimg/temp/dpid/  or   /uploadimg/dpid/
//		String path = (reqs.getTemp() ? File.separator+ "uploadimg"+ File.separator+ "temp"+ File.separator + reqs.getDpId() + File.separator
//				: File.separator+ "uploadimg"+ File.separator) + reqs.getDpId() + File.separator;
//		String realPath = request.getSession().getServletContext().getRealPath(path) + File.separator;

		String realPath = reqs.getTemp() ? appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.KFZX_UPLOAD_PATH)+ File.separator + "temp"+ File.separator + reqs.getDpId() + File.separator
				: appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.KFZX_UPLOAD_PATH)+ File.separator  + reqs.getDpId() + File.separator;
		String dirname = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd");
		realPath = realPath + dirname+ File.separator;
		String pathName = realPath + myfile.getOriginalFilename();

		if(new File(pathName).exists()){
			return ControlerResult.newError("已有同名凭证，请修改上传凭证名称！");
		}
		// 写出文件
		FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath, myfile.getOriginalFilename()));
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("fileName", myfile.getOriginalFilename());
		//特别注意此处！！！  将绝对路径修改为相对路径，进行db的保存和返回给前端，因为做了软连接。如果不替换，则前端用相对路访问时，访问不到。。。
		rtn.put("path", pathName.replace(appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.KFZX_UPLOAD_PATH), "/uploadimg"));
		logger.info("上传凭证实际保存路径： "+ pathName+ "-----返回前台path："+rtn.get("path"));
		if (reqs.getTemp()) {
			// 删除不是当日的文件
			File file = new File(realPath);
			for (File dir : file.listFiles()) {
				if (!dir.getName().equals(dirname)) {
					FileUtils.deleteQuietly(dir);
				}
			}
			rtn.put("id", "");
		} else {
			// 将凭证存放数据库
			RefundProofFileDomain rd = refundService.saveRefundProofFileDetail(reqs, pathName, myfile.getOriginalFilename());
			rtn.put("id", rd.getPkid());
		}
		return ControlerResult.newSuccess(rtn);
	}

	/**
	 * 上传文件 根据是否是临时来做处理， 是临时文件， 就存放到临时目录，返回文件地址， 如果是作为常用凭证文件， 就存放数据库， 返回当前文件的地址。
	 *
	 * @param myfiles
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadFile1", method = RequestMethod.POST)
	public ControlerResult uploadFile1(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile myfile = multiRequest.getFile("proofFile");
		if (myfile.isEmpty()) {
			return ControlerResult.newError("文件未上传！");
		}
		RefundRequest reqs = new RefundRequest();
		reqs.setDpId(ObjectUtils.toString(request.getParameter("dpId"), "0"));
		reqs.setTemp(Boolean.parseBoolean(request.getParameter("temp")));

		//TODO
		String path = (reqs.getTemp() ? File.separator+ "uploadimg"+ File.separator+ "temp"+ File.separator : File.separator+ "uploadimg"+ File.separator) + reqs.getDpId() + File.separator;
		String realPath = request.getSession().getServletContext().getRealPath(path) + File.separator;

		String dirname = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd");
		// 数据库存放文件path ， 及访问路径
		//TODO
//		String pathName = path + dirname + "/" + myfile.getOriginalFilename();
		String pathName = path + dirname + File.separator + myfile.getOriginalFilename();

		// 写出文件
		FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(realPath + dirname, myfile.getOriginalFilename()));
		Map<String, Object> rtn = new HashMap<String, Object>();
		rtn.put("fileName", myfile.getOriginalFilename());
		rtn.put("pic", myfile);
		if (reqs.getTemp()) {
			// 删除不是当日的文件
			File file = new File(realPath);
			for (File dir : file.listFiles()) {
				if (!dir.getName().equals(dirname)) {
					FileUtils.deleteQuietly(dir);
				}
			}
			rtn.put("id", "");
		} else {
			// 将凭证存放数据库
			RefundProofFileDomain rd = refundService.saveRefundProofFileDetail(reqs, pathName, myfile.getOriginalFilename());
			rtn.put("id", rd.getPkid());
		}
		return ControlerResult.newSuccess(rtn);
	}

	/**
	 * 得到常用凭证文件路径 返回数据库中所有常用凭证信息， ID、 filename 、path
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/proofFilePaths", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult proofFilePaths(@RequestBody RefundRequest reqs) {
		List<Map<String, Object>> pds = refundService.findProofDetail(reqs.getDpId());
		for(Map<String, Object> pd : pds){
			String fileName = pd.get("path").toString();
			pd.put("path", fileName.replace(appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.KFZX_UPLOAD_PATH), "/uploadimg"));
			//TODO
			logger.info("获取常用凭证地址(替换后)："+ pd.get("path"));
		}
		return ControlerResult.newSuccess(pds);
	}

	/**
	 * 删除常用凭证文件
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/proofFileDel", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult proofFileDel(@RequestBody RefundRequest reqs, HttpServletRequest request) {
		// 根据凭证文件Id 来删除凭证文件
		String path = refundService.proofFileDel(reqs.getProofId());
		logger.info("删除凭证: "+ path);
		if (StringUtils.isNotEmpty(path)) {
			// 删除对应的文件
			FileUtils.deleteQuietly(new File(path));
		}
		return StringUtils.isNotEmpty(path) ? ControlerResult.newSuccess("删除成功！") : ControlerResult.newError("删除失败！");
	}



	/**
	 * 删除常用话术 根据传值ID
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/topDel", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult topDel(@RequestBody RefundRequest reqs) {
		refundService.delTopContent(reqs.getTopId());
		return ControlerResult.newSuccess("删除成功！");
	}


	/**
	 * 常用话术 根据传值ID，是否存在， 存在就修改， 否则就添加
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/topContent", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult topContent(@RequestBody RefundRequest reqs) {
		RefundTopContentDomain domain = refundService.updateTopContent(reqs);
		return ObjectUtils.notEqual(domain, null) ? ControlerResult.newSuccess("修改成功！", domain.getPkid()) : ControlerResult
				.newError("修改失败！");
	}

	/**
	 * 常用话术 根据传值ID，是否存在， 存在就修改， 否则就添加
	 * TODO 若单个用户话术量过大，此部分需要优化，可以一次全取出，然后在内存处理，但是同步是个问题
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/topContents", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult topContents(@RequestBody List<Map<String , Object>> reqs) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
		for(Map<String , Object> r: reqs){
			try {
				RefundTopContentDomain domain = refundService.updateTopContent(r);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", domain.getPkid());
				m.put("content", domain.getContent());
				rtnList.add(m);
			} catch (Exception e) {
				logger.error("topContents保存失败： ", e);
				return ControlerResult.newError("保存失败，请重试！");
			}
		}
		return ControlerResult.newSuccess(rtnList);
	}

	/**
	 * 常用话术列表 返回所有的话术列表
	 *
	 * @param reqs
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/topContentList", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult topContentList(@RequestBody RefundRequest reqs) {
		return ControlerResult.newSuccess(refundService.findTopContentList(reqs.getDpId()));
	}

	/**
	 * 退款 单或批量 订单关怀
	 *
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refundOrdersCare", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json; charset=utf-8")
	public ControlerResult refundOrdersCare(@RequestBody CaringRequest req) {
		// 短信关怀
		Integer type = Integer.parseInt(req.getCaringType());
		if (UserInteractionType.MANUAL_REFUND_SMS_CARE.getType() == type) {
			if (StringUtils.isEmpty(req.getContent())) {
				return ControlerResult.newError("短信内容不能为空!");
			}
			BaseResponse<String> res = ordIntService.refundOrdersSMSCare(req, type);
			if (!res.isSuccess()) {
				return ControlerResult.newError(res.getErrMsg());
			}
		}
		// 旺旺或电话关怀
		if (UserInteractionType.MANUAL_REFUND_WW_CARE.getType() == type
				|| UserInteractionType.MANUAL_REFUND_MOBILE_CARE.getType() == type) {
			if (StringUtils.isEmpty(req.getContent())) {
				return ControlerResult.newError("关怀备注不能为空!");
			}
			ordIntService.refundOrdersCare(req, type);
		}
		return ControlerResult.newSuccess();
	}

	/**
	 * 退款事务
	 *
	 * @param refundQuery
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refundList", method = { RequestMethod.GET, RequestMethod.POST })
	public ControlerResult refundList(@RequestBody RefundQuery refundQuery) {

		List<RefundVO> refundVOs = refundService.findWorkRefunds(refundQuery);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", refundQuery.getTotalItem());
		resultMap.put("page", refundQuery.getCurrentPage());
		resultMap.put("totalPage", refundQuery.getTotalPage());
		resultMap.put("pageSize", refundQuery.getPageSize());
		resultMap.put("data", refundVOs);
		resultMap.put("statusStatistics", refundService.statisticsDealWithStatus(refundQuery.getDpId()));
		return ControlerResult.newSuccess(resultMap);
	}

    /**
     * 退款事务
     *
     * @param refundRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refundReasons", method = { RequestMethod.GET, RequestMethod.POST })
    public ControlerResult refundReasons(@RequestBody RefundRequest refundRequest) {

        List<String> reasons = shopReasonService.getRefundReasons(refundRequest.getDpId());
        return ControlerResult.newSuccess(reasons);
    }

    /**
     * 凭证明细查询
     *
     * @param refundRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ControlerResult refundProofHistory(Long refund_id, Long page_no, String shopId) {
    	if(page_no == null){
    		page_no = 1L;
    	}
    	RefundMessagesGetResponse rmgr = taobaoRefundManager.getRefundMessages(refund_id, page_no, shopId);

    	if(rmgr.isSuccess()){
    		RefundHistoryVO rhv = new RefundHistoryVO();
    		rhv.setAll(40 > rmgr.getTotalResults());
    		rhv.setPageNo(page_no);
    		List<Map<String, Object>> refundMessages = new ArrayList<Map<String, Object>>();

    		for(RefundMessage rm : rmgr.getRefundMessages()){
//    			created,owner_role(留言者身份),owner_nick,content,pic_urls(PicUrl []),message_type
    			Map<String, Object> refundMessage = new HashMap<String, Object>();
    			refundMessage.put("created", rm.getCreated());
    			refundMessage.put("owner_role", rm.getOwnerRole());
    			refundMessage.put("owner_nick", rm.getOwnerNick());
    			refundMessage.put("content", rm.getContent());

    			List<String> picUrls = new ArrayList<String>();
    			for(PicUrl pu : rm.getPicUrls()){
    				picUrls.add(pu.getUrl());
    			}
    			refundMessage.put("pic_urls", picUrls);
    			refundMessage.put("message_type", rm.getMessageType());
    			refundMessages.add(refundMessage);
    		}
			rhv.setRefundMessages(refundMessages);

    		return ControlerResult.newSuccess(rhv);
    	}else{
    		return ControlerResult.newError("淘宝接口获取信息失败，请重试");
    	}

    }

}
