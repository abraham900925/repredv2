/*
 * ControllerServlet.java
 *
 * Created on March 13, 2007, 4:45 PM
 */
/*###############################################################################
# Nombre del Programa :  ControllerServlet.java                                 #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad                   #
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Acceder al sistema                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES                                  #
# Autor               : Joaquin A. Mojica Quezada.                              #
# Compania            : WELLCOM S.A. DE C.V.                                    #
# Proyecto/Procliente : P-02-1245-09                 Fecha: 08/12/2009          #
# Modificacion        : Se agrega el Totales de Venta Generica por Telefónica   # 
#                       para Banco Santander                                    #
#-------------------------------------------------------------------------------#
# Autor               : Joaquin A. Mojica Quezada                               #
# Compania            : WELLCOM S.A. DE C.V.                                    #
# Proyecto/Procliente : P-02-0138-10                 Fecha: 10/05/2010          #
# Modificacion        : Cambios de liquidación Visa                             #
#-------------------------------------------------------------------------------#
# Autor               :JOAQUIN MOJICA                                           #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :P-02-0472-13                  Fecha:04/08/2014           #
# Modificaci?n        :Consolidacion de fiids para Banorte						#
#-----------------------------------------------------------------------------  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-------------------------------------------------------------------------------#
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.prosa.repred;

import com.prosa.sql.Database;
import com.prosa.sql.SessionConnection;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author afibarra
 * @version
 */
public class ControllerServlet extends HttpServlet {

	/**
	 * Fields
	 */
	private static final long serialVersionUID = 8965230147L;

	private Database db;

	private Database db_int;

	private SessionConnection sc;

	private SessionConnection sc_int;

	private Connection con;

	private Connection con_int;

	private String connectionType, driver, dbURL, userName, password,
			dataSourceName, connectionType_int, driver_int, dbURL_int,
			userName_int, password_int, dataSourceName_int;

	private HttpSession session;

	private String sessionId, oldSessionId;

	/** *********************************************************************** */

	/**
	 * List of Tables
	 */
	private String BANCOS;
	private String TBL_BANCO;
	private String TBL_TRANS_ADQ;
	private String TBL_AMOUNT_ADQ;
	private String TBL_TRANS_EMI;
	private String TBL_AMOUNT_EMI;
	private String TBL_TRANS_INST;
	private String TBL_COMP_DISPENSER;
	private String TBL_OP_TYPE;
	private String TBL_ACC_TYPE;

	/** *********************************************************************** */

	private void prepareDBObject() {

		try {
			this.db = new Database();
			this.db.setConnectionType(this.connectionType);
			this.db.setDriver(this.driver);
			this.db.setUrl(this.dbURL);
			this.db.setUserName(this.userName);
			this.db.setPassword(this.password);
			this.db.setDataSourceName(this.dataSourceName);
			this.db.doConnection();
			this.con = this.db.getConnection();
			this.session.setAttribute(this.sessionId + "db", this.db);
			this.sc = new SessionConnection(this.con);
			this.session.setAttribute(this.sessionId + "sc", this.sc);

			this.db_int = new Database();
			this.db_int.setConnectionType(this.connectionType_int);
			this.db_int.setDriver(this.driver_int);
			this.db_int.setUrl(this.dbURL_int);
			this.db_int.setUserName(this.userName_int);
			this.db_int.setPassword(this.password_int);
			this.db_int.setDataSourceName(this.dataSourceName_int);
			this.db_int.doConnection();
			this.con_int = this.db_int.getConnection();
			this.session.setAttribute(this.sessionId + "db_int", this.db_int);
			//this.session.setAttribute(this.sessionId + "db", this.db_int); armando
			this.sc_int = new SessionConnection(this.con_int);
			this.session.setAttribute(this.sessionId + "sc_int", this.sc_int);

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	private void verifySessionConnection(HttpServletRequest request) {

		// Se verifica si ya existe un Id de Sesion previo
		this.oldSessionId = (String) session
				.getAttribute(this.sessionId + "os");
		if (this.oldSessionId == null) {
			oldSessionId = "";
		}

		if (!oldSessionId.equalsIgnoreCase(this.sessionId)) {

			System.out.println("New connection: "
					+ request.getHeader("user-agent"));

			try {

				this.session
						.setAttribute(this.sessionId + "os", this.sessionId);
				this.prepareDBObject();

			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		} else {

			System.out.println("Getting Pool connection: "
					+ request.getHeader("user-agent"));

			this.sc = (SessionConnection) this.session
					.getAttribute(this.sessionId + "sc");

			if (this.sc != null) {
				this.con = this.sc.getConnection();
			}
			if (this.con == null) {
				try {
					System.out.println("Conexion = null");
					this.prepareDBObject();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
		}
	}

	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		/**
		 * Database Object Parameters
		 */
		this.connectionType = config.getInitParameter("jdbcConnectionType");
		this.driver = config.getInitParameter("jdbcDriver");
		this.dbURL = config.getInitParameter("jdbcUrl");
		this.userName = config.getInitParameter("jdbcUserName");
		this.password = config.getInitParameter("jdbcPassword");
		this.dataSourceName = config.getInitParameter("dataSourceName");
		
		this.connectionType_int = config.getInitParameter("jdbcConnectionType_int");
		this.driver_int = config.getInitParameter("jdbcDriver_int");
		this.dbURL_int = config.getInitParameter("jdbcUrl_int");
		this.userName_int = config.getInitParameter("jdbcUserName_int");
		this.password_int = config.getInitParameter("jdbcPassword_int");
		this.dataSourceName_int = config.getInitParameter("dataSourceName_int");
		/** ******************************************************************* */

		/**
		 * Tables
		 */
		this.BANCOS = config.getInitParameter("BANCOS");
		this.TBL_BANCO = config.getInitParameter("TBL_BANCO");
		this.TBL_TRANS_ADQ = config.getInitParameter("TBL_TRANS_ADQ");
		this.TBL_AMOUNT_ADQ = config.getInitParameter("TBL_AMOUNT_ADQ");
		this.TBL_TRANS_EMI = config.getInitParameter("TBL_TRANS_EMI");
		this.TBL_AMOUNT_EMI = config.getInitParameter("TBL_AMOUNT_EMI");
		this.TBL_TRANS_INST = config.getInitParameter("TBL_TRANS_INST");
		this.TBL_COMP_DISPENSER = config.getInitParameter("TBL_COMP_DISPENSER");
		this.TBL_OP_TYPE = config.getInitParameter("TBL_OP_TYPE");
		this.TBL_ACC_TYPE = config.getInitParameter("TBL_ACC_TYPE");
		/** ******************************************************************* */
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");

		/**
		 * Sesssion Management
		 */
		this.session = request.getSession(true);
		this.sessionId = session.getId();
		System.out.println("ControllerServlet session: " + this.sessionId);
		verifySessionConnection(request);
		/** ******************************************************************* */

		session.setAttribute("ROWS_PER_PAGE", new Integer(100));
		session.setAttribute("FILAS_POR_PAGINA", new Integer(100));

		/**
		 * Action Management
		 */
		String action = request.getParameter("action");
                String user = request.getParameter("txtfLogin");
                System.out.println("username-"+user);
                
		String url = "/login.jsp";

		if (action != null) 
                {
			Object urlBIRT;
			if (action.equals("index")) {
				url = "/index.jsp";
			} else if (action.equals("banks")) {
				url = "/banks.jsp";
			} else if (action.equals("banksMain")) {
				session.setAttribute("TBL_BANCO", TBL_BANCO);
				url = "/banksMain.jsp";
			} else if (action.equals("insertBank")) {
				session.setAttribute("TBL_BANCO", TBL_BANCO);
				url = "/banksInsert.jsp";
			} else if (action.equals("updateBank")) {
				session.setAttribute("TBL_BANCO", TBL_BANCO);
				url = "/banksUpdate.jsp";
			} else if (action.equals("login")) {
				// session.setAttribute("grantAccess", "true");
				// url = "/index.jsp";
				url = "/login.jsp";
			} else if (action.equals("numTransAdqFiltros")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/stat06Nt/numTransAdqFiltros.jsp";
			} 
                        else if (action.equals("numTransAdq")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/numTransAdq.jsp";
			}
                        else if (action.equals("numTransAdqMain")) {
				session.setAttribute("TBL_TRANS_ADQ", TBL_TRANS_ADQ);
				url = "/stat06Nt/numTransAdqMain.jsp";
			} else if (action.equals("amountTransAdq")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/amountTransAdq.jsp";
			} else if (action.equals("amountTransAdqMain")) {
				session.setAttribute("TBL_AMOUNT_ADQ", TBL_AMOUNT_ADQ);
				url = "/amountTransAdqMain.jsp";
			} else if (action.equals("numTransEmi")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/numTransEmi.jsp";
			} else if (action.equals("numTransEmiMain")) {
				session.setAttribute("TBL_TRANS_EMI", TBL_TRANS_EMI);
				url = "/numTransEmiMain.jsp";
/*----------------------------------------------------------------------------------
--- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010  --
----------------------------------------------------------------------------------*/
			} else if (action.equals("numTransEmiVsa")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/numTransEmiVsa.jsp";
			} else if (action.equals("numTransEmiVsaMain")) {
				session.setAttribute("TBL_TRANS_EMI", "TBL_SRR_REP_STAT07_NT_VISA");
				url = "/numTransEmiVsaMain.jsp";
/*----------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Finaliza la Modificacion   10/05/2010 --
----------------------------------------------------------------------------------*/
			} else if (action.equals("amountTransEmi")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/amountTransEmi.jsp";
			} else if (action.equals("amountTransEmiMain")) {
				session.setAttribute("TBL_AMOUNT_EMI", TBL_TRANS_EMI);
				url = "/amountTransEmiMain.jsp";
/*----------------------------------------------------------------------------------
--- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010  --
----------------------------------------------------------------------------------*/
			} else if (action.equals("amountTransEmiVsa")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/amountTransEmiVsa.jsp";
			} else if (action.equals("amountTransEmiVsaMain")) {
				session.setAttribute("TBL_AMOUNT_EMI", "TBL_SRR_REP_STAT07_IT_VISA");
				url = "/amountTransEmiVsaMain.jsp";
/*----------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Finaliza la Modificacion   10/05/2010 --
----------------------------------------------------------------------------------*/
			} else if (action.equals("transAprvInst")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/transAprvInst.jsp";
			} else if (action.equals("transAprvInstMain")) {
				session.setAttribute("TBL_TRANS_INST", TBL_TRANS_INST);
				url = "/transAprvInstMain.jsp";
			} else if (action.equals("compByDispenser")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/compByDispenser.jsp";
			} else if (action.equals("compByDispenserMain")) {
				session.setAttribute("TBL_COMP_DISPENSER", TBL_COMP_DISPENSER);
				url = "/compByDispenserMain.jsp";
			} else if (action.equals("acumCal")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/acumCal.jsp";
			} else if (action.equals("acumCalMain")) {
				session.setAttribute("TBL_TRANS_ADQ", TBL_TRANS_ADQ);
				session.setAttribute("TBL_AMOUNT_ADQ", TBL_AMOUNT_ADQ);
				session.setAttribute("TBL_TRANS_EMI", TBL_TRANS_EMI);
				session.setAttribute("TBL_AMOUNT_EMI", TBL_AMOUNT_EMI);
				session.setAttribute("TBL_TRANS_INST", TBL_TRANS_INST);
				session.setAttribute("TBL_COMP_DISPENSER", TBL_COMP_DISPENSER);
				url = "/acumCalMain.jsp";
			} else if (action.equals("typeOfOp")) {
				url = "/typeOfOp.jsp";
			} else if (action.equals("insertTypeOfOp")) {
				session.setAttribute("TBL_OP_TYPE", TBL_OP_TYPE);
				url = "/typeOfOpInsert.jsp";
			} else if (action.equals("typeOfOpMain")) {
				session.setAttribute("TBL_OP_TYPE", TBL_OP_TYPE);
				url = "/typeOfOpMain.jsp";
			} else if (action.equals("updateTypeOfOp")) {
				session.setAttribute("TBL_OP_TYPE", TBL_OP_TYPE);
				url = "/typeOfOpUpdate.jsp";
			} else if (action.equals("typeOfAccount")) {
				url = "/typeOfAccount.jsp";
			} else if (action.equals("insertTypeOfAcc")) {
				session.setAttribute("TBL_ACC_TYPE", TBL_ACC_TYPE);
				url = "/typeOfAccInsert.jsp";
			} else if (action.equals("typeOfAccMain")) {
				session.setAttribute("TBL_ACC_TYPE", TBL_ACC_TYPE);
				url = "/typeOfAccountMain.jsp";
			} else if (action.equals("updateTypeOfAcc")) {
				session.setAttribute("TBL_ACC_TYPE", TBL_ACC_TYPE);
				url = "/typeOfAccUpdate.jsp";
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-1245-09 Inicia la Modificacion   08/12/2009 -
            ---------------------------------------------------------------------------------*/
			} else if (action.equals("ElectSale")) {
				session.setAttribute("BANCOS", BANCOS);
				url = "/ElectSale.jsp";
			} else if (action.equals("ElectSaleMain")) {
				//session.setAttribute("TBL_TRANS_EMI", TBL_TRANS_EMI);
				url = "/ElectSaleMain.jsp";
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-1245-09 Finaliza la Modificacion   08/12/2009 -
            ---------------------------------------------------------------------------------*/
				/**************   	Reportes   *********************/			                      
			} else if (action.equals("showBIRTAmountTransAdq")) {
					/*                showBIRTAmountTransAdq
					 * Obtener los parametros que se "pusieron" en
					 * sesion en la pagina que manda a llamar al reporte,
					 * en éste caso "amountTransAdqMain.jsp"
					 */
					String att1 = (String)session.getAttribute("fechaIni_reporte");
					String att2 = (String)session.getAttribute("fechaFin_reporte");
					String att3 = (String)session.getAttribute("fiid_reporte");
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
					String[] ArrChaQuery = att3.split(",");
					//for (int i = 0; i < ArrChaQuery.length; i++) {
					//	System.out.println("fiid Adq stat06it"+ArrChaQuery[i]);
					//}
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
					/*
					 * "Armar" la url y ponerla en sesion
					 */
					String amb =(String)session.getAttribute("ambiente");
					if (amb.equals("Nacional")) 
                                        {
						/*---------------------------------------------------------------------------------
						-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
						---------------------------------------------------------------------------------*/
						if(ArrChaQuery.length==1)
							urlBIRT = "frameset?__report=/report/R_Tablestat06_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + "" + "&Banco2=" + "";
						else 
							if(ArrChaQuery.length==2)
								urlBIRT = "frameset?__report=/report/R_Tablestat06_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + "";
							else			 
								urlBIRT = "frameset?__report=/report/R_Tablestat06_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + ArrChaQuery[2];
						/*---------------------------------------------------------------------------------
						-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   04/08/2014 -
						---------------------------------------------------------------------------------*/			 
					}
					else
					{
					   urlBIRT = "frameset?__report=/report/R_Tablestat06_it_int.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
					}  
					session.setAttribute("urlBIRT", urlBIRT);
				url = "/showBIRT.jsp";			
			} else if (action.equals("showBIRTAmountTransEmi")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/
				String[] ArrChaQuery = att3.split(",");
					//for (int i = 0; i < ArrChaQuery.length; i++) {
					//	System.out.println("fiid stat07it"+ArrChaQuery[i]);
					//}
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/	
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
				   if(ArrChaQuery.length==1)
							urlBIRT = "frameset?__report=/report/R_Tablestat07_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + "" + "&Banco2=" + "";
						else 
							if(ArrChaQuery.length==2)
								urlBIRT = "frameset?__report=/report/R_Tablestat07_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + "";
							else			 
								urlBIRT = "frameset?__report=/report/R_Tablestat07_it.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + ArrChaQuery[2];
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
				}
				else
				{
				   urlBIRT = "frameset?__report=/report/R_Tablestat07_it_int.rptdesign" +
                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2  + "&Banco=" + att3;
				}
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";

            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010 -
            ---------------------------------------------------------------------------------*/

			} else if (action.equals("showBIRTAmountTransEmiVsa")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");

                urlBIRT = "frameset?__report=/report/R_Tablestat07_itVSA.rptdesign" +
				                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				session.setAttribute("urlBIRT", urlBIRT);
			    url = "/showBIRT.jsp";
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-0138-10 Finaliza la Modificacion   10/05/2010 -
            ---------------------------------------------------------------------------------*/

			} else if (action.equals("showBIRTNumTransAdq")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/
				String[] ArrChaQuery = att3.split(",");
					//for (int i = 0; i < ArrChaQuery.length; i++) {
					//	System.out.println("fiid Adq stat06nt"+ArrChaQuery[i]);
					//}
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/	
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
				   if(ArrChaQuery.length==1)
							urlBIRT = "frameset?__report=/report/R_Tablestat06_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + "" + "&Banco2=" + "";
						else 
							if(ArrChaQuery.length==2)
								urlBIRT = "frameset?__report=/report/R_Tablestat06_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + "";
							else			 
								urlBIRT = "frameset?__report=/report/R_Tablestat06_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + ArrChaQuery[2];
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
				}
				else
				{
					if (att3.equals("CO02")){
						urlBIRT = "frameset?__report=/report/R_Tablestat06CO02_nt_int.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
	               }
	               else
	               {
					    urlBIRT = "frameset?__report=/report/R_Tablestat06_nt_int.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				   }
				}   	
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";			
			} else if (action.equals("showBIRTNumTransEmi")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/
				String[] ArrChaQuery = att3.split(",");
					//for (int i = 0; i < ArrChaQuery.length; i++) {
					//	System.out.println("fiid Adq stat07nt"+ArrChaQuery[i]);
					//}
				/*---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
				---------------------------------------------------------------------------------*/	
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/
				   if(ArrChaQuery.length==1)
							urlBIRT = "frameset?__report=/report/R_Tablestat07_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + "" + "&Banco2=" + "";
						else 
							if(ArrChaQuery.length==2)
								urlBIRT = "frameset?__report=/report/R_Tablestat07_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + "";
							else			 
								urlBIRT = "frameset?__report=/report/R_Tablestat07_nt.rptdesign" +
					                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + ArrChaQuery[0]+ "&Banco1=" + ArrChaQuery[1]+ "&Banco2=" + ArrChaQuery[2];
					/*---------------------------------------------------------------------------------
					-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
					---------------------------------------------------------------------------------*/			 
				}
				else
				{
					if (att3.equals("CO02")){
						urlBIRT = "frameset?__report=/report/R_Tablestat07CO02_nt_int.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
	               }
	               else
	               {
				        urlBIRT = "frameset?__report=/report/R_Tablestat07_nt_int.rptdesign" +
                     "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				   }
					
				}

				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";


            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010 -
            ---------------------------------------------------------------------------------*/
			} else if (action.equals("showBIRTNumTransEmiVsa")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				String amb =(String)session.getAttribute("ambiente");
				   urlBIRT = "frameset?__report=/report/R_Tablestat07_ntVSA.rptdesign" +
				                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";			
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-0138-10 Finaliza la Modificacion   10/05/2010 -
            ---------------------------------------------------------------------------------*/

			} else if (action.equals("showBIRTTransAprvInst")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
				   urlBIRT = "frameset?__report=/report/R_Tablestat05.rptdesign" +
				                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				}
				else
				{
				   urlBIRT = "frameset?__report=/report/R_Tablestat05_int.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;	
				}
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";			
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-1245-09 Inicia la Modificacion   08/12/2009 -
            ---------------------------------------------------------------------------------*/
			} else if (action.equals("showBIRTElectSale")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
				   urlBIRT = "frameset?__report=/report/R_Tablestat_vg.rptdesign" +
				                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;
				}
				else
				{
				   urlBIRT = "frameset?__report=/report/R_Tablestat_vg.rptdesign" +
	                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3;	
				}
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";			
            /*---------------------------------------------------------------------------------
            -- Marca del Cambio : WELL-JMQ-P-02-1245-09 Finaliza la Modificacion   08/12/2009 -
            ---------------------------------------------------------------------------------*/			
			} else if (action.equals("showBIRTCompDis")) {
				String att1 = (String)session.getAttribute("fechaIni_reporte");
				String att2 = (String)session.getAttribute("fechaFin_reporte");
				String att3 = (String)session.getAttribute("fiid_reporte");
				String att4 = (String)session.getAttribute("Cajero");
				String amb =(String)session.getAttribute("ambiente");
				if (amb.equals("Nacional")) {
				   urlBIRT = "frameset?__report=/report/R_Tablesetl03.rptdesign" +
				                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3 + "&Cajero=" + att4;
				}
				else
				{
				   urlBIRT = "frameset?__report=/report/R_Tablesetl03_int.rptdesign" +
		                 "&Fecha_Menor=" + att1 + "&Fecha_Mayor=" + att2 + "&Banco=" + att3 + "&Cajero=" + att4;
					
				   
				}
				session.setAttribute("urlBIRT", urlBIRT);
			url = "/showBIRT.jsp";			
			}
		} else {
			url = "/login.jsp";
		}

		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
