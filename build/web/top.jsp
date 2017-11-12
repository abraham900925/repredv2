<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>PROSA :: REPRED</title>
	<style type="text/css">
	<!--
		body {
			background-color: #17497C;
		}
	-->
	</style>
  </head>
  
  <body><div align="left"><img border="0" src="images/logoProsa.jpg"> 
  </div></body>
</html>
