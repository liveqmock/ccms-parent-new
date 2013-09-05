package com.yunat.ccms.report.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.node.support.service.NodeJobService;
import com.yunat.ccms.report.domain.EvaluateReportCustomerDetail;
import com.yunat.ccms.report.domain.EvaluateReportOrderDetail;
import com.yunat.ccms.report.domain.EvaluateReportProductDetail;
import com.yunat.ccms.report.service.EvaluateReportCorrespondDetailService;
import com.yunat.ccms.report.support.cons.ReportFileLocator;
import com.yunat.ccms.report.support.cons.ReportFileSpec;

/**
 * 评估报告对应明细及下载
 * 
 * @author yin
 * 
 */
@Controller
public class EvaluateReportCorrespondDetailController {

	private static Logger logger = LoggerFactory.getLogger(EvaluateReportCorrespondDetailController.class);

	@Autowired
	EvaluateReportCorrespondDetailService nodeEvaluateCorrespondDetailService;

	@Autowired
	private NodeJobService nodeJobService;

	@Autowired
	private ReportFileLocator reportFileLocator;

	/**
	 * 评估节点-客户明细
	 * 
	 * @param nodeId
	 *            评估节点ID
	 * @return
	 * @throws Exception
	 *             if get customer list fail
	 * 
	 */

	@RequestMapping(value = "/node/evaluate/{nodeId}/customer-detail", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluateReportCustomerDetail> getCustomerDetailList(@PathVariable Long nodeId,
			@RequestParam Long jobId, @RequestParam String evaluateTime) throws Exception {

		return nodeEvaluateCorrespondDetailService.getCustomerDetailList(jobId, nodeId, evaluateTime);

	}

	// 评估节点-客户明细下载
	@RequestMapping(value = "/node/evaluate/{nodeId}/download/customer-detail", method = RequestMethod.GET)
	public void downloadCustomerDetailList(@PathVariable Long nodeId, @RequestParam Long jobId, HttpServletResponse res) {
		Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
		String fileName = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.EVALUATE_REPORT_CUSTOMER,
				ReportFileSpec.Extension.CSV);
		String tmpTblName = fileName.substring(Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\')));
		tmpTblName = "tmpDownLoad" + tmpTblName.substring(tmpTblName.indexOf('_'));
		res.setContentType("application/octet-stream; charset=UTF-8");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + tmpTblName + "\"");
		try {
			File file = new File(fileName);
			logger.info(file.getAbsolutePath());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(res.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 评估节点-主订单明细
	 */

	@RequestMapping(value = "/node/evaluate/{nodeId}/order-detail", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluateReportOrderDetail> getOrderDetailList(@PathVariable Long nodeId, @RequestParam Long jobId,
			@RequestParam String evaluateTime) throws Exception {

		return nodeEvaluateCorrespondDetailService.getOrderDetailList(jobId, nodeId, evaluateTime);

	}

	// 评估节点-主订单下载
	@RequestMapping(value = "/node/evaluate/{nodeId}/download/order-detail", method = RequestMethod.GET)
	public void downloadOrderDetailList(@PathVariable Long nodeId, @RequestParam Long jobId, HttpServletResponse res) {
		Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
		String fileName = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.EVALUATE_REPORT_ORDER,
				ReportFileSpec.Extension.CSV);
		String tmpTblName = fileName.substring(Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\')));
		tmpTblName = "tmpDownLoad" + tmpTblName.substring(tmpTblName.indexOf('_'));
		res.setContentType("application/octet-stream; charset=UTF-8");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + tmpTblName + "\"");
		try {
			File file = new File(fileName);
			logger.info(file.getAbsolutePath());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(res.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 评估节点-子订单明细
	 */
	@RequestMapping(value = "/node/evaluate/{nodeId}/orderitem-detail", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getOrderItemDetailList(@PathVariable Long nodeId, @RequestParam String tid)
			throws Exception {

		return nodeEvaluateCorrespondDetailService.getOrderItemDetailList(tid);

	}

	/**
	 * 评估节点-商品明细
	 */

	@RequestMapping(value = "/node/evaluate/{nodeId}/product-detail", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluateReportProductDetail> getProductDetailList(@PathVariable Long nodeId, @RequestParam Long jobId,
			@RequestParam String evaluateTime) throws Exception {

		return nodeEvaluateCorrespondDetailService.getProductDetailList(jobId, nodeId, evaluateTime);

	}

	// 评估节点-商品明细下载
	@RequestMapping(value = "/node/evaluate/{nodeId}/download/product-detail", method = RequestMethod.GET)
	public void downloadProductDetailList(@PathVariable Long nodeId, @RequestParam Long jobId, HttpServletResponse res) {
		Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
		String fileName = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.EVALUATE_REPORT_PRODUCT,
				ReportFileSpec.Extension.CSV);
		String tmpTblName = fileName.substring(Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\')));
		tmpTblName = "tmpDownLoad" + tmpTblName.substring(tmpTblName.indexOf('_'));
		res.setContentType("application/octet-stream; charset=UTF-8");
		res.setHeader("Content-Disposition", "attachment;filename=\"" + tmpTblName + "\"");
		try {
			File file = new File(fileName);
			logger.info(file.getAbsolutePath());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(res.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
