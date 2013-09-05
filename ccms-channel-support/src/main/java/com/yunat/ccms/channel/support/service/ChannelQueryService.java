package com.yunat.ccms.channel.support.service;

import java.util.List;

import com.yunat.ccms.channel.support.ChannelType;
import com.yunat.ccms.channel.support.domain.Channel;

public interface ChannelQueryService {

	Channel getChannelById(Long channelId);

	List<Channel> getAllChannel();

	List<Channel> getChannelByType(ChannelType type);

}
