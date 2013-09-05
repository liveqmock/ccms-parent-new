package com.yunat.ccms.workflow.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * 对流程图结构的简单封装RDAG（rooted  directed acyclic graph），隐藏部分不需要的api接口
 * 
 * @author xiaojing.qu
 * */
public class WorkFlowGraph<N,C>{
	
	/**有向无环图*/
	private DirectedOrderedSparseMultigraph<N, C> graph;
	/**根节点，入度为0，出度不为0*/
	private N root;
	/**叶子节点，即所有入度不为0，出度为0的节点*/
	private List<N> leafNodes;
	/**合并节点，即所有入度大于1的节点*/
	private List<N> joinNodes;
	/**拆分节点，即所有出度大于1的节点*/
	private List<N> splitNodes;
	
	/**是否是子图，因为在子图上做部分操作返回的结果会和主图有差异*/
	private boolean isSubGraph = false;
	
	public WorkFlowGraph(){
		graph = new DirectedOrderedSparseMultigraph<N, C>();
		leafNodes = new ArrayList<N>();
		joinNodes = new ArrayList<N>();
		splitNodes = new ArrayList<N>();
	}
	
	public static WorkFlowGraph<Node, Connect> getWorkFlowGraph(WorkFlow workflow){
		WorkFlowGraph<Node, Connect> graph = new WorkFlowGraph<Node,Connect>();
		for(Node node:workflow.getAllNodes()){
			graph.addNode(node);
		}
		for(Connect connect:workflow.getAllConnects()){
			graph.addConnect(connect, connect.getSourceNode(), connect.getTargetNode());
		}
		graph.init();
		return graph;
	}
	
	/**获得以某节点为跟的子图*/
	public WorkFlowGraph<N,C> getSubGraph(N rootNode){
		WorkFlowGraph<N,C> subGraph = null;
		if(graph.containsVertex(rootNode)){
			subGraph = new WorkFlowGraph<N,C>();
			subGraph.isSubGraph = true;
			copyNodeRecursive(rootNode,this,subGraph);
			subGraph.init();
		}
		return subGraph;
	}
	
	/**递归的拷贝节点*/
	private void copyNodeRecursive(N node,WorkFlowGraph<N,C> sourceGraph,WorkFlowGraph<N,C> targetGraph){
		targetGraph.addNode(node);
		for(N nextNode: sourceGraph.getNextNodes(node)){
			boolean isNodeProcessed = targetGraph.containsNode(nextNode);
			targetGraph.addNode(nextNode);
			targetGraph.addConnect(sourceGraph.getConnect(node, nextNode), node, nextNode);
			if(!isNodeProcessed){//处理过的节点，不用重复处理
				copyNodeRecursive(nextNode,sourceGraph,targetGraph);
			}
		}
	}
	

	
	/**添加节点*/
	public boolean addNode(N node){
		return graph.addVertex(node);
	}
	
	/**添加边*/
	public boolean addConnect(C connect,N source,N target){
		return graph.addEdge(connect, source, target,EdgeType.DIRECTED);
	}
	
	/**获得连接*/
	public C getConnect(N source,N target){
		return graph.findEdge(source, target);
	}
	
	/**节点总数*/
	public int getNodeCount(){
		return graph.getVertexCount();
	}
	
	/**连接总数*/
	public int getConnectCount(){
		return graph.getEdgeCount();
	}
	
	/**节点入度*/
	public int getInDegree(N node){
		return graph.inDegree(node);
	}
	
	/**节点出度*/
	public int getOutDegree(N node){
		return graph.outDegree(node);
	}
	
	/**所有节点*/
	public Collection<N> getAllNodes(){
		return graph.getVertices();
	}
	
	/**节点的前驱结点*/
	public Collection<N> getPreNodes(N node){
		return graph.getPredecessors(node);
	}
	
	/**节点的 后继结点**/
	public Collection<N> getNextNodes(N node){
		return graph.getSuccessors(node);
	}
	
	/**节点是否是根节点**/
	public boolean isRoot(N node){
		return graph.inDegree(node)==0 && graph.outDegree(node)>0;
	}
	
	/**节点是否是叶子节点**/
	public boolean isLeaf(N node){
		return graph.inDegree(node)>0 && graph.outDegree(node)==0;
	}
	
	/**节点是否是分支节点**/
	public boolean isSplit(N node){
		return graph.inDegree(node)==1 && graph.outDegree(node)>1;
	}
	
	/**节点是否是合并节点**/
	public boolean isJoin(N node){
		return graph.inDegree(node)>1 && graph.outDegree(node) == 1;
	}
	
	/**图中是否存在节点**/
	public boolean containsNode(N node){
		return graph.containsVertex(node);
	}
	
	
	/**
	 * 初始化
	 * @return 该路程是否合法
	 * **/
	public boolean init(){
		boolean isValid = true;
		for(N node:graph.getVertices()){
			if(isRoot(node)){
				setRoot(node);
			}
			if(isJoin(node)){
				joinNodes.add(node);
			}
			if(isSplit(node)){
				splitNodes.add(node);
			}
			if(isLeaf(node)){
				leafNodes.add(node);
			}
		}
		return isValid;
	}
	
	/**设置根节点*/
	public void setRoot(N root) {
		if(this.root!=null&&root!=null&&!root.equals(this.root)){
			new IllegalStateException("包含多个节点入度为0的节点:"+this.root.toString()+" vs "+root.toString()).printStackTrace();
		}
		this.root = root;
	}
	
	/**获取根节点，即是 开始节点 */
	public N getRootNode() {
		return root;
	}

	/**获取所有叶子节点 */
	public List<N> getLeafNodes() {
		return leafNodes;
	}

	/**获取所有合并节点 */
	public List<N> getJoinNodes() {
		return joinNodes;
	}
	
	/**获取所有分支节点 */
	public List<N> getSplitNodes() {
		return splitNodes;
	}


}

