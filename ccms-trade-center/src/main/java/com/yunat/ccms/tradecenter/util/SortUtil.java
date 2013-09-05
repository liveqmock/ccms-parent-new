/**
 *
 */
package com.yunat.ccms.tradecenter.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

/**
 *
 *
 * @author xiahui.zhang
 * @version 创建时间：2013-6-6 下午06:12:50
 */
public class SortUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void stockDataListSort(List<?> stockDatas, String field, String sortType) {
		Comparator mycmp = ComparableComparator.getInstance();
		mycmp = ComparatorUtils.nullLowComparator(mycmp); //允许null
		if (StringUtils.equals(sortType, "desc")) {
			mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序
		}
		ArrayList<Object> sortFields = new ArrayList<Object>();
		sortFields.add(new BeanComparator(field, mycmp));
		ComparatorChain multiSort = new ComparatorChain(sortFields);
		Collections.sort(stockDatas, multiSort);
	}

}
