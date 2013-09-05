package com.yunat.ccms.channel.support.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.yunat.ccms.channel.support.cons.EnumBlackList;
import com.yunat.ccms.channel.support.domain.BlackList;
import com.yunat.ccms.channel.support.domain.BlackListEntityManager;
import com.yunat.ccms.channel.support.domain.EmailBlackList;
import com.yunat.ccms.channel.support.domain.MemberBlackList;
import com.yunat.ccms.channel.support.domain.MobileBlackList;
import com.yunat.ccms.channel.support.repository.EmailBlackListRepository;
import com.yunat.ccms.channel.support.repository.MemberBlackListRepository;
import com.yunat.ccms.channel.support.repository.MobileBlackListRepository;
import com.yunat.ccms.core.support.cons.TableConstant;

/**
 * 黑名单业务实现类
 * 
 * @author kevin.jiang 2013-4-24
 */
@Service
public class BlackListServiceImpl implements BlackListService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmailBlackListRepository emailBlackListRepository;

	@Autowired
	MobileBlackListRepository mobileBlackListRepository;

	@Autowired
	MemberBlackListRepository memberBlackListRepository;

	@Autowired
	BlackListEntityManager blackListEntityManager;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public BlackList loadBlackList(String value, EnumBlackList type) {

		switch (type) {

		case EMAIL:
			return emailBlackListRepository.findOne(value);

		case MOBILE:
			return mobileBlackListRepository.findOne(value);

		case MEMBER:
			return memberBlackListRepository.findOne(value);

		default:
			return null;
		}
	}

	@Override
	public void createBlackList(List<String> values, EnumBlackList type) throws Exception {

		if (values == null || values.size() <= 0) {

			return;
		}

		// 去重
		values = removeRepeatBlackList(values, type);

		switch (type) {

		case EMAIL:
			List<EmailBlackList> emailList = new ArrayList<EmailBlackList>();
			for (String key : values) {

				emailList.add((EmailBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			emailBlackListRepository.save(emailList);
			break;

		case MOBILE:
			List<MobileBlackList> mobileList = new ArrayList<MobileBlackList>();
			for (String key : values) {

				mobileList.add((MobileBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			mobileBlackListRepository.save(mobileList);
			break;

		case MEMBER:
			List<MemberBlackList> memberList = new ArrayList<MemberBlackList>();
			for (String key : values) {

				memberList.add((MemberBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			memberBlackListRepository.save(memberList);
			break;

		default:
			;
		}

	}

	/**
	 * 去掉重复的数据，不保存。 用于界面录入的数据去重
	 * 
	 * @param values
	 * @param type
	 *            类型 邮箱，手机号，会员名称
	 * @return 去重后的清单
	 */
	@SuppressWarnings("rawtypes")
	private List<String> removeRepeatBlackList(List<String> values, EnumBlackList type) {

		// List自身去重
		Set<String> valueSet = new HashSet<String>();
		for (String contact : values) {

			if (!valueSet.contains(contact)) {

				valueSet.add(contact);
			}
		}

		// 跟数据库的记录去重
		List list = null;
		switch (type) {

		case EMAIL:
			list = emailBlackListRepository.findByValues(new ArrayList<String>(valueSet));
			break;

		case MOBILE:

			list = mobileBlackListRepository.findByValues(new ArrayList<String>(valueSet));
			break;

		case MEMBER:

			list = memberBlackListRepository.findByValues(new ArrayList<String>(valueSet));
			break;

		default:
			;
		}

		if (list == null || list.size() <= 0) {

			return new ArrayList<String>(valueSet);
		}

		for (int i = 0; i < list.size(); i++) {

			String contact = ((BlackList) list.get(i)).getContact();
			valueSet.remove(contact);
		}
		return new ArrayList<String>(valueSet);
	}

	@Override
	public void deleteBlackList(List<String> values, EnumBlackList type) throws Exception {

		if (values == null || values.size() <= 0) {

			return;
		}

		switch (type) {
		case EMAIL:
			List<EmailBlackList> emailList = new ArrayList<EmailBlackList>();
			for (String key : values) {

				emailList.add((EmailBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			emailBlackListRepository.deleteInBatch(emailList);
			break;

		case MOBILE:
			List<MobileBlackList> mobileList = new ArrayList<MobileBlackList>();
			for (String key : values) {

				mobileList.add((MobileBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			mobileBlackListRepository.deleteInBatch(mobileList);
			break;

		case MEMBER:
			List<MemberBlackList> memberList = new ArrayList<MemberBlackList>();
			for (String key : values) {

				memberList.add((MemberBlackList) blackListEntityManager.createManualEntityByValue(type, key));
			}
			memberBlackListRepository.deleteInBatch(memberList);
			break;

		default:
			;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page getBlackListAll(int pageIndex, int pageSize, String filterValue, EnumBlackList type, String sortName,
			String sortOrder) {

		// 分页页码从1开始
		if (pageIndex < 1) {

			return null;
		}

		Sort.Direction direct = Sort.Direction.ASC;

		if ("desc".equals(sortOrder)) {

			direct = Sort.Direction.DESC;
		}

		switch (type) {

		case EMAIL:
			if (StringUtils.isEmpty(filterValue)) {
				return emailBlackListRepository.findAll(new PageRequest(pageIndex - 1, pageSize, direct,
						new String[] { sortName }));
			} else {
				return emailBlackListRepository.findAll(queryEmail(filterValue), new PageRequest(pageIndex - 1,
						pageSize, direct, new String[] { sortName }));
			}

		case MOBILE:
			if (StringUtils.isEmpty(filterValue)) {
				return mobileBlackListRepository.findAll(new PageRequest(pageIndex - 1, pageSize, direct,
						new String[] { sortName }));
			} else {
				return mobileBlackListRepository.findAll(queryMobile(filterValue), new PageRequest(pageIndex - 1,
						pageSize, direct, new String[] { sortName }));
			}

		case MEMBER:
			if (StringUtils.isEmpty(filterValue)) {
				return memberBlackListRepository.findAll(new PageRequest(pageIndex - 1, pageSize, direct,
						new String[] { sortName }));
			} else {
				return memberBlackListRepository.findAll(queryMember(filterValue), new PageRequest(pageIndex - 1,
						pageSize, direct, new String[] { sortName }));
			}

		default:
			return null;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getBlackListAll(String filterValue, EnumBlackList type) {

		switch (type) {

		case EMAIL:

			if (StringUtils.isEmpty(filterValue)) {
				return emailBlackListRepository.findAll();
			} else {
				return emailBlackListRepository.findByContactValue(filterValue);
			}

		case MOBILE:
			if (StringUtils.isEmpty(filterValue)) {

				return mobileBlackListRepository.findAll();
			} else {
				return mobileBlackListRepository.findByContactValue(filterValue);
			}

		case MEMBER:
			if (StringUtils.isEmpty(filterValue)) {

				return memberBlackListRepository.findAll();
			} else {
				return memberBlackListRepository.findByContactValue(filterValue);
			}

		default:
			return null;
		}
	}

	public Long getNewId() {
		return new Date().getTime();
	}

	public static Specification<EmailBlackList> queryEmail(final String value) {
		return new Specification<EmailBlackList>() {

			@Override
			public Predicate toPredicate(Root<EmailBlackList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get("contact").as(String.class), "%" + value + "%");
			}
		};
	}

	public static Specification<MobileBlackList> queryMobile(final String value) {
		return new Specification<MobileBlackList>() {

			@Override
			public Predicate toPredicate(Root<MobileBlackList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get("contact").as(String.class), "%" + value + "%");
			}
		};
	}

	public static Specification<MemberBlackList> queryMember(final String value) {
		return new Specification<MemberBlackList>() {

			@Override
			public Predicate toPredicate(Root<MemberBlackList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get("contact").as(String.class), "%" + value + "%");
			}
		};
	}

	/**
	 * 手动导入数据
	 * 
	 * @param uploadUrl
	 *            上传文件服务器路径
	 * @param type
	 *            类型：邮件EMAIL，手机MOBILE，会员MEMBER
	 * @return
	 */
	@Override
	public List<Map<String, Object>> uploadBlackList(String uploadUrl, EnumBlackList type) throws Exception {

		uploadUrl = uploadUrl.replace("\\", "\\\\");

		Long newId = getNewId();
		String tempTableName = "";
		String importTableName = "";
		String redTableName = "";
		String columnName = "";

		switch (type) {
		case EMAIL:
			importTableName = TableConstant.EDM_BLACKLIST;
			tempTableName = TableConstant.TMP_EDM_MANAGER_IMPORT;
			redTableName = TableConstant.EDM_REDLIST;
			columnName = "email";
			break;
		case MOBILE:
			importTableName = TableConstant.MOBILE_BLACKLIST;
			tempTableName = TableConstant.TMP_MOBILE_MANAGER_IMPORT;
			redTableName = TableConstant.MOBILE_REDLIST;
			columnName = "mobile";
			break;
		case MEMBER:
			importTableName = TableConstant.MEMBER_BLACKLIST;
			tempTableName = TableConstant.TMP_MEMBER_MANAGER_IMPORT;
			columnName = "customerno";
			break;
		default:
			;
		}

		// 创建临时表
		StringBuffer createTableSql = new StringBuffer();
		createTableSql.append("DROP TABLE if exists ").append(tempTableName).append(newId).append(";");
		createTableSql.append("create table ").append(tempTableName).append(newId);
		if ("email".equals(columnName)) {
			createTableSql.append(" ( " + columnName + " varchar(100))");
		} else if ("mobile".equals(columnName)) {
			createTableSql.append(" ( " + columnName + " varchar(20))");
		} else {
			createTableSql.append(" ( " + columnName + " varchar(50))");
		}

		logger.info("creatSql :" + createTableSql.toString());
		this.jdbcTemplate.execute(createTableSql.toString());

		StringBuffer copySql = new StringBuffer();
		copySql.append("LOAD DATA LOCAL INFILE '").append(uploadUrl).append("' INTO TABLE ").append(tempTableName)
				.append(newId).append(" FIELDS TERMINATED BY ',' LINES TERMINATED BY '\r\n'");
		logger.info("copySql:" + copySql.toString());
		this.jdbcTemplate.execute(copySql.toString());

		// 查询要上传的数据与 黑名单，红名单中相重复数据、
		StringBuffer repeat = new StringBuffer();
		repeat.append("select distinct a.").append(columnName).append(" from ");
		repeat.append(tempTableName).append(newId);
		repeat.append(" a left join ").append(importTableName);
		repeat.append(" b on trim(a.").append(columnName).append(") = trim(b.").append(columnName).append(")");
		if (!"".equals(redTableName)) {
			repeat.append(" left join ").append(redTableName).append(" c on trim(a.").append(columnName)
					.append(") = trim(c.").append(columnName).append(") ");
			repeat.append(" where b.").append(columnName).append(" is not null ");
			repeat.append("or c.").append(columnName).append(" is not null");
		} else {
			repeat.append(" where b.").append(columnName).append(" is not null ");
		}
		List<Map<String, Object>> repeatMobles = this.jdbcTemplate.queryForList(repeat.toString());

		// 添加语句
		StringBuffer insertTableSql = new StringBuffer();
		insertTableSql.append("insert into ").append(importTableName);
		insertTableSql.append(" select distinct").append(" a.").append(columnName).append(",").append("'")
				.append(BlackListEntityManager.IMPORT).append("',").append(" now()").append(" from ")
				.append(tempTableName).append(newId);
		insertTableSql.append(" a left join ").append(importTableName).append(" b ")
				.append(" on trim(a." + columnName + ") = trim(b." + columnName + ")");
		if (!"".equals(redTableName)) {
			insertTableSql.append(" left join ").append(redTableName).append(" c on trim(a.").append(columnName)
					.append(") = trim(c.").append(columnName).append(") ");
			insertTableSql.append(" where b." + columnName + " is null ");
			insertTableSql.append("and c.").append(columnName).append(" is null");
		} else {
			insertTableSql.append(" where b." + columnName + " is null ");
		}

		logger.info("insertSql:" + insertTableSql.toString());

		this.jdbcTemplate.execute(insertTableSql.toString());

		StringBuffer dropTableSql = new StringBuffer();
		dropTableSql.append(" drop table if exists  ").append(tempTableName).append(newId);
		logger.info("drop table :" + dropTableSql.toString());
		this.jdbcTemplate.execute(dropTableSql.toString());

		return repeatMobles;
	}
}
