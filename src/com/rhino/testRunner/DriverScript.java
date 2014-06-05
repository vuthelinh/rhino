package com.rhino.testRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.rhino.utility.Const;
import com.rhino.utility.Excel_Reader;
import com.rhino.keyword.Keywords;

import static com.rhino.keyword.Keywords.OBJECT;
import static com.rhino.keyword.Keywords.CONFIG;
import static com.rhino.keyword.Keywords.APP_LOGS;

public class DriverScript {
			/*
			 *  Author: vuthelinh@gmail.com 
			 */
			 
	// suite.xlsx
	public static Excel_Reader listFile = new Excel_Reader(System.getProperty("user.dir") + Const.BIN_LIST_FILE);
	public static String prefix = listFile.getCellData(Const.SUITE_LIST_SHEET, Const.DATA_PREFIX, 2);
	
	public static Method method[];

	public static Map<String, Method> methods = new HashMap<String, Method>();

	// current test suite
	public static Excel_Reader currentSuite;
	public int counterDataSet = 0;

	public static Keywords keywords = new Keywords();
	public static String keyword_execution_result;
	public ArrayList<String> list_result_action=null;
		
	// Case
	public static ArrayList<String> list_test_case= null;
	public static ArrayList<String> run_mode_case= null;
	public static ArrayList<String> run_mode_data_set= null;
	public static ArrayList<String> list_steps= null;
	public static ArrayList<String> list_data= null;
	public static ArrayList<String> list_object= null;
	
	// Suite
	public static ArrayList<String> list_suite= new ArrayList<String>();
	public static ArrayList<String> run_mode_suite=  new ArrayList<String>();

	public static void main(String[] args) throws IllegalAccessException,IllegalArgumentException, InvocationTargetException, IOException {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		
		loading_config_sys();
		loading_object_repository();

		DriverScript testRunner = new DriverScript();
		testRunner.start();
	}

	public DriverScript() {

		method = keywords.getClass().getMethods();// gets all methods in src/com/rhino/keyword.java class
		for (Method mt : method) {
			methods.put(mt.getName(), mt); // This class implements a hash table, which maps [keys] to [values]
			/*
			 * mt.getName : type [key] 
			 * mt         : public java.lang.String com.rhino.keyword.Keywords.type(java.lang.String,java.lang.String) [value]
			 */
		}
	}

	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		list_suite     = listFile.get_data_cols(Const.SUITE_LIST_SHEET, Const.SUITE_NAME_COL);
		run_mode_suite = listFile.get_data_cols(Const.SUITE_LIST_SHEET,Const.RUN_MODE);

		for (int i = 1; i < list_suite.size(); i++) {
			if (run_mode_suite.get(i).equals("YES")) {
				
				currentSuite = new Excel_Reader(System.getProperty("user.dir") + Const.BIN_SCENARIO_PATH + list_suite.get(i) + ".xlsx");

				list_test_case = new ArrayList<String>();
				list_test_case = currentSuite.get_data_cols(Const.CASE_LIST_SHEET, Const.CASE_NAME_COL);
				
				run_mode_case = new ArrayList<String>();
				run_mode_case = currentSuite.get_data_cols(Const.CASE_LIST_SHEET, Const.RUN_MODE);
				
				for (int j = 1; j < list_test_case.size(); j++) {
					
					if (run_mode_case.get(j).equals("Y")) {
						
						run_mode_data_set = new ArrayList<String>();
						run_mode_data_set = currentSuite.get_data_cols(list_test_case.get(j), Const.RUN_MODE);
					
							for ( counterDataSet = 1; counterDataSet < run_mode_data_set.size(); counterDataSet++) {
								if (run_mode_data_set.get(counterDataSet).equals("Y")) {

									System.out.println("=====Executing Suite: " + list_suite.get(i)
											+ " - Test Case: " + list_test_case.get(j)
											+ " - Data set #["+ counterDataSet + "]======");
									
									APP_LOGS.debug("=====Executing Suite: " + list_suite.get(i)
											+ " - Test Case: " + list_test_case.get(j)
											+ " - Data set #["+ counterDataSet + "]======");
									
									executeKeywords(list_test_case.get(j));
								}else{
									System.out.println("[info] Executing: | Running mode of data set #["+ counterDataSet + "] is NO " );
									APP_LOGS.debug("[info] Executing: | Running mode of data set #["+ counterDataSet + "] is NO " );
									list_result_action = new ArrayList<String>();
									list_result_action.add(Const.SKIP);
									write_log(list_result_action,list_test_case.get(j));
								}
							}// end For
					}else{
						System.out.println("[info] Executing: | Running mode test case " + list_test_case.get(j) + " is NO");
						APP_LOGS.debug("[info] Executing: | Running mode test case " + list_test_case.get(j) + " is NO");
						//list_result_action.add(Const.SKIP);
					}
				}
			}else{
				System.out.println("[info] Executing: | Running mode suite " + list_suite.get(i) + " is NO");
				APP_LOGS.debug("[info] Executing: | Running mode suite " + list_suite.get(i) + " is NO");
				//list_result_action.add(Const.SKIP);
			}
		} // end for
	}

	public void executeKeywords(String nameTestCase) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			
		String data="";
		String object="";
		
		list_result_action = new ArrayList<String>();
		
		ArrayList<String> dataList = new ArrayList<String>();
		
		int numberOfStep = currentSuite.get_total_row_by_value(Const.CASE_STEP_SHEET, Const.CASE_NAME_COL, nameTestCase );
		
		list_steps = new ArrayList<String>();
		list_steps = currentSuite.get_data_cols(Const.CASE_STEP_SHEET, Const.KEYWORD);
		
		list_data = new ArrayList<String>();
		list_data = currentSuite.get_data_cols(Const.CASE_STEP_SHEET, Const.DATA, list_steps.size());
		
		list_object = new ArrayList<String>();
		list_object = currentSuite.get_data_cols(Const.CASE_STEP_SHEET, Const.OBJECT, list_steps.size());
		
		for (int i = 1; i <= numberOfStep; i++) {
			
				data = list_data.get(i);

				run_mode_data_set = new ArrayList<String>();
				run_mode_data_set = currentSuite.get_data_cols(nameTestCase, Const.RUN_MODE);
				
				if (data.startsWith("col-")) {
					dataList = currentSuite.get_data_cols(nameTestCase,data.split("-")[1], run_mode_data_set.size());
					data = dataList.get(counterDataSet);
				}
				
				object = list_object.get(i);

				Method mt = methods.get(list_steps.get(i));

				if (mt != null) {

					keyword_execution_result = (String) mt.invoke(keywords,object, data);

					list_result_action.add(keyword_execution_result);

					if (keyword_execution_result.contains("FAIL") && CONFIG.getProperty("capture_mode").equals("ON")) {
						String nameImg = nameTestCase + "_Step_" + i + "_dataTest_" + counterDataSet;						
						Keywords.captureEntirePageScreenshot("",nameImg);
					}
				} else {
					System.out.println("[error] Unknown command: '" + list_steps.get(i) + "'");
					list_result_action.add(Const.FAIL);
					Keywords.close(object, data);					
					break;
				}
			
		}// end for 
		write_log(list_result_action,nameTestCase);
	}

	public void write_log(ArrayList<String> tmp, String nameTestCase) {

		String colName = prefix + "_" + counterDataSet;
		currentSuite.addColumn(Const.CASE_STEP_SHEET, colName);
		
		int numberOfStep = tmp.size()+1;
		int y = 0;
		
		for (int i =2;i <= numberOfStep; i++){

			if(nameTestCase.equals(currentSuite.getCellData(Const.CASE_STEP_SHEET, Const.CASE_NAME_COL, i))) {
				currentSuite.setCellData(Const.CASE_STEP_SHEET, colName, i,tmp.get(y));
				
				if(tmp.get(y).startsWith(Const.FAIL)){
					currentSuite.setCellData(Const.CASE_STEP_SHEET, colName, i,tmp.get(y));
					currentSuite.setCellData(nameTestCase, Const.RESULT, (counterDataSet+1), Const.FAIL);
					break;
				}else if(tmp.get(y).startsWith(Const.SKIP)){
					currentSuite.setCellData(Const.CASE_STEP_SHEET, colName, i,tmp.get(y));
					currentSuite.setCellData(nameTestCase, Const.RESULT, (counterDataSet+1), Const.SKIP);
					break;
				}else{
					currentSuite.setCellData(nameTestCase, Const.RESULT, (counterDataSet+1), Const.PASS);
				}
				y++;
			}
		}
	}

	public static void loading_config_sys() throws IOException {

		FileInputStream configSystem = new FileInputStream(System.getProperty("user.dir") + Const.CONFIG_SYS);
		CONFIG = new Properties();
		CONFIG.load(configSystem);
	}

	public static void loading_object_repository() throws IOException {
		FileInputStream fs = null;
		OBJECT = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		URL url = classLoader.getResource(Const.OBJECT_RESPO);

		try {
			File folder = new File(url.toURI());
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				for (File file : files) {
					fs = new FileInputStream(file);
					OBJECT.load(fs);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
