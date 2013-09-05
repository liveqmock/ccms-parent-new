package com.yunat.ccms.rule.center.drl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.io.FileUtils;
import com.yunat.ccms.rule.center.conf.plan.Plan;

@Component
@Scope(value = "prototype")
public class DRLFileWriteAsStorage implements DRLFileBuilder {
	private static Logger logger = LoggerFactory.getLogger(DRLFileWriteAsStorage.class);

	@Autowired
	private DRLFragmentBuilder<Plan> drlFragmentBuilder;

	@Autowired
	private DRLFileLocator drlFileLocator;

	/**
	 * @param plan
	 * @return String
	 *         文件名(含扩展名)
	 */
	public String createDRLFile(final Plan plan) {
		String fileName = drlFileLocator.getFileName(plan.getId());
		String fileFullPath = drlFileLocator.getFileFullPath(plan.getShopId(), plan.getId(), fileName);
		try {
			DRLFragment drl = drlFragmentBuilder.toDRL(plan);
			FileUtils.writeFileByUTF8(fileFullPath, drl.toFileContent(), false);
			return fileName;
		} catch (IOException e) {
			logger.info("创建drl文件{}出现异常:{}", fileFullPath, e.getMessage());
			e.printStackTrace();
		}
		return null;

	}
}
