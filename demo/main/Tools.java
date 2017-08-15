package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class Tools {
	
	/**
	 * 从大数据集中拆分出小样本, size指定获取的行数
	 * */
	public static void getSample(String src, String des, long size) throws IOException {
		
		FileInputStream fis = new FileInputStream(src);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		FileOutputStream fos = new FileOutputStream(des);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		
		for (int row = 0; row < size; row++) {
			String line = br.readLine();
			bw.write(line);
			bw.write(System.lineSeparator());
		}
		
		bw.close();
		osw.close();
		fos.close();
		
		br.close();
		isr.close();
		fis.close();
		
		System.out.println("get sample successful, size is: " + size);
	}
	
	/**
	 * 提取数据文件的指定列(列从0开始计数),按行写入到新文件
	 * */
	public static void getColumn(String src, String des, int colNum) throws IOException {
		FileInputStream fis = new FileInputStream(src);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		FileOutputStream fos = new FileOutputStream(des);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		
		// 依次读取文件每一行
		String line = br.readLine();
		while (line != null) {
			String column = line.split("\\t")[colNum].trim();	// 提取出指定列
			if (column.equals("")) {							// 过滤无效空记录
				;
			} else {
				bw.write(column);
				bw.write(System.lineSeparator());				// 每个记录占一行				
			}
			
			line = br.readLine();
		}
		
		bw.close();
		osw.close();
		fos.close();
		
		br.close();
		isr.close();
		fis.close();
		
		System.out.println("extract column to file successful, cloumnNum is: " + colNum);
	}
	
	/**
	 * 数据规模统计
	 * */
	public static void getSize(String src) throws IOException {
		HashSet<String> chars_set = new HashSet<String>();		// 存储单个字符
		HashSet<String> terms_set = new HashSet<>();			// 存储所有纪录
		
		FileInputStream fis = new FileInputStream(src);
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);
		
		String line = br.readLine();
		while (line != null) {
			terms_set.add(line);
			
			String[] arrs = line.split("");
			for (String arr : arrs)
				chars_set.add(arr);
			
			line = br.readLine();
		}
		
		System.out.println("chars_set.size():" + chars_set.size());
		System.out.println("terms_set.size():" + terms_set.size());
	}
	
	public static void main(String[] args) throws IOException {
		String path1 = "";
		String path2 = "";
		
		// 抽取第一列
		getColumn(path1, path2, 1);
		getSize(path2);
	}
}
