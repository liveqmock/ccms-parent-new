package com.yunat.ccms.node.biz.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunat.ccms.core.support.exception.CcmsBusinessException;
import com.yunat.ccms.node.spi.support.NodeCloneHandler;

@Component
public class NodeQueryHandler implements NodeCloneHandler {

	@Autowired
	NodeQueryRepository nodeQueryRepository;

	@Override
	public void clone(Long nodeId, Long newNodeId) {

		NodeQuery oldNode = nodeQueryRepository.findOne(nodeId);

		if (oldNode == null) {

			return;
		}

		NodeQuery newNode = new NodeQuery();

		newNode.setNodeId(newNodeId);
		newNode.setIsExclude(oldNode.getIsExclude());
		newNode.setTimeType(oldNode.getTimeType());
		newNode.setPlatCode(oldNode.getPlatCode());

		Set<NodeQueryDefined> oldDefineds = oldNode.getQueryDefineds();
		Set<NodeQueryDefined> newDefineds = new HashSet<NodeQueryDefined>();

		Iterator<NodeQueryDefined> dit = oldDefineds.iterator();
		while (dit.hasNext()) {

			NodeQueryDefined oldDefined = dit.next();
			NodeQueryDefined newDefined = new NodeQueryDefined();
			newDefined.setNodeQuery(newNode);
			newDefined.setQuery(oldDefined.getQuery());
			newDefined.setRelation(oldDefined.getRelation());
			newDefined.setExtCtrlInfo(oldDefined.getExtCtrlInfo());

			Set<NodeQueryCriteria> oldCriterias = oldDefined.getCriterias();
			Set<NodeQueryCriteria> newCriterias = new HashSet<NodeQueryCriteria>();

			Iterator<NodeQueryCriteria> cit = oldCriterias.iterator();
			while (cit.hasNext()) {

				NodeQueryCriteria oldCriteria = cit.next();
				NodeQueryCriteria newCriteria = new NodeQueryCriteria();
				newCriteria.setNodeQueryDefined(newDefined);
				newCriteria.setOperator(oldCriteria.getOperator());
				newCriteria.setQueryCriteria(oldCriteria.getQueryCriteria());
				newCriteria.setRelation(oldCriteria.getRelation());
				newCriteria.setSubGroup(oldCriteria.getSubGroup());
				newCriteria.setTargetValue(oldCriteria.getTargetValue());
				newCriterias.add(newCriteria);
			}
			newDefined.setCriterias(newCriterias);
			newDefineds.add(newDefined);
		}
		newNode.setQueryDefineds(newDefineds);
	}

	@Override
	public void delete(Long nodeId) {

		nodeQueryRepository.delete(nodeId);
	}

	@Override
	public boolean updatable() {

		return false;
	}

	@Override
	public void refresh(Long nodeId, Long newNodeId) {

		throw new CcmsBusinessException("the updateable is false, will invoke this function is error .");
	}

}
