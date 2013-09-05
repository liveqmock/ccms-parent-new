package com.yunat.ccms.core.support.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConvertCharset {

	/** 最开始的源文件 */
	private File srcFile;
	/** 最开始的目的地文件 */
	private File targetFile;
	/** 文件分隔符 */
	private final String separator = System.getProperties().getProperty(
			"file.separator");

	final ConvertEncoding convertEncoding = new ConvertEncoding();

	public static void main(String[] args) {
		ConvertCharset execute = new ConvertCharset();
		// 目录
		// String inFilename = "D:\\tomcat6\\webapps\\";
		File target = new File("D:\\", "shop_2.txt");
		String inFilename = target.getAbsolutePath(); // +target.getName();//"D:\\shop_2.txt";
		// 源文件为单个文件
		// String inFilename = "D:\\tomcat6\\webapps\\doc\\aio.html";
		String outFilename = "D:\\shop_4.txt";
		String encoding = "GBK";
		try {
			execute.executeConvertFile(inFilename, outFilename, encoding);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置包含的要进行处理的文件(扩展名)，以"|"进行分隔
	 * 如果没有设置，则执行默认包含的文本文件:txt、ini、java、jsp、jspa、htm、
	 * html、xml、js、vbs、css、properties、ftl、php、asp
	 *
	 * @param fileExtName
	 */
	public void setInclude(String fileExtName) {
		convertEncoding.setInclude(fileExtName);
	}

	public void executeConvertFile(String inFilename, String outFilename,
			String encoding) throws FileNotFoundException, IOException {
		srcFile = new File(inFilename);
		targetFile = new File(outFilename);
		if (!srcFile.exists()) {
			System.out.println("源文件不存在：" + srcFile.getAbsolutePath());
			return;
		}
		execute(inFilename, outFilename, encoding);
	}

	/**
	 * 具体执行文件转换的操作
	 *
	 * @param inFilename
	 * @param outFilename
	 * @param encoding
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void execute(String inFilename, String outFilename, String encoding)
			throws FileNotFoundException, IOException {
		File inFile = new File(inFilename);
		// 如果不是目录，即是标准文件
		if (!inFile.isDirectory()) {
			convertEncoding.convertEncoding(inFilename, outFilename, encoding); // 直接进行转换并保存
		} else {

			File outFile = new File(outFilename);
			outFile.mkdirs(); // 创建相对应的目的目录

			String[] fileList = inFile.list(); // 得到所有的子文件和目录
			for (String childFilename : fileList) {
				String srcChildFilename = getCurSrcFilename(inFile,
						childFilename);
				String targetChildFilename = getCurTargetFilename(inFile,
						childFilename);
				// 递归调用，扫描所有子目录
				execute(srcChildFilename, targetChildFilename, encoding);

			}
		}
	}

	/**
	 * 得到当前子文件的源路径
	 *
	 * @param parent
	 *            父目录
	 * @param childFilename
	 * @return
	 */
	private String getCurSrcFilename(File parent, String childFilename) {
		return parent.getAbsoluteFile() + separator + childFilename;
	}

	/**
	 * 得到当前子文件的目的路径
	 *
	 * @param parent
	 *            父目录
	 * @param childFilename
	 * @return
	 */
	private String getCurTargetFilename(File parent, String childFilename) {
		String parentFilename = parent.getAbsolutePath();
		int index = srcFile.getAbsolutePath().length();
		String relativePath = parentFilename.substring(index);
		String curTargetFilename = targetFile.getAbsolutePath() + separator
				+ relativePath + separator + childFilename;
		return curTargetFilename;
	}
}
