package com.yunat.ccms.core.support.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import cn.xddai.chardet.CharsetDetector;

public class ConvertEncoding {

	/** 要进行处理的文件集合 */
	HashSet<String> include = new HashSet<String>();
	private final String defaultIncludeFile = "csv|txt"; // "txt|ini|java|jsp|jspa|htm|html|xml|js|vbs|css|properties|ftl|php|asp";

	public ConvertEncoding() {
		// 如果没有设置要进行处理的文件，就执行默认包含的文件
		if (include.isEmpty()) {
			setInclude(defaultIncludeFile);
		}
	}

	public void setInclude(String fileExtName) {
		String[] arr = fileExtName.split("\\|");
		for (String name : arr) {
			include.add(name.trim());
		}
	}

	public void convertEncoding(String inFilename, String outFilename,
			String encoding) throws FileNotFoundException, IOException {
		// 源文件
		File inFile = new File(inFilename);
		if (!inFile.exists()) {
			System.out.println(inFile.getAbsolutePath() + "不存在");
			return;
		}
		// 目的文件
		File outFile = new File(outFilename);
		// 如果源路径是目录，则在目的路径创建目录后返回
		if (inFile.isDirectory()) {
			outFile.mkdirs();
			return;
		} else {
			outFile.createNewFile();
			boolean isTxt = isTextFile(inFile);
			if (isTxt) {// 如果是文本文件，进行编码转换
				convertEncodingProcess(inFilename, outFilename, encoding);
			} else {
				// 如果是非文本文件，直接复制
				copyBinaryFile(inFilename, outFilename);
			}
		}
	}

	public void convertEncodingProcess(String inFilename, String outFilename,
			String encoding) throws FileNotFoundException, IOException {
		File inFile = new File(inFilename);
		String currentEncoding = getFileCharacter(inFile);
		System.out.println(currentEncoding);
		File outFile = new File(outFilename);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile), currentEncoding));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), encoding));

		String str = "";
		while ((str = reader.readLine()) != null) {
			writer.write(str + "\r\n");
		}
		writer.flush();
		reader.close();
		writer.close();
	}

	public void copyBinaryFile(String inFilename, String outFilename)
			throws FileNotFoundException, IOException {
		File inFile = new File(inFilename);
		File outFile = new File(outFilename);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				inFile));
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(outFile));

		byte[] data = new byte[1];
		while (bis.read(data) != -1) {
			bos.write(data);
		}
		bos.flush();
		bis.close();
		bos.close();
	}

	public String getFileCharacter(File file) throws FileNotFoundException,
			IOException {

		CharsetDetector charDect = new CharsetDetector();
		FileInputStream fis = new FileInputStream(file);
		String[] probableSet = charDect.detectChineseCharset(fis);

		if (probableSet.length == 1) {
			if ("x-euc-tw".equals(probableSet[0])) {
				return "GB2312";
			} else {
				if ("GB18030".equals(probableSet[0])) {
					return "GB2312";
				}
				return probableSet[0];
			}
		} else if (probableSet.length > 1) {
			for (String character : probableSet) {
				if ("GB2312".equals(character) || "GB18030".equals(character)) {
					return "GB2312";
				}
				if ("UTF-8".equals(character)) {
					return "UTF-8";
				}
				if ("ASCII".equals(character)) {
					return "ASCII";
				}
			}
		}

		return null;
	}

	public String getFileCharacter(InputStream is)
			throws FileNotFoundException, IOException {

		CharsetDetector charDect = new CharsetDetector();

		String[] probableSet = charDect.detectChineseCharset(is);

		if (probableSet.length == 1) {
			if ("x-euc-tw".equals(probableSet[0])) {
				return "GB2312";
			} else {
				if ("GB18030".equals(probableSet[0])) {
					return "GB2312";
				}
				return probableSet[0];
			}
		} else if (probableSet.length > 1) {
			for (String character : probableSet) {
				if ("GB2312".equals(character) || "GB18030".equals(character)) {
					return "GB2312";
				}
				if ("UTF-8".equals(character)) {
					return "UTF-8";
				}
				if ("ASCII".equals(character)) {
					return "ASCII";
				}
			}
		}

		return null;
	}

	public String getFileExtName(File file) {
		if (file.isDirectory()) {
			return null;
		}
		String fullName = file.getName();
		int index = fullName.indexOf(".");
		return fullName.substring(index + 1);
	}

	public boolean isTextFile(File file) {
		String extName = getFileExtName(file);
		return include.contains(extName);
	}

}
