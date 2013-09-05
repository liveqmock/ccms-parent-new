package com.yunat.ccms.node.support.describe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunat.ccms.core.support.cons.AppCons;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.core.support.utils.HStringUtils;
import com.yunat.ccms.workflow.validation.NodeConnectConstraint;

public class ConnectDescribeRead implements NodeDescribe {

	private static Logger logger = LoggerFactory.getLogger(ConnectDescribeRead.class);

	private Map<String, NodeConnectConstraint> nodeConnectConditionMap;

	public ConnectDescribeRead(Map<String, NodeConnectConstraint> nodeConnectConstraintMap_) {
		this.nodeConnectConditionMap = nodeConnectConstraintMap_;
	}

	@Override
	public void load() {
		initNodeConnection();
	}

	@SuppressWarnings("unchecked")
	private void initNodeConnection() {
		if (nodeConnectConditionMap == null) {

			try {
				nodeConnectConditionMap = new HashMap<String, NodeConnectConstraint>();
				SAXReader reader = new SAXReader();
				Document document = reader.read(AppCons.class.getResourceAsStream("/config/nodeConnectConfig.xml"));
				Element rootElement = document.getRootElement();
				List<Element> nodes = rootElement.elements("node");
				for (Element node : nodes) {
					NodeConnectConstraint nodeConnect = new NodeConnectConstraint();
					String nodeType = node.attributeValue("nodeType").trim();
					Integer inCount = HStringUtils.safeToInteger(node.attributeValue("inCount").trim());
					Integer outCount = HStringUtils.safeToInteger(node.attributeValue("outCount").trim());
					nodeConnect.setNodeType(nodeType);
					nodeConnect.setInCount(inCount);
					nodeConnect.setOutCount(outCount);

					Element inTypeElement = node.element("inType");
					if (inTypeElement != null) {
						Element inTypeIncludeElement = inTypeElement.element("include");
						if (inTypeIncludeElement != null) {
							List<Element> inTypeIncludeNodeList = inTypeIncludeElement.elements();
							if (inTypeIncludeNodeList != null && inTypeIncludeNodeList.size() > 0) {
								Set<NodeConnectConstraint.ConnectionCondition> inIncludeSet = new HashSet<NodeConnectConstraint.ConnectionCondition>();
								for (Element nodeId : inTypeIncludeNodeList) {
									NodeConnectConstraint.ConnectionCondition connectionCon = nodeConnect.crateConnectionCondition();
									Boolean isGroup = Boolean.parseBoolean(nodeId.attributeValue("isGroup").trim());
									String targetNodeType = nodeId.getTextTrim();
									connectionCon.setGroup(isGroup);
									connectionCon.setTargetnodeType(targetNodeType);
									inIncludeSet.add(connectionCon);
								}
								nodeConnect.setInIncludeSet(inIncludeSet);
							}
						}

						Element inTypeExcludeElement = inTypeElement.element("exclude");
						if (inTypeExcludeElement != null) {
							List<Element> inTypeExcludeNodeList = inTypeExcludeElement.elements();
							if (inTypeExcludeNodeList != null && inTypeExcludeNodeList.size() > 0) {
								Set<NodeConnectConstraint.ConnectionCondition> inExcludeSet = new HashSet<NodeConnectConstraint.ConnectionCondition>();
								for (Element nodeId : inTypeExcludeNodeList) {
									NodeConnectConstraint.ConnectionCondition connectionCon = nodeConnect.crateConnectionCondition();
									Boolean isGroup = Boolean.parseBoolean(nodeId.attributeValue("isGroup").trim());
									String targetNodeType = nodeId.getTextTrim();
									connectionCon.setGroup(isGroup);
									connectionCon.setTargetnodeType(targetNodeType);
									inExcludeSet.add(connectionCon);
								}
								nodeConnect.setInExcludeSet(inExcludeSet);
							}
						}
					}

					Element outTypeElement = node.element("outType");
					if (outTypeElement != null) {
						Element outTypeIncludeElement = outTypeElement.element("include");
						if (outTypeIncludeElement != null) {
							List<Element> outTypeIncludeList = outTypeIncludeElement.elements();
							if (outTypeIncludeList != null && outTypeIncludeList.size() > 0) {
								Set<NodeConnectConstraint.ConnectionCondition> outIncludeSet = new HashSet<NodeConnectConstraint.ConnectionCondition>();
								for (Element outNode : outTypeIncludeList) {
									NodeConnectConstraint.ConnectionCondition connectionCon = nodeConnect.crateConnectionCondition();
									Boolean isGroup = Boolean.parseBoolean(outNode.attributeValue("isGroup").trim());
									String targetNodeType = outNode.getTextTrim();
									connectionCon.setGroup(isGroup);
									connectionCon.setTargetnodeType(targetNodeType);
									outIncludeSet.add(connectionCon);
								}
								nodeConnect.setOutIncludeSet(outIncludeSet);
							}
						}

						Element outTypeExcludeElement = outTypeElement.element("exclude");
						if (outTypeExcludeElement != null) {
							List<Element> outExcludeList = outTypeExcludeElement.elements();
							if (outExcludeList != null && outExcludeList.size() > 0) {
								Set<NodeConnectConstraint.ConnectionCondition> outExcludeSet = new HashSet<NodeConnectConstraint.ConnectionCondition>();
								for (Element outNode : outExcludeList) {
									NodeConnectConstraint.ConnectionCondition connectionCon = nodeConnect.crateConnectionCondition();
									Boolean isGroup = Boolean.parseBoolean(outNode.attributeValue("isGroup").trim());
									String targetNodeType = outNode.getTextTrim();
									connectionCon.setGroup(isGroup);
									connectionCon.setTargetnodeType(targetNodeType);
									outExcludeSet.add(connectionCon);
								}
								nodeConnect.setOutExcludeSet(outExcludeSet);
							}
						}
					}
					nodeConnectConditionMap.put(nodeType, nodeConnect);
				}

			} catch (Exception e) {
				String msg = "系统配置文件(节点连接关系)错误";
				logger.error(msg, e);
				throw new CcmsBusinessException(msg, e);
			}

		}
	}

}
