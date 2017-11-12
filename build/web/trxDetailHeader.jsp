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
			<strong>Detalle De Transacciones </strong>
		</div>
-->
		<br><br>
		<form action="ControllerServlet?action=" method="post"
			enctype="application/x-www-form-urlencoded" name="frmTrxDetail"
			target="mainFrame" id="frmTrxDetail">
			<div align="center">
				<table width="80%" border="0" cellspacing="0" cellpadding="5" class="tbl_border" >
                <tr>
                   <th class= "td_header" width="100%" colspan=4 >DETALLE DE TRANSACCIONES</th>
                </tr>
					<tr>
						<td>
							<div align="right">
								Tipo Detalle:
								<select name="ddDetailType" id="ddDetailType" tabindex="1">
								</select>
							</div>
						</td>
						<td>
							<div align="left">
								Tipo Respuesta:
								<select name="ddAnswerType" id="ddAnswerType" tabindex="2">
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								Tipo Operaci&oacute;n:
								<select name="ddOpType" id="ddOpType" tabindex="3">
								</select>
							</div>
						</td>
						<td>
							<div align="left">
								Tipo Retenci&oacute;n:
								<select name="dd" id="dd" tabindex="4">
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								Tipo Cuenta:
								<select name="ddAccountType" id="ddAccountType" tabindex="5">
								</select>
							</div>
						</td>
						<td>
							<div align="left">
								Tipo Cobro:
								<select name="ddPaymentType" id="ddPaymentType" tabindex="6">
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								Tarjeta:
								<select name="ddCard" id="ddCard" tabindex="7">
								</select>
							</div>
						</td>
						<td>
							<div align="left"></div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">
								Fecha Inicial:
								<input name="txtfStartDate" type="text" id="txtfStartDate"
									tabindex="8" />
							</div>
						</td>
						<td>
							<div align="left">
								Fecha Final:
								<input name="txtfEndDate" type="text" id="txtfEndDate"
									tabindex="10" />
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
