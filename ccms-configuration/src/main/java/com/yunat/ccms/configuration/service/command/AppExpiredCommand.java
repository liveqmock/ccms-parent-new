package com.yunat.ccms.configuration.service.command;

import com.yunat.ccms.configuration.domain.AppExpired;

public interface AppExpiredCommand {
	public void save(AppExpired dto);
	public void update(AppExpired dto);
}
