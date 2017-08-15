package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;


/**
 * 程序入口类
 * */
public class Main {
	public static void main(String[] args) throws Exception {
		String src = "";
		String des = "";
		int min_count = 100;			// 树剪枝的阈值
		
		TrieTree tree = treeInit2(src, 1);	// 树的初始化, 输入为包含词条列的文件，需要指明目标词条所在的列号(列从0计数)
		
		/**
		 * 剪枝之前必须更新节点的max_org_count值
		 * */
		tree.updateMaxOrgCount();
//		tree.cart(min_count);
		
		// 遍历并排序
		LinkedList<SimpleEntry<Integer, String>> results = tree.level();
		Collections.sort(results, new Comparator<SimpleEntry<Integer, String>>(){
			@Override
			public int compare(SimpleEntry<Integer, String> o1, SimpleEntry<Integer, String> o2) {
				return o2.getKey() - o1.getKey();
			}
		});
				
		// 将结果写出到文件
		saveToFile(results, des);
		System.out.println("process sucessful");
	}
	
	/**
	 * 树的初始化：读取输入文件中的每一行，插入trie树，最后返回树
	 * @param src : 词条文件，每个词条占一行
	 * @return tree : 返回初步构造的树
	 * @throws IOException 
	 * */
	public static TrieTree treeInit1(String src) throws IOException {
		TrieTree tree = new TrieTree();
		
		/**
		 * 从文件读取词条，字符串反序后插入TrieTree
		 * */
		FileInputStream fis = new FileInputStream(src);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String line = br.readLine();
		while (line != null) {
			if (line.equals("")) {	// 过滤掉词条为空字符串
				;
			} else{
				tree.insert(new StringBuilder(line).reverse().toString());
			}
			line = br.readLine();				
		}
		
		br.close();
		isr.close();
		fis.close();
		
		return tree;
	}
	
	/**
	 * 树的初始化：读取输入文件中的每一行，插入trie树，最后返回树
	 * @param src : 词条文件，每个词条占一行
	 * @return tree : 返回初步构造的树
	 * @throws IOException 
	 * */
	public static TrieTree treeInit2(String src, int colNum) throws IOException {
		TrieTree tree = new TrieTree();
		
		/**
		 * 从文件读取词条，字符串反序后插入TrieTree
		 * */
		FileInputStream fis = new FileInputStream(src);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String line = br.readLine();
		while (line != null) {
			String term = line.split("\\t")[colNum].trim();
			if (term.equals("")) {	// 过滤掉词条为空字符串
				;
			} else{
				tree.insert(new StringBuilder(term).reverse().toString());
			}
			line = br.readLine();
		}
		
		br.close();
		isr.close();
		fis.close();
		
		return tree;
	}
	
	/**
	 * 输出便利结果到文件
	 * @param list : 链表，泛型为SimpleEntry<Integer, String>
	 * @param des : 目标存储路径
	 * @return
	 * @author stevinpan
	 * @throws IOException 
	 * */
	public static void saveToFile(LinkedList<SimpleEntry<Integer, String>> list, String des) throws IOException {
		FileOutputStream fos = new FileOutputStream(des);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		
		long counter = 1;
		for (SimpleEntry<Integer, String> entry : list) {
			String kv = entry.getKey()+"\t"+entry.getValue();
			bw.write(kv);
			bw.write(System.lineSeparator());
			
			System.out.println(counter++);
		}
		
		bw.close();
		osw.close();
		fos.close();
	}
}
