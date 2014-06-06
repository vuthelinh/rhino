package com.rhino.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.rhino.keyword.Keywords;

import java.io.File;

public class UpdateVersion {

	/*
	 * Author: vuthelinh@gmail.com
	 */
	public static Properties CONFIGS;
	public static Excel_Reader keywordFile = null;
	public static Method method[];
	public static Keywords keywords = new Keywords();

	public static void main(String[] args) throws IOException {

		FileInputStream configSystem = new FileInputStream(System.getProperty("user.dir") + Const.CONFIG_SYS);
		CONFIGS = new Properties();
		CONFIGS.load(configSystem);
		
		String myscripts = CONFIGS.getProperty("keyword.update");

		method = keywords.getClass().getMethods();

		File folder = new File(myscripts);
		File[] listOfFiles = folder.listFiles();
		int i = 0;
		
		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith("xlsx") && !file.getName().startsWith("ListFile")) {
				keywordFile = new Excel_Reader(myscripts + file.getName());
				i=2;
				
				System.out.println("Updating keyword for test case: " + keywordFile.path );
				
				if(keywordFile.isSheetExist("Keywords")==false){
					keywordFile.addSheet("Keywords");
					keywordFile.addColumn("Keywords", "Keyword");
				}
				
				for (Method mt : method) {
					keywordFile.setCellData("Keywords", "Keyword", i, mt.getName());
					i++;
				}
			}
		}

	}
}
