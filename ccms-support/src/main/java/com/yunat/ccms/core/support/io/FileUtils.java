package com.yunat.ccms.core.support.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.yunat.ccms.core.support.utils.ConvertCharset;

public class FileUtils {

	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static String getFileSuffix(String fileName) {
		return org.apache.commons.io.FilenameUtils.getExtension(fileName);
	}

	/**
	 * 读取文件内容.
	 * 
	 * @param name
	 *            文件路径.
	 * @return 文件文本内容
	 * @throws IOException
	 */
	public static String readFileByUTF8(String name) throws IOException {
		File file = new File(name);
		return org.apache.commons.io.FileUtils.readFileToString(file, Charsets.UTF_8);
	}

	/**
	 * 读取文件内容.
	 * 
	 * @param name
	 *            文件路径.
	 * @return 文件文本内容
	 * @throws IOException
	 */
	public static String readFile(String name, Charset encoding) throws IOException {
		File file = new File(name);
		return org.apache.commons.io.FileUtils.readFileToString(file, encoding);
	}

	/**
	 * 读取文件内容.
	 * 
	 * @param name
	 *            文件路径.
	 * @return 文件文本内容
	 * @throws IOException
	 */
	public static String readFile(String name) throws IOException {
		File file = new File(name);
		return org.apache.commons.io.FileUtils.readFileToString(file);
	}

	/**
	 * 读取文件内容.
	 * 
	 * @param file
	 *            文件对象
	 * @return 文件字节数组.
	 * @throws IOException
	 * 
	 */
	public static byte[] readFile(File file) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToByteArray(file);
	}

	public static void writeFileByUTF8(String fileName, String data, boolean append) throws IOException {
		File file = new File(fileName);
		org.apache.commons.io.FileUtils.write(file, data, Charsets.UTF_8, append);
	}

	public static void writeFile(String fileName, String data, Charset encoding, boolean append) throws IOException {
		File file = new File(fileName);
		org.apache.commons.io.FileUtils.write(file, data, encoding, append);
	}

	public static void createFolder(String dir) throws IOException {
		File file = new File(dir);
		org.apache.commons.io.FileUtils.forceMkdir(file);
	}

	/**
	 * 
	 * 1:如果参数数量大于1,认为是目录结构
	 * 2:如果不包含根目录，则从java运行目录
	 * 
	 * @param paths
	 *            无限级目录参数, 例如 a b c ,执行完后为 a/b/c(此处/为文件路径分割符号)
	 * @return 创建后的完整路径
	 * @throws IOException
	 */
	public static String concat(String... paths) {
		String concatedPath = null;
		String[] normalizePath = new String[paths.length];
		int idx = 0;
		for (String path : paths) {
			normalizePath[idx++] = org.apache.commons.io.FilenameUtils.normalize(path);
		}
		File file = org.apache.commons.io.FileUtils.getFile(normalizePath);
		if (file != null) {
			concatedPath = file.getAbsolutePath();
		}
		logger.info("concat path [{}],result:{}", StringUtils.join(paths, ","), concatedPath);
		return concatedPath;
	}

	/**
	 * 删除一个文件.
	 * 
	 * @param filePathAndName
	 *            文件完整绝对路径及文件名
	 * @throws IOException
	 */
	public static void delFile(String filePath) throws IOException {
		File myDelFile = new File(filePath);
		if (myDelFile.exists()) {
			myDelFile.delete();
		}
	}

	/**
	 * 删除文件夹.
	 * 
	 * @param path
	 *            文件夹完整路径.
	 * @throws IOException
	 */
	public static void deleteDir(String path) throws IOException {
		File file = new File(path);
		org.apache.commons.io.FileUtils.deleteDirectory(file);
	}

	/**
	 * 拷贝文件.
	 * 
	 * @param oldPathFile
	 *            源文件
	 * @param newPathFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(String srcFilePath, String destFilePath) throws IOException {
		File srcFile = new File(srcFilePath);
		File destFile = new File(destFilePath);
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * 拷贝文件.
	 * 
	 * @param oldFile
	 *            源文件
	 * @param newFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
	}

	/**
	 * 拷贝文件夹
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyFolder(String srcDir, String destDir) throws IOException {
		File srcDirFile = new File(srcDir);
		File destDirFile = new File(destDir);
		org.apache.commons.io.FileUtils.copyDirectory(srcDirFile, destDirFile);
	}

	/**
	 * 文件编码格式转换
	 * 
	 * @param sourceFileName
	 *            源文件名
	 * @param targetFileName
	 *            目标文件名
	 * @param encode
	 *            目标文件的编码
	 * @return
	 */
	public static boolean converFile(String sourceFileName, String targetFileName, String encode) {
		ConvertCharset execute = new ConvertCharset();
		try {
			execute.executeConvertFile(sourceFileName, targetFileName, encode);
			return true;
		} catch (FileNotFoundException e) {
			logger.warn("", e);
			return false;
		} catch (IOException e) {
			logger.warn("", e);
			return false;
		}
	}

}
