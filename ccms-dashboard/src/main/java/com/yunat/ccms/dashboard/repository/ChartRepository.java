package com.yunat.ccms.dashboard.repository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ChartRepository {
	private Logger logger = LoggerFactory.getLogger(ChartRepository.class);
	
	public static final String COMMON_FLOW = "common";
	
	public static final String DACU_FLOW = "dacu";
	
	public static final String UNIVERSAL_FLOW = "universal";
	
	
	
	@Autowired
	protected JdbcTemplate  jdbcTemplate;

	
	
	
	/**
	 * 根据活动状态返回图表数据 (dataRange  前一个月到今天)
	 * @param campaignStatus  活动状态
	 * @return
	 * @throws SQLException
	 */
	
	public List<Map<String,Object>> queryChartByCampaignStatus(String campaignStatus){
		Calendar calendar = Calendar.getInstance(); 
		String  afterDate= new  SimpleDateFormat( "yyyy-MM-dd").format(calendar.getTime()); 
		calendar.add(Calendar.MONTH, -1);    //前一个月
		String  beforeDate=new  SimpleDateFormat( "yyyy-MM-dd").format(calendar.getTime()); 
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select   date_format(created_time,'%Y-%m-%d')  as fullTime,count(camp_id) as CampCount ");
		sb.append(" from tb_campaign   where   DATE_FORMAT(created_time,'%Y-%m-%d')> ? ");
		sb.append(" and DATE_FORMAT(created_time,'%Y-%m-%d')<= ?   ");
		sb.append(" and workflow_type  in  ('"+COMMON_FLOW+"','"+DACU_FLOW+"','"+UNIVERSAL_FLOW+"')  ");
		sb.append(" and disabled =0  ");
		
		if(campaignStatus != null){
			sb.append(" and  camp_status =? ");
		}
		sb.append(" group by date_format(created_time,'%Y-%m-%d')  ");
		logger.info("活动趋势图" + sb.toString() + "beforeDate" + beforeDate + "afterDate"
				+ afterDate);
	    
	    return campaignStatus != null ? jdbcTemplate.queryForList(sb.toString(),
				 beforeDate, afterDate,campaignStatus) : jdbcTemplate
				.queryForList(sb.toString(), beforeDate, afterDate);
	
		
	}
}
