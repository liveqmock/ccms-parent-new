package com.yunat.ccms.tradecenter.repository;

public interface CareStatusRepository {

	/**
	 * 插入或更新发货通知状态（根据tid）
	 * @param tid
	 * @param shipmentCareStatus
	 */
	void inOrUpShipmentCareStatus(String tid, int shipmentCareStatus);

	/**
	 * 插入或更新同城通知状态（根据tid）
	 * @param tid
	 * @param arriveCareStatus
	 */
	void inOrUpArriveCareStatus(String tid, int arriveCareStatus);

	/**
	 * 插入或更新派送通知状态(根据tid)
	 * @param tid
	 * @param deliveryCareStatus
	 */
	void inOrUpDeliveryCareStatus(String tid, int deliveryCareStatus);

	/**
	 * 插入或更新签收通知状态(根据tid)
	 * @param tid
	 * @param signCareStatus
	 */
	void inOrUpSignCareStatus(String tid, int signCareStatus);

	/** 插入或更新 关怀状态 **/
	void inOrUpCareStatus(String tid,String statusName,int statusValue);

	/**
	 * 插入或更新确认收货通知状态(根据tid)
	 * @param tid
	 * @param signCareStatus
	 */
	void inOrUpConfirmCareStatus(String tid,  int confirmCareStatus);
}
