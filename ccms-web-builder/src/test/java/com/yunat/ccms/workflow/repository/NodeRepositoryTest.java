package com.yunat.ccms.workflow.repository;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.yunat.ccms.AbstractJunit4SpringContextBaseTests;

@Component
public class NodeRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	@Autowired
	private NodeRepository nodeRepository;

	@Test
	public void testFindAllFitCondition() {
		Pageable pageable = new PageRequest(5, 5);
		Page<Map<String, Object>> page = nodeRepository.findAllFitCondition(pageable);
		System.out.println(page.getTotalPages());
		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
	}

	@Test
	public void testFindAllNodesByCondition() {
		Pageable pageable = new PageRequest(5, 5);
		Page<Long> page = nodeRepository.findAllNodesByCondition(pageable);
		System.out.println(page.getTotalPages());
		System.out.println(page.getNumberOfElements());
		System.out.println(page.getTotalElements());
	}
}
