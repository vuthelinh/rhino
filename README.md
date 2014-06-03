rhino
=====

As an manual tester who have limited knowledge about the programming language but  I would like to have a tool to allow me write automation test script by using the formal specification language.


>ant download_webdriver

>ant update_lib

>ant clean install 
	-Dselenium.host=http://xxx.xxx.xxx
	-Dselenium.browser=iexplorer 
	-Dselenium.timeout=60
	-Dcapture.mode=ON
	-Dselenium.suite=C:\scripts_demo 
	-Dselenium.report=D:\myreport 
	-Dweb.driver.ie=C:\IEdriverServer.exe

>ant report -Dselenium.report=D:\myreport

