rhino
=====

As a manual tester who have limited knowledge about the programming language but  I would like to have a tool to allow me write automation test script by using the formal specification language.


>ant download_webdriver

>ant update_lib

>ant keyword_update -Dkeyword.update="path\to\folder_contains_your_scripts"

>ant clean install 
	-Dselenium.host=http://xxx.xxx.xxx
	-Dselenium.browser=iexplorer 
	-Dselenium.timeout=60
	-Dcapture.mode=ON
	-Dselenium.suite=.\scripts_demo
	-Dselenium.report=.\target 
	-Dweb.driver.ie=C:\IEdriverServer.exe

>ant report -Dselenium.report=.\target

