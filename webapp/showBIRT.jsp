<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%!
	String urlBIRT;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Reportes RED (REPRED)</title>

	</head>

	<body>
		<%
			//Obtener la url del reporte
			urlBIRT = (String)session.getAttribute("urlBIRT");
		    System.out.println("URL BIRT: " + urlBIRT );
			response.sendRedirect(urlBIRT);
			//response.sendRedirect("http://www.nfl.com");
		%>
	</body>
</html>
