package com.yunat.ccms.channel.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.channel.support.domain.ChannelLogger;
import com.yunat.ccms.channel.support.repository.ChannelLoggerRespository;

@Service
@Transactional
public class ChannelLoggerServiceImpl implements ChannelLoggerService {

	@Autowired
	private ChannelLoggerRespository channelLoggerRespository;
	
	@Override
	public void saveOrUpdate(ChannelLogger channelLogger) {
		channelLoggerRespository.saveAndFlush(channelLogger);
	}

}