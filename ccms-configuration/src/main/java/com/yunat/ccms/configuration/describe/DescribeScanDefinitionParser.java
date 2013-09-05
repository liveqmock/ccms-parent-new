package com.yunat.ccms.configuration.describe;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class DescribeScanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	private static int n = 0;

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String basePackage = element.getAttribute("base-package");
        if (StringUtils.hasText(basePackage)) {
            bean.addPropertyValue("basePackage", basePackage);
        }
	}

	@Override
	protected String resolveId(Element element,
			AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {
		String id = super.resolveId(element, definition, parserContext);
		if (StringUtils.hasText(id)) {
			return id;
		}
		return "describe_scan_id_" + n ++;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return DescribeScan.class;
	}

}