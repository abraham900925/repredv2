<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess" />

<%
		if (!ga.AccessGranted(session, "grantAccess")) {
		response.sendRedirect("login.jsp");
	}
%>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="css/styles.css" rel="stylesheet" type="text/css">
		<title>JSP Page</title>
		<style type="text/css">
            <!--
            body,td,th {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 12px;
            }
            -->
        </style>
	</head>
	<body>
<!-- 
		<div align="center">
			<strong>Carga del Día </strong>
		</div>
 -->
		<br><br>
		<form action="ControllerServlet?action=" method="post"
			enctype="application/x-www-form-urlencoded" name="frmDailyLoad"
			target="mainFrame" id="frmDailyLoad">
			<div align="center">
				<table width="60%" height="25" border="0" cellpadding="0" cellspacing="0" class="tbl_border" >
                <tr>
                   <th class= "td_header" width="100%" colspan=4 >CARGO DEL D&Iacute;A<br></th>
                </tr>
					<tr>
						<td width="40%">
							<div align="center">
								Archivo:
								<input name="txtfArchive" type="text" id="txtfArchive"
									tabindex="1" size="15" />
							</div>
						</td>
						<td width="34%">
							<div align="center">
								Fecha Inicial:
								<input name="txtStartDate" type="text" id="txtStartDate"
									tabindex="2" size="15" />
							</div>
						</td>
						<td width="26%">
							<div align="center">
								Generar Acumulado:
								<input name="cbAcum" type="checkbox" id="cbAcum" tabindex="3"
									value="checkbox" checked>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<br />
			<div align="center">
				<table width="4%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="33%">
							<div align="center">
								<input name="btnOK" type="submit" id="btnOK" tabindex="4"
									value="Aceptar" />
							</div>
						</td>
						<td width="33%">
							<div align="center"></div>
						</td>
						<td width="34%">
							<div align="center">
								<input name="btnClear" type="reset" id="Modificar" tabindex="5"
									value="Borrar" />
							</div>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
