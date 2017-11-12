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
    String TBL_COMP_DISPENSER = "";
    String query = "";
    int numResults = 0;
    String[] colsReport = {
        "Fecha","Banco Adq", "Cajero", "Tipo Operación", "Tipo Cuenta", "THab. Propios", "Importe", "THab. Otros", "Importe", "THab. Intercambio", "Importe"};
    String headerFilter = "Compensación por Cajero Setl03";
    String ambiente=null;
    /**
    * HTML
    */
    HTML html = null;
    String btnOK = "";
    String txtfStartDate = "";
    String txtfEndDate = "";
    String txtfFiid;
    String cbFiid;
    String cbCajero;
    String txtfcajero;
    String role;
    String strconnbtnPDF;
    String strconnbtnExcel;

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
                "<br><table width=\"50%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                + "    <tr>"
                + "        <td height=\"43\">"
                + "		   <div align=\"center\">"
                + "				<form action=\"PDFServlet?sessionIDPDF=" + sessionIDPDF + "\" method=\"post\" name=\"frmPDF\" id=\"frmPDF\">"
                + "					<input name=\"btnPDF\" type=\"submit\" id=\"btnPDF\" value=\"Obtener PDF\">"
				+ strconnbtnPDF
                + "				</form>"
                + "		   </div>"
                + "		   </td>"
                + "        <td>"
                + "		   <div align=\"center\">"
                + "				<form action=\"ExcelServlet?sessionIDExcel=" + sessionIDExcel + "\" method=\"post\" name=\"frmExcel\" id=\"frmExcel\">"
                + "					<input name=\"btnExcel\" type=\"submit\" id=\"btnExcel\" value=\"Obtener XLS\">"
				+ strconnbtnExcel
                + "				</form>"
                + "			</div>"
                + "		    </td>"
                + "        <td>"
                + "		    <div align=\"center\">"
                + "				<form action=\"ControllerServlet?action=showBIRTCompDis\" method=\"post\" target=\"mainFrameAmountTransAdq\" name=\"frmBIRTTransAdq\" id=\"frmBIRTTransAdq\">"
                + "					<input name=\"btnBIRT\" type=\"submit\" id=\"btnBIRT\" value=\"Reporte\">"
                + "				</form>"
                + "		    </div>"
                + "		   </td>"                               
                + "    </tr>"
                + "</table>"
            );
            //html.setCSSTable("titTablaTransDaily");
            //html.setCSSTRTable("datosTablaTransDaily");
            html.setCSSTable("tbl_border");
            html.setCSSTRHeaderTable("td_header");
            html.setCSSTRDataTable("td_data");

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
    sessionID = request.getRequestedSessionId();// + "db";
    ambiente=(String)session.getAttribute("ambiente");
    	    if(ambiente.equals("Nacional")){

	    	 db = (Database)session.getAttribute(sessionID + "db");
			 strconnbtnPDF="<input name=\"connPDF\" type=\"hidden\" id=\"conn\" value=\"db\">";
			 strconnbtnExcel="<input name=\"connEXC\" type=\"hidden\" id=\"conn\" value=\"db\">";

		 }else if(ambiente.equals("Internacional")){

			 db = (Database)session.getAttribute(sessionID + "db_int");
			 strconnbtnPDF="<input name=\"connPDF\" type=\"hidden\" id=\"conn\" value=\"db_int\">";
			 strconnbtnExcel="<input name=\"connEXC\" type=\"hidden\" id=\"conn\" value=\"db_int\">";
		 }
   // db = (Database)session.getAttribute(sessionID);
    TBL_COMP_DISPENSER = (String)session.getAttribute("TBL_COMP_DISPENSER");
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
        btnOK = request.getParameter("btnOK");
        /**
        * Not a Query
        */
        if((btnOK == null) && (pagination == null)) {

            currentRow = 1;
            out.println(
                " <div align=\"center\" class=\"tituloPral\">"
                + "<b>Resultados.</b>"
                + "</div>"
            );
        /**
        * Query
        */
        } else if((btnOK != null) && (pagination == null)) {
        
            /**
            * HTML
            */
            html = new HTML();
            role = (String)session.getAttribute("role");
            txtfFiid = request.getParameter("txtfFiid");
            cbFiid = request.getParameter("cbFiid");
            cbCajero = request.getParameter("cbCajero");
            txtfcajero= request.getParameter("txtCajero");
            
            txtfStartDate = request.getParameter("txtfStartDate");
			txtfEndDate = request.getParameter("txtfEndDate");			
            //Pongo en session los objetos leidos
            //if (role.equals("banco")||role.equals("bancoint")){
            if (role.equals("bancoint")){
               session.setAttribute("fiid_reporte",txtfFiid);
			   System.out.println("fiid_reporte: " + txtfFiid);
			   session.setAttribute("Cajero",txtfcajero);
			   System.out.println("Cajero: " + cbCajero);
			}
			else
			{
               session.setAttribute("fiid_reporte",cbFiid);
			   System.out.println("fiid_reporte: " + cbFiid);
			   session.setAttribute("Cajero",cbCajero);
			   System.out.println("Cajero: " + cbCajero);
			}
            session.setAttribute("fechaIni_reporte",txtfStartDate);
            session.setAttribute("fechaFin_reporte",txtfEndDate);            
            session.setAttribute("Cajero",txtfcajero);            
            System.out.println("fechaIni_reporte: " + txtfStartDate);
         	System.out.println("fechaFin_reporte: " + txtfEndDate);
         	
            currentRow = 1;
			//if (role.equals("banco")||role.equals("bancoint")){
            if (role.equals("bancoint")){
			System.out.println("Es banco: " + role); 
            query =
                "SELECT " +
                "FECHA, CODIGO_BANCO_ADQ, CAJERO, CODIGO_TIPO_OPERACION, CODIGO_TIPO_CUENTA, CONTADOR_NUESTROS_THAB, IMPORTE_NUESTROS_THAB, CONTADOR_OTROS_THAB, IMPORTE_OTROS_THAB, CONTADOR_INTERCAMBIO_THAB, IMPORTE_INTERCAMBIO_THAB " +
                "FROM " + TBL_COMP_DISPENSER +
                " WHERE FECHA >= TO_DATE(" + "'" + txtfStartDate + "','dd/mm/yyyy') " +
                "AND FECHA <= TO_DATE(" + "'" + txtfEndDate + "','dd/mm/yyyy') " +
                "AND CODIGO_BANCO_ADQ = '" + (role.equals("banco") ? txtfFiid : cbFiid) + "'" +
                //"AND CAJERO = '" + (role.equals("banco") ? cbCajero:txtfcajero) + "'";
				"AND CAJERO = '" + (role.equals("bancoint") ? cbCajero:txtfcajero) + "'";
			}
			else
			{
            query =
                "SELECT " +
                "FECHA, CODIGO_BANCO_ADQ, CAJERO, CODIGO_TIPO_OPERACION, CODIGO_TIPO_CUENTA, CONTADOR_NUESTROS_THAB, IMPORTE_NUESTROS_THAB, CONTADOR_OTROS_THAB, IMPORTE_OTROS_THAB, CONTADOR_INTERCAMBIO_THAB, IMPORTE_INTERCAMBIO_THAB " +
                "FROM " + TBL_COMP_DISPENSER +
                " WHERE FECHA >= TO_DATE(" + "'" + txtfStartDate + "','dd/mm/yyyy') " +
                "AND FECHA <= TO_DATE(" + "'" + txtfEndDate + "','dd/mm/yyyy') " +
                //"AND CODIGO_BANCO_ADQ = '" + (role.equals("bancoint") ? txtfFiid : cbFiid) + "'" +
				"AND CODIGO_BANCO_ADQ = '" + cbFiid + "'" +
                "AND CAJERO = '" + txtfcajero  + "'";
			}
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
                out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=N\">Siguientes</a>");
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
                out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=P\">Previos</a>");
                out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=N\">Siguientes</a>");
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
            out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=P\">Previos</a>");
            out.println("&nbsp&nbsp<a href=\"compByDispenserMain.jsp?paginacion=N\">Siguientes</a>");
            out.println("<br><br>");
            resultTable(out);
        }
    %>

</body>
</html>
