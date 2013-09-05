package com.yunat.ccms.channel.support.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.channel.support.domain.RemoteLogger;

public interface RemoteLoggerRepository extends JpaRepository<RemoteLogger, Serializable> {

}
