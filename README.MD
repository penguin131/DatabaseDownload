Simple application for download csv-files into database and REST service for providing data from a table in json format.

<b>To start in IntelliJ IDEA:</b>
<h4>&emsp;1.Import project from external model (maven). Need JDK1.8.</h4>
<h4>&emsp;2.Create new H2 database and fill config.xml.</h4>
<h4>&emsp;3.Create maven run/debug configuration (tomcat plugin, tomcat7:run)</h4>
<br>
Struts 1 does not support DELETE requests, so i did three actions:<br>
  Download.do, Delete.do and Main.do for working with data.<br>
