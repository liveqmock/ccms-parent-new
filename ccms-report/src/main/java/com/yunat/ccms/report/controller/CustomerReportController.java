package com.yunat.ccms.report.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.yunat.ccms.report.support.cons.ReportFileLocator;
import com.yunat.ccms.report.support.cons.ReportFileSpec;

/**
 * ©Copyright：yunat Project：ccms-report Module ID：基础节点 Comments：客户组下载
 * used：<JDK1.6>
 * 
 * Author：yinwei Create Date： 2013-4-10 Version:1.0
 * 
 * Modified By： Modified Date： Why & What is modified： Version：
 */

@Controller
public class CustomerReportController {

	private static Logger logger = LoggerFactory.getLogger(CustomerReportController.class);

	private static final String[] columnNames = { "full_name", "sex", "birthday", "state", "city", "vip_info",
			"customerno", "mobile", "email", "buyer_credit_lev", "buyer_good_ratio", "address" };

	/**
	 * java regex用的手机号码的正则表达式的验证
	 */
	public static final String JAVA_REGEX_MOBILE_VALIDATA = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9]){1}\\d{8}$";

	/**
	 * 邮件验证
	 */
	public static final String JAVA_REGEX_EMAIL_VALIDATA = "^[a-zA-Z0-9_\\.\\-]+\\@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]{2,4}$";

	@Autowired
	private NodeJobService nodeJobService;

	@Autowired
	private ReportFileLocator reportFileLocator;

	// 目标组客户数据列表展现
	@ResponseBody
	@RequestMapping(value = "/node/target/report/{nodeId}", method = RequestMethod.GET)
	public List<Map<String, Object>> showCustomerList(@PathVariable Long nodeId, @RequestParam Long campId,
			@RequestParam Long jobId) throws Exception {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {
			Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
			String filename = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.CUSTOMER,
					ReportFileSpec.Extension.CSV);
			File file = new File(filename);
			final BufferedReader reader;
			if (file.exists()) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
				readIntoList(reader, file, dataList, columnNames);
			}
			this.convertDataList(dataList);
		} catch (Exception e) {
			throw new Exception("预览客户数据出现异常");
		}
		return dataList;
	}

	// XXX 这里应该用个匿名内部类实现,类似rowMapper
	private List<Map<String, Object>> convertDataList(List<Map<String, Object>> dataList) {

		for (Map<String, Object> map : dataList) {
			map.put("buyer_good_ratio",
					map.get("buyer_good_ratio") != null ? (String.valueOf(map.get("buyer_good_ratio")) + "%") : null);

			String newMobile = "";
			Object mobile = map.get("mobile");
			if (mobile instanceof String && !StringUtils.isEmpty((String) mobile)) {

				// 还要判断手机号码是不是符合规格
				if (((String) mobile).matches(JAVA_REGEX_MOBILE_VALIDATA)) {
					newMobile = ((String) map.get("mobile")).substring(0, 3) + "****"
							+ ((String) map.get("mobile")).substring(7);
				}

			}
			map.put("mobile", newMobile);

			String newEmail = "";
			Object email = map.get("email");
			if (email instanceof String && !StringUtils.isEmpty((String) email)) {

				if (((String) email).matches(JAVA_REGEX_EMAIL_VALIDATA)) {
					String[] es = map.get("email").toString().split("@");
					if (es.length == 2) {
						newEmail = es[0].substring(0, 1) + "****@" + es[1];
					}
				}

			}

			map.put("email", newEmail);
		}

		return dataList;
	}

	private void readIntoList(BufferedReader reader, File csv, List<Map<String, Object>> targetCustomers,
			String[] columnNames) throws IOException {

		// 跳过标题
		reader.readLine();
		String line = reader.readLine();
		int columnNum = columnNames.length;
		// 只显示100条
		for (int num = 0; num < 100 && line != null; num++, line = reader.readLine()) {
			while (!line.endsWith("\"")) {
				line = line + reader.readLine();
			}
			line = line.substring(1, line.length() - 1);
			String[] values = line.split("\",\"", -1);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < columnNum; i++) {
				// 获得去掉两边引号之后的数据
				String column = columnNames[i];
				if (column != null) {
					map.put(column, values[i]);
				} else {
					map.put("", values[i]);
				}
			}
			targetCustomers.add(map);
		}
	}

	// 目标组下载
	@RequestMapping(value = "/node/target/download/{nodeId}", method = RequestMethod.GET)
	public void downloadCustomerList(@PathVariable Long nodeId, @RequestParam Long campaignId,
			@RequestParam Long jobId, HttpServletResponse res) {
		Long subjobId = nodeJobService.getSubjobId(jobId, nodeId);
		String fileName = reportFileLocator.buildFilename(subjobId, ReportFileSpec.Category.CUSTOMER,
				ReportFileSpec.Extension.ZIP);
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
