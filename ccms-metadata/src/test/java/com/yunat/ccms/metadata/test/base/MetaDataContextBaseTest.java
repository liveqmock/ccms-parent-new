package com.yunat.ccms.metadata.test.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.support.GenericXmlContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations = { "classpath:config/spring/spring-servlet.xml",
		"classpath:config/spring/applicationContext.xml" }, loader = GenericXmlContextLoader.class)
@TransactionConfiguration(defaultRollback = false)
public abstract class MetaDataContextBaseTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
