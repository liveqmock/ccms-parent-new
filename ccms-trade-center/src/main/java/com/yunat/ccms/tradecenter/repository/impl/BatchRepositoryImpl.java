package com.yunat.ccms.tradecenter.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.tradecenter.repository.BatchRepository;

@Repository
public class BatchRepositoryImpl implements BatchRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public <T>  void batchInsert(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if ((i+1) % 200 == 0) {
                em.flush();
                em.clear();
            }
        }
	}

	@Override
	@Transactional
	public <T> void batchUpdate(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            em.merge(list.get(i));
            if ((i+1) % 200 == 0) {
                em.flush();
                em.clear();
            }
        }
	}
}
