package com.yunat.ccms.tradecenter.support.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关联对象列表
 * 将两个相关联的对象绑定到一起
 * @author 李卫林
 *
 * @param <A>
 * @param <B>
 */
public class AssociatBeanList<A, B> {
	List<AssociatBean> associatBeans = new ArrayList<AssociatBean>();
	List<A> as = new ArrayList<A>();
	List<B> bs = new ArrayList<B>();
	public void add(A a, B b) {
		AssociatBean associatBean = new AssociatBean(a, b);

		associatBeans.add(associatBean);
		as.add(a);
		bs.add(b);
	}

	/**
	 * 获得abmap
	 * @return
	 */
	public Map<A, B> getAbMap() {
		Map<A, B> abMap = new HashMap<A, B>();

		for (AssociatBean associatBean : associatBeans) {
			abMap.put(associatBean.getA(), associatBean.getB());
		}

		return abMap;
	}

	public List<A> getAs() {
		return as;
	}

	public void setAs(List<A> as) {
		this.as = as;
	}

	public List<B> getBs() {
		return bs;
	}

	public void setBs(List<B> bs) {
		this.bs = bs;
	}

	public int size() {
		return associatBeans.size();
	}

	/**
	 * 关联对象
	 * @author 李卫林
	 *
	 */
	public class AssociatBean {
		private A a;
		private B b;


		public AssociatBean(A a, B b) {
			super();
			this.a = a;
			this.b = b;
		}



		public AssociatBean() {
			super();
		}



		public A getA() {
			return a;
		}
		public void setA(A a) {
			this.a = a;
		}
		public B getB() {
			return b;
		}
		public void setB(B b) {
			this.b = b;
		}

		public boolean equals(AssociatBean obj) {
			if (a.equals(obj.getA()) && b.equals(obj.getB())) {
				return true;
			} else {
				return false;
			}
		}
	}

}
