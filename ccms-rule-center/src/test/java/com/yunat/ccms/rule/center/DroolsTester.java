package com.yunat.ccms.rule.center;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsTester {
	public static class User {
		private int money; // 手中的钱
		private int kp; // 空瓶数
		private int totals; // 喝掉的瓶数

		public int getMoney() {
			return money;
		}

		public void setMoney(int money) {
			this.money = money;
		}

		public int getKp() {
			return kp;
		}

		public void setKp(int kp) {
			this.kp = kp;
		}

		public int getTotals() {
			return totals;
		}

		public void setTotals(int totals) {
			this.totals = totals;
		}

	}
	
	public static void main(String[] args) throws Exception {
		KnowledgeBuilder kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kBuilder.add(ResourceFactory.newClassPathResource("drools/a.drl"), ResourceType.DRL);
		if (kBuilder.hasErrors()) {
			System.out.println("rule has error : ");
			for (KnowledgeBuilderError kbError : kBuilder.getErrors()) {
				System.out.println(kbError.getMessage());
			}
		}
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
		kb.addKnowledgePackages(kBuilder.getKnowledgePackages());
		StatefulKnowledgeSession session = kb.newStatefulKnowledgeSession();
		User user = new User();
		user.setMoney(50);
		session.insert(user);
		session.fireAllRules();
		session.dispose();
	}
}
