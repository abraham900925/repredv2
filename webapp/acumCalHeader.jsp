<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.prosa.sql.Database"%>
<%@page import="com.prosa.io.HTML"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess"/>
<%!
    /**
    * Database
    */
    Database db;
	Database db_int;
    String sessionID;
    String BANCOS;
    String query;
    ArrayList fiidAL;
    Iterator it;
    String ambiente;

    /**
    * HTML
    */
    HTML html;
    String fiid;
    String role;
%>
<%
	if (!ga.AccessGranted(session, "grantAccess")) {
		response.sendRedirect("login.jsp");
	} 

ambiente=(String)session.getAttribute("ambiente");
System.out.println("Ambiente acumCalHeader:" + ambiente);
	/**
	 * Database
	 */
	 sessionID = request.getRequestedSessionId();

	 if(ambiente.equals("Nacional")){
		  db = (Database)session.getAttribute(sessionID + "db");
	 	 }else if(ambiente.equals("Internacional")){
	 	  db_int = (Database)session.getAttribute(sessionID + "db_int");		 
	 	 }
	// db = (Database)session.getAttribute(sessionID + "db");
	 BANCOS = (String)session.getAttribute("BANCOS");
	 System.out.println("BANCOS:" + BANCOS);
	 /**************************************************************************/
	 
	 /**
	 * HTML
	 */
	 html = new HTML();
	
	 fiid = (String)session.getAttribute("fiid");
	 role = (String)session.getAttribute("role");
		System.out.println("role:" + role);

	 query = "SELECT DISTINCT CODIGO_BANCO FROM " + BANCOS;
	 if(ambiente.equals("Nacional")){
		 db.setQuerySelect(query);
		 db.executeQuerySelect();
		 fiidAL = db.getRSColsData();
	 }else if(ambiente.equals("Internacional")){
		 db_int.setQuerySelect(query);
		 db_int.executeQuerySelect();
		 fiidAL = db_int.getRSColsData();
	 }
	// db.setQuerySelect(query);
	 //db.executeQuerySelect();
	 //fiidAL = db.getRSColsData();
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
			<strong>Calendario de Acumulados</strong>
		</div>
 -->
		<form action="ControllerServlet?action=acumCalMain" method="post"
			enctype="application/x-www-form-urlencoded" name="frmAcumCal"
			target="mainFrameAcumCal" id="frmAcumCal">
			<div align="center">
				<table width="100%" border="0" cellspacing="5" cellpadding="5" class="tbl_border">
                <tr>
                   <th class= "td_header" width="100%" colspan=4 >CALENDARIO DE ACUMULADOS</th>
                </tr>
				
                    <tr width="50%">						
						<td>
							<div align="left">
								Fecha Inicial:
								<input name="txtfStartDate" type="text" id="txtfStartDate"
									tabindex="2" />
                        		<a href="javascript:void(0)" onclick="if(self.gfPop)gfPop.fPopCalendar(document.frmAcumCal.txtfStartDate);return false;" HIDEFOCUS>
                            		<img name="popcalStart" align="absmiddle" src="images/calbtn.gif" width="34" height="22" border="0" alt="">
                        		</a>
							</div>
						</td>
						<td>
							<div align="left">
								Fecha Final:
								<input name="txtfEndDate" type="text" id="txtfEndDate"
									tabindex="3" />
                        		<a href="javascript:void(0)" onclick="if(self.gfPop)gfPop.fPopCalendar(document.frmAcumCal.txtfEndDate);return false;" HIDEFOCUS>
                            		<img name="popcalEnd" align="absmiddle" src="images/calbtn.gif" width="34" height="22" border="0" alt="">
                        		</a>
                        		<iframe width=174 height=189 name="gToday:normal:agenda.js" id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no" frameborder="0" style="visibility:visible;z-index:999;position:absolute;left:-500px;top:0px;"></iframe>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="left">
								Acumulado:
								<select name="ddAcum" id="ddAcum" tabindex="1">
									<option value="0">
										No. Tx's Adquirente Stat06 NT
									</option>
									<option value="1">
										Importe de Tx's Adquirente Stat06 IT
									</option>
									<option value="2">
										No. Tx's Emisor Stat07 NT
									</option>
									<option value="3">
										Importe de Tx's Emisor Stat07 IT
									</option>
									<option value="4">
										Tx's Aprobadas por Instituci&oacute;n Stat05
									</option>
									<option value="5">
										Compensaci&oacute;n por Cajero Setl03
									</option>
								</select>
							</div>
						</td>
                        <td><div align="left">
                        		Adquirente:
                                <%
                        	if(role.equals("banco")||role.equals("bancoInt")) {
                                   	out.println(
                                      	"<input name=\"txtfFiid\" type=\"text\" id=\"txtfFiid\" readonly value=\"" + fiid + "\">"
                                    	);
                                    } else if(role.equals("administrador") || role.equals("operador")) {
                                   	out.println(html.getComboBox("cbFiid", fiidAL));
                                   }
                                %>
                        </div></td>
                    </tr>
				</table>
			</div>
			<br />
			<div align="center">
				<table width="4%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="33%">
							<div align="center">
								<input name="btnRefresh" type="submit" id="btnRefresh"
									tabindex="4" value="Refrescar" />
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
