package com.yunat.ccms.metadata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.metadata.face.FaceBoolean;

/**
 * 元数据显示配置
 *
 * @author kevin.jiang 2013-3-16
 */
@Controller
@RequestMapping(value = "/meta/show/config/")
public class MetaShowConfigController {

	/**
	 * 分页读取展示索引配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-show-catalogs.do")
	@ResponseBody
	public FaceBoolean pageShowCatalogs(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存展示索引配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-show-catalog.do")
	@ResponseBody
	public FaceBoolean saveShowCatalog(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条展示索引配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-show-catalog.do")
	@ResponseBody
	public FaceBoolean loadShowCatalog(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

	/**
	 * 分页读取展示字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-show-columns.do")
	@ResponseBody
	public FaceBoolean pageShowColumns(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 根据展示索引，分页读取展示字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-showcolumns-bycatalog.do")
	@ResponseBody
	public FaceBoolean pageShowColumnsByCatalog(@RequestParam("catalogId") Long catalogId,
			@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存展示字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-show-column.do")
	@ResponseBody
	public FaceBoolean saveShowColumn(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条展示字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-show-column.do")
	@ResponseBody
	public FaceBoolean loadShowColumn(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}
}
