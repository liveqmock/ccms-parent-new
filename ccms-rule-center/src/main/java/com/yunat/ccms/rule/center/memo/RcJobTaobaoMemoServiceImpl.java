package com.yunat.ccms.rule.center.memo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.rule.center.runtime.job.RcJobDetailLogRepository;
import com.yunat.ccms.rule.center.runtime.job.RcJobLogRepository;

@Service
@Transactional
public class RcJobTaobaoMemoServiceImpl implements RcJobTaobaoMemoService {

	private final static Logger logger = LoggerFactory.getLogger(RcJobTaobaoMemoServiceImpl.class);

	@Autowired
	private RcJobTaobaoMemoRepository rcJobTaobaoMemoRepository;

	@Autowired
	private RcJobLogRepository rcJobLogRepository;

	@Autowired
	private RcJobDetailLogRepository rcJobDetailLogRepository;

	@Override
	public List<RcJobTaobaoMemo> findByStatus(String status) {
		return rcJobTaobaoMemoRepository.findByStatusOrderBySubmitTimeAsc(status);
	}

	@Override
	public RcJobTaobaoMemo saveOrUpdate(RcJobTaobaoMemo job) {
		return rcJobTaobaoMemoRepository.save(job);
	}

	@Override
	public void enableJobCountFlag(String shopId, String tid) {
		logger.info("disableJobCountFlag  shopId:{},tid{}", shopId, tid);
		rcJobLogRepository.enableJobCountFlag(shopId, tid);
		rcJobDetailLogRepository.enableJobCountFlag(shopId, tid);
	}
}
