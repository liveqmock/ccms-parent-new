package com.yunat.ccms.channel.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.domain.BlackList;
import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.io.FileUtils;
import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.core.support.utils.ZipUtils;

@Component
public class BlackListUploadSupport {

	@Autowired
	private AppPropertiesQuery appPropertiesQuery;

	public static final String SMS_EDM_ALTER_NULL = "文件内容不能为空";
	public static final String SMS_EDM_MMS_ALTER_WRONG_STYLE = "文件内容格式不正确，请填写正确的内容信息";
	public static final String SMS_EDM_MMS_ALTER_WRONG_RIGHT = "文件内容合法";
	public static final String EDM = "EDM";
	public static final String MEMBER = "MEMBER";
	public static final Pattern EMAILER = Pattern
			.compile("^[a-zA-Z0-9_\\.\\-]+\\@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]{2,4}$");
	public static final Pattern PHONE = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9]){1}\\d{8}$");

	public String getDownloadPath() {
		String fileStrore = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		return FileUtils.concat(fileStrore, "blacklist", "download");
	}

	public String getUploadPath() {
		String fileStrore = appPropertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		return FileUtils.concat(fileStrore, "blacklist", "upload");
	}

	/**
	 * 读取文件，并判断文件中内容是否合法
	 * 
	 * @param file
	 * @return List<String>
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static String isFileLegal(File file) throws IOException {
		String line = null;
		String alter = null;
		Matcher matcher = null;
		FileReader fr = null;
		BufferedReader br = null;
		int i = 0;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);

		} catch (java.io.FileNotFoundException e) {
			alter = SMS_EDM_ALTER_NULL;
			br.close();
			fr.close();
			return alter;
		} catch (Exception e) {
			alter = "文件读取错误";
			br.close();
			fr.close();
			return alter;
		}
		while ((line = br.readLine()) != null) {
			i++;
		}
		if (i == 0) {
			alter = SMS_EDM_ALTER_NULL;
		}
		br.close();
		fr.close();

		return alter;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param oldFile
	 *            源文件
	 * @param newFile
	 *            目标文件
	 * @param mobile_edm_member
	 *            传入的类型
	 * @return
	 * @throws IOException
	 */
	public static List<String> copyFile(File oldFile, File newFile, String mobile_edm_member) throws IOException {
		List<String> list = new ArrayList<String>();
		if (oldFile == null) {
			return list;
		}
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile), "gb2312"));
		BufferedWriter bd = new BufferedWriter(new FileWriter(newFile));

		while ((line = br.readLine()) != null) {

			line = line.trim();

			// 验证 手机号码、邮件格式是否正确 会员账号验证长度
			if (MEMBER.equals(mobile_edm_member)) {
				if (line.length() >= 5 && line.length() <= 25) {
					bd.write(line, 0, line.length());
					bd.write("\r\n");
					bd.flush();
				} else {
					list.add(line);
				}

			} else {
				Pattern pattern = EDM.equals(mobile_edm_member) ? BlackListUploadSupport.EMAILER
						: BlackListUploadSupport.PHONE;
				if (pattern.matcher(line).matches()) {
					bd.write(line, 0, line.length());
					bd.write("\r\n");
					bd.flush();
				} else {
					list.add(line);
				}
			}

		}
		bd.close();
		br.close();
		return list;
	}

	// 拼接字符串
	public static String getMessage(StringBuffer sbfs, StringBuffer error, String mobile_edm_member) {
		String message = "上传文件成功 ! ";
		if (sbfs.indexOf(",") > 0) {
			if (mobile_edm_member.equals("EDM") || mobile_edm_member.equals("MOBILE")) {
				message += "[已存在同名的红名单或黑名单数据，将不会上传]\t\r";
			} else if (mobile_edm_member.equals("MEMBER")) {
				message += "[已存在同名的黑名单数据，将不会上传]\t\r";
			}

			if (sbfs.length() > 40) {
				message += sbfs.substring(0, 40) + "...";
			} else {
				message += sbfs.toString();
			}
		}

		if (mobile_edm_member.equals("EDM") || mobile_edm_member.equals("MOBILE")) {
			if (error.length() > 5) {
				message += "[存在不符合格式，将不会上传]\t\r";
				if (error.length() > 40) {
					message += error.substring(1, 40) + "...";
				} else {
					message += error.toString();
				}
			}
		} else if (mobile_edm_member.equals("MEMBER")) {
			if (error.length() > 2) {
				message += "[存在不符合格式，将不会上传]\t\r";
				if (error.length() > 40) {
					message += error.substring(1, 40) + "...";
				} else {
					message += error.toString();
				}
			}
		}
		return message;
	}

	/**
	 * 获取需要删除的红黑名单数据 list
	 * 
	 * @return
	 */
	public static List<String> getdeletes(String values, String value) {
		JSONArray array = JSONArray.fromObject(values);
		Object objArray[] = array.toArray();
		List<String> mobileList = Lists.newArrayList();
		for (Object obj : objArray) {
			String mobileStr = ((JSONObject) obj).getString(value);
			mobileList.add(mobileStr);
		}
		return mobileList;
	}

	public Map<String, String> Download(List<BlackList> listRecords, EnumBlackList type) {

		String name = "";
		String fileNamebf = "";

		switch (type) {

		case EMAIL:

			name = "邮件地址";
			fileNamebf = "email_list_";
			break;
		case MOBILE:

			name = "手机号码";
			fileNamebf = "mobile_list_";
			break;
		case MEMBER:

			name = "账号";
			fileNamebf = "member_list_";
			break;
		}

		// 文件名生成
		String filePath = getDownloadPath();
		String fileName = fileNamebf + HStringUtils.formatDatetimeCompact(new Date());

		// 写文件数据
		List<StringBuffer> listStrings = new ArrayList<StringBuffer>();
		listStrings.add(new StringBuffer(name + ",来源,时间"));
		for (BlackList blackList : listRecords) {
			StringBuffer records = new StringBuffer();
			records.append(blackList.getContact().trim()).append(",");
			records.append(blackList.getSource() == null ? "" : blackList.getSource()).append(",");
			records.append(blackList.getCreated() == null ? "" : HStringUtils.formatDatetime(blackList.getCreated()));
			listStrings.add(records);
		}

		File target = new File(filePath, fileName + "_1.csv");
		try {
			// 必须指定GBK，csv文件特殊
			org.apache.commons.io.FileUtils.writeLines(target, "GBK", listStrings);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// append到目标文件
		String zipFileName = fileName + ".csv.zip";
		File file = new File(filePath, zipFileName);

		// 文件压缩
		ZipUtils.compress(file, target);

		// 清空文件
		target.delete();

		Map<String, String> map = new HashMap<String, String>();
		map.put("fileName", zipFileName);
		map.put("filePath", file.getAbsolutePath());
		map.put("fileSize", String.valueOf(file.length()));

		return map;
	}
}
