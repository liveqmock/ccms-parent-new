package com.yunat.ccms.biz.core.repository.impl;

import static org.junit.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.yunat.ccms.biz.core.test.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.biz.repository.impl.ProgramRepositoryImpl;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProgramRepositoryImplTest extends AbstractJunit4SpringContextBaseTests {

	private ProgramRepositoryImpl repository;
	private SimpleJpaRepository programRepositoryMock;
	
	@Before
	public void setUp() throws Exception {
		repository = new ProgramRepositoryImpl();
		
		programRepositoryMock = mock(SimpleJpaRepository.class);
		repository.setProgramRepository(programRepositoryMock);
	}

	@Test
	public void testFindByProgName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByProgNameAndNotProgId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByFilterName() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveAndFlush() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
