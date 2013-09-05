package com.yunat.ccms.report.support.template;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorFileTemplate {
	private static Logger logger = LoggerFactory.getLogger(GeneratorFileTemplate.class);

	public static void run(String filename, StringBuilder sql, List<String> columnHeaders,
			GeneratorFileCallback callback) throws Exception {
		// 初始化写文件
		PrintWriter writer = initPrintWriter(filename);
		if (null != writer) {
			// 写文件头
			callback.writeFileHeader(columnHeaders, writer);

			// 写文件内容
			callback.writeFileContent(sql, writer);
			
			// 关闭写文件
			closePrintWriter(writer);
		}
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

}