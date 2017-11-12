<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.prosa.sql.Database"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess"/>

<%!
    /**
    * Database
    */
    Database db = null;
    String sessionID = "";
    ArrayList banksPag = null;
    Iterator it = null;
    int idx = -1;
    int i = 0;
    String[] rowValues = null;
    String[] tmpAS = null;
    int ROWS_PER_PAGE = 0;
    String TBL_ACC_TYPE = "";
    String query = "";
    String ambiente=null;
    /**
    * HTML
    */
    String btnUpdateOpType = null;
    String bntModify = null;
    String rbtnUpdateOpType = null;
    String txtfName = null;
    String txtfDesc = null;
    String opTypeID = null;
%>

<%
    if(!ga.AccessGranted(session, "grantAccess")){
        response.sendRedirect("login.jsp");
    }
    /**
    * Database
    */
    sessionID = request.getRequestedSessionId();// + "db";
    
		ambiente=(String)session.getAttribute("ambiente");	    
		if(ambiente.equals("Nacional")){

	    	 db = (Database)session.getAttribute(sessionID + "db");
	    	 
		 }else if(ambiente.equals("Internacional")){

			 db = (Database)session.getAttribute(sessionID + "db_int");
		 }
    
   // db = (Database)session.getAttribute(sessionID);
    TBL_ACC_TYPE = (String)session.getAttribute("TBL_ACC_TYPE");
    banksPag = (ArrayList)session.getAttribute(sessionID + "banksPag");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/styles.css" rel="stylesheet" type="text/css">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            rbtnUpdateOpType = request.getParameter("rbtn");
            bntModify = request.getParameter("bntModify");
            
            if(rbtnUpdateOpType != null) {
                idx = Integer.parseInt(rbtnUpdateOpType);
                it = banksPag.iterator();
                while(it.hasNext()) {
                    tmpAS = (String[])it.next();
                    if(i == idx) {
                        rowValues = tmpAS;
                    }
                    i++;
                }
                out.println(
                    "<form action=\"ControllerServlet?action=updateTypeOfAcc\" method=\"post\" enctype=\"application/x-www-form-urlencoded\" name=\"frmUpdateTypeOfAcc\" target=\"mainFrameTypeOfAccount\" id=\"frmUpdateTypeOfAcc\">" +
                    "        <div align=\"center\">" +
                    "            <table width=\"50%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                    "                <tr>" +
                    "                    <td><div align=\"center\">Nombre:" +
                    "                        <input name=\"txtfName\" type=\"text\" value=\"" + rowValues[2] + "\" id=\"txtfName\" tabindex=\"1\" size=\"15\" maxlength=\"50\" />" +
                    "                    </div></td>" +
                    "                    <td><div align=\"center\">Descripci&oacute;n:" +
                    "                        <input name=\"txtfDesc\" type=\"text\" value=\"" + rowValues[3] + "\" id=\"txtfDesc\" tabindex=\"2\" size=\"15\" maxlength=\"255\" />" +
                    "                    </div></td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </div>" +
                    "        <br/>" +
                    "        <div align=\"center\">" +
                    "            <table width=\"4%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                    "                <tr>" +
                    "                    <td width=\"33%\"><div align=\"center\">" +
                    "                        <input name=\"bntModify\" type=\"submit\" id=\"bntModify\" tabindex=\"5\" value=\"Modificar\" />" +
                    "                    </div></td>" +
                    "                    <td width=\"34%\"><div align=\"center\">" +
                    "                        <input name=\"btnClear\" type=\"reset\" id=\"btnClear\" tabindex=\"6\" value=\"Borrar\" />" +
                    "                    </div></td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </div>" +
                    "<input name=\"opTypeID\" type=\"hidden\" id=\"opTypeID\" value=\"" + rowValues[0] + "\"/>" +
                    "</form>"
                );
            } else if(bntModify != null) {
                
                txtfName = request.getParameter("txtfName");
                txtfDesc = request.getParameter("txtfDesc");
                opTypeID = request.getParameter("opTypeID");
                
                query =
                    "UPDATE " + TBL_ACC_TYPE +
                    " SET NOMBRE = '" + txtfName + "', " +
                    "DESCRIPCION = '" + txtfDesc + "' " +
                    "WHERE ID_TIPO_CUENTA = '" + opTypeID + "'";
                db.setQueryUpdate(query);
                db.executeQueryUpdate();
                out.println("<b>Registro Actualizado.</b>");
            } else {
                out.println("<br>No Seleccionó Ningún Registro.</br>");
            }
        %>
    </body>
</html>
