package com.yunat.ccms.node.biz.query;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.metadata.face.FaceBoolean;
import com.yunat.ccms.metadata.face.FaceNode;
import com.yunat.ccms.metadata.face.JsonFaceConverter;
import com.yunat.ccms.metadata.service.MetaQueryConfigService;

/**
 * 查询配置界面相关，和查询配置后台处理有关的，检索类的操作除外
 * 
 * @author kevin.jiang 2013-3-16
 */
@Controller
@RequestMapping(value = "/node/query")
public class NodeQueryController {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	QueryNodeConfigService queryNodeConfigService;

	@Autowired
	MetaQueryConfigService metaQueryConfigService;

	/**
	 * 加载查询配置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/config/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public FaceNode loadConfig(@PathVariable Long id) {

		FaceNode node = queryNodeConfigService.loadConfigForFace(id);
		if (node == null) {

			node = new FaceNode();
		}
		logger.info("controller json: " + JsonFaceConverter.toJson(node));
		return node;
	}

	/**
	 * 保存查询配置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/config/{id}", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
	@ResponseBody
	public FaceBoolean saveConfig(@PathVariable Long id, @RequestParam("addinfo") String sJson) {

		try {
			logger.info(sJson);
			FaceNode face_n = JsonFaceConverter.JsonToFaceNode(sJson);
			queryNodeConfigService.saveConfigFromFace(face_n, id);

			return new FaceBoolean(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
			return new FaceBoolean(false);
		}
	}

	/**
	 * 新增查询配置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/config/{id}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public FaceBoolean addConfig(@PathVariable Long id, @RequestParam("addinfo") String sJson) {

		try {
			// String sJson = "";
			logger.info(sJson);
			FaceNode face_n = JsonFaceConverter.JsonToFaceNode(sJson);
			queryNodeConfigService.saveConfigFromFace(face_n, id);

			return new FaceBoolean(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("caused by:", e);
			return new FaceBoolean(false);
		}
	}

	/**
	 * 加载商品信息
	 */
	@RequestMapping(value = "/products", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<CriteriaProduct> loadProducts(@RequestParam String title, @RequestParam String dpId,
			@RequestParam String outId) {

		return queryNodeConfigService.loadProductList(title, dpId, outId);
	}
}
