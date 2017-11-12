<%
/*###############################################################################
# Nombre del Programa :  numTransAdqMain.jsp                                    #
# Autor               :  JOAQUIN MOJICA                                         #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                       FECHA:30/10/2008     #
# Descripcion General :	 Pantalla que muestra No. Transacciones Adquiriente     #
#                        STAT06_NT                                              #
# Programa Dependiente:  numTransAdq.jsp                                        #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :                                                         #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES                                  #
#-----------------------------------------------------------------------------  #
# Autor               : JOAQUIN MOJICA                                          #
# Proyecto/Procliente : P-09-0124-10                  Fecha:01/12/2010          #
# Modificacion        : Adquirente Compartamos con cajero Reciclador            #
#-----------------------------------------------------------------------------  #
# Autor               :JOAQUIN MOJICA                                           #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :P-02-0472-13                  Fecha:30/07/2014           #
# Modificacion        :Consolidacion de fiids para Banorte						#
#-----------------------------------------------------------------------------  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page contentType="text/html; charset=iso-8859-1" errorPage="errorPage.jsp"%>
<%@ page import="com.prosa.sql.Database" %>
<%@ page import="com.prosa.io.*" %>
<%@ page import="com.prosa.dao.Stat06ntImp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.sql.*"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess"/>

<%!
    /**
    * Database
    */
    Database db = null;
    ResultSet rs;
    String sessionID = "";
    Stat06ntImp    repTable= new Stat06ntImp();
    ArrayList banksPag = null;
    Iterator it = null;
    int ROWS_PER_PAGE = 0;
    String pagination = "";
    int currentRow = 0;
    String TBL_TRANS_ADQ = "";
    String query = "";
    int numResults = 0;
    //--------------------------------------------------------------------------------
    // Marca del Cambio : WELL-JMQ-P-09-0124-10 Inicia la Modificacion   01/12/2010  -
    //--------------------------------------------------------------------------------
    String[] colsReport = {
            "Fecha", "Banco", "Retiro MN", "Retiro Dlls", "Ven Gen", "Pago Elec", "Consultas", "Camb Nip", "Rechazos","Retenidas", "Transfer","Deposito", "UltMovs","Surcharge" };
    //---------------------------------------------------------------------------------
    // Marca del Cambio : WELL-JMQ-P-09-0124-10 Finaliza la Modificacion   01/12/2010 -
    //---------------------------------------------------------------------------------
    String headerFilter = "No. Tx's Adquirente Stat06 NT";
    
    String ambiente=null;
    String bancoCO02;

    /**
    * HTML
    */
    HTML html = null;
    String btnOK = "";
    String txtfStartDate = "";
    String txtfEndDate = "";
    String fechaInicial = "";
    String fechaFinal = "";
    String ddlFiids = "";
    String txtfFiid;
    String cbFiid;
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
    void resultTable(javax.servlet.jsp.JspWriter out) 
    {
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
                + "				<form action=\"ControllerServlet?action=showBIRTNumTransAdq\" method=\"post\" target=\"mainFrameAmountTransAdq\" name=\"frmBIRTTransAdq\" id=\"frmBIRTTransAdq\">"
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
        } 
        catch(Exception e) 
        {
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
    
    sessionID = request.getRequestedSessionId(); //+ "db";
    
		ambiente=(String)session.getAttribute("ambiente");	    
		if(ambiente.equals("Nacional"))
                {

	    	 db = (Database)session.getAttribute(sessionID + "db");
			 strconnbtnPDF="<input name=\"connPDF\" type=\"hidden\" id=\"conn\" value=\"db\">";
			 strconnbtnExcel="<input name=\"connEXC\" type=\"hidden\" id=\"conn\" value=\"db\">";
		 }
                else if(ambiente.equals("Internacional"))
                {
			 db = (Database)session.getAttribute(sessionID + "db_int");
			 strconnbtnPDF="<input name=\"connPDF\" type=\"hidden\" id=\"conn\" value=\"db_int\">";
			 strconnbtnExcel="<input name=\"connEXC\" type=\"hidden\" id=\"conn\" value=\"db_int\">";
		 }
    //db = (Database)session.getAttribute(sessionID);
    TBL_TRANS_ADQ = (String)session.getAttribute("TBL_TRANS_ADQ");
 
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
    
        btnOK = request.getParameter("btnOK");
        /**
        * Not a Query
        */
        
        
        /**
         * VI@SOLUTIONS MARCA DE CAMBIO 
         * AUTHOR: ING. ABRAHAM VARGAS GARCIA
         * DESCRIPTION: DELETE VALIDATION ALWAYS IT DOES A QUERY IF IT NOT FOUND DATA THEN DISPLAY MESSAGE (NO SE ENCONTRARON REG)
         * 
         */
        if(true)
        {
        
            /**
            * HTML
            */
            html = new HTML();
            role = (String)session.getAttribute("role");
            txtfFiid = request.getParameter("txtfFiid");
             /**
              * VI@SOLUTIONS SE CAMBIAN EL NOMBRE DE LAS VARIABLES
              * AUTHOR: ING. ABRAHAM VARGAS GARCIA 
              */
            // txtfStartDate = request.getParameter("txtfStartDate");
            //txtfEndDate = request.getParameter("txtfEndDate");
            fechaInicial = request.getParameter("fechaInicial");
            fechaFinal = request.getParameter("fechaFinal");
            ddlFiids    = request.getParameter("selectBost");
			/*
			---------------------------------------------------------------------------------
			-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   30/07/2014 -
			---------------------------------------------------------------------------------
			*/	
			//cbFiid = request.getParameter("cbFiid");
           /* String []bancosEmi = request.getParameterValues("cbFiid");
            String bancosEmiStr="";
            String bancosEmiStraux="";
			
            for(int i=0;i<bancosEmi.length;i++) 
            {
                if(!bancosEmiStr.equals(""))
                   bancosEmiStr+="','";
            	   bancosEmiStr+=bancosEmi[i];
            	//System.out.println("*****fiids reporte*******");
            	//System.out.print(bancosEmiStr+", ");
				if(!bancosEmiStraux.equals(""))
					bancosEmiStraux+=",";
				bancosEmiStraux+=bancosEmi[i];
				//System.out.println("*****fiids reporte O3*******");
            	//System.out.print(bancosEmiStraux+", ");
            }*/
            //Pongo en session los objetos leidos
            if (role.equals("banco")||role.equals("administrador"))
            {
               //session.setAttribute("fiid_reporte",(role.equals("banco") ? txtfFiid : cbFiid));
			   //System.out.println("fiid_reporte: " + (role.equals("banco") ? txtfFiid : cbFiid));
			   session.setAttribute("fiid_reporte", ddlFiids);
			   //System.out.println("fiid_reporte: " + bancosEmiStraux);
	    }
	   else
	    {
              bancoCO02=(String)(role.equals("bancoint") ? txtfFiid : ddlFiids);
              if (bancoCO02.equals("CO02"))
              {
                //System.out.println("Banco especial");          
                colsReport[0] ="Fecha";
                colsReport[1] ="Banco";
                colsReport[2] ="Retiro MN";
                colsReport[3] ="Linea de Credito";
                colsReport[4] ="Ping";
   	        colsReport[5] ="Cons. Fact.";
	        colsReport[6] ="Consultas";
                colsReport[7] ="Asigna";
                colsReport[8] ="Rechazos";
                colsReport[9] ="Avances";
                colsReport[10] ="Transfer";
                colsReport[11] ="Mini Ext."; 
                colsReport[12] ="Pago Fact.";
              };    
                session.setAttribute("fiid_reporte",(role.equals("bancoint") ? txtfFiid : ddlFiids));
                System.out.println("fiid_reporte: " + (role.equals("bancoint") ? txtfFiid : ddlFiids));
	     }
            /*
			---------------------------------------------------------------------------------
			-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   30/07/2014 -
			---------------------------------------------------------------------------------
			*/	
               session.setAttribute("fechaIni_reporte",fechaInicial);
               session.setAttribute("fechaFin_reporte",fechaFinal);            
               System.out.println("fechaIni_reporte: " + fechaInicial);
               System.out.println("fechaFin_reporte: " + fechaFinal);
               currentRow = 1;
            if (role.equals("bancoint"))
            {
            query =
                "SELECT " +
                "FECHA , CODIGO_BANCO_EMI,RETIRO_MN, RETIRO_DLLS, VTA_GENERICA, PAGO_ELEC, CONSULTAS, CAMBIO_NIP, " +
                "RECHAZOS, RETENIDAS, TRANSFER, DEPOSITO, P_SOBRE, TRANFEE " +
                "FROM " + TBL_TRANS_ADQ +
                " WHERE FECHA >= TO_DATE(" + "'" + fechaInicial + "','dd/mm/yyyy') " +
                "AND FECHA <= TO_DATE(" + "'" + fechaFinal + "','dd/mm/yyyy') " + 
                /*
                ---------------------------------------------------------------------------------
                -- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   20/03/2014 -
                ---------------------------------------------------------------------------------
                */	 
                "AND CODIGO_BANCO_ADQ IN('" + (role.equals("bancoint") ? txtfFiid : ddlFiids) + "')";
                /*
                ---------------------------------------------------------------------------------
                -- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   20/03/2014 -
                ---------------------------------------------------------------------------------
                */	 
	    }
	   else
            {
    //--------------------------------------------------------------------------------
    // Marca del Cambio : WELL-JMQ-P-09-0124-10 Inicia la Modificacion   01/12/2010  -
    //--------------------------------------------------------------------------------		
            query =
                "SELECT " +
                "FECHA , CODIGO_BANCO_EMI,RETIRO_MN, RETIRO_DLLS, VTA_GENERICA, PAGO_ELEC, CONSULTAS, CAMBIO_NIP, " +
                "RECHAZOS, RETENIDAS, TRANSFER, DEPOSITO, ULTMOVS, TRANFEE " +
                "FROM " + TBL_TRANS_ADQ +
                " WHERE FECHA between STR_TO_DATE(" + "'" + fechaInicial + "','%d/%m/%Y') " +
                "AND STR_TO_DATE(" + "'" + fechaFinal + "','%d/%m/%Y') "+ 
                
                  //oracle    
                 //" WHERE FECHA >= TO_DATE(" + "'" + fechaInicial + "','dd/mm/yyyy') " +
                //"AND FECHA <= TO_DATE(" + "'" + fechaFinal + "','dd/mm/yyyy') " 
                //WHERE FECHA =STR_TO_DATE('10/23/2017', '%m/%d/%Y');
                    
                /*
				---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   20/03/2014 -
				---------------------------------------------------------------------------------
				*/	 
				 "AND CODIGO_BANCO_ADQ IN('" +  ddlFiids + "')";
				/*
				---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   20/03/2014 -
				---------------------------------------------------------------------------------
				*/	 
    //--------------------------------------------------------------------------------
    // Marca del Cambio : WELL-JMQ-P-09-0124-10 Inicia la Modificacion   01/12/2010  -
    //--------------------------------------------------------------------------------                
	   }
            System.out.println("query: " + query);

            db.setQuerySelect(query);
            session.setAttribute("QUERY_SELECT", query);
            /**
            * Execute Query
            */
             db.executeQuerySelect();
             rs=db.getResultSet();
            
           out.println(repTable.buildTableReport(rs));
            
        }
    %>
    
    <div id="chartGraphic" style="height: 400px"></div>
    
    
<script>
    $(document).ready(function() 
    {
      $('#example').DataTable();
    } );
    //Script Chart
     Highcharts.chart('chartGraphic', 
     {
    chart: {
        type: 'pie',
        options3d: 
        {
            enabled: true,
            alpha: 45
        }
    },
    title: {
        text: 'Contents of Highsoft\'s weekly fruit delivery'
    },
    subtitle: {
        text: '3D donut in Highcharts'
    },
    plotOptions: {
        pie: {
            innerSize: 100,
            depth: 45
        }
    },
    series: [{
        name: 'Delivered amount',
        data: [
            ['Bananas', 8],
            ['Kiwi', 3],
            ['Mixed nuts', 1],
            ['Oranges', 6],
            ['Apples', 8],
            ['Pears', 4],
            ['Clementines', 4],
            ['Reddish (bag)', 1],
            ['Grapes (bunch)', 1]
        ]
    }]
});
</script>
    <br/><br/>
    <div class="row">
    <div class="text-right col-md-6">
        <div class="classWithPad">Widget 1</div>
    </div>
    <div class="text-left col-md-6">
        <div class="classWithPad">Widget 2</div>
    </div>
</div>
</body>
</html>
