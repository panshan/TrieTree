package main;

import java.util.LinkedList;

/**
 * 树节点类
 * */

public class Node {
	char val;		// 保存当前节点的字符
	int count;		// 统计经过当前节点的字符串数目
	boolean isEnd;	// 标志当前节点是否是一个词的末尾
	
	Node parent;	// 存储当前节点的父节点
	LinkedList<Node> childList;	// 存储当前节点的直接子节点
	
	int org_count;	// 当前节点的原始频次
	int max_org_count;	// 当前节点路径上的最大org_count
	
	/**
	 * 带参构造方法，构造含有指定字符val的节点，并指明新建节点的父节点
	 * */
	public Node(char val, Node parent) {
		this.val = val;
		this.count = 1;
		this.isEnd = false;
		
		this.parent = parent;
		this.childList = new LinkedList<Node>();
		
		this.org_count = 0;
		this.max_org_count = 0;
	}

	/**
	 * 无参构造方法，初始化val=' ', count=0, isEnd=false, parent=null, childList=new List()
	 * */
	public Node() {
		this(' ', null);
	}
	
	
	/**
	 * 根据指定的字符，获取当前节点的子节点。子节点不存在则返回null
	 * */
	public Node getNode(char val) {
		// 依次遍历链表
		for (Node child : childList) {
			if (child.val == val)
				return child;
			else
				continue;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return this.val + ":" + this.count;
	}
}
