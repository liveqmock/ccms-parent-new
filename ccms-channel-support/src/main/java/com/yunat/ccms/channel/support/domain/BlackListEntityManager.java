package com.yunat.ccms.channel.support.domain;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.cons.EnumBlackList;

/**
 * 黑名单实体管理类
 * 
 * @author kevin.jiang 2013-4-24
 */
@Component
public class BlackListEntityManager {

	public static String MANUAL = "手工录入";
	public static String IMPORT = "手工导入";

	private BlackList newEntity(EnumBlackList type) {

		switch (type) {

		case EMAIL:
			return new EmailBlackList();
		case MOBILE:
			return new MobileBlackList();
		case MEMBER:
			return new MemberBlackList();
		default:
			return null;
		}
	}

	public BlackList createManualEntityByValue(EnumBlackList type, String value) {

		if (type == null) {

			return null;
		}
		BlackList blackList = newEntity(type);
		blackList.setContact(value);
		blackList.setSource(MANUAL);
		blackList.setCreated(new Date());
		return blackList;
	}

	public BlackList createImportEntityByValue(EnumBlackList type, String value) {

		if (type == null) {

			return null;
		}

		BlackList blackList = newEntity(type);
		blackList.setContact(value);
		blackList.setSource(IMPORT);
		blackList.setCreated(new Date());
		return blackList;
	}
}
