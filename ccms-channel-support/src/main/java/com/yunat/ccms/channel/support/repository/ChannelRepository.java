package com.yunat.ccms.channel.support.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.channel.support.domain.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

	@Query("select DISTINCT c from Channel c where c.channelType = :channelType")
	public List<Channel> findChannelByType(@Param("channelType") Long channelType);

}
