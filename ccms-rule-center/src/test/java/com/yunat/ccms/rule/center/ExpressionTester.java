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
import com.yunat.ccms.rule.center.runtime.fact.Order;

public class ExpressionTester {
	public static void main(String[] args) {
		KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kBuilder.add(ResourceFactory.newClassPathResource("drools/test1.drl"), ResourceType.DRL);
		if (kBuilder.hasErrors()) {
			System.out.println("rule has error : ");
			for (KnowledgeBuilderError kbError : kBuilder.getErrors()) {
				System.out.println(kbError.getMessage());
			}
		}
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
		kb.addKnowledgePackages(kBuilder.getKnowledgePackages());
		StatefulKnowledgeSession session = kb.newStatefulKnowledgeSession();
		
		Order order = new Order();
		order.setTid("1023222");
		List<String> productList = Lists.newArrayList();
		productList.add("123");
		productList.add("234");
		order.setHasProducts(productList);
		session.insert(order);
		session.fireAllRules();
		session.dispose();
		
	}
}
