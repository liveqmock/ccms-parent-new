package com.yunat.ccms.metadata.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;
import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.pojo.Catalog;
import com.yunat.ccms.metadata.pojo.CatalogCriteria;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.DicTypeValue;
import com.yunat.ccms.metadata.pojo.QueryCriteria;
import com.yunat.ccms.metadata.pojo.ReferType;
import com.yunat.ccms.metadata.repository.DicTypeDao;
import com.yunat.ccms.metadata.repository.QueryCriteriaDao;
import com.yunat.ccms.metadata.repository.ShowCatalogDao;

/**
 * 元数据基本检索服务
 * 
 * @author kevin.jiang 2013-3-18
 */
@Service
public class MetaDataRetrieveService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ShowCatalogDao showCatalogDao;

	@Autowired
	private DicTypeDao dicTypeDao;

	@Autowired
	private QueryCriteriaDao queryCriteriaDao;

	@Autowired
	JdbcPaginationHelper jdbcPaginationHelper;

	/**
	 * 检索属性目录
	 * 
	 * @return
	 */
	public List<Catalog> retrieveCatalog() {

		return showCatalogDao.findAll();
	}

	public Catalog retrieveCatalog(Long id) {

		return showCatalogDao.findOne(id);
	}

	/**
	 * 根据目录检索属性
	 * 
	 * @return
	 */
	public List<CatalogCriteria> retrieveAttribute(Long catalogKey) {

		Catalog catalog = showCatalogDao.findOne(catalogKey);

		List<CatalogCriteria> list = new ArrayList<CatalogCriteria>();
		Iterator<CatalogCriteria> it = catalog.getCatalogColumns().iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}

		Collections.sort(list, new Comparator<CatalogCriteria>() {

			@Override
			public int compare(CatalogCriteria c1, CatalogCriteria c2) {

				return c1.getShowOrder() - c2.getShowOrder();
			}

		});
		return list;
	}

	/**
	 * 检索系统定义的字典
	 * 
	 * @return
	 */
	public List<DicType> retrieveDicTypes() {

		return dicTypeDao.findAll();
	}

	/**
	 * 检索系统定义的字典
	 * 
	 * @return
	 */
	public List<DicType> retrieveDicTypes(List<Long> ids) {

		if (ids == null || ids.size() <= 0) {
			return null;
		}

		return dicTypeDao.findByIds(ids);
	}

	/**
	 * 检索属性数据类型与操作符的对照关系
	 * 
	 * @return
	 */
	public Map<EnumQueryType, List<MetaOperator>> retrieveAttrOperMap() {

		Map<EnumQueryType, List<MetaOperator>> resultMap = new HashMap<EnumQueryType, List<MetaOperator>>();
		for (EnumQueryType type : EnumQueryType.values()) {

			resultMap.put(type, type.getMappedOperators());
		}
		return resultMap;
	}

	/**
	 * 通过属性获取对照操作
	 * 
	 * @param type
	 * @return
	 */
	public List<MetaOperator> retrieveOpersByAttr(EnumQueryType type) {

		List<MetaOperator> list = type.getMappedOperators();
		Collections.sort(list, new Comparator<MetaOperator>() {

			@Override
			public int compare(MetaOperator c1, MetaOperator c2) {

				return (int) (c1.getKey() - c2.getKey());
			}

		});
		return list;
	}

	/**
	 * 检索字典和字典值的对照关系
	 * 
	 * @return
	 */
	public Map<DicType, List<DicTypeValue>> retrieveDicValueMap() {

		List<DicType> dicList = dicTypeDao.findAll();
		Map<DicType, List<DicTypeValue>> resultMap = new HashMap<DicType, List<DicTypeValue>>();
		if (dicList != null && dicList.size() > 0) {
			for (DicType dic : dicList) {

				List<DicTypeValue> list = new ArrayList<DicTypeValue>();
				Iterator<DicTypeValue> it = dic.getValues().iterator();
				while (it.hasNext()) {
					list.add(it.next());
				}
				resultMap.put(dic, list);
			}
		}
		return resultMap;
	}

	/**
	 * 检索字典和字典值的对照关系, 有排序
	 * 
	 * @return
	 */
	public List<DicTypeValue> retrieveDicValuesByDic(DicType dic) {

		List<DicTypeValue> list = new ArrayList<DicTypeValue>();
		Iterator<DicTypeValue> it = dic.getValues().iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}

		Collections.sort(list, new Comparator<DicTypeValue>() {

			@Override
			public int compare(DicTypeValue c1, DicTypeValue c2) {

				return (int) (c1.getDicTypeValueId() - c2.getDicTypeValueId());
			}

		});

		return list;
	}

	public List<String[]> retrieveReferDic(ReferType refer) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select ").append(refer.getReferName()).append(" as name, ").append(refer.getReferKey())
				.append(" as value ");
		sql.append(" from ").append(refer.getReferTable());
		sql.append(" order by ").append(refer.getOrderColumn());
		logger.info(sql.toString());

		List<String[]> rltList = new ArrayList<String[]>();
		List<Map<String, Object>> list = jdbcPaginationHelper.getJdbcOperations().queryForList(sql.toString());
		if (list != null && list.size() > 0) {

			for (Map<String, Object> amap : list) {

				String[] aArray = new String[2];
				aArray[0] = (String) amap.get("name");
				aArray[1] = String.valueOf(amap.get("value"));
				rltList.add(aArray);
			}
		}
		return rltList;
	}

	public QueryCriteria loadQueryCriteria(Long id) {

		return queryCriteriaDao.findOne(id);
	}

	public List<QueryCriteria> loadDicsByKeys(List<Long> keys) {

		if (keys == null || keys.size() <= 0) {

			return null;
		}

		return queryCriteriaDao.findByKeys(keys);
	}
}
