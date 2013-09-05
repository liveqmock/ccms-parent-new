package com.yunat.ccms.dashboard.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardChannelRepository {
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	/**
	 * @author yinwei
	 * @param channelType
	 *            渠道类型
	 * @param nowDate
	 *            当天时间
	 * @return
	 */
	public List<Map<String, Object>> queryChannelSendInfo(String channelType, String nowDate) {
		StringBuffer sqlScript = new StringBuffer();
		sqlScript
				.append("  SELECT c.camp_id campId, c.camp_name campName, n.node_id nodeId, n.value nodeName,s.output_count  sendNum, ");
		sqlScript
				.append("  case  when  s.output_count is null && (s.status=12||s.status=13||s.status=22||s.status=31||s.status=41||s.status=52)  then '发送失败'   ");
		sqlScript.append("  when s.output_count is null && s.status=10  then '待发送'    ");
		sqlScript.append("	when  s.output_count is null && s.status=11  then '发送中'  ");
		sqlScript
				.append("  when  s.output_count is not null && (s.status=21 || s.status=51) then  '已发送' end  sendStatus , ");
		sqlScript.append(" (case when s.plantime is not null then s.plantime else j.starttime end ) as plantime ");
		sqlScript
				.append("  FROM tb_campaign   c , twf_workflow  w,   twf_node n  ,  twf_log_job  j , twf_log_subjob  s  ");
		sqlScript.append("  where   c.workflow_id = w.workflow_id   and w.workflow_id = n.workflow_id  ");
		if (!"".equals(channelType)) {
			sqlScript.append("	and  n.type = ?  ");
		}
		sqlScript.append("  and c.camp_id = j.camp_id   and j.is_test = ?  ");
		sqlScript.append("  and j.job_id  = s.job_id ");
		sqlScript.append("  and n.node_id = s.node_id ");
		sqlScript
				.append("  and (case when s.plantime is null then date_format(s.starttime,'%Y-%m-%d') =  ?  when s.plantime is not null then date_format(s.plantime,'%Y-%m-%d') =  ? end)");

		Object[] args = channelType.equals("") ? new Object[] { false, nowDate, nowDate } : new Object[] { channelType,
				false, nowDate, nowDate };
		// TODO
		// List<Map<String, Object>> columnList =
		// jdbcTemplate.queryForList(sqlScript.toString(), args);
		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
		return columnList;
	}

}
