package com.yunat.ccms.rule.center.runtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.yunat.ccms.rule.center.RuleCenterBusinessException;
import com.yunat.ccms.rule.center.engine.EngineState;
import com.yunat.ccms.rule.center.engine.FactResult;
import com.yunat.ccms.rule.center.engine.FactResult.RuleHitDetail;
import com.yunat.ccms.rule.center.engine.FactResultHandler;
import com.yunat.ccms.rule.center.engine.RuleEngineService;
import com.yunat.ccms.rule.center.runtime.fact.Order;
import com.yunat.ccms.rule.center.runtime.job.RcJob;
import com.yunat.ccms.rule.center.runtime.job.RcJobDetailLog;
import com.yunat.ccms.rule.center.runtime.job.RcJobLog;
import com.yunat.ccms.rule.center.runtime.job.RcJobService;
import com.yunat.ccms.rule.center.runtime.job.RcJobStatus;

@Component
public class RcJobScheduler {

	private final static Logger logger = LoggerFactory.getLogger(RcJobScheduler.class);

	@Autowired
	@Qualifier("rcJobRunnerExecutor")
	private Executor executor;

	@Autowired
	private RcJobService rcJobService;

	@Autowired
	private RuleEngineService ruleEngineService;

	@Autowired
	private FactResultHandler factResultHandler;

	@Scheduled(fixedDelay = 1000 * 20)
	public void check() {
		List<RcJob> jobList = rcJobService.findByStatus(RcJobStatus.INIT.name());
		logger.info("规则引擎：检查是否有需要处理的任务,返回{}", jobList.size());
		if (!CollectionUtils.isEmpty(jobList)) {
			for (RcJob job : jobList) {
				try {
					if (EngineState.avaliable(job.getShopId())) {
						job.setStatus(RcJobStatus.ACQUIRED.name());
						rcJobService.save(job);
						executor.execute(new RcJobWorker(job));
					} else {
						rcJobService.delete(job);
						logger.warn("规则引擎:店铺{}对应规则未初始化，不能处理订单{}", new Object[] { job.getShopId(), job.getTid() });
					}
				} catch (Exception e) {
					logger.error("规则引擎:提交店铺{}订单{}任务时出现异常{}",
							new Object[] { job.getShopId(), job.getTid(), e.getMessage() });
					e.printStackTrace();
				}
			}
		}

	}

	public class RcJobWorker implements Runnable {

		public final RcJob job;

		public RcJobWorker(RcJob job) {
			Assert.notNull(job);
			this.job = job;
		}

		@Override
		public void run() {
			String tid = job.getTid();
			String shopId = job.getShopId();
			logger.info("规则引擎:开始处理店铺{}订单{}", shopId, tid);
			try {
				job.setStatus(RcJobStatus.PROCESSING.name());
				rcJobService.save(job);
				RcJobLog jobLog = new RcJobLog();
				jobLog.setStartTime(new Date());
				FactResult result = ruleEngineService.execute(shopId, Long.valueOf(tid), Order.class);
				setupRcJobLog(jobLog, job, result);
				rcJobService.saveLog(jobLog);
				factResultHandler.handle(job, result);
				job.setStatus(RcJobStatus.SUCCESS.name());
				rcJobService.save(job);
				return;
			} catch (RuleCenterBusinessException e) {
				e.printStackTrace();
				logger.error("规则引擎:处理店铺{}订单{}时出现异常{}", new Object[] { shopId, tid, e.getMessage() });
				job.setStatus(RcJobStatus.ERROR.name());
				job.setErrorCode(StringUtils.abbreviate(e.getMessage(), 50));
				rcJobService.save(job);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("规则引擎:处理店铺{}订单{}时出现异常{}", new Object[] { shopId, tid, e.getMessage() });
				job.setStatus(RcJobStatus.ERROR.name());
				job.setErrorCode(StringUtils.abbreviate(e.getMessage(), 50));
				rcJobService.save(job);
			}

		}
	}

	private static void setupRcJobLog(RcJobLog jobLog, RcJob job, FactResult result) {
		jobLog.setTid(job.getTid());
		jobLog.setShopId(job.getShopId());
		jobLog.setEndTime(new Date());
		jobLog.setHits(result.getHits());
		jobLog.setCountFlag(false);
		if (result.getHits() > 0) {
			List<RcJobDetailLog> detailLogs = new ArrayList<RcJobDetailLog>();
			for (RuleHitDetail ruleHitDetail : result.getHitDetails()) {
				RcJobDetailLog detailLog = new RcJobDetailLog(job.getTid(), job.getShopId(), ruleHitDetail.getPlanId(),
						ruleHitDetail.getRuleId());
				detailLog.setCountFlag(false);
				detailLogs.add(detailLog);
			}
			jobLog.setDetails(detailLogs);
		}

	}
}
