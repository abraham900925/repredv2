<%
/*###############################################################################
# Nombre del Programa :  ElectSaleHeader.jsp                                    #
# Autor               :  Joaquin A. Mojica Quezada.                             #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:08/12/2009     #
# Descripcion General :	 Se agrega el Totales de Venta Generica por Telefonica  # 
#                       para Banco Santander                                    #
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Acceder al sistema                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES                                  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.prosa.sql.Database"%>
<%@page import="com.prosa.io.HTML"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess" />
<%!
    /**
    * Database
    */
    Database db;
    String sessionID;
    String BANCOS;
    String query;
    ArrayList fiidAL;
    Iterator it;
	String ambiente=null;	
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

	/**
	 * Database
	 */
	 sessionID = request.getRequestedSessionId();
	 
		ambiente=(String)session.getAttribute("ambiente");	    
		if(ambiente.equals("Nacional")){

	    	 db = (Database)session.getAttribute(sessionID + "db");
	    	 
		 }else if(ambiente.equals("Internacional")){

			 db = (Database)session.getAttribute(sessionID + "db_int");
		 }
	 
	 
	 //db = (Database)session.getAttribute(sessionID + "db");
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
	 //


	 if((role.equals("banco") && fiid.equals("S044")) || (role.equals("banco") && fiid.equals("L060")) || (role.equals("banco") && fiid.equals("P044"))){
		 query=query+  " WHERE CODIGO_BANCO = '" + fiid + "'";
	 }else if(role.equals("banco")){
		 query=query+  " WHERE CODIGO_BANCO LIKE '%" + fiid.substring(1,4) + "%' AND codigo_banco not in ('S044', 'L060','P044')";
	 }

	 //
	 //System.out.println("Query:" + query);

	 db.setQuerySelect(query);
	 db.executeQuerySelect();
	 fiidAL = db.getRSColsData();
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
			<strong>No. Transacciones Emisor STAT07_NT</strong>
		</div>
-->
		<form action="ControllerServlet?action=ElectSaleMain" method="post"
			enctype="application/x-www-form-urlencoded" name="frmNumTransEmi"
			target="mainFrameNumTransEmi" id="frmNumTransEmi">
			<div align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tbl_border" >
                <tr>
                   <th class= "td_header" width="100%" colspan=4 >Totales de Venta Generica por Telefonica por Banco </th>
                </tr>
					<tr>
						<td>
							<div align="center">
								Fecha Inicial:
								<input name="txtfStartDate" type="text" id="txtfStartDate"
									tabindex="1" size="15" />
                        		<a href="javascript:void(0)" onclick="if(self.gfPop)gfPop.fPopCalendar(document.frmNumTransEmi.txtfStartDate);return false;" HIDEFOCUS>
                            		<img name="popcalStart" align="absmiddle" src="images/calbtn.gif" width="34" height="22" border="0" alt="">
                        		</a>
							</div>
						</td>
						<td>
							<div align="center">
								Fecha Final:
								<input name="txtfEndDate" type="text" id="txtfEndDate"
									tabindex="2" size="15" />
                        		<a href="javascript:void(0)" onclick="if(self.gfPop)gfPop.fPopCalendar(document.frmNumTransEmi.txtfEndDate);return false;" HIDEFOCUS>
                            		<img name="popcalEnd" align="absmiddle" src="images/calbtn.gif" width="34" height="22" border="0" alt="">
                        		</a>
                        		<iframe width=174 height=189 name="gToday:normal:agenda.js" id="gToday:normal:agenda.js" src="ipopeng.htm" scrolling="no" frameborder="0" style="visibility:visible;z-index:999;position:absolute;left:-500px;top:0px;"></iframe>
							</div>
						</td>
                        <td><div align="right">Adquirente:</div></td>
                        <td><div align="left">
                                &nbsp;
                                <%
/*
                               	if(role.equals("banco")||role.equals("bancoint")) {
                                   	out.println(
                                       	"<input name=\"txtfFiid\" type=\"text\" id=\"txtfFiid\" readonly value=\"" + fiid + "\">"
                                   	);
                                  } else if(role.equals("administrador")) {
                                   	out.println(html.getComboBox("cbFiid", fiidAL));
                                   }
*/
                                    if(role.equals("bancoint")) {
                                        out.println(
                                            "<input name=\"txtfFiid\" type=\"text\" id=\"txtfFiid\" readonly value=\"" + fiid + "\">");
                                    } else if(role.equals("banco")) {
                                    	out.println(html.getComboBox("cbFiid", fiidAL));
                                    } else if(role.equals("administrador")) {
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
									value="Limpiar" />
							</div>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
