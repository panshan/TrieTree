package main;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * 定义Trie树，以Node作为节点类
 * @author stevinpan
 * */

public class TrieTree {
	public Node root = null;		// 树的根节点，不存储字符信息
	private int min_count = 0;	// 剪枝的最小阈值
	
	/**
	 * 构造方法，初始化根节点
	 * */
	public TrieTree() {
		root = new Node();
	}
	
	/**
	 * 判断当前树是否存在指定字符串
	 * */
	public boolean isExist(String word) {
		Node curr = root;		// 获取根结点作为当前指针
		
		if (curr == null || word == null)
			return false;
		
		// 依次遍历当前字符串的每个串
		for (int index = 0; index < word.length(); index++) {
			Node next = curr.getNode(word.charAt(index));		// 获取包含当前字符的子节点
			
			if (next != null) {		// 子节点存在，指针后移
				curr = next;
			} else {				// 子节点不存在，直接返回
				return false;
			}
		}
		
		// 根据curr节点的标志位，判断是否是单词结尾
		if (curr.isEnd) {
			System.out.println(curr.count);
			return true;
		}
		return false;
	}
	
	/**
	 * 插入字符串，相同字符串也要插入
	 * 插入成功返回true
	 * 
	 * */
	public boolean insert(String word) {
		if (word == null)
			return false;
		
		if (root == null)
			root = new Node();

		Node curr = root;			// 获取根结点作为当前指针
		
		// 依次遍历字符串，树路径上所有节点count++
		for (int index = 0; index < word.length(); index++) {
			Node next = curr.getNode(word.charAt(index));		// 获取包含当前字符的子节点
			
			if (next != null) {		// 存在包含当前字符的子节点，子节点count++,指针后移
				next.count++;
				curr = next;
			} else {				// 不存在包含当前字符的节点，则创建新节点，指针后移
				next = new Node(word.charAt(index), curr);
				curr.childList.add(next);
				curr = next;
			}
		}
		// 标注出单词结尾
		curr.isEnd = true;
		curr.org_count++;
		
		return true;
	}
	
	/**
	 * trie树的层次遍历，每行一个职业名称
	 * */
	public void printAll() {
		printAll(root);
	}
	private void printAll(Node node) {
		LinkedList<Node> childList = node.childList;		// 获取当前节点的子节点
		for (Node child : childList) {						// 依次遍历每个子节点，如果子节点isEnd==true,则反向输出该节点
			if (child.isEnd) {
				System.out.println(child.count+"\t"+toRoot(child));
			}
			printAll(child);
		}
	}
	
	/**
	 * 层次遍历，每行按层次输出
	 * 从根节点开始，遇到isEnd=true则输出向上的路径，层次遍历
	 * */
	public LinkedList<SimpleEntry<Integer, String>> level() {
		LinkedList<SimpleEntry<Integer, String>> result = new LinkedList<SimpleEntry<Integer, String>>();
		level(root, result);
		
		return result;
	}
	private void level(Node node, LinkedList<SimpleEntry<Integer, String>>  results) {
		// 遇到isEnd=true节点，向上回溯
		if (node.isEnd) {
			// 获取当前节点向上的全路径
			String full_path = toRoot(node);
			
			// 获取当前节点各个isEnd祖先节点全路径
			String parent_paths = toParents(node.parent);
						
			// 输出到List
			results.add(new SimpleEntry<Integer, String>(node.count, node.org_count + "\t" + full_path + parent_paths));
			
			// 递归访问子节点
			for (Node child : node.childList) {
				level(child, results);
			}
			
		} else {	// 依次遍历子节点
			for (Node child : node.childList) {
				level(child, results);
			}
		}
	}
	
	/**
	 * 根据当前节点，向上反向输出,直至root节点
	 * */
	public String toRoot(Node node) {
		if (node != root) {
			return node.val + toRoot(node.parent);
		} else {
			return "";
		}
	}
	
	/**
	 * 根据当前节点，获取所有祖先节点的路径，以"\t"分隔输出
	 * */
	public String toParents(Node node) {
		String res = "";
		while (node != root) {
			if (node.isEnd && (node.org_count > this.min_count)) {
				res += "\t" + toRoot(node);
				node = node.parent;
			} else {
				node = node.parent;
			}
		}
		
		return res;
	}
	
	/**
	 * 查找叶子节点,返回叶子节点组成的链表
	 * */
	public LinkedList<Node> findLeaf() {
		LinkedList<Node> leafList = new LinkedList<>();
		findLeaf(root, leafList);
		
		return leafList;
	}
	private void findLeaf(Node node, LinkedList<Node> leafList) {
		if (node.childList.size() == 0) {	// 找到叶子节点
			leafList.add(node);
		} else {							// 遍历当前节点的子节点
			LinkedList<Node> childList = node.childList;
			for (Node child : childList) {
				findLeaf(child, leafList);
			}
		}
	}
	
	/**
	 * 从叶子节点向上回溯遍历,遇到isEnd=true节点就获取词路径
	 * */
	public LinkedList<SimpleEntry<Integer, String>> back() {
		LinkedList<Node> leafList = findLeaf();
		LinkedList<SimpleEntry<Integer, String>> result = new LinkedList<>();
		
		// 从每个叶子节点开始向上遍历
		for (Node node : leafList) {
			Node curr = node;
			int count = curr.count;
			StringBuilder sb = new StringBuilder();
			
			sb.append("\t" + count);
			while (curr != root) {
				if (curr.isEnd) {
					String path = toRoot(curr);	// 获取词路径
					sb.append("\t");
					sb.append(path);						
				}
				
				curr = curr.parent;
			}
			
			if (sb.length() > 0)
				result.add(new SimpleEntry<Integer, String>(count, sb.toString()));
		}
		
		return result;
	}
	
	/**
	 * 从根节点开始，更新每个isEnd=true节点的max_org_count
	 * */
	public void updateMaxOrgCount() {
		updateMaxOrgCount(root);
	}
	private void updateMaxOrgCount(Node node) {
		if (!node.isEnd) {		// 如果当前节点isEnd=false，则递归遍历子节点
			LinkedList<Node> childList = node.childList;
			for (Node child : childList) {
				updateMaxOrgCount(child);
			}
		} else {				// 如果当前节点的isEnd=true
			Node parent_isEnd = getParent(node);	// 获取当前节点的isEnd=true父节点
			if (parent_isEnd == null) {				// 如果不存在这样的父节点，则当前节点的org_count即为max_org_count
				node.max_org_count = node.org_count;
			} else {								// 如果存在这样的父节点，则将当前节点的org_count与父节点的max_org_count比较，更新当前节点的max_org_count
				node.max_org_count = node.org_count > parent_isEnd.max_org_count ? node.org_count : parent_isEnd.max_org_count;
			}
			
			LinkedList<Node> childList = node.childList;
			for (Node child : childList) {
				updateMaxOrgCount(child);
			}
		}
	}
	
	/**
	 * 获取当前节点的上一个isEnd=true节点
	 * */
	public Node getParent(Node node) {
		Node parent = node.parent;			// 获取当前节点的父节点
		
		while (parent != null && !parent.isEnd) {	// 父节点存在，且父节点的isEnd=false，则向上遍历
			parent = parent.parent;
		}
		
		if (parent == null) {				// 最终不存在isEnd=true的父节点
			return null;
		} else {							// 存在isEnd=true的父节点
			return parent;
		}
	}
	
	/**
	 * trie树剪枝
	 * */
	public void cart(int min_count) {
		this.min_count = min_count;
		cart(root);
	}
	private void cart(Node node) {
		LinkedList<Node> childList = node.childList;		// 获取当前节点的子节点
		for (Node child : childList) {						// 依次遍历每个子节点，如果子节点isEnd==true && max_org_count < min_count, 则设置isEnd=false
			if (child.isEnd && child.max_org_count < this.min_count) {
				child.isEnd = false;
			}
			cart(child);
		}
	}
//	
//	public static void main(String[] args) {
//		TrieTree tree = new TrieTree();
//		tree.insert(new StringBuilder("博士生导师").reverse().toString());
//		tree.insert(new StringBuilder("硕士生导师").reverse().toString());
//		tree.insert(new StringBuilder("硕士生导师").reverse().toString());
//		tree.insert(new StringBuilder("博士硕士生导师").reverse().toString());
//		tree.insert(new StringBuilder("导师").reverse().toString());
//		tree.insert(new StringBuilder("导师").reverse().toString());
//		tree.insert(new StringBuilder("导师").reverse().toString());
//		tree.insert(new StringBuilder("老师").reverse().toString());
//		tree.insert(new StringBuilder("高级工程师").reverse().toString());
//		
//		tree.updateMaxOrgCount();
//		
//		System.out.println("***************************");
//		LinkedList<SimpleEntry<Integer, String>> paths = tree.level();
//		for (SimpleEntry<Integer, String> entry : paths) {
//			System.out.println(entry.getKey()+"\t"+entry.getValue());
//		}
//	}

}
