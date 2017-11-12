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
	Database db_int=null;
	String ambiente=null;
    String sessionID = "";
    ArrayList banksPag = null;
    Iterator it = null;
    int ROWS_PER_PAGE = 0;
    String pagination = "";
    int currentRow = 0;
    //String TBL_AMOUNT_ADQ = "";
    String[] TBL_REPORTS = {"TBL_TRANS_ADQ", "TBL_AMOUNT_ADQ", "TBL_TRANS_EMI", "TBL_AMOUNT_EMI", "TBL_TRANS_INST", "TBL_COMP_DISPENSER"};
    int tblRptsIdx;
    String query = "";
    int numResults = 0;
    String[] colsReport = {"Fecha de Creación"};
    String[] reportTitle = {"No. Tx's Adquirente Stat06 NT", "Importe de Tx's Adquirente Stat06 IT", "No. Tx's Emisor Stat07 NT", "Importe de Tx's Emisor Stat07 IT", "Tx's Aprobadas por Institución Stat05", "Compensación por Cajero Setl03"};
    String headerFilter = "";
    
    /**
    * HTML
    */
    HTML html = null;
    String btnRefresh = "";
    String ddAcum = "";
    String txtfStartDate = "";
    String txtfEndDate = "";
    String txtfFiid;
    String cbFiid;
    String role;


	/**
	* REPORTS
	*/
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
                "<br><table width=\"50%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tbl_border\">" 
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
                + "<br>Reporte:<br>"
                + "<u>" + reportTitle[tblRptsIdx] + "</u>"
            );
            html.setCSSTable("titTablaTransDaily");
            html.setCSSTRTable("datosTablaTransDaily");
            out.println(html.getTable(this.colsReport, this.banksPag));
        } catch(Exception e) {
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
    role = (String)session.getAttribute("role");
    ambiente=(String)session.getAttribute("ambiente");
    sessionID = request.getRequestedSessionId(); //+ "db";
    if(ambiente.equals("Nacional")){
    	db = (Database)session.getAttribute(sessionID+ "db");
	 }else if(ambiente.equals("Internacional")){
		 db_int = (Database)session.getAttribute(sessionID+ "db_int");
	 }
    //db = (Database)session.getAttribute(sessionID);
	TBL_REPORTS[0] = (String)session.getAttribute("TBL_TRANS_ADQ");
	TBL_REPORTS[1] = (String)session.getAttribute("TBL_AMOUNT_ADQ");
	TBL_REPORTS[2] = (String)session.getAttribute("TBL_TRANS_EMI");
	TBL_REPORTS[3] = (String)session.getAttribute("TBL_AMOUNT_EMI");
	TBL_REPORTS[4] = (String)session.getAttribute("TBL_TRANS_INST");
	TBL_REPORTS[5] = (String)session.getAttribute("TBL_COMP_DISPENSER");
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
<link href="resources/css/styles.css" rel="stylesheet" type="text/css"/>
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
            role = (String)session.getAttribute("role");
            txtfFiid = request.getParameter("txtfFiid");
            cbFiid = request.getParameter("cbFiid");
			tblRptsIdx = Integer.parseInt(request.getParameter("ddAcum"));
            txtfStartDate = request.getParameter("txtfStartDate");
			txtfEndDate = request.getParameter("txtfEndDate");
           currentRow = 1;
            query =
                "SELECT DISTINCT FECHA " +
                "FROM " + TBL_REPORTS[tblRptsIdx] +
                " WHERE FECHA >= TO_DATE(" + "'" + txtfStartDate + "','dd/mm/yyyy') " +
                "AND FECHA <= TO_DATE(" + "'" + txtfEndDate + "','dd/mm/yyyy') " +
                "AND CODIGO_BANCO_ADQ = '" + (role.equals("banco") ? txtfFiid : cbFiid) + "'";
            System.out.println("query: " + query);
    if(ambiente.equals("Nacional")){
    	db.setQuerySelect(query);
	 }else if(ambiente.equals("Internacional")){
		 db_int.setQuerySelect(query);
	 }

//            db.setQuerySelect(query);
            session.setAttribute("QUERY_SELECT", query);
            /**
            * Execute Query
            */
    if(ambiente.equals("Nacional")){
            db.executeQuerySelect();
            numResults = db.getNumRowsRS();
	 }else if(ambiente.equals("Internacional")){
           db_int.executeQuerySelect();
            numResults = db.getNumRowsRS();
	 }

//            db.executeQuerySelect();
            //numResults = db.getNumRowsRS();
           if(numResults < 0) {
                numResults = 0;
            }
            out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
            if(numResults > 0) {

                /**
                * PDF y XLS Reports
                * Common Data For Both Reports
                */
		if(ambiente.equals("Nacional")){
        		banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
	 	}else if(ambiente.equals("Internacional")){
        		banksPag = db_int.getNextResultSetData(ROWS_PER_PAGE);
	 	}      
                //banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
                session.setAttribute(sessionID + "banksPag", banksPag);
                headerFilter = "Consulta sin filtro.";

                /**
                * PDF
                */
                sessionIDPDF = session.getId() + "PDF";
                session.setAttribute(sessionIDPDF, pdf);
                session.setAttribute(sessionIDPDF + "Header", reportTitle[tblRptsIdx]);
                session.setAttribute(sessionIDPDF +  "ColsTitles", colsReport);
                session.setAttribute(sessionIDPDF + "PageFooter", "BANCOS");
                /**
                * Excel
                */
                sessionIDExcel = session.getId() + "Excel";
                session.setAttribute(sessionIDExcel, excel);
                session.setAttribute(sessionIDExcel + "Header", reportTitle[tblRptsIdx]);
                session.setAttribute(sessionIDExcel +  "ColsTitles", colsReport);
                session.setAttribute(sessionIDExcel +  "SheetName", "Bancos");

                /**
                * Pagination
                */
                out.println("Resultados " + currentRow
                    + " al " + (currentRow + ROWS_PER_PAGE - 1));
                out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=N\">Siguientes</a>");
                out.println("<br><br>");
                resultTable(out);
            }//IF numResults > 0
        /**
        * Forward Pagination
        */
        } else if(pagination.equals("N")) {
                out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
                		if(ambiente.equals("Nacional")){
        		banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
	 	}else if(ambiente.equals("Internacional")){
        		banksPag = db_int.getNextResultSetData(ROWS_PER_PAGE);
	 	}
                //banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
                it = banksPag.iterator();
                int pageSize = banksPag.size();
                if((currentRow + pageSize) < numResults)
                    currentRow += ROWS_PER_PAGE;
                out.println("Resultados " + currentRow
                    + " al " + (currentRow + pageSize - 1));
                out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=N\">Siguientes</a>");
                out.println("<br><br>");
                resultTable(out);
        /**
        * Reverse Pagination
        */
        } else if(pagination.equals("P")) {
            out.println("<b>Número de resultados: " + numResults + "<b><br><br>");
            		if(ambiente.equals("Nacional")){
        		banksPag = db.getNextResultSetData(ROWS_PER_PAGE);
	 	}else if(ambiente.equals("Internacional")){
        		banksPag = db_int.getNextResultSetData(ROWS_PER_PAGE);
	 	}
            //banksPag = db.getPrevResultSetData(ROWS_PER_PAGE);
            it = banksPag.iterator();
            int pageSize = banksPag.size();
            if((currentRow - pageSize) > 0)
                currentRow -= ROWS_PER_PAGE;
            out.println("Resultados " + currentRow
                + " al " + (currentRow + pageSize - 1));
            out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=P\">Previos</a>");
            out.println("&nbsp&nbsp<a href=\"acumCalMain.jsp?paginacion=N\">Siguientes</a>");
            out.println("<br><br>");
            resultTable(out);
        }
    %>
</body>
</html>
