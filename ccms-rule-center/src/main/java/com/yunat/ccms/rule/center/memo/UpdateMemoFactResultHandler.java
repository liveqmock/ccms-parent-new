package com.yunat.ccms.rule.center.memo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.rule.center.engine.FactResult;
import com.yunat.ccms.rule.center.engine.FactResult.RuleHitDetail;
import com.yunat.ccms.rule.center.engine.FactResultHandler;
import com.yunat.ccms.rule.center.runtime.job.RcJob;

/**
 * 得到处理结果后更新备注，即实现 个性化包裹
 * 
 * @author xiaojing.qu
 * 
 */
@Component
public class UpdateMemoFactResultHandler implements FactResultHandler {

	private final static Logger logger = LoggerFactory.getLogger(UpdateMemoFactResultHandler.class);

	@Autowired
	private MemoContentBuilder memoBuilder;

	@Autowired
	private RcJobTaobaoMemoService taobaoMemoService;

	@Override
	public void handle(RcJob rcJob, FactResult result) {
		if (result != null && result.getHits() > 0) {
			RcJobTaobaoMemo taobaoMemo = new RcJobTaobaoMemo();
			taobaoMemo.setTid(rcJob.getTid());
			taobaoMemo.setShopId(rcJob.getShopId());
			taobaoMemo.setSubmitTime(new Date());
			taobaoMemo.setStatus(RcJobTaobaoMemoStatus.INIT.name());
			Set<Long> matchedRules = new HashSet<Long>();
			for (RuleHitDetail rhd : result.getHitDetails()) {
				matchedRules.add(rhd.getRuleId());
			}
			String memo = memoBuilder.buildMemoContent(rcJob.getShopId(), matchedRules);
			logger.info("规则引擎：处理订单{}后生成的备注:{}", rcJob.getTid(), memo);
			taobaoMemo.setMemo(memo);
			String lastMemo = memoBuilder.getLastMemo(rcJob.getShopId(), Long.valueOf(rcJob.getTid()));
			logger.info("规则引擎：处理订单{}的原备注:{}", rcJob.getTid(), lastMemo);
			taobaoMemo.setLastMemo(lastMemo);
			taobaoMemoService.saveOrUpdate(taobaoMemo);
		}

	}
}
