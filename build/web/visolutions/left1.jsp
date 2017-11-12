
<%
/*###############################################################################
# Nombre del Programa :  left.jsp                                               #
# Autor               :  Joaquin A. Mojica Quezada.                             #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:08/12/2009     #
# Descripcion General :	 Componente que contiene el menu de la izquierda        # 
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Acceder al sistema                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
#								MODIFICACIONES                                  #
# Autor               : Joaquin A. Mojica Quezada                               #
# Compania            : WELLCOM S.A. DE C.V.                                    #
# Proyecto/Procliente : P-02-1245-09                 Fecha: 08/12/2009          #
# Modificacion        : Se agrega el reporte de Totales de Venta Genérica por   #
#                       Telefónica para Banco Santander                         #
#-----------------------------------------------------------------------------  #
# Autor               : Joaquin A. Mojica Quezada                               #
# Compania            : WELLCOM S.A. DE C.V.                                    #
# Proyecto/Procliente : P-02-0138-10                 Fecha: 10/05/2010          #
# Modificacion        : Cambios de liquidación Visa                             #
#-----------------------------------------------------------------------------  #
# Autor               :Sergio Escalante Ramirez                                 #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :C-04-2006-14                Fecha:12/06/2014             #
# Modificacion        :Intercambio de imágenes PROSA    						#
#-----------------------------------------------------------------------------  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
%>
<!---------------------------------------------------------------------------------
--  Marca del Cambio : WELL-JMQ-P-02-1245-09 Inicia la Modificacion   08/12/2009 -
----------------------------------------------------------------------------------->
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.prosa.sql.Database"%>
<%@page import="com.prosa.io.HTML"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<!---------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-1245-09 Finaliza la Modificacion   08/12/2009 -
----------------------------------------------------------------------------------->
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess" />
<%!
/*---------------------------------------------------------------------------------
--  Marca del Cambio : WELL-JMQ-P-02-1245-09 Inicia la Modificacion   08/12/2009  -
----------------------------------------------------------------------------------*/
    /**
    * Database
    */
    Database db;
    String ambiente=null;
    String sessionID;
    String BANCOS;
    String query;
    ArrayList fiidAL;
    Iterator it;
/*----------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010   --
----------------------------------------------------------------------------------*/   
    int Valor_visa=0;
/*----------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Finaliza la Modificacion   10/05/2010 --
----------------------------------------------------------------------------------*/
    

    /**
    * HTML
    */
    HTML html;
    String fiid;
    String role;
/*---------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-1245-09 Finaliza la Modificacion   08/12/2009 -
----------------------------------------------------------------------------------*/    
%>
<%
    String role;
	if (!ga.AccessGranted(session, "grantAccess")) {
		response.sendRedirect("login.jsp");
	}
/*---------------------------------------------------------------------------------
--  Marca del Cambio : WELL-JMQ-P-02-1245-09 Inicia la Modificacion   08/12/2009  -
----------------------------------------------------------------------------------*/	
	/**
	 * Database
	 */
	role = (String)session.getAttribute("role");
	ambiente=(String)session.getAttribute("ambiente");
	System.out.println("ambiente: " + ambiente );
	sessionID = request.getRequestedSessionId();
	int valor=0;
	if(ambiente.equals("Nacional"))
        {
	 System.out.println("Se fue por nacional");
	 db = (Database)session.getAttribute(sessionID + "db"); 	
         role = (String)session.getAttribute("role");
         String txtfFiid=(String)session.getAttribute("fiid");
         String amb = (String)session.getAttribute("ambiente");
         System.out.println("role Menu:" + role);
         String query="Select * from tbl_srr_banco where codigo_banco='" + txtfFiid + "' and genera_tot_vg<>'N'";
	 /*
         db.setQuerySelect(query);
	 db.executeQuerySelect();
	 fiidAL = db.getRSColsData();
	 valor=db.getNumRowsRS();	 
	 System.out.println("FiidAL ="+fiidAL);
	 System.out.println("valor ="+valor);*/

/*--------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010 --
--------------------------------------------------------------------------------*/
      /*
     query="SELECT IND_STAT07_VISA_INC FROM BANCOS WHERE CODIGO_BANCO='" + txtfFiid + "'";
	 db.setQuerySelect(query);
	 db.executeQuerySelect();
	 fiidAL = db.getRSColsData();
	 if (fiidAL!=null && fiidAL.size()>0) {
	    Valor_visa=Integer.parseInt(((String[])fiidAL.get(0))[0]);
	 }
	 System.out.println("Valor_visa ="+Valor_visa);*/
/*--------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0138-10 Inicia la Modificacion   10/05/2010 --
--------------------------------------------------------------------------------*/
	 
/*---------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-1245-09 Finaliza la Modificacion   08/12/2009 -
----------------------------------------------------------------------------------*/    	 
	 }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"></link>
        <link href="bootstrap/css/simple-sidebar.css" rel="stylesheet"></link>
        <script src="bootstrap/js/jquery.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        
    </head>
    <body>
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="row" style="background-color: grey">
                <div class="col-sm-4">
                    <div class="navbar-header">
                        <button type="button" href="#menu-toggle" id="menu-toggle" class="btn btn-default navbar-btn">Menu</button>
                    </div>
                </div>
                <div class="col-sm-4 text-center" > 
                    <h2><span class="label label-danger">BIENVENIDOS AL:</h2>
                    <h4><span class="label label-danger">SISTEMA DE REPORTES RED</span></h4>
                </div>
                <div class="col-sm-4" >
                </div>
            </div>
        </div>
        <div class="container-fluid">
            <div id="wrapper">
                <div id="sidebar-wrapper">
                    <ul class="sidebar-nav">
                        <li class="sidebar-brand">
                            <a href="#">                        
                                <center><h4>SISTEMA DE REPORTES </h4></center>
                                <center><h4>RED</h4></center>
                            </a>
                        </li>
                        <li>
                            <a href="#">Reportes</a>     
                        </li>
                        <li>
                            <a href="ControllerServlet?action=numTransAdq" class="link" target="mainFrame">Stat06-NT</a>     
                        </li>
                        <li>
                            <a href="ControllerServlet?action=amountTransAdq" class="link" target="mainFrame">Stat06-IT</a> 
                        </li>
                        <li>
                            <a href="ControllerServlet?action=numTransEmi" class="link" target="mainFrame">Stat07-NT</a> 
                        </li>
                        <li>
                            <a href="ControllerServlet?action=amountTransEmi" class="link" target="mainFrame">Stat07-IT</a>               
                        </li>

                    </ul>
                </div><!--slider wrapper-->
            </div><!--fin wrapper-->
            
        </div><!-- /.container-fluid -->        
        
    </body>
</html>
