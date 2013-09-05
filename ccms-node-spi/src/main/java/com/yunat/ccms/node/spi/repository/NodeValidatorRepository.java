package com.yunat.ccms.node.spi.repository;

import com.yunat.ccms.node.spi.support.NodeValidator;

public interface NodeValidatorRepository {

	public NodeValidator getNodeValidator(String nodeType);

}
