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
			<strong>Bancos</strong>
		</div>
 -->
		
		<form action="ControllerServlet?action=banksMain" method="post"
			enctype="application/x-www-form-urlencoded" name="frmBank"
			target="mainFrameBanks" id="frmBank">
			<!--<div align="center">
                <table width="50%" border="0" cellspacing="0" cellpadding="0" >
                    <tr>
                        <td><div align="center">Nombre:
                            <input name="txtfName" type="text" id="txtfName" tabindex="1" size="15" />
                        </div></td>
                        <td><div align="center">Descripci&oacute;n:
                            <input name="txtfDesc" type="text" id="txtfDesc" tabindex="2" size="15" />
                        </div></td>
                        <td><div align="center">Extensi&oacute;n:
                            <input name="txtfExt" type="text" id="txtfExt" tabindex="3" size="15" />
                        </div></td>
                    </tr>
                </table>
            </div>
            <br/>-->
			<div align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tbl_border" >
                <tr>
                   <th class= "td_header" width="100%" colspan=4 >BANCOS</th>
                </tr>
					<tr>
						<td width="33%">
							<div align="center">
								<input name="btnRefresh" type="submit" id="btnRefresh"
									tabindex="1" value="Obtener Datos" />
							</div>
						</td>
						<!--<td width="33%"><div align="center">
                            <input name="bntModify" type="submit" id="bntModify" tabindex="5" value="Modificar" />
                        </div></td>
                        <td width="34%"><div align="center">
                            <input name="btnClear" type="reset" id="btnClear" tabindex="6" value="Borrar" />
                        </div></td>-->
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
