package com.yunat.ccms.configuration.describe;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DescribeHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		 registerBeanDefinitionParser("component-scan", new DescribeScanDefinitionParser());
	}

}