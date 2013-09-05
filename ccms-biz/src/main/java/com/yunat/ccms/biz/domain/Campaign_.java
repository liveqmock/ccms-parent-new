package com.yunat.ccms.biz.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Campaign.class)
public class Campaign_ {
	public static volatile SingularAttribute<Campaign, Long> campId;
	public static volatile SingularAttribute<Campaign, CampaignStatus> campState;

}