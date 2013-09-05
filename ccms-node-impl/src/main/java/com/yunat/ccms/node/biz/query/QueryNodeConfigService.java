package com.yunat.ccms.node.biz.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.metadata.face.FaceDicValue;
import com.yunat.ccms.metadata.face.FaceNode;
import com.yunat.ccms.metadata.face.FaceOperator;
import com.yunat.ccms.metadata.face.FaceQuery;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;
import com.yunat.ccms.metadata.metamodel.EnumRelation;
import com.yunat.ccms.metadata.metamodel.MetaCriteria;
import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.metamodel.MetaQuery;
import com.yunat.ccms.metadata.pojo.DicTypeValue;
import com.yunat.ccms.metadata.pojo.Query;
import com.yunat.ccms.metadata.pojo.QueryCriteria;
import com.yunat.ccms.metadata.repository.QueryCriteriaDao;
import com.yunat.ccms.metadata.repository.QueryDao;
import com.yunat.ccms.metadata.service.MetaDataRetrieveService;
import com.yunat.ccms.metadata.service.MetaQueryConfigService;

@Service
public class QueryNodeConfigService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	QueryDao queryDao;

	@Autowired
	NodeQueryRepository nodeQueryRepository;

	@Autowired
	MetaQueryConfigService metaQueryConfigService;

	@Autowired
	NodeQueryDefinedRepository nodeQueryDefinedRepository;

	@Autowired
	NodeQueryCriteriaRepository nodeQueryCriteriaRepository;

	@Autowired
	MetaDataRetrieveService metaDataRetrieveService;

	@Autowired
	QueryCriteriaDao queryCriteriaDao;

	@Autowired
	JdbcPaginationHelper jdbcPaginationHelper;

	/**
	 * 查询运行时：把查询节点的查询运行的实体类转换成查询定义
	 * 
	 * @param queryId
	 *            查询定义ID
	 * @return
	 */
	public MetaQuery loadMetaQueryFromPojo(NodeQueryDefined definedQuery) {

		MetaQuery metaQuery = (new MetaQuery()).loadFromPojo(definedQuery.getQuery());
		metaQuery.setId(definedQuery.getId());
		if (definedQuery.getRelation() != null) {
			metaQuery.setRelation(EnumRelation.valueOf(definedQuery.getRelation()));
		}

		List<NodeQueryCriteria> list = new ArrayList<NodeQueryCriteria>(definedQuery.getCriterias());

		Collections.sort(list, new Comparator<NodeQueryCriteria>() {

			@Override
			public int compare(NodeQueryCriteria c1, NodeQueryCriteria c2) {

				// key值相同时，根据ID顺序
				if (c1.getQueryCriteria().getQueryCriteriaId() == c2.getQueryCriteria().getQueryCriteriaId()) {

					return (int) (c1.getId() - c2.getId());
				}

				// key值不同是，根据key值顺序
				return (int) (c1.getQueryCriteria().getQueryCriteriaId() - c2.getQueryCriteria().getQueryCriteriaId());
			}

		});

		for (NodeQueryCriteria criteria : list) {

			MetaCriteria meta_c = (new MetaCriteria().loadFromPojo(criteria.getQueryCriteria()));
			meta_c.setOperator(MetaOperator.valueOf(criteria.getOperator()));
			meta_c.setTargetValues(criteria.getTargetValue());

			if (criteria.getRelation() != null && !"".equals(criteria.getRelation())) {
				meta_c.setRelation(EnumRelation.valueOf(criteria.getRelation()));
			}
			meta_c.setId(criteria.getId());
			meta_c.setSubGroup(criteria.getSubGroup());
			meta_c.setUiCode(criteria.getUiCode());
			metaQuery.getCriterias().add(meta_c);
		}

		return metaQuery;
	}

	/**
	 * 查询运行时：把查询定义转换成查询节点的查询运行的实体类
	 * 
	 * @param queryId
	 *            查询定义ID
	 * @return
	 */
	public NodeQueryDefined convertMetaQueryToPojo(MetaQuery metaQuery) {

		NodeQueryDefined defined = null;
		if (metaQuery.getId() == null) { // 创建

			defined = new NodeQueryDefined();
			Query query = metaQuery.genPojo();
			defined.setQuery(query);

		} else { // 更新

			defined = nodeQueryDefinedRepository.findOne(metaQuery.getId());

			// 先删除原有条件
			nodeQueryCriteriaRepository.deleteCriteriaByQuery(defined);
			defined.getCriterias().clear();
		}

		for (MetaCriteria metaCriteria : metaQuery.getCriterias()) {

			NodeQueryCriteria criteria = convertMetaCriteriaToPojo(metaCriteria);
			defined.getCriterias().add(criteria);
		}
		if (metaQuery.getRelation() != null) {
			defined.setRelation(metaQuery.getRelation().name());
		}

		return defined;
	}

	/**
	 * 查询运行时：把查询条件定义转换成查询节点的查询条件的实体类
	 * 
	 * @param query
	 *            元数据查询
	 */
	private NodeQueryCriteria convertMetaCriteriaToPojo(MetaCriteria metaCriteria) {

		NodeQueryCriteria nodeQueryCriteria = new NodeQueryCriteria();
		nodeQueryCriteria.setQueryCriteria(metaCriteria.genPojo());
		nodeQueryCriteria.setOperator(metaCriteria.getOperator().name());
		nodeQueryCriteria.setSubGroup(metaCriteria.getSubGroup());
		nodeQueryCriteria.setRelation(metaCriteria.getRelation().name());
		nodeQueryCriteria.setTargetValue(metaCriteria.getTargetValues());
		nodeQueryCriteria.setUiCode(metaCriteria.getUiCode());
		return nodeQueryCriteria;
	}

	/**
	 * 读取查询节点保存的查询配置
	 * 
	 * @return
	 */
	public NodeQuery loadQueryNodeByID(Long nodeId) {

		return nodeQueryRepository.findOne(nodeId);
	}

	/**
	 * 保存查询节点的配置
	 * 
	 * @param node
	 */
	public void saveQueryNodeConfig(NodeQuery nodeConfig) {

		nodeQueryRepository.save(nodeConfig);
	}

	/**
	 * 保存来自界面的查询配置
	 * 
	 * @param query
	 */
	public void saveConfigFromFace(FaceNode face, Long nodeId) {

		NodeQuery node_q = nodeQueryRepository.findOne(nodeId);

		if (node_q == null) {

			node_q = new NodeQuery();
			node_q.setNodeId(nodeId);
		}

		node_q.setIsExclude(Boolean.valueOf(face.getExclude()));
		node_q.setTimeType(Integer.valueOf(face.getTimeType()));
		node_q.setPlatCode(face.getPlat());
		node_q.getQueryDefineds().clear();

		for (FaceQuery faceQuery : face.getQueries()) {

			if (faceQuery.getCons() == null || faceQuery.getCons().size() <= 0) {
				continue;
			}
			MetaQuery meta_q = (new MetaQuery()).parseFace(faceQuery);
			NodeQueryDefined defined = convertMetaQueryToPojo(meta_q);
			defined.setNodeQuery(node_q);

			Map<String, Boolean> aMap = new HashMap<String, Boolean>();
			aMap.put("buy", Boolean.valueOf(faceQuery.getIsBuy()));
			defined.setExtCtrlInfo(JSONSerializer.toJSON(aMap).toString());
			node_q.getQueryDefineds().add(defined);
		}
		nodeQueryRepository.save(node_q);

		// 界面会把删除的条件的ID以数组方式 提交，需要把这些条件删除：查询级别（Query）
		if (face.getDelqueries() != null && face.getDelqueries().size() > 0) {

			for (String nodeQueryId : face.getDelqueries()) {

				nodeQueryCriteriaRepository.deleteCriteriaByQuery(Long.valueOf(nodeQueryId));
				nodeQueryDefinedRepository.delete(Long.valueOf(nodeQueryId));
			}
		}
	}

	/**
	 * 为界面读取查询配置
	 * 
	 * @param query
	 */
	@SuppressWarnings({ "unchecked" })
	public FaceNode loadConfigForFace(Long nodeId) {

		FaceNode face = new FaceNode();

		NodeQuery node_q = nodeQueryRepository.findOne(nodeId);

		if (node_q == null) {

			return null;
		}

		List<NodeQueryDefined> list = new ArrayList<NodeQueryDefined>(node_q.getQueryDefineds());

		Collections.sort(list, new Comparator<NodeQueryDefined>() {

			@Override
			public int compare(NodeQueryDefined c1, NodeQueryDefined c2) {

				return (int) (c1.getId() - c2.getId());
			}
		});

		for (NodeQueryDefined defined : list) {

			MetaQuery meta_q = loadMetaQueryFromPojo(defined);
			FaceQuery face_q = meta_q.genFace();
			genOptionsForFace(face_q, meta_q);

			Map<String, Boolean> aMap = JSONObject.fromObject(defined.getExtCtrlInfo());
			face_q.setIsBuy(String.valueOf(aMap.get("buy")));
			face.getQueries().add(face_q);
		}

		face.setExclude(String.valueOf(node_q.getIsExclude()));
		face.setTimeType(String.valueOf(node_q.getTimeType()));
		face.setPlat(node_q.getPlatCode());
		return face;
	}

	/**
	 * 提供界面的辅助信息，用于方便显示下拉框
	 * 
	 * @param face_q
	 * @param meta_q
	 */
	@SuppressWarnings("rawtypes")
	private void genOptionsForFace(FaceQuery face_q, MetaQuery meta_q) {

		Map<String, Map<String, List>> optionsMap = new HashMap<String, Map<String, List>>();
		Map<String, List> operMap = new HashMap<String, List>();
		Map<String, List> dicMap = new HashMap<String, List>();

		for (MetaCriteria criteria : meta_q.getCriterias()) {

			List<MetaOperator> operList = metaDataRetrieveService.retrieveOpersByAttr(criteria.getQueryType());
			if (operList != null) {

				List<FaceOperator> alist = new ArrayList<FaceOperator>();
				for (MetaOperator mo : operList) {

					FaceOperator operator = new FaceOperator();
					operator.setName(mo.getChineseName());
					operator.setValue(mo.name());
					alist.add(operator);
				}
				operMap.put(String.valueOf(criteria.getKey()), alist);
			}

			if (criteria.getQueryType() == EnumQueryType.DIC || criteria.getQueryType() == EnumQueryType.ORDERED_DIC) {

				QueryCriteria qc = queryCriteriaDao.findOne(criteria.getKey());
				List<DicTypeValue> dicList = metaDataRetrieveService.retrieveDicValuesByDic(qc.getDatabaseColumn()
						.getDicType());
				if (dicList != null) {

					List<FaceDicValue> alist = new ArrayList<FaceDicValue>();
					for (DicTypeValue value : dicList) {

						FaceDicValue dic = new FaceDicValue();
						dic.setName(value.getShowName());
						dic.setValue(value.getTypeValue());
						alist.add(dic);
					}
					dicMap.put(String.valueOf(criteria.getKey()), alist);
				}
			}

			if (criteria.getQueryType() == EnumQueryType.REFER) {

				QueryCriteria qc = queryCriteriaDao.findOne(criteria.getKey());
				List<String[]> referList = metaDataRetrieveService.retrieveReferDic(qc.getDatabaseColumn()
						.getReferType());
				if (referList != null) {

					List<FaceDicValue> alist = new ArrayList<FaceDicValue>();
					for (String[] value : referList) {

						FaceDicValue dic = new FaceDicValue();
						dic.setName(value[0]);
						dic.setValue(value[1]);
						alist.add(dic);
					}
					dicMap.put(String.valueOf(criteria.getKey()), alist);
				}
			}
		}
		optionsMap.put("operators", operMap);
		optionsMap.put("dicvalues", dicMap);
		face_q.setOptions(optionsMap);
	}

	/**
	 * 加载商品信息
	 * 
	 * @param title
	 *            商品标题
	 * @param dpId
	 *            店铺ID
	 * @param outId
	 *            外部编码
	 * @return
	 */
	public List<CriteriaProduct> loadProductList(String title, String dpId, String outId) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select num_iid, dp_id, title, outer_id from plt_taobao_product where 1=1 ");

		if (title != null && !"".equals(title)) {
			sql.append(" and title like '%" + title + "%'");
		}

		if (dpId != null && !"".equals(dpId)) {
			sql.append(" and dp_id = '" + dpId + "'");
		}

		if (outId != null && !"".equals(outId)) {
			sql.append(" and outer_id = '" + outId + "'");
		}

		sql.append(" order by title");
		logger.info(sql.toString());

		List<CriteriaProduct> rltList = new ArrayList<CriteriaProduct>();
		List<Map<String, Object>> list = jdbcPaginationHelper.getJdbcOperations().queryForList(sql.toString());
		if (list != null && list.size() > 0) {

			for (Map<String, Object> amap : list) {

				CriteriaProduct product = new CriteriaProduct();
				product.setId((String) amap.get("num_iid"));
				product.setDpId((String) amap.get("dp_id"));
				product.setTitle((String) amap.get("title"));
				product.setOuterId((String) amap.get("outer_id"));
				rltList.add(product);
			}
		}
		return rltList;
	}
}
