package com.yunat.ccms.dashboard.repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CampaignRepository {

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 通过活动状态和节点状态查询节点信息
	 * 
	 * @param campaignStatus
	 * @param nodeStatus
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryNodeByComposite(String campaignStatus, String nodeStatus) {
		return null;
	}

	/**
	 * 通过活动状态查询活动信息
	 * 
	 * @param campaignStatus
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryCampaignByStatus(String campaignStatus) {
		StringBuffer sb = new StringBuffer();
		sb.append("select camp_id,camp_name ,camp_status, created_time  ");
		sb.append("from tb_campaign ");
		sb.append("where  camp_status = ?  and  disabled = ?  ");
		sb.append("order by  created_time desc ");
		return jdbcTemplate.queryForList(sb.toString(), campaignStatus, false);
	}

	/**
	 * 查询错误活动信息
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryCampaignByErrorStatus(String errorSubStatusCode, Date beforeDate,
			Date afterDate) {

		// Date是可变对象 进行保护拷贝
		Date beforeDateClone = new Date(beforeDate.getTime());
		Date afterDateClone = new Date(afterDate.getTime());

		StringBuffer sb = new StringBuffer();
		sb.append(" select camp.camp_id, camp.camp_name, camp.camp_status, camp.start_time, max(subjob.endtime) as endtime  ");
		sb.append(" from twf_log_subjob as subjob, tb_campaign as camp  ");
		sb.append(" where (subjob.status in (?) and  camp.camp_id = subjob.camp_id and camp.disabled = ?   ");
		sb.append(" and subjob.starttime between ? and ?) ");
		sb.append(" group by camp.camp_id, camp.camp_name order by subjob.endtime desc ");

		return jdbcTemplate.queryForList(sb.toString(), errorSubStatusCode, 0, beforeDateClone, afterDateClone);
	}

	public List<Map<String, Object>> nodeInfoByComposite(String[] campStatusArray, String[] subjobStatusArray) {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.camp_id, c.camp_name ,c.camp_status, c.created_time  ");
		sb.append("from   tb_campaign c, twf_log_subjob  s  ");
		sb.append("where  c.camp_id = s.camp_id   and  c.disabled =?   and c.camp_status in (?) and s.status	in (?)  ");
		return jdbcTemplate.queryForList(sb.toString(), 0, StringUtils.join(campStatusArray, ","),
				StringUtils.join(subjobStatusArray, ","));

	}

}
