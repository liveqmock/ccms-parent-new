package com.yunat.ccms.biz.core.specification;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.biz.core.test.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.biz.domain.Program;
import com.yunat.ccms.biz.repository.specification.ProgramSpecifications;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProgramSpecificationsTest extends AbstractJunit4SpringContextBaseTests {

	private CriteriaBuilder criteriaBuilderMock;
	private CriteriaQuery criteriaQueryMock;
	private Root<Program> programRootMock;
	
	private final static String PROG_NAME = "progName";
	private final static Long PROG_ID = 1L;
	private final static Long USER_ID = 1L;

	@Before
	public void setUp() throws Exception {
		criteriaBuilderMock = mock(CriteriaBuilder.class);
		criteriaQueryMock = mock(CriteriaQuery.class);
		programRootMock = mock(Root.class);
	}

	@Test
	public void testFilterName() {
		Path filterNamePathMock = mock(Path.class);
		when(programRootMock.get("progName")).thenReturn(filterNamePathMock);
		
		Path createrPathMock = mock(Path.class);
		when(programRootMock.get("creater")).thenReturn(createrPathMock);
		
		Path userIdPathMock = mock(Path.class);
		when(createrPathMock.get("id")).thenReturn(userIdPathMock);
		
		Predicate templateNameToEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(filterNamePathMock, PROG_NAME)).thenReturn(
				templateNameToEqualPredicateMock);
		
		Predicate userIdToEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(userIdPathMock, USER_ID)).thenReturn(userIdToEqualPredicateMock);
		
		Predicate complexAndPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.and(userIdToEqualPredicateMock, 
				templateNameToEqualPredicateMock)).thenReturn(complexAndPredicateMock);
		
		Specification<Program> actual = ProgramSpecifications.filterName(USER_ID, PROG_NAME);
		Predicate actualPredicate = actual.toPredicate(programRootMock, 
				criteriaQueryMock, criteriaBuilderMock);
		
		verify(programRootMock, times(1)).get("progName");
		verify(programRootMock, times(1)).get("creater");
		verify(createrPathMock, times(1)).get("id");
		
		verify(criteriaBuilderMock, times(1)).equal(filterNamePathMock, PROG_NAME);
		verify(criteriaBuilderMock, times(1)).equal(userIdPathMock, USER_ID);
		
		verify(criteriaBuilderMock, times(1)).and( 
				userIdToEqualPredicateMock, templateNameToEqualPredicateMock);
		
		verifyNoMoreInteractions(complexAndPredicateMock);
		verifyZeroInteractions(criteriaQueryMock, createrPathMock, userIdPathMock, 
				templateNameToEqualPredicateMock, complexAndPredicateMock);
		
		assertEquals(complexAndPredicateMock, actualPredicate);
	}
	
	@Test
	public void testProgName() {
		Path progNamePathMock = mock(Path.class);
		when(programRootMock.get("progName")).thenReturn(progNamePathMock);
		
		Predicate returnPredicate = mock(Predicate.class);
		when(criteriaBuilderMock.equal(progNamePathMock, 
				PROG_NAME)).thenReturn(returnPredicate);
		
		Specification<Program> actual = ProgramSpecifications.progName(PROG_NAME);
		Predicate actualPredicate = actual.toPredicate(programRootMock, 
				criteriaQueryMock, criteriaBuilderMock);
		
		verify(programRootMock, times(1)).get("progName");
		verify(criteriaBuilderMock, times(1)).equal(progNamePathMock, PROG_NAME);
		
		verifyNoMoreInteractions(progNamePathMock);
		
		verifyZeroInteractions(criteriaQueryMock, progNamePathMock, returnPredicate);
		
		assertEquals(returnPredicate, actualPredicate);
	}
	
	@Test
	public void testProgNameAndNotProgId() {
		Path progNamePathMock = mock(Path.class);
		when(programRootMock.get("progName")).thenReturn(progNamePathMock);
		
		Path progIdPathMock = mock(Path.class);
		when(programRootMock.get("progId")).thenReturn(progIdPathMock);
		
		Predicate progNameToEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(progNamePathMock, PROG_NAME)).thenReturn(progNameToEqualPredicateMock);
		
		Predicate progIdToNotEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.notEqual(progIdPathMock, PROG_ID)).thenReturn(progIdToNotEqualPredicateMock);
		
		Predicate returnPredicate = mock(Predicate.class);
		when(criteriaBuilderMock.and(progNameToEqualPredicateMock, 
				progIdToNotEqualPredicateMock)).thenReturn(returnPredicate);
		
		Specification<Program> actual = ProgramSpecifications.progNameAndNotProgId(PROG_NAME, PROG_ID);
		Predicate actualPredicate = actual.toPredicate(programRootMock, 
				criteriaQueryMock, criteriaBuilderMock);
		
		verify(programRootMock, times(1)).get("progName");
		verify(programRootMock, times(1)).get("progId");
		
		verify(criteriaBuilderMock, times(1)).and(
				progNameToEqualPredicateMock, progIdToNotEqualPredicateMock);
		
		verifyNoMoreInteractions(returnPredicate);
		verifyZeroInteractions(criteriaQueryMock, progNamePathMock, progIdPathMock, 
				progNameToEqualPredicateMock, progIdToNotEqualPredicateMock, returnPredicate);
		
		assertEquals(returnPredicate, actualPredicate);
	}
}
