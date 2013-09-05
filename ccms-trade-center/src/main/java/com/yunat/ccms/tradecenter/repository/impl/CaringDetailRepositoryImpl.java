//package com.yunat.ccms.tradecenter.repository.impl;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.springframework.stereotype.Repository;
//
//import com.yunat.ccms.tradecenter.domain.CaringDetailDomain;
//import com.yunat.ccms.tradecenter.repository.CaringDetailRepository;
//
//@Repository
//public class CaringDetailRepositoryImpl implements CaringDetailRepository {
//
//	@PersistenceContext
//    private EntityManager em;
//
//
//	@Override
//	public void recordOne(CaringDetailDomain cdd) {
//		Query query = em.createQuery("from tb_tc_caring_detail p where tid= '"+ cdd.getTid()+ "' and oid= '"+ cdd.getOid()+ "' and caringperson= '"+ cdd.getCaringperson()+ "'");
//
//		if (query.getSingleResult() != null) {
//            em.createNativeQuery("update tb_tc_caring_detail set updated = now() , content = '"+ cdd.getContent()+ "' where oid = '" +  cdd.getOid() + "'")
//              .executeUpdate();
//        } else {
////            em.createNativeQuery("insert into tb_tc_caring_detail(tid,oid,dp_id,customerno,caring_type,content,created,caringperson) " +
////            		"values('" + cdd.getTid() + "','" + cdd.getOid() + "','" + cdd.getDpId() + "','" + cdd.getCustomerno() + "','" + cdd.getCaringType() +
////            		"','" + cdd.getContent()  + "',now(),'" + cdd.getCaringperson() +"')")
////            .executeUpdate();
//        	em.persist(cdd);
//        }
//
//	}
//
//	@Override
//	public void recordAll(List<CaringDetailDomain> cdds) {
//		for (CaringDetailDomain caringDetailDomain : cdds) {
//			recordOne(caringDetailDomain);
//		}
//		em.flush();
//	}
//
//}
