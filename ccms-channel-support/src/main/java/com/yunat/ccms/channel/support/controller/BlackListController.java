package com.yunat.ccms.channel.support.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yunat.ccms.channel.support.BlackListUploadSupport;
import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.domain.BlackList;
import com.yunat.ccms.channel.support.service.BlackListService;
import com.yunat.ccms.core.support.vo.PagedResultVo;
import com.yunat.ccms.core.support.vo.SimpleResponseVO;

/**
 * 黑名单前端服务层Controller
 * 
 * @author kevin.jiang 2013-4-24
 */
@Controller
@RequestMapping(value = "/channel/blacklist")
public class BlackListController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	BlackListService blackListService;

	@Autowired
	BlackListUploadSupport blackListUploadSupport;

	@RequestMapping(value = "/{type}/exists", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Boolean load(@PathVariable String type, @RequestParam String value) {

		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(value)) {
			return null;
		}

		BlackList blackList = blackListService.loadBlackList(value, EnumBlackList.valueOf(type));
		return (blackList != null);
	}

	@RequestMapping(value = "/{qtype}/page", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public PagedResultVo<BlackList> pageList(@PathVariable String qtype, @RequestParam int page, @RequestParam int rp,
			@RequestParam String query, @RequestParam String sortname, @RequestParam String sortorder) {

		if (StringUtils.isEmpty(qtype)) {
			return null;
		}

		Page<BlackList> rltPage = blackListService.getBlackListAll(page, rp, query, EnumBlackList.valueOf(qtype),
				sortname, sortorder);
		PagedResultVo<BlackList> prv = new PagedResultVo<BlackList>();
		prv.setData(rltPage.getContent());
		prv.setPage(page);
		prv.setTotal(rltPage.getTotalElements());
		return prv;
	}

	@RequestMapping(value = "/{type}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public SimpleResponseVO create(@PathVariable String type, @RequestBody List<String> values) {

		if (type == null || "".equals(type.trim())) {

			return new SimpleResponseVO(false, "404", "没有传参数type，或者参数type无效");
		}

		if (values == null || values.size() <= 0) {

			return new SimpleResponseVO(false, "404", "没有传参数values，或者参数values没有元素");
		}

		try {

			blackListService.createBlackList(values, EnumBlackList.valueOf(type));
			return new SimpleResponseVO(true, "", "");

		} catch (Exception e) {

			e.printStackTrace();
			return new SimpleResponseVO(false, "404", "程序出现异常：" + e.getMessage());
		}
	}

	/**
	 * 删除功能是传入一组值，删除一个或者多个，限列表一页范围之内
	 */
	@RequestMapping(value = "/{type}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
	@ResponseBody
	public SimpleResponseVO delete(@PathVariable String type, @RequestBody List<String> values) {

		if (type == null || "".equals(type.trim())) {

			return new SimpleResponseVO(false, "404", "没有传参数type，或者参数type无效");
		}

		if (values == null || values.size() <= 0) {

			return new SimpleResponseVO(false, "404", "没有传参数values，或者参数values没有元素");
		}

		try {

			blackListService.deleteBlackList(values, EnumBlackList.valueOf(type));
			return new SimpleResponseVO(true, "", "");

		} catch (Exception e) {

			e.printStackTrace();
			return new SimpleResponseVO(false, "404", "程序出现异常：" + e.getMessage());
		}
	}

	@RequestMapping(value = "/{type}/upload", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public SimpleResponseVO upload(@PathVariable String type, @RequestParam("file") CommonsMultipartFile mFile) {

		String uploadUrl = null;
		if (!mFile.isEmpty()) {
			String uploadPath = blackListUploadSupport.getUploadPath();
			String path = com.yunat.ccms.core.support.io.FileUtils.concat(uploadPath, type);
			// 获取本地存储路径
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();

			Long newId = blackListService.getNewId();
			String fileName = "uploadFile" + newId + ".txt";
			uploadUrl = com.yunat.ccms.core.support.io.FileUtils.concat(path, fileName);
			logger.info("upload path:" + uploadUrl);

			File file1 = new File(uploadUrl); // 新建一个文件
			try {
				mFile.getFileItem().write(file1); // 将上传的文件写入新建的文件中

			} catch (Exception e) {
				e.printStackTrace();
				return new SimpleResponseVO(false, "", "上传数据失败:" + e.getMessage());
			}
		}

		// 上传并返回被判断为重复的数据
		StringBuilder builder = new StringBuilder();
		if (uploadUrl != null) {

			try {
				List<Map<String, Object>> list = blackListService.uploadBlackList(uploadUrl,
						EnumBlackList.valueOf(type));

				if (list != null && list.size() > 0) {

					for (Map<String, Object> map : list) {

						String value = (String) map.get(map.keySet().iterator().next());
						builder.append(value);
						if (list.indexOf(map) >= 0 && list.indexOf(map) < (list.size() - 1)) {
							builder.append(",");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new SimpleResponseVO(false, "", "导入数据失败:" + e.getMessage());
			}
		}

		return new SimpleResponseVO(true, "", builder.toString());
	}

	@RequestMapping(value = "/{type}/download", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	@ResponseBody
	public ResponseEntity<byte[]> download(@PathVariable String type, @RequestParam String filterValue)
			throws IOException {

		if (type == null) {

			return null;
		}
		List<BlackList> listRecords = blackListService.getBlackListAll(filterValue, EnumBlackList.valueOf(type));
		Map<String, String> map = blackListUploadSupport.Download(listRecords, EnumBlackList.valueOf(type));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", map.get("fileName"));
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(map.get("filePath"))), headers,
				HttpStatus.CREATED);
	}
}
