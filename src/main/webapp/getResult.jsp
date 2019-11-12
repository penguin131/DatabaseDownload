<%@ page import="java.util.List"%><%@ page import="myApp.rest.DatabaseRequests"%><%@ page import="com.google.gson.Gson"%><%@ page language="java"
contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	List data = null;
	try {
	    data = DatabaseRequests.getTableData("MAIN.T_DICTIONARY");}
	catch (Exception e) {
	    e.printStackTrace(); }
	String jsonStr = new Gson().toJson(data);
%>
<%=jsonStr%>