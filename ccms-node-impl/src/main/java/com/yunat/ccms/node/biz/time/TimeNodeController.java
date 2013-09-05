package com.yunat.ccms.node.biz.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TimeNodeController {

	private Logger logger = LoggerFactory.getLogger(TimeNodeController.class);

	@Autowired
	private NodeTimeQuery nodeTimeQuery;

	@Autowired
	private NodeTimeCommand nodeTimeCommand;

	/**
	 * 在时间节点的配置dialog的准备的
	 * 
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/node/time", method = RequestMethod.GET)
	@ResponseBody
	public ModelMap open(@RequestParam("name") String name, @RequestParam("id") Long nodeId, ModelMap model)
			throws Exception {
		try {
			NodeTime node = nodeTimeQuery.findByNodeId(nodeId);
			/************** 传递ModelMap *******************/
			model.addAttribute("id", nodeId);
			model.addAttribute("name", name);
			model.addAttribute("timeNode", node);
		} catch (Exception e) {
			logger.info("打开时间节点出现异常 : [节点id {}, info {}]", nodeId, e);
			throw new Exception("时间节点打开异常！");
		}
		return model;
	}

	/**
	 * 保存时间节点
	 * 
	 * @param nodeTimeEntity
	 * @param weekCycleValue
	 * @param monthCycleValue
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/node/time", method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody NodeTime nodeTimeEntity) throws Exception {
		try {
			NodeTime nodeTime = new NodeTime();
			// notified fist param :source second param :target
			BeanUtils.copyProperties(nodeTimeEntity, nodeTime);
			nodeTimeCommand.saveTimeNode(nodeTime);
		} catch (Exception e) {
			logger.info("保存时间节点出现异常 : [节点id {}, info {}]", nodeTimeEntity.getId(), e);
			throw new Exception("时间节点保存异常！");
		}
	}
}
