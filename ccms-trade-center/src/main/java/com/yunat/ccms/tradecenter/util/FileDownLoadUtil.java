package com.yunat.ccms.tradecenter.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件下载帮助工具
 *
 * @author ming.peng
 * @date 2013-7-2
 * @since 4.2.0
 */
public class FileDownLoadUtil {


	/**
	 * 刷新和关闭流
	 * @param resp
	 * @param filename
	 * @param sb
	 * @throws IOException
	 */
	public static void flushClose(PrintWriter writer) {
		writer.flush();
		writer.close();
	}


	/**
	 * 将内容写出去
	 * @param resp
	 * @param filename
	 * @param sb
	 * @throws IOException
	 */
	public static void writer(PrintWriter writer, String content) throws IOException {
		writer.print(content);
	}


	/**
	 * 将文件写出去
	 * @param resp
	 * @param filename
	 * @param sb
	 * @throws IOException
	 */
	public static void writer(HttpServletResponse resp, String content) throws IOException {
		resp.getWriter().print(content);
	}

	/**
	 * 将文件写出去
	 * @param resp
	 * @param filename
	 * @param sb
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writer(HttpServletResponse resp, String filename, String content) throws UnsupportedEncodingException, IOException {
		setHeader(resp, filename);
		writer(resp, content);
	}


	/**
	 * 设置写出文件头
	 * @param resp
	 * @param filename
	 * @throws UnsupportedEncodingException
	 */
	public static void setHeader(HttpServletResponse resp, String filename) throws UnsupportedEncodingException {
		resp.setContentType("application/octet-stream;charset=gb2312");
		resp.addHeader("Content-Disposition", "attachment; filename=\"" + new String(filename.getBytes("GBK"), "ISO8859-1") + ".csv\"");
	}


}
