package com.yunat.ccms.biz.core.specification;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import com.yunat.ccms.biz.core.test.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus_;
import com.yunat.ccms.biz.domain.Campaign_;
import com.yunat.ccms.biz.repository.specification.CampaignSpecifications;
import com.yunat.ccms.biz.support.cons.WorkflowEnum;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CampaignSpecificationsTest extends
		AbstractJunit4SpringContextBaseTests {

	private CriteriaBuilder criteriaBuilderMock;
	private CriteriaQuery criteriaQueryMock;
	private Root<Campaign> campaignRootMock;

	@Before
	public void setUp() throws Exception {
		criteriaBuilderMock = mock(CriteriaBuilder.class);
		criteriaQueryMock = mock(CriteriaQuery.class);
		campaignRootMock = mock(Root.class);
	}

	@Test
	public void testCampStateIn() {
		Path campStatePathMockF = mock(Path.class);
		//when(campaignRootMock.get("campState")).thenReturn(campStatePathMockF);
		when(campaignRootMock.get(Campaign_.campState)).thenReturn(campStatePathMockF);
		Path campStatePathMock = mock(Path.class);
		//when(campStatePathMockF.get("statusId")).thenReturn(campStatePathMock);
		when(campStatePathMockF.get(CampaignStatus_.statusId)).thenReturn(campStatePathMock);

		Collection<String> campStates = new ArrayList<String>();
		campStates.add(CampaignState.DESIGN.getCode());
		campStates.add(CampaignState.WAIT_EXECUTE.getCode());

		Predicate campStatePredicateMock = mock(Predicate.class);
		when(campStatePathMock.in(campStates)).thenReturn(
				campStatePredicateMock);

		Specification<Campaign> actual = CampaignSpecifications
				.campStateIn(campStates);
		Predicate actualPredicate = actual.toPredicate(campaignRootMock,
				criteriaQueryMock, criteriaBuilderMock);

		//verify(campaignRootMock, times(1)).get("campState");
		//verify(campStatePathMockF, times(1)).get("statusId");
		verify(campaignRootMock, times(1)).get(Campaign_.campState);
		verify(campStatePathMockF, times(1)).get(CampaignStatus_.statusId);
		verify(campStatePathMock, times(1)).in(campStates);

		verifyNoMoreInteractions(campStatePathMock);

		verifyZeroInteractions(criteriaQueryMock, campStatePathMock,
				campStatePredicateMock);

		assertEquals(campStatePredicateMock, actualPredicate);
	}

	@Test
	public void testCampState() {
		Path campStatePathMockF = mock(Path.class);
		when(campaignRootMock.get("campState")).thenReturn(campStatePathMockF);
		Path campStatePathMock = mock(Path.class);
		when(campStatePathMockF.get("statusId")).thenReturn(campStatePathMock);

		Predicate campStateToEqualPredicateMock = mock(Predicate.class);
		when(
				criteriaBuilderMock.equal(campStatePathMock,
						CampaignState.DESIGN.getCode())).thenReturn(
				campStateToEqualPredicateMock);

		Specification<Campaign> actual = CampaignSpecifications
				.campState(CampaignState.DESIGN.getCode());
		Predicate actualPredicate = actual.toPredicate(campaignRootMock,
				criteriaQueryMock, criteriaBuilderMock);

		verify(campaignRootMock, times(1)).get("campState");
		verify(campStatePathMockF, times(1)).get("statusId");
		verify(criteriaBuilderMock, times(1)).equal(campStatePathMock,
				CampaignState.DESIGN.getCode());

		verifyNoMoreInteractions(campStatePathMock);

		verifyZeroInteractions(criteriaQueryMock, campStatePathMock,
				campStateToEqualPredicateMock);

		assertEquals(campStateToEqualPredicateMock, actualPredicate);

	}

	
	@Test
	public void testCreatorAndWorkflowTypeIn() {
		Path workflowTypeInPathMock = mock(Path.class);
		when(campaignRootMock.get("workflowType")).thenReturn(
				workflowTypeInPathMock);

		Path userDomainPathMock = mock(Path.class);
		when(campaignRootMock.get("creater")).thenReturn(userDomainPathMock);
		Path userIdPathMock = mock(Path.class);
		when(userDomainPathMock.get("id")).thenReturn(userIdPathMock);

		Predicate userPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(userIdPathMock, 1L)).thenReturn(
				userPredicateMock);

		Collection<String> workflowTypes = new ArrayList<String>();
		workflowTypes.add(WorkflowEnum.STANDARD.toString());

		Predicate workflowPredicateMock = mock(Predicate.class);
		when(workflowTypeInPathMock.in(workflowTypes)).thenReturn(
				workflowPredicateMock);

		Predicate returnPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.and(userPredicateMock, workflowPredicateMock))
				.thenReturn(returnPredicateMock);

		Specification<Campaign> actual = CampaignSpecifications
				.creatorAndWorkflowTypeIn(1L, workflowTypes);
		Predicate actualPredicate = actual.toPredicate(campaignRootMock,
				criteriaQueryMock, criteriaBuilderMock);

		verify(campaignRootMock, times(1)).get("creater");
		verify(userDomainPathMock, times(1)).get("id");
		verify(criteriaBuilderMock, times(1)).equal(userIdPathMock, 1L);

		verify(campaignRootMock, times(1)).get("workflowType");
		verify(workflowTypeInPathMock, times(1)).in(workflowTypes);

		verify(criteriaBuilderMock, times(1)).and(userPredicateMock,
				workflowPredicateMock);
		verifyNoMoreInteractions(returnPredicateMock);

		verifyZeroInteractions(criteriaQueryMock, userPredicateMock,
				workflowPredicateMock, returnPredicateMock);

		assertEquals(returnPredicateMock, actualPredicate);

	}
	
	
	@Test
	public void testCheckerInvestigatorAndCampStateIn() {
		Path userDomainPathMock = mock(Path.class);
		when(campaignRootMock.get("investigator")).thenReturn(
				userDomainPathMock);
		Path userIdPathMock = mock(Path.class);
		when(userDomainPathMock.get("id")).thenReturn(userIdPathMock);

		Predicate userPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.equal(userIdPathMock, 1L)).thenReturn(
				userPredicateMock);

		Path campStatePathMockF = mock(Path.class);
		when(campaignRootMock.get("campState")).thenReturn(campStatePathMockF);
		Path campStatePathMock = mock(Path.class);
		when(campStatePathMockF.get("statusId")).thenReturn(campStatePathMock);

		Collection<String> campStates = new ArrayList<String>();
		campStates.add(CampaignState.DESIGN.getCode());
		campStates.add(CampaignState.WAIT_EXECUTE.getCode());

		Predicate campStatePredicateMock = mock(Predicate.class);
		when(campStatePathMock.in(campStates)).thenReturn(
				campStatePredicateMock);

		Predicate returnPredicateMock = mock(Predicate.class);
		when(criteriaBuilderMock.and(userPredicateMock, campStatePredicateMock))
				.thenReturn(returnPredicateMock);

		Specification<Campaign> actual = CampaignSpecifications
				.checkerInvestigatorAndCampStateIn(1L, campStates);
		Predicate actualPredicate = actual.toPredicate(campaignRootMock,
				criteriaQueryMock, criteriaBuilderMock);

		verify(campaignRootMock, times(1)).get("investigator");
		verify(userDomainPathMock, times(1)).get("id");
		verify(criteriaBuilderMock, times(1)).equal(userIdPathMock, 1L);

		verify(campaignRootMock, times(1)).get("campState");
		verify(campStatePathMockF, times(1)).get("statusId");
		verify(campStatePathMock, times(1)).in(campStates);

		verify(criteriaBuilderMock, times(1)).and(userPredicateMock,
				campStatePredicateMock);
		verifyNoMoreInteractions(returnPredicateMock);

		verifyZeroInteractions(criteriaQueryMock, userPredicateMock,
				campStatePredicateMock, returnPredicateMock);

		assertEquals(returnPredicateMock, actualPredicate);
	}

}
