package com.yunat.ccms.rule.center.runtime.job;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RcJobServiceImpl implements RcJobService {

	private final static Logger logger = LoggerFactory.getLogger(RcJobServiceImpl.class);

	@Autowired
	private RcJobRepository rcJobRepository;

	@Autowired
	private RcJobLogRepository rcJobLogRepository;

	@Override
	public List<RcJob> findByStatus(final String status) {
		return rcJobRepository.findByStatusOrderBySubmitTimeAsc(status);
	}

	@Override
	public RcJob save(final RcJob job) {
		return rcJobRepository.save(job);
	}

	@Override
	public List<RcJob> save(final List<RcJob> jobList) {
		return rcJobRepository.save(jobList);
	}

	@Override
	public void saveLog(final RcJobLog log) {
		rcJobLogRepository.save(log);
	}

	@Override
	public Collection<RcJobLog> getJobLogsByShopIdEndTimeBetween(final String shopId, final Date from, final Date to) {
		return rcJobLogRepository.findByShopIdAndEndTimeBetween(shopId, from, to);
	}

	@Override
	public void delete(RcJob job) {
		rcJobRepository.delete(job);
	}
}
