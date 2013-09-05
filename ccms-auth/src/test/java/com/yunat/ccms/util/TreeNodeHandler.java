package com.yunat.ccms.util;

import com.yunat.ccms.util.Tree.TreeNode;

public interface TreeNodeHandler<E> {

	void handle(TreeNode<E> node);
}
