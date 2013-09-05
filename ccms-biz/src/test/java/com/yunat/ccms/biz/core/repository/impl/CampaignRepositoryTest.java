package com.yunat.ccms.biz.core.repository.impl;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.core.test.AbstractJunit4SpringContextBaseTests;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.repository.impl.CampaignRepositoryImpl;
import com.yunat.ccms.biz.support.cons.WorkflowEnum;
import com.yunat.ccms.biz.vo.CampaignForWeb;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CampaignRepositoryTest extends AbstractJunit4SpringContextBaseTests {

	private static final int PAGE_INDEX = 0;
	private static final int CAMP_COUNT = 5;

	private CampaignRepositoryImpl repository;

	private SimpleJpaRepository campaignRepositoryMock;

	@Before
	public void setUp() {
		repository = new CampaignRepositoryImpl();

		campaignRepositoryMock = mock(SimpleJpaRepository.class);
		repository.setCampaignRepository(campaignRepositoryMock);
	}

	@Test
	public void testFindByFilterCampaignFilterPageable() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByMySelfFilter() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByFilterCampaignSimpleFilterPageable() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByInvestigatorAndCampStateInExtends() {
		final User user = new User();
		user.setId(1L);

		final Collection<CampaignStatus> campStatus = new ArrayList<CampaignStatus>();
		final CampaignStatus cs = new CampaignStatus();
		cs.setStatusId("A1");
		campStatus.add(cs);

		final CampaignStatus css = new CampaignStatus();
		css.setStatusId("A3");
		campStatus.add(css);
		final Pageable pageable = new PageRequest(PAGE_INDEX, CAMP_COUNT);

		final List<Campaign> expected = new ArrayList<Campaign>();
		final Page foundPage = new PageImpl<Campaign>(expected);

		when(campaignRepositoryMock.findAll(any(Specification.class), any(Pageable.class))).thenReturn(foundPage);

		final Page<Campaign> actual = repository.findByInvestigatorAndCampStateInExtends(user, campStatus, pageable);
		final ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
		verify(campaignRepositoryMock, times(1)).findAll(any(Specification.class), pageSpecificationArgument.capture());
		verifyNoMoreInteractions(campaignRepositoryMock);

		final Pageable pageSpecification = pageSpecificationArgument.getValue();
		assertEquals(PAGE_INDEX, pageSpecification.getPageNumber());
		assertEquals(CAMP_COUNT, pageSpecification.getPageSize());
		assertEquals(expected, actual.getContent());
	}

	@Test
	public void testFindByInvestigatorAndCampStateIn() {
		final User user = new User();
		user.setId(1L);

		final Collection<CampaignStatus> campStatus = new ArrayList<CampaignStatus>();
		final CampaignStatus cs = new CampaignStatus();
		cs.setStatusId("A1");
		campStatus.add(cs);

		final CampaignStatus css = new CampaignStatus();
		css.setStatusId("A3");
		campStatus.add(css);
		final Pageable pageable = new PageRequest(PAGE_INDEX, CAMP_COUNT);

		final List<Campaign> expected = new ArrayList<Campaign>();
		final Page foundPage = new PageImpl<Campaign>(expected);

//		final Map<String, Object> aclMap = new HashMap<String, Object>();
//		aclMap.put(AclQueryParameterHelper.PARAM_PERMISSIONS, null);
//		aclMap.put(AclQueryParameterHelper.PARAM_USERNAME, SecurityHelper.getUsername());
//		aclMap.put(AclQueryParameterHelper.PARAM_CLASSNAME, Campaign.class.getName());
//		final Set<Long> aclMask = Sets.newHashSet();
//		aclMask.add(new Long(ADMINISTRATION.getMask()));
//		aclMask.add(new Long(READ.getMask()));
//		aclMap.put(AclQueryParameterHelper.PARAM_ACLMASK, aclMask);
//
//		final AclQueryParameterHelper aclQueryParameterHelperMock = mock(AclQueryParameterHelper.class);
//		when(AclQueryParameterHelper.getQueryParametersHasReadACL(Campaign.class)).thenReturn(aclMap);

		final Page<CampaignForWeb> actual = repository.findByInvestigatorAndCampStateIn(user, campStatus, pageable);
	}

	@Test
	public void testFindByCampState() {
		final CampaignStatus cs = new CampaignStatus();
		cs.setStatusId("A1");

		final Sort sort = new Sort(Direction.ASC, "campId");
		final List<Campaign> expected = new ArrayList<Campaign>();
		when(campaignRepositoryMock.findAll(any(Specification.class), any(Sort.class))).thenReturn(expected);

		final List<Campaign> actual = repository.findByCampState(cs, sort);

		final ArgumentCaptor<Sort> sortSpecificationArgument = ArgumentCaptor.forClass(Sort.class);
		verify(campaignRepositoryMock, times(1)).findAll(any(Specification.class), sortSpecificationArgument.capture());

		final Sort sortSpecification = sortSpecificationArgument.getValue();
		assertEquals(Sort.Direction.ASC, sortSpecification.getOrderFor("campId").getDirection());
		assertEquals(expected, actual);
	}

	@Test
	public void testFindByCreatorAndWorkflowTypeIn() {
		final User user = new User();
		user.setId(1L);

		final Collection<String> workflowTypes = new ArrayList<String>();
		workflowTypes.add(WorkflowEnum.STANDARD.toString());

		final List<Campaign> expected = new ArrayList<Campaign>();
		when(campaignRepositoryMock.findAll(any(Specification.class))).thenReturn(expected);

		final List<Campaign> actual = repository.findByCreatorAndWorkflowTypeIn(user, workflowTypes);

		verify(campaignRepositoryMock, times(1)).findAll(any(Specification.class));
		assertEquals(expected, actual);
	}

	@Test
	public void testFindAll() {
		final Sort sort = new Sort(Direction.ASC, "campId");
		final List<Campaign> expected = new ArrayList<Campaign>();
		when(campaignRepositoryMock.findAll(any(Sort.class))).thenReturn(expected);

		final List<Campaign> actual = repository.findAll(sort);

		verify(campaignRepositoryMock, times(1)).findAll(any(Sort.class));
		assertEquals(expected, actual);
	}

	@Test
	public void testFindByCampId() {
		final Long campId = 1L;
		final Campaign expected = new Campaign();
		when(campaignRepositoryMock.findOne(any(Serializable.class))).thenReturn(expected);

		final Campaign actual = repository.findByCampId(campId);

		verify(campaignRepositoryMock, times(1)).findOne(any(Serializable.class));
		assertEquals(expected, actual);
	}

	@Test
	public void testSaveOrUpdate() {
		final Campaign campaign = new Campaign();
		campaign.setCampName("ABC");
		final CampaignStatus cs = new CampaignStatus();
		cs.setStatusId(CampaignState.DESIGN.getCode());
		campaign.setCampStatus(cs);
		repository.saveOrUpdate(campaign);

		final ArgumentCaptor<Campaign> campArgument = ArgumentCaptor.forClass(Campaign.class);
		verify(campaignRepositoryMock, times(1)).save(campArgument.capture());
		final Campaign camp = campArgument.getValue();
		assertEquals(camp, campaign);
	}

	@Test
	public void testDelete() {
		final Campaign deleted = new Campaign();
		deleted.setCampId(1L);
		deleted.setCampName("ABC");
		final CampaignStatus cs = new CampaignStatus();
		cs.setStatusId(CampaignState.DESIGN.getCode());
		deleted.setCampStatus(cs);

		repository.delete(deleted);

	}

}