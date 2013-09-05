/**
 *
 */
package com.yunat.ccms.tradecenter.util;

import java.util.UUID;

import com.yunat.base.enums.app.AppEnum;
import com.yunat.base.enums.app.PlatEnum;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-7 下午03:39:47
 */
public class TaskUtil {


	/**
	 * 任务id,格式：应用ID(0) + "-" + 平台ID(taobao) + "-" +userName+"-"+uuid
	 *
	 * */
	public static String getTaskId(String ccmsUser){
		return AppEnum.CCMS.getAppId()+"-"+PlatEnum.taobao+"-" + UUID.randomUUID().toString().replaceAll("-", "");

	}

	public static void main(String[] args) {
		System.out.println(TaskUtil.getTaskId("11"));
	}

}
