package com.yunat.ccms.tradecenter.repository.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.domain.CareStatusDomain;
import com.yunat.ccms.tradecenter.repository.CareStatusRepository;

/**
 *
 * @author 李卫林
 *
 */
@Repository
public class CareStatusRepositoryImpl implements CareStatusRepository {

    @PersistenceContext
    private EntityManager em;


    @Transactional
	public synchronized void  inOrUpShipmentCareStatus(String tid, int shipmentCareStatus) {
		CareStatusDomain careStatusDomain = em.find(CareStatusDomain.class, tid);
		if (careStatusDomain != null) {
			em.createQuery("update CareStatusDomain set shipmentCareStatus = :shipmentCareStatus, updated=now() where tid = :tid")
			  .setParameter("shipmentCareStatus", shipmentCareStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			careStatusDomain = new CareStatusDomain();
			careStatusDomain.setTid(tid);
			careStatusDomain.setShipmentCareStatus(shipmentCareStatus);
			careStatusDomain.setCreated(new Date());
			careStatusDomain.setUpdated(new Date());
			em.persist(careStatusDomain);
		}
	}

	@Transactional
	@Override
	public synchronized void inOrUpArriveCareStatus(String tid, int arriveCareStatus) {
		//查询制定主键是否存在
		CareStatusDomain careStatusDomain = em.find(CareStatusDomain.class, tid);

		if (careStatusDomain != null) {
			em.createQuery("update CareStatusDomain set arriveCareStatus = :arriveCareStatus, updated=now() where tid = :tid")
			  .setParameter("arriveCareStatus", arriveCareStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			careStatusDomain = new CareStatusDomain();
			careStatusDomain.setTid(tid);
			careStatusDomain.setArriveCareStatus(arriveCareStatus);
			careStatusDomain.setCreated(new Date());
			careStatusDomain.setUpdated(new Date());
			em.persist(careStatusDomain);
		}
	}

    @Transactional
	@Override
	public synchronized void inOrUpDeliveryCareStatus(String tid, int deliveryCareStatus) {
		//查询制定主键是否存在
		CareStatusDomain deliveryStatusDomain = em.find(CareStatusDomain.class, tid);

		if (deliveryStatusDomain != null) {
			em.createQuery("update CareStatusDomain set deliveryCareStatus = :deliveryCareStatus, updated=now() where tid = :tid")
			  .setParameter("deliveryCareStatus", deliveryCareStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			deliveryStatusDomain = new CareStatusDomain();
			deliveryStatusDomain.setTid(tid);
			deliveryStatusDomain.setDeliveryCareStatus(deliveryCareStatus);
			deliveryStatusDomain.setCreated(new Date());
			deliveryStatusDomain.setUpdated(new Date());
			em.persist(deliveryStatusDomain);
		}
	}

    @Transactional
	@Override
	public synchronized void inOrUpSignCareStatus(String tid, int signCareStatus) {
		//查询制定主键是否存在
		CareStatusDomain signStatusDomain = em.find(CareStatusDomain.class, tid);

		if (signStatusDomain != null) {
			em.createQuery("update CareStatusDomain set signCareStatus = :signCareStatus, updated=now() where tid = :tid")
			  .setParameter("signCareStatus", signCareStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			signStatusDomain = new CareStatusDomain();
			signStatusDomain.setTid(tid);
			signStatusDomain.setSignCareStatus(signCareStatus);
			signStatusDomain.setCreated(new Date());
			signStatusDomain.setUpdated(new Date());
			em.persist(signStatusDomain);
		}
	}

    @Override
    @Transactional
    public synchronized void inOrUpCareStatus(String tid, String statusName, int statusValue) {
        //查询制定主键是否存在
        CareStatusDomain signStatusDomain = em.find(CareStatusDomain.class, tid);
        if (signStatusDomain != null) {
            em.createNativeQuery("update tb_tc_care_status set " + statusName + " = " + statusValue + ", updated=now() where tid = '" +  tid + "'")
              .executeUpdate();
        } else {
            em.createNativeQuery("insert into tb_tc_care_status(tid," + statusName + ",created,updated) values('" + tid + "'," + statusValue + ",now(),now())")
            .executeUpdate();
        }
    }

	@Override
	@Transactional
	public synchronized void inOrUpConfirmCareStatus(String tid, int confirmCareStatus) {
		// TODO Auto-generated method stub
		//查询制定主键是否存在
		CareStatusDomain confirmStatusDomain = em.find(CareStatusDomain.class, tid);

		if (confirmStatusDomain != null) {
			em.createQuery("update CareStatusDomain set confirmCareStatus = :confirmCareStatus, updated=now() where tid = :tid")
			  .setParameter("confirmCareStatus", confirmCareStatus).setParameter("tid", tid)
			  .executeUpdate();
		} else {
			confirmStatusDomain = new CareStatusDomain();
			confirmStatusDomain.setTid(tid);
			confirmStatusDomain.setConfirmCareStatus(confirmCareStatus);
			confirmStatusDomain.setCreated(new Date());
			confirmStatusDomain.setUpdated(new Date());
			em.persist(confirmStatusDomain);
		}
	}
}
