package com.yunat.ccms.channel.support.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.channel.support.domain.RemoteLogger;
import com.yunat.ccms.channel.support.repository.RemoteLoggerRepository;

@Component
public class RemoteLoggerServiceImpl implements RemoteLoggerService {

	@Autowired
	private RemoteLoggerRepository remoteLoggerRepository;
	
	@Override
	public void save(String exceptionLogKey, String exceptionMessage) {
		RemoteLogger remoteLogger = new RemoteLogger();
		remoteLogger.setFunctionName(exceptionLogKey);
		remoteLogger.setExceptionDesc(exceptionMessage);
		remoteLogger.setCreated(new Date());
		remoteLoggerRepository.saveAndFlush(remoteLogger);
	}
}