package com.yunat.ccms.rule.center.drl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yunat.ccms.configuration.CCMSPropertiesEnum;
import com.yunat.ccms.configuration.service.query.AppPropertiesQuery;
import com.yunat.ccms.core.support.io.FileUtils;
import com.yunat.ccms.core.support.utils.DateUtils;
import com.yunat.ccms.rule.center.RuleCenterCons;

/**
 * drl文件定位
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class DRLFileLocator {

	private static Logger logger = LoggerFactory.getLogger(DRLFileLocator.class);

	@Autowired
	private AppPropertiesQuery propertiesQuery;

	private String fileStoreRoot;

	@PostConstruct
	public void init() {
		fileStoreRoot = propertiesQuery.retrieveConfigValue(CCMSPropertiesEnum.CCMS_UPLOAD_DIR);
		Assert.notNull(fileStoreRoot, "相应配置不存在！");
	}

	public String getFileName(Long planId) {
		return "plan_" + planId + "_" + DateUtils.getFullStringDate(new Date()) + RuleCenterCons.DRL_FILE_EXTENSION;
	}

	/**
	 * 获取当前规则引擎正在使用（/或者即将使用）的规则文件地址
	 * 
	 * @param shopId
	 * @param planId
	 * @param fileName
	 * @return
	 */
	public String getFileFullPath(String shopId, Long planId, String fileName) {
		String shopPath = "taobao_" + shopId;
		String fileFullPath = FileUtils.concat(fileStoreRoot, RuleCenterCons.RULE_STORE_ROOT, shopPath,
				RuleCenterCons.RULE_STORE_RUNTIME, fileName);
		logger.info("Full path of DRL: shop:{},plan:{},file:{}", new Object[] { shopId, planId, fileName });
		return fileFullPath;

	}

	/**
	 * 
	 * @param shopId
	 * @param fileName
	 */
	public void backUp(String shopId, String fileName) {
		String shopPath = "taobao_" + shopId;
		String fileFullPath = FileUtils.concat(fileStoreRoot, RuleCenterCons.RULE_STORE_ROOT, shopPath,
				RuleCenterCons.RULE_STORE_RUNTIME, fileName);
		String fileBackupPath = FileUtils.concat(fileStoreRoot, RuleCenterCons.RULE_STORE_ROOT, shopPath,
				RuleCenterCons.RULE_STORE_BACKUP);
		logger.info("备份文件{}到目录{}", fileFullPath, fileBackupPath);
		try {
			org.apache.commons.io.FileUtils.moveFileToDirectory(new File(fileFullPath), new File(fileBackupPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("备份文件{}到目录{}出现异常：{}", new Object[] { fileFullPath, fileBackupPath, e.getMessage() });
		}
	}
}
