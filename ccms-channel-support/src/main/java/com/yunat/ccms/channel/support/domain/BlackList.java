package com.yunat.ccms.channel.support.domain;

import java.util.Date;

public interface BlackList {

	public String getContact();

	public void setContact(String contact);

	public String getSource();

	public void setSource(String source);

	public Date getCreated();

	public void setCreated(Date created);
}
