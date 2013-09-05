package com.yunat.ccms.metadata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.metadata.face.FaceAttribute;
import com.yunat.ccms.metadata.face.FaceCatalog;
import com.yunat.ccms.metadata.face.FaceDic;
import com.yunat.ccms.metadata.face.FaceDicValue;
import com.yunat.ccms.metadata.face.FaceOperator;
import com.yunat.ccms.metadata.face.FaceRelation;
import com.yunat.ccms.metadata.face.FaceValueType;
import com.yunat.ccms.metadata.face.JsonFaceConverter;
import com.yunat.ccms.metadata.metamodel.EnumQueryType;
import com.yunat.ccms.metadata.metamodel.EnumRelation;
import com.yunat.ccms.metadata.metamodel.MetaOperator;
import com.yunat.ccms.metadata.pojo.Catalog;
import com.yunat.ccms.metadata.pojo.CatalogCriteria;
import com.yunat.ccms.metadata.pojo.DicType;
import com.yunat.ccms.metadata.pojo.DicTypeValue;
import com.yunat.ccms.metadata.pojo.QueryCriteria;
import com.yunat.ccms.metadata.service.MetaDataRetrieveService;

/**
 * 元数据检索
 * 
 * @author kevin.jiang 2013-3-16
 */
@Controller
@RequestMapping(value = "/meta")
public class MetaDataRetrieveController {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	MetaDataRetrieveService metaDataRetrieveService;

	/**
	 * 正式使用的接口：检索属性目录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceCatalog> retrieveCatalog() {

		Catalog catalog = metaDataRetrieveService.retrieveCatalog(1L);// 客户信息
		List<FaceCatalog> rltList = new ArrayList<FaceCatalog>();

		// 把目录也加到树结构中
		FaceCatalog cata = new FaceCatalog();
		cata.setId(String.valueOf(catalog.getCatalogId()));
		cata.setpId(null);
		cata.setDbType(null);
		cata.setQueryType(null);
		cata.setLabelName(catalog.getShowName());
		rltList.add(cata);

		List<CatalogCriteria> list = metaDataRetrieveService.retrieveAttribute(catalog.getCatalogId());
		if (list != null) {

			for (CatalogCriteria catalogColumn : list) {

				FaceCatalog faceCata = new FaceCatalog();
				faceCata.setId(String.valueOf(catalogColumn.getId()));
				faceCata.setpId(String.valueOf(catalog.getCatalogId()));

				String name = catalogColumn.getShowName();
				if (catalogColumn.getShowName() == null || "".equals(catalogColumn.getShowName())) {
					name = catalogColumn.getQueryCriteria().getDatabaseColumn().getShowName();
				}
				faceCata.setKey(String.valueOf(catalogColumn.getQueryCriteria().getQueryCriteriaId()));
				faceCata.setLabelName(name);
				faceCata.setDbType(catalogColumn.getQueryCriteria().getDatabaseColumn().getColumnType());
				faceCata.setQueryType(catalogColumn.getQueryCriteria().getQueryType());
				rltList.add(faceCata);
			}
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 根据目录检索属性
	 * 
	 * @return
	 */
	@RequestMapping(value = "/catalog/{id}/attribute", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceAttribute> retrieveAttribute(@PathVariable Long id) {

		List<CatalogCriteria> list = metaDataRetrieveService.retrieveAttribute(id);
		List<FaceAttribute> rltList = new ArrayList<FaceAttribute>();
		if (list != null) {

			for (CatalogCriteria catalogColumn : list) {

				FaceAttribute attr = new FaceAttribute();
				attr.setKey(String.valueOf(catalogColumn.getQueryCriteria().getQueryCriteriaId()));

				String name = catalogColumn.getShowName();
				if (catalogColumn.getShowName() == null && "".equals(catalogColumn.getShowName())) {
					name = catalogColumn.getQueryCriteria().getDatabaseColumn().getColumnName();
				}

				attr.setName(name);
				rltList.add(attr);
			}
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 检索系统定义的数据类型（目标值类型）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/value-type", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceValueType> retrieveValueType() {

		List<FaceValueType> rltList = new ArrayList<FaceValueType>();

		for (EnumQueryType ttv : EnumQueryType.values()) {

			FaceValueType attr = new FaceValueType();
			attr.setKey(ttv.name());
			attr.setName(ttv.getChineseName());
			rltList.add(attr);
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 检索系统定义的操作符
	 * 
	 * @return
	 */
	@RequestMapping(value = "/operator", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceOperator> retrieveOperators() {

		List<FaceOperator> rltList = new ArrayList<FaceOperator>();

		for (MetaOperator operator : MetaOperator.values()) {

			FaceOperator attr = new FaceOperator();
			attr.setValue(operator.name());
			attr.setName(operator.getChineseName());
			rltList.add(attr);
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 检索系统定义的布尔关系
	 * 
	 * @return
	 */
	@RequestMapping(value = "/relation", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceRelation> retrieveRelations() {

		List<FaceRelation> rltList = new ArrayList<FaceRelation>();

		for (EnumRelation tcr : EnumRelation.values()) {

			FaceRelation attr = new FaceRelation();
			attr.setKey(tcr.name());
			attr.setName(tcr.getChineseName());
			rltList.add(attr);
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 正式使用的接口：通过属性获取对照操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/attribute/{id}/operator", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceOperator> retrieveOpersByAttr(@PathVariable Long id) {

		List<FaceOperator> resultList = new ArrayList<FaceOperator>();

		QueryCriteria qc = metaDataRetrieveService.loadQueryCriteria(id);
		EnumQueryType type = EnumQueryType.valueOf(qc.getQueryType());
		if (type != null) {

			List<MetaOperator> list = metaDataRetrieveService.retrieveOpersByAttr(type);
			for (MetaOperator operator : list) {

				FaceOperator op = new FaceOperator();
				op.setValue(operator.name());
				op.setName(operator.getChineseName());
				resultList.add(op);
			}
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(resultList));
		return resultList;
	}

	/**
	 * 正式使用的接口：根据字典检索字典值
	 * 
	 * @return
	 */
	@RequestMapping(value = "/dic/{id}/value", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<FaceDicValue> retrieveDicValuesByDic(@PathVariable("id") Long id) {

		QueryCriteria qc = metaDataRetrieveService.loadQueryCriteria(id);
		List<DicTypeValue> list = metaDataRetrieveService.retrieveDicValuesByDic(qc.getDatabaseColumn().getDicType());
		List<FaceDicValue> rltList = new ArrayList<FaceDicValue>();
		if (list != null) {

			for (DicTypeValue dicValue : list) {

				FaceDicValue attr = new FaceDicValue();
				attr.setValue(String.valueOf(dicValue.getTypeValue()));
				attr.setName(dicValue.getShowName());

				DicType dic = dicValue.getDicType();
				FaceDic face_dic = new FaceDic();
				face_dic.setKey(String.valueOf(dic.getDicTypeId()));
				face_dic.setName(dic.getShowName());

				rltList.add(attr);
			}
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(rltList));
		return rltList;
	}

	/**
	 * 正式使用的接口：检索系统定义的字典
	 * 
	 * @return
	 */
	@RequestMapping(value = "/dics", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, List<FaceDicValue>> retrieveDicTypes(@RequestParam String ids) {

		JSONArray idArray = JSONArray.fromObject(ids);
		List<Long> idList = new ArrayList<Long>();
		for (int i = 0; i < idArray.size(); i++) {

			idList.add(idArray.getLong(i));
		}
		logger.info("controller json:" + JsonFaceConverter.toJson(idList));

		Map<String, List<FaceDicValue>> rltMap = new HashMap<String, List<FaceDicValue>>();

		List<QueryCriteria> list = metaDataRetrieveService.loadDicsByKeys(idList);
		for (QueryCriteria cri : list) {

			if (EnumQueryType.DIC.name().equals(cri.getQueryType())
					|| EnumQueryType.ORDERED_DIC.name().equals(cri.getQueryType())) {

				List<FaceDicValue> faceDicList = new ArrayList<FaceDicValue>();
				Iterator<DicTypeValue> dicIt = cri.getDatabaseColumn().getDicType().getValues().iterator();
				while (dicIt.hasNext()) {

					DicTypeValue value = dicIt.next();
					FaceDicValue faceValue = new FaceDicValue();
					faceValue.setName(value.getShowName());
					faceValue.setValue(value.getTypeValue());
					faceDicList.add(faceValue);

				}
				rltMap.put(String.valueOf(cri.getQueryCriteriaId()), faceDicList);
			}

			if (EnumQueryType.REFER.name().equals(cri.getQueryType())) {

				List<String[]> referValueList = metaDataRetrieveService.retrieveReferDic(cri.getDatabaseColumn()
						.getReferType());

				List<FaceDicValue> faceReferList = new ArrayList<FaceDicValue>();
				for (String[] referValue : referValueList) {

					FaceDicValue faceValue = new FaceDicValue();
					faceValue.setName(referValue[0]);
					faceValue.setValue(referValue[1]);
					faceReferList.add(faceValue);
				}
				rltMap.put(String.valueOf(cri.getQueryCriteriaId()), faceReferList);
			}

		}
		return rltMap;
	}
}
