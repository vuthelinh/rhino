package com.rhino.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.Properties;

import com.rhino.utility.Const;
import com.rhino.utility.Excel_Reader;

public class ReportUtil {
	/*
	 *  Author: vuthelinh@gmail.com 
	 */
	
	public static String folder_report = "test-output";
	public static Date today = new Date();
	
	public static Excel_Reader list_suite = null;
	public static Excel_Reader current_suite = null;
	
	public static Properties CONFIG;

	public static void main(String[] arg) throws Exception {
			
		new File(folder_report).mkdirs(); // make directory 
			
		// Read setting.properties
		FileInputStream fs = new FileInputStream(System.getProperty("user.dir")	+ Const.CONFIG_SYS);
		CONFIG = new Properties();
		CONFIG.load(fs);

		int failed = 0;
		int skipped = 0;

		// Create index.html
		String indexHtmlPath = folder_report + "\\index.html";
		new File(indexHtmlPath).createNewFile();
		
		// get totals test suite in list suite
		list_suite = new Excel_Reader(System.getProperty("user.dir") + Const.BIN_LIST_FILE);
		String prefix = list_suite.getCellData(Const.SUITE_LIST_SHEET, Const.DATA_PREFIX, 2);
		
		int totalsTestSuite = list_suite.getRowCount(Const.SUITE_LIST_SHEET)-1; // no count header file
				
		try {

			FileWriter fstream = new FileWriter(indexHtmlPath);
			BufferedWriter indexHTML = new BufferedWriter(fstream);
			// Design index.html
			indexHTML.write("<html><HEAD> <TITLE>Automation Test Results</TITLE></HEAD>"
					+ "<body>"
					
					+ "<h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6>"
						+ "<b>Automation Test Results</b>"
					+ "</h4>"
						
					+ "<table  width=\"100%\" >"
					+ "<tr>"
					+ 	"<td>"
					+ 		"<table border=\"1\">"
					+ 			"<tr>"
					+ 				"<h4><FONT COLOR=660000 FACE=Arial SIZE=4.5> "
					+ 					"Test Details "
					+ 				"</h4>"
					+ 	"<td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75>"
					+ 		"<b>"
					+ 			"Date"
					+ 		"</b>"
					+ 	"</td>"
					+ 	"<td width=220 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75>"
					+ 		"<b>" 
					+ 			today.toString() 
					+		"</b> "
					+ 	"</td>"
					+ "</tr>"); 
			
			indexHTML.write("</td></tr><tr><td width=150 align=left bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE=Arial SIZE=2.75><b>Project</b></td><td width=150 align=left><FONT COLOR=#153E7E FACE=Arial SIZE=2.75><b>");
			indexHTML.write(CONFIG.getProperty("Project"));
			indexHTML.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Version</b></td><td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>");
			indexHTML.write(CONFIG.getProperty("Version"));
			indexHTML.write("</b></td></tr><tr><td width=150 align= left  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2.75><b>Author</b></td><td width=150 align= left ><FONT COLOR=#153E7E FACE= Arial  SIZE=2.75><b>");
			indexHTML.write(CONFIG.getProperty("Author"));
			indexHTML.write("</b></td></table></td><td = align=\"right\">"
					+ "<div id=\"chart_div\" style=\"width: 450px; height: 250px;\"></div>"
					+ "</td></tr>"
					+ "</table><h4> <FONT COLOR=660000 FACE= Arial  SIZE=4.5>Report</h4>"
					+ "<table  border=1 cellspacing=1 cellpadding=1 width=100%>"
					+ "<tr>"
					+ "<td width=25% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>TEST SUITE</b></td>"
					+ "<td width=45% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>DESCRIPTION</b></td>"
					+ "<td width=25%  align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>COMMENT</b></td>"
					+ "<td width=5% align= center  bgcolor=#153E7E><FONT COLOR=#E0E0E0 FACE= Arial  SIZE=2><b>EXECUTION RESULT</b></td>"
					+ "</tr>");

			
			   String suite_result = "";
			   totalsTestSuite=totalsTestSuite+2;

			for (int suiteIndex = 2; suiteIndex < totalsTestSuite; suiteIndex++) {

				suite_result = "";

				String suiteName = list_suite.getCellData(Const.SUITE_LIST_SHEET, Const.SUITE_NAME_COL, suiteIndex);
				String suiteDescription = list_suite.getCellData(Const.SUITE_LIST_SHEET, Const.SUITE_DES_COL, suiteIndex);
				String suiteComment = list_suite.getCellData(Const.SUITE_LIST_SHEET, Const.SUITE_COMMENT_COL, suiteIndex);

				current_suite = new Excel_Reader(System.getProperty("user.dir") + Const.BIN_SCENARIO_PATH + suiteName + ".xlsx");

				for (int currentTestCaseID = 2; currentTestCaseID <= current_suite.getRowCount(Const.CASE_LIST_SHEET); currentTestCaseID++) {

					// make the file corresponding to test Steps
					String testSteps_file = folder_report + "\\" + suiteName + "_steps.html";
					new File(testSteps_file).createNewFile();

					int rows = current_suite.getRowCount(Const.CASE_STEP_SHEET);
					int cols = current_suite.getColumnCount(Const.CASE_STEP_SHEET);
					
					FileWriter fstream_test_steps = new FileWriter(testSteps_file);
					BufferedWriter codeHTML = new BufferedWriter(fstream_test_steps);
					
					codeHTML.write("<html><HEAD> <TITLE>"
									+ suiteName
									+ " Test Results</TITLE></HEAD><body><h4 align=center><FONT COLOR=660066 FACE=AriaL SIZE=6>"
									+ suiteName
									+ " Detailed Test Results</h4><table width=100% border=1 cellspacing=1 cellpadding=1 >");
					codeHTML.write("<tr>");

					for (int colNum = 0; colNum < cols; colNum++) {
						codeHTML.write("<td align= center bgcolor=#153E7E><FONT COLOR=#ffffff FACE= Arial  SIZE=2><b>");
						codeHTML.write(current_suite.getCellData(	Const.CASE_STEP_SHEET, colNum, 1));
					}
					codeHTML.write("</b></tr>");

					// fill the whole sheet in detail report
					boolean result_col = false;
					
					for (int rowNum = 2; rowNum <= rows; rowNum++) {
						codeHTML.write("<tr>");
						for (int colNum = 0; colNum < cols; colNum++) {
							String data = current_suite.getCellData(Const.CASE_STEP_SHEET, colNum, rowNum);
							result_col = current_suite.getCellData(Const.CASE_STEP_SHEET, colNum, 1).contains(prefix);
							
							if (result_col) {
								if (data.contains(Const.PASS)) {
									codeHTML.write("<td align=center bgcolor=green><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
								} else if (data.contains(Const.FAIL)) {
									codeHTML.write("<td align=center bgcolor=red><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
									if (suite_result.equals("")) {
										suite_result = Const.FAIL;
										failed++;
									}
								}else{
									codeHTML.write("<td align=center bgcolor=#969696><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
									if (suite_result.equals("")) {
										suite_result = Const.SKIP;
										skipped++;
								} }
							}
							else
							codeHTML.write("<td align= center bgcolor=#F6CEF5><FONT COLOR=#000000 FACE= Arial  SIZE=1>");
							codeHTML.write(data);
						}
						codeHTML.write("</tr>");
					}
					codeHTML.write("</tr>");
					codeHTML.write("</table>");
					codeHTML.close();

				}

				indexHTML.write("<tr>");
				indexHTML.write("<td align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2>"
						+ 		"<a href=" + suiteName.replace(" ", "%20")	+ "_steps.html>"
						+ 			suiteName 
						+ 		"</a>"
						+ "</td>");
				
				indexHTML.write("<td align= left><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + suiteDescription);
				indexHTML.write("<td align= center><FONT COLOR=#153E7E FACE= Arial  SIZE=2>" + suiteComment);
								
				indexHTML.write("</td><td align=center  bgcolor=");
				
				if (suite_result.equals(Const.FAIL)) {
					indexHTML.write("#FFB3B5><FONT COLOR=white FACE=Arial SIZE=2><b>FAILED</b></td></tr>");
				}else if (suite_result.equals(Const.SKIP)) {
					indexHTML.write("#FFCC00><FONT COLOR=white FACE=Arial SIZE=2><b>SKIPPED</b></td></tr>");
				} else {
					indexHTML.write("#00D636><FONT COLOR=white FACE=Arial SIZE=2><b>PASSED</b></td></tr>");
				}
			}
			int numberOfSortedSuite = list_suite.getRowCount(Const.SUITE_LIST_SHEET)-1;
			int result_passed =  numberOfSortedSuite - failed	- skipped;
			// Close the output stream
			
			indexHTML.write("</table>"
					+ "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>"
					+ " <script type=\"text/javascript\">"
					+ " google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
					+ "google.setOnLoadCallback(drawChart);"
					+ " function drawChart() {"
					+ " var data = google.visualization.arrayToDataTable(["
					+ " ['Result','Number of Result']," 
					+ " ['PASSED  = " + result_passed + "',"	+ result_passed	+ "],"
					+ " ['FAILED  = " + failed + "'," + failed + "],"
					+ " ['SKIPPED = " + skipped	+ "'," + skipped + "]" + " ]);"
					+ " var options = {"
					+ "title: 'Total Test Suites: "	+ numberOfSortedSuite + "'"
					+ ",slices: {0: {color: '#00D636'}, 1: {color: '#FFB3B5'}, 2: {color:'#FFCC00'}}"
					+ ",is3D:true  };"
					+ " var chart = new google.visualization.PieChart(document.getElementById('chart_div'));"
					+ "chart.draw(data, options);" + " }" + "</script>");
			indexHTML.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
