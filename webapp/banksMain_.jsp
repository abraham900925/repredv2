<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=iso-8859-1" errorPage="errorPage.jsp"%>
<%@ page import="com.prosa.sql.Database" %>
<%@ page import="com.prosa.io.*" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess"/>

<%!
    /**
    * Database
    */
    Database db = null;
    String sessionID = "";
    ArrayList banksPag = null;
    Iterator it = null;
    int ROWS_PER_PAGE = 0;
    String pagination = "";
    int currentRow = 0;
    String TBL_BANCO = "";
    String query = "";
    String ambiente="";
    /**
    * HTML
    */
    HTML html = null;
    String btnRefresh = "";
    int numResults = 0;
    String[] colsReport = {
        "ID", "Código", "Nombre","Descripción","Surcharge","Accion"};
    String headerFilter = "";
    String sessionIDPDF = "";
    PDF pdf = null;
    ByteArrayOutputStream bPdf = null;
    String sessionIDExcel = "";
    Excel excel = null;
    ByteArrayOutputStream bExcel = null;
    
    /**
    * HTML Table Display
    */
    void resultTable(javax.servlet.jsp.JspWriter out) {
        try {
            out.println(
                "<br><table width=\"50%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                + "    <tr>"
                + "        <td height=\"43\">"
                + "		   <div align=\"center\">"
                + "				<form action=\"PDFServlet?sessionIDPDF=" + sessionIDPDF + "\" method=\"post\" name=\"frmPDF\" id=\"frmPDF\">"
                + "					<input name=\"btnPDF\" type=\"submit\" id=\"btnPDF\" value=\"Obtener PDF\">"
                + "				</form>"
                + "		   </div>"
                + "		   </td>"
                + "        <td>"
                + "		   <div align=\"center\">"
                + "				<form action=\"ExcelServlet?sessionIDExcel=" + sessionIDExcel + "\" method=\"post\" name=\"frmExcel\" id=\"frmExcel\">"
                + "					<input name=\"btnExcel\" type=\"submit\" id=\"btnExcel\" value=\"Obtener XLS\">"
                + "				</form>"
                + "			</div>"
                + "		    </td>"
                + "    </tr>"
                + "</table>"
            );
            html.setCSSTable("tableTitle");
            html.setCSSTRTable("tableData");
            out.println("<form action=\"ControllerServlet?action=updateBank\" method=\"post\" name=\"frmUpdateBank\" id=\"frmUpdateBank\" target=\"mainFrameBanks\">");
            out.println(html.getTableRadioBtn(colsReport, banksPag));
            out.println(
                "<br>" +
                "<input name=\"btnUpdateBank\" type=\"submit\" id=\"btnUpdateBank\" value=\"Modificar Registro\">" +
                "</form>"
            );
        } catch (Exception e) {
        }
    }
%>

<%
    /**
    * Access
    */
    if(!ga.AccessGranted(session, "grantAccess")){
        response.sendRedirect("login.jsp");
    }
    /**
    * Database
    */
    sessionID = request.getRequestedSessionId() + "db";
    if(ambiente.equals("Nacional")){

   	 db = (Database)session.getAttribute(sessionID + "db");
   	 
	 }else if(ambiente.equals("Internacional")){

		 db = (Database)session.getAttribute(sessionID + "db_int");
	 }

  //  db = (Database)session.getAttribute(sessionID);
    TBL_BANCO = (String)session.getAttribute("TBL_BANCO");
    /**
    * Pagination
    */
    pagination = request.getParameter("paginacion");
    ROWS_PER_PAGE =
        ((Integer)session.getAttribute("ROWS_PER_PAGE")).intValue();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="css/styles.css" rel="stylesheet" type="text/css"/>
<title>Untitled Document</title>
</head>
<body>
    <%
        /** 
        * Checks If It Is A Query And Not Link Menu
        */
        btnRefresh = request.getParameter("btnRefresh");
        /**
        * Not a Query
        */
        if((btnRefresh == null) && (pagination == null)) {

            currentRow = 1;
            out.println(
                " <div align=\"center\" class=\"tituloPral\">"
                + "<b>Resultados.</b>"
                + "</div>"
            );
        /**
        * Query
        */
        } else if((btnRefresh != null) && (pagination == null)) {
            
            /**
            * HTML
            */
            html = new HTML();
            
            currentRow = 1;
            query =
                "SELECT " +
                "ID_BANCO, CODIGO_BANCO, NOMBRE, DESCRIPCION, USO_SURCHARGE " +
                "FROM " + TBL_BANCO;
            System.out.println("query: " + query);

            db.setQuerySelect(query);
            session.setAttribute("QUERY_SELECT", query);
            /**
            * Execute Query
            */
            db.executeQuerySelect();
            numResults = db.getNumRowsRS();
            if(numResults < 0) {
                numResults = 0;
            }
            out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
            if(numResults > 0) {

                /**
                * PDF y XLS Reports
                * Common Data For Both Reports
                */
                banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
                session.setAttribute(sessionID + "banksPag", banksPag);
                headerFilter = "Consulta sin filtro.";

                /**
                * PDF
                */
                sessionIDPDF = session.getId() + "PDF";
                session.setAttribute(sessionIDPDF, pdf);
                session.setAttribute(sessionIDPDF + "Header", headerFilter);
                session.setAttribute(sessionIDPDF +  "ColsTitles", colsReport);
                session.setAttribute(sessionIDPDF + "PageFooter", "BANCOS");
                /**
                * Excel
                */
                sessionIDExcel = session.getId() + "Excel";
                session.setAttribute(sessionIDExcel, excel);
                session.setAttribute(sessionIDExcel + "Header", headerFilter);
                session.setAttribute(sessionIDExcel +  "ColsTitles", colsReport);
                session.setAttribute(sessionIDExcel +  "SheetName", "Bancos");

                /**
                * Pagination
                */
                out.println("Resultados " + currentRow
                    + " al " + (currentRow + ROWS_PER_PAGE - 1));
                out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=N\">Siguientes</a>");
                out.println("<br><br>");
                resultTable(out);
            }//IF numResults > 0
        /**
        * Forward Pagination
        */
        } else if(pagination.equals("N")) {
                out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
                banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
                it = banksPag.iterator();
                int pageSize = banksPag.size();
                if((currentRow + pageSize) < numResults)
                    currentRow += ROWS_PER_PAGE;
                out.println("Resultados " + currentRow
                    + " al " + (currentRow + pageSize - 1));
                out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=N\">Siguientes</a>");
                out.println("<br><br>");
                resultTable(out);
        /**
        * Reverse Pagination
        */
        } else if(pagination.equals("P")) {
            out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
            banksPag = db.getPrevResultSetData(ROWS_PER_PAGE);
            it = banksPag.iterator();
            int pageSize = banksPag.size();
            if((currentRow - pageSize) > 0)
                currentRow -= ROWS_PER_PAGE;
            out.println("Resultados " + currentRow
                + " al " + (currentRow + pageSize - 1));
            out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=P\">Previos</a>");
            out.println("&nbsp&nbsp<a href=\"banksMain.jsp?paginacion=N\">Siguientes</a>");
            out.println("<br><br>");
            resultTable(out);
        }
    %>
</body>
</html>
