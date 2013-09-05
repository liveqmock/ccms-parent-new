package com.yunat.ccms.channel.support.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.domain.Channel;
import com.yunat.ccms.channel.support.repository.ChannelRepository;

@Service
public class ChannelQueryServiceImpl implements ChannelQueryService {

	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public Channel getChannelById(Long channelId) {
		return channelRepository.findOne(channelId);
	}

	@Override
	public List<Channel> getAllChannel() {
		return channelRepository.findAll();
	}

	@Override
	public List<Channel> getChannelByType(ChannelType type) {
		return channelRepository.findChannelByType(type.getCode());
	}
}
