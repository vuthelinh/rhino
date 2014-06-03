package com.rhino.utility;

public final class Const {

	/*
	 *  Author: vuthelinh@gmail.com 
	 */
	
	private Const() {
		// private constructor prevent user create new instant
	}
	
	public static final String RUN_MODE         		= "Runmode";
	
	// Scenario
	public static final String BIN_SCENARIO_PATH 		= "\\bin\\";

	
	public static final String BIN_LIST_FILE 		=  BIN_SCENARIO_PATH + "ListFile.xlsx";
	
	public static final String SUITE_LIST_SHEET 		= "List Suite";
	public static final String DATA_PREFIX 			= "Prefix";
	public static final String SUITE_NAME_COL 		= "Test Suite";
	public static final String SUITE_DES_COL  		= "Description";
	public static final String SUITE_COMMENT_COL 		= "Comment";
	
	// Test Steps
	public static final String DATA 			= "Data";
	public static final String OBJECT 			= "Object";
	public static final String RESULT 			= "Result";
	public static final String KEYWORD 			= "Keyword";
	
	// Configuration
	public static final String CONFIG_SYS 			= "\\setting.properties";
	public static final String CONFIG 			= "config";
	
	// Object Repository
	//public static final String OBJECT_RESPO 		= "com/rhino/object";
	public static final String OBJECT_RESPO 		= "object";
	
	// Report
	public static final String IMAGES 			= "\\screenshot\\";
	
	// System
	public static final String PASS 			= "PASS";
	public static final String FAIL 			= "FAIL";
	public static final String SKIP 			= "SKIP";
		
	// Test case	
	public static final String CASE_LIST_SHEET 		= "List Case";
	public static final String CASE_STEP_SHEET 		= "Test Steps";
	public static final String CASE_NAME_COL 		= "Test Case";
	public static final String CASE_DES_COL 		= "Description";
}
