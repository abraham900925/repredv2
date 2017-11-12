/*###############################################################################
# Nombre del Programa :  ExcelServlet.java                                      #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07             	   FECHA:28/10/2004     #
# Descripcion General :	 Reporte de Transacciones                               #
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Conexionde a la BD                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES  #
# Autor               :  JOAQUIN ANGEL MOJICA QUEZADA                           #
# Compania            :  Wellcom S.A. de C.V.                                   #
# Proyecto/Procliente :  F-04-1112-07                Fecha: 30/11/2007          #
# Modificacion        :  Correccion en la generacion de Reportes                #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
#################################################################################
#################################################################################
#								MODIFICACIONES  #
# Autor               :  ABRAHAM VARGAS                                         #
# Compania            :  VI@SOLUTIONS S.A. de C.V.                              #
# Proyecto/Procliente :  F-04-1112-07                Fecha: 30/11/2017          #
# Modificacion        :  Cambio de reporteador                                  #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
package com.prosa.io;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.prosa.io.Excel;
import com.prosa.sql.Database;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.prosa.service.ReporteService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.prosa.dao.Stat06ntImp;
import com.prosa.exceptions.WellException;
//import org.apache.tools.ant.taskdefs.Tstamp;

/**
 *
 * <p>T�tulo: ExcelServlet</p>
 * <p>Descripci�n: Servlet para la creaci�n de archivos .xls</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compa��a: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public class ExcelServlet extends HttpServlet implements Serializable {
    
    /*
     * Agregado el 07/Mar/2007
     */
    private ByteArrayOutputStream bOut;
    private String[] colsTitles;
    private String headerFiltro;
    private String sheetName;
    private int filesPerPage;
    private ArrayList uidReport;
    private Database db;
    private String query;
    private InputStream              fileIn;
    private Workbook                 wb;
    private Sheet                    sheet;
    private CellStyle                cellStyle;
    private Stat06ntImp              stat06Impl;
    private ReporteService           serviceReports; 
    
  	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	     
    private String connEXC;
    /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/       
    /**************************************************************************/

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    public void init(ServletConfig config) throws ServletException {

        super.init(config);
    }

    /**
     * Atiende a las peticiones "GET" y "POST"
     * @param request de �ste par�metro se obtienen los valores necesarios
     * para regresar un archivo de tipo .xls
     * @param response -
     * @throws ServletException
     */
    public void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException 
    {
        //stat06Impl = new Stat06ntImp();
        serviceReports= new ReporteService();
        try {
            fileIn=ExcelServlet.class.getClassLoader().getResourceAsStream("/stat06nt.xlt");
            
            
            /*
             * Agregado el 07/Mar/2007
             */
            //Excel excel;
            /******************************************************************/

            // Stream de salida
            //ByteArrayOutputStream bOut;

            HttpSession session = request.getSession(true);
            /*
            String sessionID = session.getId() + "Excel";
            String excelID = request.getParameter("sessionIDExcel");
            String fileName = request.getParameter("fileName");
          	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	     
            //String connEXC = request.getParameter("connEXC");
            /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/
            
           /* if(fileName == null) {
                fileName = "archivo";
            }*/

            if(true) 
            {
                
                /*
                 * Agregado el 07/Mar/2007
                 */
                //excel = (Excel)session.getAttribute(excelID);
                /*excel = new Excel();
                headerFiltro = (String)session.getAttribute(sessionID + "Header");
                colsTitles = (String[])session.getAttribute(sessionID + "ColsTitles");
                sheetName = (String)session.getAttribute(sessionID + "SheetName");
                filesPerPage = ((Integer) session.getAttribute("FILAS_POR_PAGINA")).intValue();
              	/*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Inicia la Modificacion   30/11/2007	*/	                
               /* if (connEXC==null)
                   {
                   sessionID = session.getId() + "db";
                   }
                else
                {
                	sessionID = session.getId() + connEXC;	
                }*/
                /*-- Marca del Cambio : WELL-JMQ-F-04-1112-07 Finaliza la Modificacion   30/11/2007	*/            
                //uidReport = (ArrayList)session.getAttribute(sessionID + "UIDReport");
                //db = (Database)session.getAttribute(sessionID);
                query = (String)session.getAttribute("QUERY_SELECT");
                
                //doDocument(excel); cambio vi@solutions
                /**************************************************************/

                //bOut = (ByteArrayOutputStream) session.getAttribute(excelID);
                StringBuffer attachment = new StringBuffer();
                attachment.append("attachment;filename=\"");
                attachment.append("stat06Nt");
                attachment.append(".xls\"");
                response.setContentType(CONTENT_TYPE);
                response.setHeader("Content-Disposition",attachment.toString());
                response.setHeader("Content-Description","Archivo Microsoft Excel");
                generaReporteStat06Nt().write(bOut);
                ServletOutputStream out = response.getOutputStream();
                bOut.writeTo(out);
                out.flush();
                out.close();
                
            }
        } 
        catch(Exception ex) 
        {
            System.err.println(ex.toString());
        }
    }

    public void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {

        this.doGet(request, response);
    }
    
    private void doDocument(Excel excel) {
        
        try {
            bOut = new ByteArrayOutputStream();
            excel.setExcelFile(bOut);
            excel.setSheetColsTitles("Filtro de consulta");
            excel.setSheetRowValues(headerFiltro);
            excel.newSheet(sheetName, 0);
            excel.addSheetData();
            excel.setDBSheetColsTitles(colsTitles);
            excel.setDBSheetColTitlesSize(6);
            //excel.setSheetRowValues(acumDelDia);
            excel.setDBSheetRowValuesSize(8);
            excel.setPagingSize(filesPerPage);
            excel.newSheet("Datos", 1);
            //excel.addSheetData();
            excel.setDB( db);
            excel.setQuery(query);
            excel.addDBSheetData();
            if(this.uidReport != null) {
                excel.setSheetRowValues(this.uidReport);
                excel.newSheet("Datos_2", 2);
                excel.addSheetData();
            }
            excel.closeFile();
        } catch(Exception e) {
            System.err.println(e.toString());
        }
    }
    
    /*Autor: Ing.Abraham Vargas modificacion: Se crea metodo que genera reporte stat06nt*/
    public Workbook generaReporteStat06Nt() throws IOException, WellException
    {
   
     return  this.serviceReports.createReportStat06Nt(query);
       
        
    }
    public Workbook generaReporteStat07Nt() throws IOException
    {
        
       return null;
    }
    public Workbook generaReporteStat08Nt() throws IOException
    {
        
       return null;
    }
    
    public Workbook generaReporteStat09Nt() throws IOException
    {
        
       return null;
    }
    
    public Workbook generaReporteStat10Nt() throws IOException
    {
        
       return null;
    }
    
    
    public Workbook generaReporteStat11Nt() throws IOException
    {
        
       return null;
    }
    /*Autor: Ing.Abraham Vargas fin modificacion: Se crea metodo que genera reporte stat06nt*/
    
}
