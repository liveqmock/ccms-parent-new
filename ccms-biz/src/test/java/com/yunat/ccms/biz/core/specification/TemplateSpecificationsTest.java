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
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.repository.specification.TemplateSpecifications;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemplateSpecificationsTest extends AbstractJunit4SpringContextBaseTests {

	private CriteriaBuilder criteriaBuilderMock;
	private CriteriaQuery criteriaQueryMock;
	private Root<Template> templateRootMock;

	private final static String TEMPLATE_NAME = "test template";
	private final static Long USER_ID = 1L;
	
	@Before
	public void setUp() throws Exception {
		criteriaBuilderMock = mock(CriteriaBuilder.class);
		criteriaQueryMock = mock(CriteriaQuery.class);
		templateRootMock = mock(Root.class);
	}
	
	@Test
	public void testFilterName() {
		Path filterNamePathMock = mock(Path.class);
		when(templateRootMock.get("templateName")).thenReturn(filterNamePathMock);
		
		Path createrPathMock = mock(Path.class);
		when(templateRootMock.get("creater")).thenReturn(createrPathMock);
		
		Path userIdPathMock = mock(Path.class);
		when(createrPathMock.get("id")).thenReturn(userIdPathMock);
		
		Predicate templateNameToEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(filterNamePathMock, TEMPLATE_NAME)).thenReturn(
				templateNameToEqualPredicateMock);
		
		Predicate userIdToEqualPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(userIdPathMock, USER_ID)).thenReturn(userIdToEqualPredicateMock);
		
		Predicate complexAndPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.and(userIdToEqualPredicateMock, 
				templateNameToEqualPredicateMock)).thenReturn(complexAndPredicateMock);
		
		Specification<Template> actual = TemplateSpecifications.filterName(USER_ID, TEMPLATE_NAME);
		Predicate actualPredicate = actual.toPredicate(templateRootMock, 
				criteriaQueryMock, criteriaBuilderMock);
		
		verify(templateRootMock, times(1)).get("templateName");
		verify(templateRootMock, times(1)).get("creater");
		verify(createrPathMock, times(1)).get("id");
		
		verify(criteriaBuilderMock, times(1)).equal(filterNamePathMock, TEMPLATE_NAME);
		verify(criteriaBuilderMock, times(1)).equal(userIdPathMock, USER_ID);
		
		verify(criteriaBuilderMock, times(1)).and( 
				userIdToEqualPredicateMock, templateNameToEqualPredicateMock);
		
		verifyNoMoreInteractions(complexAndPredicateMock);
		verifyZeroInteractions(criteriaQueryMock, createrPathMock, userIdPathMock, 
				templateNameToEqualPredicateMock, complexAndPredicateMock);
		
		assertEquals(complexAndPredicateMock, actualPredicate);
	}
	
	@Test
	public void testTemplateName() {
		Path templateNamePathMock = mock(Path.class);
		when(templateRootMock.get("templateName")).thenReturn(templateNamePathMock);
		
		Predicate returnPredicate = mock(Predicate.class);
		when(criteriaBuilderMock.equal(templateNamePathMock, 
				TEMPLATE_NAME)).thenReturn(returnPredicate);
		
		Specification<Template> actual = TemplateSpecifications.templateName(TEMPLATE_NAME);
		Predicate actualPredicate = actual.toPredicate(templateRootMock, 
				criteriaQueryMock, criteriaBuilderMock);
		verify(templateRootMock, times(1)).get("templateName");
		verify(criteriaBuilderMock, times(1)).equal(templateNamePathMock, TEMPLATE_NAME);
		
		verifyNoMoreInteractions(templateNamePathMock);
		
		verifyZeroInteractions(criteriaQueryMock, templateNamePathMock, returnPredicate);
		
		assertEquals(returnPredicate, actualPredicate);
	}
}
