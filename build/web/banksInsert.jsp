<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

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
    Database db = null;
    String sessionID = "";
    ArrayList banksPag = null;
    Iterator it = null;
    int idx = -1;
    int i = 0;
    String[] tmpAS = null;
    int ROWS_PER_PAGE = 0;
    String TBL_BANCO = "";
    String query = "";
    String ambiente=null;
    /**
    * HTML
    */
    HTML html = null;
    String btnInsertBank = null;
    String txtfCode = null;
    String txtfName = null;
    String txtfDesc = null;
    String txtfExt = null;
%>

<%
    if(!ga.AccessGranted(session, "grantAccess")){
        response.sendRedirect("login.jsp");
    }
    /**
    * Database
    */
    sessionID = request.getRequestedSessionId(); //+ "db";
    ambiente=(String)session.getAttribute("ambiente");
    	    if(ambiente.equals("Nacional")){

	    	 db = (Database)session.getAttribute(sessionID + "db");
	    	 
		 }else if(ambiente.equals("Internacional")){

			 db = (Database)session.getAttribute(sessionID + "db_int");
		 }
    
    //db = (Database)session.getAttribute(sessionID);
    TBL_BANCO = (String)session.getAttribute("TBL_BANCO");
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
        	btnInsertBank = request.getParameter("btnInsertBank");
       
            if(btnInsertBank == null) {
                out.println(
                    "<form action=\"ControllerServlet?action=insertBank\" method=\"post\" enctype=\"application/x-www-form-urlencoded\" name=\"frmInsertBank\" target=\"mainFrame\" id=\"frmInsertBank\">" +
                    "        <div align=\"center\">" +
                    "            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tbl_border\">" +
                    "                <tr>" +
                    "				 <th class= \"td_header\" width=\"100%\" colspan=4 >INSERTAR BANCO</th>" +
                    "                </tr>" +
                    "                    <td><div align=\"center\">C&oacute;digo:" +
                    "                        <input name=\"txtfCode\" type=\"text\" id=\"txtfCode\" tabindex=\"1\" size=\"15\" maxlength=\"50\" />" +
                    "                    </div></td>" +
                    "                    <td><div align=\"center\">Nombre:" +
                    "                        <input name=\"txtfName\" type=\"text\" id=\"txtfName\" tabindex=\"2\" size=\"15\" maxlength=\"50\" />" +
                    "                    </div></td>" +
                    "                    <td><div align=\"center\">Descripci&oacute;n:" +
                    "                        <input name=\"txtfDesc\" type=\"text\" id=\"txtfDesc\" tabindex=\"3\" size=\"15\" maxlength=\"255\" />" +
                    "                    </div></td>" +
                    "                    <td><div align=\"center\">Extensi&oacute;n:" +
                    "                        <input name=\"txtfExt\" type=\"text\" id=\"txtfExt\" tabindex=\"4\" size=\"15\" maxlength=\"3\"/>" +
                    "                    </div></td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </div>" +
                    "        <br/>" +
                    "        <div align=\"center\">" +
                    "            <table width=\"4%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                    "                <tr>" +
                    "                    <td width=\"33%\"><div align=\"center\">" +
                    "                        <input name=\"btnInsertBank\" type=\"submit\" id=\"btnInsertBank\" tabindex=\"5\" value=\"Insertar\" />" +
                    "                    </div></td>" +
                    "                    <td width=\"34%\"><div align=\"center\">" +
                    "                        <input name=\"btnClear\" type=\"reset\" id=\"btnClear\" tabindex=\"6\" value=\"Borrar\" />" +
                    "                    </div></td>" +
                    "                </tr>" +
                    "            </table>" +
                    "        </div>" +
                    "</form>"
                );
            } else if(btnInsertBank != null) {
            	
            	txtfCode = request.getParameter("txtfCode");
                txtfName = request.getParameter("txtfName");
                txtfDesc = request.getParameter("txtfDesc");
                txtfExt = request.getParameter("txtfExt");
            	
            	html = new HTML();
            	html.addFieldToValidate("C&oacute;digo", txtfCode);
            	html.addFieldToValidate("Nombre", txtfName);
            	html.addFieldToValidate("Descripci&oacute;n", txtfDesc);
            	html.addFieldToValidate("Extensi&oacute;n", txtfExt);
               
                if(html.validateFields().size() != 0) {
                	out.println("Los campos: " + html.validateFields() + " son obligatorios.");
                	out.println(
                		"<br><br><a href=\"#\" onclick=\"history.go(-1)\">Regresar</a>"
                	);
                } else {

	                query =
	                	"INSERT INTO " + TBL_BANCO +
	                	" ( CODIGO_BANCO, NOMBRE, DESCRIPCION, USO_SURCHARGE) " +
	                	"VALUES ( '" + txtfCode + " ', '" + txtfName + "', '" + txtfDesc + "', '" + txtfExt + "')";
	                db.setQueryInsert(query);
	                db.executeQueryInsert();
	                out.println(
	                	"<b>Registro Insertado.</b>" +
	                	"<br><br><a href=\"#\" onclick=\"history.go(-1)\">Agregar otro</a>"
	                );
                }
            }
        %>
    </body>
</html>
