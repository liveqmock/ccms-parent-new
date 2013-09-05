package com.yunat.ccms.biz.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunat.ccms.auth.login.LoginInfoHolder;
import com.yunat.ccms.auth.user.User;
import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.CampaignStatus;
import com.yunat.ccms.biz.repository.CampaignRepository;
import com.yunat.ccms.biz.repository.specification.CampaignSpecifications;
import com.yunat.ccms.biz.support.filter.CampaignFilter;
import com.yunat.ccms.biz.support.filter.CampaignSimpleFilter;
import com.yunat.ccms.biz.vo.CampaignForWeb;
import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;

@Repository
public class CampaignRepositoryImpl implements CampaignRepository {
	private static final String select = "Select ";
	private static final String selection = "camp_id campId, camp_name campName, "
			+ "date_format(created_time,'%Y-%c-%d %H:%i:%s') createdTime, "
			+ "date_format(start_time,'%Y-%c-%d') startTime, " + "date_format(end_time,'%Y-%c-%d') endTime, "
			+ "camp_status status, edition, pic_url picUrl, "
			+ "(select login_name from tb_sysuser where id = camp_.creater) creater, "
			+ "(select login_name from tb_sysuser where id = camp_.investigator) investigator, "
			+ "(select prog_name from tb_program where prog_id = camp_.prog_id) progName ";

	private static final String from = "From tb_campaign camp_ ";

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcPaginationHelper jdbcPaginationHelper;

	private SimpleJpaRepository<Campaign, Long> campaignRepository;

	@Override
	public Page<CampaignForWeb> findByFilter(final CampaignFilter campFilter, final Pageable pageable) {
		return findByMySelfFilter(campFilter, null, pageable);
	}

	@Override
	public Page<CampaignForWeb> findByMySelfFilter(final CampaignFilter campFilter, final User user,
			final Pageable pageable) {

		final Map<String, Object> paramMap = Maps.newHashMap();
		final StringBuilder join = new StringBuilder();
		final StringBuilder where = new StringBuilder("where camp_.workflow_type = :workflowType ");
		paramMap.put("workflowType", campFilter.getWorkflowType());
		where.append("and camp_.disabled = 0 ");
		where.append("and camp_.edition = :edition ");
		paramMap.put("edition", LoginInfoHolder.getProductEdition().name());

		if (StringUtils.isNotBlank(campFilter.getPlatCode())) {
			where.append("and camp_.plat_code = :platCode ");
			paramMap.put("platCode", campFilter.getPlatCode());
		}

		if (null != user) {
			where.append("and camp_.creater = :creater ");
			paramMap.put("creater", user.getId());
		}

		if (StringUtils.isNotBlank(campFilter.getCampState())) {
			where.append("and camp_.camp_status = :campStatus ");
			paramMap.put("campStatus", campFilter.getCampState());
		}

		if (StringUtils.isNotBlank(campFilter.getCampCategory())) {
			where.append("and camp_.camp_type in (:campType) ");
			final List<Integer> campInts = new ArrayList<Integer>();
			final String[] campTypes = campFilter.getCampCategory().split(",");
			for (final String str : campTypes) {
				if (StringUtils.isNotBlank(str)) {
					final Integer ints = Integer.valueOf(str);
					campInts.add(ints);
				}
			}
			paramMap.put("campType", campInts);
		}

		if (StringUtils.isNotBlank(campFilter.getFilterName())) {
			where.append("and camp_.camp_name = :campName ");
			paramMap.put("campName", campFilter.getFilterName());
		}

		if (null != campFilter.getProgramId()) {
			where.append("and camp_.prog_id = :progId ");
			paramMap.put("progId", campFilter.getProgramId());
		}

		if (StringUtils.isNotBlank(campFilter.getKeywords())) {
			final String regex = "(.*" + campFilter.getKeywords().trim().replaceAll("[ ]+", ".*)|(.*") + ".*)";
			where.append("and ( camp_.camp_id regexp ").append("\"").append(regex).append("\"")
					.append(" or camp_.camp_name regexp ").append("\"").append(regex).append("\" )");
		}

		final StringBuffer sqlFetchRows = new StringBuffer(select).append(selection).append(from)
				.append(join.toString()).append(where);
		return convert(jdbcPaginationHelper.queryForMap(sqlFetchRows.toString(), paramMap, pageable), pageable);
	}

	private static Page<CampaignForWeb> convert(final Page<Map<String, Object>> page, final Pageable pageable) {
		final List<CampaignForWeb> list = convert(page.getContent());
		return new PageImpl<CampaignForWeb>(list, pageable, page.getTotalElements());
	}

	private static List<CampaignForWeb> convert(final List<Map<String, Object>> list) {
		final List<CampaignForWeb> rt = Lists.newArrayListWithExpectedSize(list.size());
		for (final Map<String, Object> m : list) {
			rt.add(convert(m));
		}
		return rt;
	}

	private static CampaignForWeb convert(final Map<String, Object> map) {
		final CampaignForWeb c = new CampaignForWeb();
		c.setCampId((Long) map.get("campId"));
		c.setCampName((String) map.get("campName"));
		c.setCreatedTime((String) map.get("createdTime"));
		c.setCreater((String) map.get("creater"));
		c.setEdition((String) map.get("edition"));
		c.setEndTime((String) map.get("endTime"));
		c.setInvestigator((String) map.get("investigator"));
		c.setPicUrl((String) map.get("picUrl"));
		c.setProgName((String) map.get("progName"));
		c.setStartTime((String) map.get("startTime"));
		c.setStatus((String) map.get("status"));
		return c;
	}

	@Override
	public Page<CampaignForWeb> findByFilter(final CampaignSimpleFilter filter, final Pageable pageable) {

		final Map<String, Object> paramMap = Maps.newHashMap();
		final StringBuilder join = new StringBuilder();
		final StringBuilder where = new StringBuilder("where camp_.workflow_type = :workflowType ");
		paramMap.put("workflowType", filter.getWorkflowType());
		where.append("and camp_.disabled = 0 ");
		where.append("and camp_.edition = :edition ");
		paramMap.put("edition", LoginInfoHolder.getProductEdition().name());

		if (StringUtils.isNotBlank(filter.getPlatCode())) {
			where.append("and camp_.plat_code = :platCode ");
			paramMap.put("platCode", filter.getPlatCode());
		}

		if (StringUtils.isNotBlank(filter.getFilterName())) {
			where.append("and camp_.camp_name = :campName ");
			paramMap.put("campName", filter.getFilterName());
		}

		final StringBuffer sqlFetchRows = new StringBuffer(select).append(selection).append(from)
				.append(join.toString()).append(where);
		final Page<Map<String, Object>> page = jdbcPaginationHelper.queryForMap(sqlFetchRows.toString(), paramMap,
				pageable);
		return convert(page, pageable);
	}

	@Override
	public Page<CampaignForWeb> findByInvestigatorAndCampStateIn(final User user,
			final Collection<CampaignStatus> campStatus, final Pageable pageable) {
		final Collection<String> campState = new ArrayList<String>();
		for (final CampaignStatus cs : campStatus) {
			campState.add(cs.getStatusId());
		}

		final Map<String, Object> paramMap = Maps.newHashMap();
		final StringBuilder join = new StringBuilder();
		final StringBuilder where = new StringBuilder("where 1 = 1 ");
		where.append("and camp_.disabled = 0 ");

		if (null != user) {
			where.append("and camp_.investigator = :investigator ");
			paramMap.put("investigator", user.getId());
		}

		if (!CollectionUtils.isEmpty(campState)) {
			where.append("and camp_.camp_status in (:campStatus) ");
			paramMap.put("campStatus", campState);
		}

		final StringBuffer sqlFetchRows = new StringBuffer(select).append(selection).append(from)
				.append(join.toString()).append(where);
		final Page<Map<String, Object>> page = jdbcPaginationHelper.queryForMap(sqlFetchRows.toString(), paramMap,
				pageable);
		return convert(page, pageable);
	}

	public Page<Campaign> findByInvestigatorAndCampStateInExtends(final User user,
			final Collection<CampaignStatus> campStatus, final Pageable pageable) {
		final Collection<String> campState = new ArrayList<String>();
		for (final CampaignStatus cs : campStatus) {
			campState.add(cs.getStatusId());
		}

		return campaignRepository.findAll(
				CampaignSpecifications.checkerInvestigatorAndCampStateIn(user.getId(), campState), pageable);
	}

	@Override
	public List<Campaign> findByCampState(final CampaignStatus campState, final Sort sort) {
		return campaignRepository.findAll(CampaignSpecifications.campState(campState.getStatusId()), sort);
	}

	@Override
	public List<Campaign> findByCreatorAndWorkflowTypeIn(final User user, final Collection<String> workflowTypes) {
		return campaignRepository.findAll(CampaignSpecifications.creatorAndWorkflowTypeIn(user.getId(), workflowTypes));
	}

	@Override
	public List<Campaign> findByCampIdIn(final List<Long> idList) {
		return campaignRepository.findAll(CampaignSpecifications.campIdIn(idList));
	}

	@Override
	public Campaign findByCampId(final Long campId) {
		return campaignRepository.findOne(campId);
	}

	@Override
	public void saveOrUpdate(final Campaign campaign) {
		campaignRepository.saveAndFlush(campaign);
	}

	@Override
	public void delete(final Campaign campaign) {
		campaignRepository.delete(campaign);
		campaignRepository.flush();
	}

	@Override
	public List<Campaign> findAll(final Sort sort) {
		return campaignRepository.findAll(sort);
	}

	@Override
	public List<Campaign> findByCampStateIn(final Collection<CampaignStatus> campStates) {
		final Collection<String> colls = new ArrayList<String>();
		for (final CampaignStatus cs : campStates) {
			colls.add(cs.getStatusId());
		}
		return campaignRepository.findAll(CampaignSpecifications.campStateIn(colls));
	}

	@Override
	public List<Campaign> findByCampaignName(final String campaignName) {
		return campaignRepository.findAll(CampaignSpecifications.hasCampaignName(campaignName));
	}

	public void setCampaignRepository(final SimpleJpaRepository<Campaign, Long> campaignRepository) {
		this.campaignRepository = campaignRepository;
	}

	/**
	 * An initialization method which is run after the bean has been
	 * constructed. This ensures that the entity manager is injected before we
	 * try to use it.
	 */
	@PostConstruct
	public void init() {
		final JpaEntityInformation<Campaign, Long> campaignEntityInfo = new JpaMetamodelEntityInformation<Campaign, Long>(
				Campaign.class, entityManager.getMetamodel());
		campaignRepository = new SimpleJpaRepository<Campaign, Long>(campaignEntityInfo, entityManager);
	}

}