package com.yunat.ccms.configuration.service.query;

import com.yunat.ccms.configuration.domain.AppExpired;

public interface AppExpiredQuery {
	AppExpired findByAssociateUsername(String associateUsername);
}