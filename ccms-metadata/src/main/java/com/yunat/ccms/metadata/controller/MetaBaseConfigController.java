package com.yunat.ccms.metadata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunat.ccms.metadata.face.FaceBoolean;

/**
 * 元数据基本配置
 *
 * @author kevin.jiang 2013-3-16
 */
@Controller
@RequestMapping(value = "/meta/base/config/")
public class MetaBaseConfigController {

	/**
	 * 分页读取物理表配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-db-tables.do")
	@ResponseBody
	public FaceBoolean pageDbTables(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存物理表配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-db-table.do")
	@ResponseBody
	public FaceBoolean saveDbTable(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条物理表配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-db-table.do")
	@ResponseBody
	public FaceBoolean loadDbTable(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

	/**
	 * 分页读取物理字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-db-columns.do")
	@ResponseBody
	public FaceBoolean pageDbColumns(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 根据一个物理表，分页读取物理字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-dbcolumns-bytable.do")
	@ResponseBody
	public FaceBoolean pageDbColumnsByTable(@RequestParam("tableId") Long tableId,
			@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存物理字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-db-column.do")
	@ResponseBody
	public FaceBoolean saveDbColumn(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条物理字段配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-db-column.do")
	@ResponseBody
	public FaceBoolean loadDbColumn(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

	/**
	 * 分页读取字典配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-dics.do")
	@ResponseBody
	public FaceBoolean pageDics(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存字典配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-dic.do")
	@ResponseBody
	public FaceBoolean saveDic(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条字典配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-dic.do")
	@ResponseBody
	public FaceBoolean loadDic(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

	/**
	 * 分页读取字典值配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-dic-values.do")
	@ResponseBody
	public FaceBoolean pageDicValues(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 根据字典，分页读取字典值配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-dicvalues-bydic.do")
	@ResponseBody
	public FaceBoolean pageDicValuesByDic(@RequestParam("dicId") Long dicId, @RequestParam("pageIndex") int index,
			@RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存字典值配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-dic-value.do")
	@ResponseBody
	public FaceBoolean saveDicValue(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条字典值配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-dic-value.do")
	@ResponseBody
	public FaceBoolean loadDicValue(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

	/**
	 * 分页读取引用配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/page-refers.do")
	@ResponseBody
	public FaceBoolean pageRefers(@RequestParam("pageIndex") int index, @RequestParam("numPerPage") int numPerPage) {

		return new FaceBoolean(true);
	}

	/**
	 * 保存引用配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/save-refer.do")
	@ResponseBody
	public FaceBoolean saveRefer(@RequestParam("saveJson") String json) {

		return new FaceBoolean(true);
	}

	/**
	 * 读取一条引用配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/load-refer.do")
	@ResponseBody
	public FaceBoolean loadRefer(@RequestParam("id") Long id) {

		return new FaceBoolean(true);
	}

}
