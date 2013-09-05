package com.yunat.ccms.node.biz.evaluate;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 评估节点(1.打开 2.保存 控制层)
 * 
 * @author yin
 * 
 */
@Controller
public class NodeEvaluateController {

	@Autowired
	NodeEvaluateService nodeEvaluateService;

	/**
	 * 
	 * 
	 * @param nodeId
	 *            node unique identify
	 * @return NodeEvaluate entity
	 * @throws Exception
	 *             if open fail
	 */
	@RequestMapping(value = "/node/evaluate/{nodeId}", method = RequestMethod.GET)
	@ResponseBody
	public NodeEvaluate open(@PathVariable Long nodeId) throws Exception {
		NodeEvaluate nodeEvaluate = new NodeEvaluate();
		try {
			nodeEvaluate = nodeEvaluateService.findByNodeId(nodeId);
		} catch (Exception e) {
			throw new Exception("评估节点打开异常！");
		}
		return nodeEvaluate;
	}

	/**
	 * 
	 * @param nodeEvaluate
	 *            the page parm wrap
	 * @throws Exception
	 *             if save fail
	 */

	@RequestMapping(value = "/node/evaluate", method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody NodeEvaluate nodeEvaluate) throws Exception {
		try {
			nodeEvaluate.setCreated(new Date());
			nodeEvaluateService.saveNode(nodeEvaluate);
		} catch (Exception e) {
			throw new Exception("评估节点保存异常！");
		}
	}
}
