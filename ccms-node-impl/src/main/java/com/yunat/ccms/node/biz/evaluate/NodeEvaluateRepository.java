package com.yunat.ccms.node.biz.evaluate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * 评估节点存储接口
 * 
 * @author yin
 * 
 */
public interface NodeEvaluateRepository extends Repository<NodeEvaluate, Long>, CrudRepository<NodeEvaluate, Long> {

	NodeEvaluate findByNodeId(Long nodeId);

}
