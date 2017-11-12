/*###############################################################################
# Nombre del Programa :  PDFServlet.java                                        #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07              	   FECHA:28/10/2004         #
# Descripcion General :	 Generacion de Archivos PDF                             #
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Conexionde a la BD                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES                                  #
# Autor               :  JOAQUIN ANGEL MOJICA QUEZADA                           #
# Compania            :  Wellcom S.A. de C.V.                                   #
# Proyecto/Procliente :  F-04-1112-07                Fecha: 30/11/2007          #
# Modificacion        :  Correccion en la generacion de Reportes de la opci     #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.prosa.io.PDF;
import com.prosa.sql.Database;
import java.util.ArrayList;

/**
 *
 * <p>Titulo: PDFServlet </p>
 * <p>Descripcion: Servlet para la creacion de archivos .pdf </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compania: Wellcom </p>
 * @author Armando F. Ibarra
 */
public class PDFServlet
    extends HttpServlet implements Serializable {
    
    /*
     * Agregado el 07/Mar/2007
     */
    private ByteArrayOutputStream bOut;
    private String[] colsTitles;
    private String headerFiltro;
    private String pageFooter;
    private int filesPerPage;
    private ArrayList uidReport;
    private Database db;
    private String query;
  	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	     
    private String connPDF;
    /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/   
    /**************************************************************************/

    static final private String CONTENT_TYPE = "application/pdf";
    
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
    }

    /**
     * Atiende a las peticiones "GET" y "POST"
     * @param request de este parametro se obtienen los valores necesarios
     * para regresar un archivo de tipo .pdf
     * @param response -
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {
        
        /*
         * Agregado el 07/Mar/2007
         */
        PDF pdf;
        /******************************************************************/

        // Stream de salida
        //ByteArrayOutputStream bOut;

        HttpSession session = request.getSession(true);
        String sessionID = session.getId() + "PDF";
        String pdfID = request.getParameter("sessionIDPDF");
        String fileName = request.getParameter("fileName");
      	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	     
        String connPDF = request.getParameter("connPDF");
        /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/
        if(fileName == null) {
            fileName = "archivo";
        }

        if(sessionID.equals(pdfID)) {
            
            /*
             * Agregado el 07/Mar/2007
             */
            //pdf = (PDF)session.getAttribute(pdfID);
            pdf = new PDF();
            headerFiltro = (String)session.getAttribute(sessionID + "Header");
            colsTitles = (String[])session.getAttribute(sessionID + "ColsTitles");
            pageFooter = (String)session.getAttribute(sessionID + "PageFooter");
            filesPerPage = ((Integer) session.getAttribute("FILAS_POR_PAGINA")).intValue();
          	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	                
            if (connPDF==null)
               {
               sessionID = session.getId() + "db";
               }
            else
            {
            	sessionID = session.getId() + connPDF;	
            }
            /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/            
            uidReport = (ArrayList)session.getAttribute(sessionID + "UIDReport");
            db = (Database) session.getAttribute(sessionID);
            query = (String)session.getAttribute("QUERY_SELECT");
            doDocument(pdf);
            /**************************************************************/

            //bOut = (ByteArrayOutputStream)session.getAttribute(pdfID);
            StringBuffer attachment = new StringBuffer();
            attachment.append("attachment;filename=\"");
            attachment.append(fileName);
            attachment.append(".pdf\"");
            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-Disposition",
                attachment.toString());
            response.setHeader("Content-Description",
                "Archivo .pdf");
            ServletOutputStream out = response.getOutputStream();
            bOut.writeTo(out);
            out.flush();
            out.close();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {

        this.doGet(request, response);
    }
    
    private void doDocument(PDF pdf) {
        
        try {
            bOut = new ByteArrayOutputStream();
            pdf.setPdfFile(bOut);
            pdf.setHasPageHeader(true);
            pdf.setPageHeader(headerFiltro);
            pdf.setHasPageNumeration(true);
            pdf.setHasPageFooter(true);
            pdf.setPageFooter("PROSA." + pageFooter);
            pdf.setPageOrientation("L");
            pdf.openDocument();
            pdf.setTableNumberOfCols(colsTitles.length);

            //pdf.setTableColsTitles(colsTitles);
            pdf.setDBColsTitles(colsTitles);

            pdf.setTableColTitleSize(6);
            //pdf.setTableRowValues(acumDelDia);
            pdf.setIsGrayedTable(true);
            pdf.setTableColTextSize(6);

            pdf.setPagingSize(filesPerPage);
            //pdf.addPageTable();
            pdf.setQuery(query);
            pdf.setDB(db);
            pdf.addDBPageTable();
            if(this.uidReport != null) {
                pdf.newPage();
                pdf.setTableRowValues(this.uidReport);
                pdf.addPageTable();
            }

            pdf.closeDocument();
        } catch(Exception e) {
            System.err.println(e.toString());
        }
    }
}