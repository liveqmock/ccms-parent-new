package com.yunat.ccms.rule.center.drl;

import com.yunat.ccms.rule.center.conf.plan.Plan;

/**
 * 
 * @author tao.yang
 * 
 */
public interface DRLFileBuilder {

	/**
	 * 
	 * @param plan
	 * @return
	 */
	String createDRLFile(final Plan plan);
}