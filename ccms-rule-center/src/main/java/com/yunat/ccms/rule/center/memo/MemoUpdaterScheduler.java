package com.yunat.ccms.rule.center.memo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.drools.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.TradeMemoUpdateRequest;
import com.yunat.base.enums.app.PlatEnum;
import com.yunat.ccms.channel.external.taobao.handler.CommonInvokerHandler;
import com.yunat.ccms.rule.center.RuleCenterCons;
import com.yunat.ccms.ucenter.rest.service.AccessTokenService;
import com.yunat.ccms.ucenter.rest.vo.AccessToken;

@Component
public class MemoUpdaterScheduler {
	private final static Logger logger = LoggerFactory.getLogger(MemoUpdaterScheduler.class);

	@Autowired
	@Qualifier("momoUpdaterExecutor")
	private Executor executor;

	@Autowired
	private RcJobTaobaoMemoService memoService;

	@Autowired
	private AccessTokenService accessTokenService;

	@Autowired
	private CommonInvokerHandler invokerHandler;

	@Scheduled(fixedDelay = 1000 * 20)
	private void execute() {
		final List<RcJobTaobaoMemo> list = memoService.findByStatus(RcJobTaobaoMemoStatus.INIT.name());
		logger.info("规则引擎 : 扫描需要更新备注的订单，返回{}", list.size());
		for (final RcJobTaobaoMemo memo : list) {
			executor.execute(new WorkerThread(memo));
			memo.setStatus(RcJobTaobaoMemoStatus.ACQUIRED.name());
			memoService.saveOrUpdate(memo);
		}
	}

	@Transactional
	public class WorkerThread implements Runnable {

		private final RcJobTaobaoMemo memoJob;

		public WorkerThread(final RcJobTaobaoMemo momoJob) {
			memoJob = momoJob;
		}

		@Override
		public void run() {
			logger.info("规则引擎: 开始处理订单备注任务：订单号{}, 备注内容{},原备注内容{}", new Object[] { memoJob.getTid(), memoJob.getMemo(),
					memoJob.getLastMemo() });
			memoJob.setStatus(RcJobTaobaoMemoStatus.PROCESSING.name());
			memoJob.setStartTime(new Date());
			memoService.saveOrUpdate(memoJob);
			final String updatedMemo;
			if (StringUtils.isEmpty(memoJob.getLastMemo())) {
				updatedMemo = memoJob.getMemo();
			} else {
				updatedMemo = memoJob.getLastMemo() + RuleCenterCons.REMARK_CONTENT_SEPERATOR + memoJob.getMemo();
			}
			logger.info("规则引擎: 开始处理订单备注任务：订单号{}, 备注内容{},原备注内容{},组装后的备注内容:{}",
					new Object[] { memoJob.getTid(), memoJob.getMemo(), memoJob.getLastMemo(), updatedMemo });
			// invoker api
			final TaobaoResponse resp = invokerApi(updatedMemo);
			if (null == resp) {
				memoJob.setStatus(RcJobTaobaoMemoStatus.ERROR.name());
				memoJob.setErrorMsg("未知异常,可能是调用出现问题");
			} else if (!resp.isSuccess()) {
				memoJob.setStatus(RcJobTaobaoMemoStatus.ERROR.name());
				memoJob.setErrorMsg("code:" + resp.getSubCode() + ", msg:" + resp.getSubMsg());
			} else {
				memoJob.setStatus(RcJobTaobaoMemoStatus.SUCCESS.name());
				memoService.enableJobCountFlag(memoJob.getShopId(), memoJob.getTid());
			}
			memoJob.setEndTime(new Date());
			memoService.saveOrUpdate(memoJob);
			logger.info("规则引擎: 订单备注任务结束：订单号{},状态{}", memoJob.getTid(), memoJob.getStatus());
		}

		private TaobaoResponse invokerApi(final String actualMemo) {
			// 取sessionKey
			final AccessToken accessToken = accessTokenService.getAccessToken(PlatEnum.taobao, memoJob.getShopId());
			final String sessionKey = accessToken.getAccessToken();
			// default invoker update
			TaobaoResponse resp = null;
			final TradeMemoUpdateRequest req = new TradeMemoUpdateRequest();
			req.setTid(Long.valueOf(memoJob.getTid()));
			req.setMemo(actualMemo);
			resp = invokerHandler.execute(req, sessionKey);
			return resp;
		}
	}
}
