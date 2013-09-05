package com.yunat.ccms.rule.center.engine;

import com.yunat.ccms.rule.center.runtime.job.RcJob;

/**
 * 在规则引擎处理得到结果后，对结果进行处理
 * 是规则引擎对外作用的窗口
 * 
 * @author xiaojing.qu
 * 
 */
public interface FactResultHandler {

	void handle(RcJob rcJob, FactResult result);

}
