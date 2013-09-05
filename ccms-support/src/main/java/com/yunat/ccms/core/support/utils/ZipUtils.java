package com.yunat.ccms.core.support.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class ZipUtils {

	static final int BUFFER = 8192;
	private static final String DEFAULT_FILE_ENCODING = "UTF-8";
	private static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

	private static String fileEncoding = System.getProperty("sun.jnu.encoding");

	public static void compress(File zipFile, File srcFile) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			out.setEncoding(ZipUtils.fileEncoding);
			String basedir = "";
			compress(srcFile, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			compressDirectory(file, out, basedir);
		} else {
			compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 ,不带外层文件夹 */
	public static void compressDirectoryNoBaseDir(File dir, File basedir) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(dir);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
					new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			out.setEncoding(ZipUtils.fileEncoding);
			if (!basedir.exists()) {
				return;
			}

			File[] files = basedir.listFiles();
			for (int i = 0; i < files.length; i++) {
				/* 递归 */
				compress(files[i], out, "");
			}

			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/** 压缩一个目录 */
	private static void compressDirectory(File dir, ZipOutputStream out,
			String basedir) {
		if (!dir.exists()) {
			return;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + File.separator);
		}
	}

	/** 压缩一个文件 */
	public static void compressFile(File file, ZipOutputStream out,
			String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void unZip(File inFile, String desDir) throws IOException {
		int i = inFile.getName().lastIndexOf('.');

		File newdir = new File(desDir);
		boolean bool = newdir.mkdirs();
		logMkDir(bool);
		byte[] c = new byte[1024];
		int slen;
		ZipFile zf = new ZipFile(inFile, ZipUtils.fileEncoding);
		Enumeration enu = zf.getEntries();
		while (enu.hasMoreElements()) {
			ZipEntry file = (ZipEntry) enu.nextElement();
			String name = file.getName();
			i = name.replace('/', '\\').lastIndexOf('\\');
			if (i != -1) {
				File dirs = new File(desDir + File.separator
						+ name.replace('/', '\\').substring(0, i));
				dirs.mkdirs();
				dirs = null;
			}else{ // 如果找不到  '/' ，就表示是 文件
				int j = file.getName().lastIndexOf(".");
				name = new Date().getTime()+file.getName().substring(j);
			}

			if (!file.isDirectory()) {
				InputStream fi = zf.getInputStream(file);
				if (fi != null) {
					FileOutputStream out = new FileOutputStream(desDir
							+ File.separator + name);
					while ((slen = fi.read(c, 0, c.length)) != -1) {
						out.write(c, 0, slen);
					}
					out.close();
					fi.close();
				}
			}
		}
		zf.close();

	}

	private static void logMkDir(boolean bool) {
		if(bool == false) {
			logger.error("建立解压文件夹失败");
		}
	}


	public static void unZipForUtf8(File inFile, String desDir, String suffix)
			throws IOException {
		String[] suffixs = suffix.split(",");
		List sufList = Arrays.asList(suffixs);
		int i = inFile.getName().lastIndexOf('.');

		File newdir = new File(desDir);
		boolean bool = newdir.mkdirs();
		logMkDir(bool);

		byte[] c = new byte[1024];
		int slen;
		ZipFile zf = new ZipFile(inFile, ZipUtils.fileEncoding);
		Enumeration enu = zf.getEntries();
		while (enu.hasMoreElements()) {
			ZipEntry file = (ZipEntry) enu.nextElement();
			i = file.getName().replace('/', '\\').lastIndexOf('\\');
			if (i != -1) {
				File dirs = new File(desDir + File.separator
						+ file.getName().replace('/', '\\').substring(0, i));
				dirs.mkdirs();
				dirs = null;
			}

			if (!file.isDirectory()) {
				String fileName = file.getName();
				String[] fileNames = fileName.split("\\.");
				String fileSuffix = fileNames[fileNames.length - 1]
						.toLowerCase();
				InputStream fi = zf.getInputStream(file);
				if (sufList.contains(fileSuffix)) {
					String temp;
					// StringBuffer fileStr = new StringBuffer();
					// 转编码
					if (fi != null) {
						String encoding = new ConvertEncoding()
								.getFileCharacter(fi);
						fi = zf.getInputStream(file);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(
										fi,
										StringUtils.isEmpty(encoding) ? DEFAULT_FILE_ENCODING
												: encoding));
						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										desDir + File.separator
												+ file.getName()), "UTF-8"));
						while ((temp = br.readLine()) != null) {
							bw.write(temp);
						}
						if (br != null) {
							bw.close();
							br.close();
						}
					}
				} else {

					if (fi != null) {
						FileOutputStream out = new FileOutputStream(desDir
								+ File.separator + file.getName());
						while ((slen = fi.read(c, 0, c.length)) != -1) {
							out.write(c, 0, slen);
						}
						out.close();
						fi.close();
					}
				}
			}
		}
		zf.close();

	}

	public static void zip(File inDir, File desFile) throws IOException {
		byte[] buf = new byte[1024];
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(desFile));
		List<String> list = new ArrayList<String>();
		list(list, inDir.getAbsolutePath());
		for (String element : list) {
			File file = new File(element);
			if (file.isFile()) {
				FileInputStream in = new FileInputStream(file);
				String path = file.getAbsolutePath();
				int index = path.indexOf(inDir.getName())
						+ inDir.getName().length() + 1;
				if (index > path.length() - 1) {
					index = path.length() - 1;
				}
				path = path.substring(index, path.length());
				out.putNextEntry(new ZipEntry(path));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
		}
		out.close();
	}

	public static void zip(File inDir, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		ZipOutputStream out = new ZipOutputStream(os);
		List<String> list = new ArrayList<String>();
		list(list, inDir.getAbsolutePath());
		for (String element : list) {
			File file = new File(element);
			if (file.isFile()) {
				FileInputStream in = new FileInputStream(file);
				String path = file.getAbsolutePath();
				int index = path.indexOf(inDir.getName())
						+ inDir.getName().length() + 1;
				path = path.substring(index, path.length());
				out.putNextEntry(new ZipEntry(path));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
		}
		out.close();
	}

	public static void list(List<String> lst, String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			lst.add(f.getAbsolutePath() + File.separator);
			String dirs[] = f.list();
			for (int i = 0; dirs != null && i < dirs.length; i++) {
				list(lst, f.getAbsolutePath() + File.separator + dirs[i]);
			}
		}
		if (f.isFile()) {
			lst.add(f.getAbsolutePath());
		}
	}

}
