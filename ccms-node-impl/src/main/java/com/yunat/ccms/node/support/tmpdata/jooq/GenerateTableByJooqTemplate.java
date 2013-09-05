package com.yunat.ccms.node.support.tmpdata.jooq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.annotation.ResultType;
import com.yunat.ccms.node.spi.NodeTemporaryDataRegistry;

@Component
public class GenerateTableByJooqTemplate {
	private static Logger logger = LoggerFactory.getLogger(GenerateTableByJooqTemplate.class);

	@Autowired
	@Qualifier("namedParameterJdbcTemplate")
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private NodeTemporaryDataRegistry registry;

	public String create(Class<? extends AbstractJooqTmplTable> jooqTemplate, Long jobId, Long subjobId, String suffix) {
		AbstractJooqTmplTable template = null;
		try {
			template = (AbstractJooqTmplTable) Class.forName(jooqTemplate.getName()).newInstance();
		} catch (Exception e) {
			logger.info("instance jooq template happened exceptioon : {}", e);
		}

		String templateTableName = generatJooqTemplateTableName(subjobId, suffix, template);
		StringBuffer sql = new StringBuffer();
		sql.append("drop table if exists ").append(templateTableName).append(";");
		logger.info("{}", sql.toString());
		namedParameterJdbcTemplate.getJdbcOperations().execute(sql.toString());

		String ddlSQL = generateConcreteDDL(template.getDDL(), templateTableName);
		logger.info("{}", ddlSQL);
		namedParameterJdbcTemplate.getJdbcOperations().execute(ddlSQL);
		registry.register(jobId, templateTableName, ResultType.TABLE.toString());
		return templateTableName;
	}

	private String generatJooqTemplateTableName(Long subjobId, String suffix, AbstractJooqTmplTable templateName) {
		String suffixFull = null;
		if (StringUtils.isEmpty(suffix)) {
			suffixFull = "_" + subjobId;
		} else {
			suffixFull = "_" + subjobId + "_" + (suffix.startsWith("_") ? suffix.replaceFirst("_", "") : suffix);
		}

		return templateName.getName() + suffixFull;
	}

	private String generateConcreteDDL(String ddl, String templateTableName) {
		Pattern p = Pattern.compile("^[ ]*CREATE[ ]+TABLE[ ]+[_a-zA-Z0-9?]+", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE);
		Matcher m = p.matcher(ddl);
		if (m.find()) {
			ddl = m.replaceFirst("CREATE TABLE " + templateTableName);
		}
		return ddl;
	}
}
