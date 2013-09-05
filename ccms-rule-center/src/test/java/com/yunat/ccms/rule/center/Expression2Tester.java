package com.yunat.ccms.rule.center;

import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.google.common.collect.Lists;
import com.yunat.ccms.rule.center.runtime.fact.Customer;
import com.yunat.ccms.rule.center.runtime.fact.Order;

public class Expression2Tester {
	public static void main(final String[] args) {
		final KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kBuilder.add(ResourceFactory.newClassPathResource("drools/test2.drl"), ResourceType.DRL);
		if (kBuilder.hasErrors()) {
			System.out.println("rule has error : ");
			for (final KnowledgeBuilderError kbError : kBuilder.getErrors()) {
				System.out.println(kbError.getMessage());
			}
		}

		final KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
		kb.addKnowledgePackages(kBuilder.getKnowledgePackages());
		final StatefulKnowledgeSession session = kb.newStatefulKnowledgeSession();

		final Order order = new Order();
		order.setTid("1023222");
		order.setReceiverLocation("吉林省,吉林市");
		final List<String> productList = Lists.newArrayList();
		productList.add("123");
		productList.add("234");
		order.setHasProducts(productList);

		final Customer customer = new Customer();
		customer.setCustomerType("1");
		order.setCustomer(customer);

		session.insert(order);
		session.fireAllRules();
		session.dispose();

	}
}
