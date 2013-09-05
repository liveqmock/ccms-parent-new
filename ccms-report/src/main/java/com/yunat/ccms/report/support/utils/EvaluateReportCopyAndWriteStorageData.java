package com.yunat.ccms.report.support.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.yunat.ccms.node.biz.evaluate.NodeEvaluateParamObject;
import com.yunat.ccms.report.support.cons.ReportFileSpec;

@Component
@Scope(value = "prototype")
public class EvaluateReportCopyAndWriteStorageData {

	private static Logger logger = LoggerFactory.getLogger(CopyAndWriteStorageData.class);
	private static final int NORMAL_LOOP = 5000;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void generate(String filename, StringBuilder sql, NodeEvaluateParamObject nodeEvaluateParamObject,
			List<String> columnHeaders) {

		generateCsvFile(filename, sql, nodeEvaluateParamObject, columnHeaders);
	}

	private static PrintWriter initPrintWriter(String filename) {
		File file = new File(filename);
		if (file.exists()) {
			file.delete();
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file, "GBK");
		} catch (Exception e) {
			logger.info("应用程序发生IO异常 : [{}]", e.getMessage());
		}
		return writer;
	}

	private static void closePrintWriter(PrintWriter writer) {
		writer.flush();
		writer.close();
	}

	private void generateCsvFile(final String filename, final StringBuilder sql,
			final NodeEvaluateParamObject nodeEvaluateParamObject, final List<String> columnHeaders) {

		PrintWriter writer = initPrintWriter(filename);
		if (null != writer) {
			// 写文件头
			writer.println(Joiner.on(",").join(columnHeaders.iterator()));
			// 写文件内容
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("startDate", nodeEvaluateParamObject.getStartDate());
			paramMap.put("endDate", nodeEvaluateParamObject.getEndDate());
			paramMap.put("shopId", nodeEvaluateParamObject.getShopId());

			List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql.toString(), paramMap);
			int total = list.size();

			int resultNum = 0;
			for (int i = 0; i <= total; i++) {
				int start = i * NORMAL_LOOP;
				int end = NORMAL_LOOP;
				String limlitSql = sql.append(" limit " + start + " , " + end + " ").toString();

				List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(limlitSql, paramMap);
				printCsvResults(results, writer);
				resultNum += results.size();
				if (resultNum == total) {
					break;
				}
			}
			// 关闭写文件
			closePrintWriter(writer);
		}
	}

	public void compressToZip(String filename) {
		ZipOutputStream out = null;
		String zipFilename = filename.substring(0, filename.lastIndexOf(".")) + "."
				+ ReportFileSpec.Extension.ZIP.toString().toLowerCase();
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFilename));
			InputStream in = null;
			File file = new File(filename);
			out.putNextEntry(new ZipEntry(file.getName()));
			in = new FileInputStream(file);
			byte[] b = new byte[8192];
			int len = -1;
			while ((len = in.read(b)) > -1) {
				out.write(b, 0, len);
			}
			in.close();
			file.delete();
			out.closeEntry();
		} catch (Exception e) {
			logger.info("压缩文件异常 : [{}]", e.getMessage());
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void printCsvResults(List<Map<String, Object>> results, PrintWriter writer) {
		for (Map<String, Object> result : results) {
			StringBuilder resultsStringBuilder = new StringBuilder();
			Set<Entry<String, Object>> entrys = result.entrySet();
			for (Entry<String, Object> entry : entrys) {
				Object value = entry.getValue();
				resultsStringBuilder.append("\"");
				if (value != null) {
					resultsStringBuilder.append(value);
				}
				resultsStringBuilder.append("\",");
			}
			resultsStringBuilder.deleteCharAt(resultsStringBuilder.length() - 1);
			writer.println(resultsStringBuilder);
		}
	}
}
