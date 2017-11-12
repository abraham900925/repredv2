<%
/*###############################################################################
# Nombre del Programa :  numTransAdqHeader.jsp                                  #
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
#								MODIFICACIONES  #
#-----------------------------------------------------------------------------  #
# Autor               :JOAQUIN MOJICA                                           #
# Proyecto/Procliente :P-02-0163-09                  Fecha:01/06/2009           #
# Modificaci�n        :Alta Banco Cajeros de Conveniencia PROSA para Front End  #
#-----------------------------------------------------------------------------  #
# Autor               :JOAQUIN MOJICA                                           #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :P-02-0472-13                  Fecha:20/03/2014           #
# Modificaci�n        :Consolidacion de fiids para Banorte			#
#-----------------------------------------------------------------------------  #
# Autor               :Sergio Escalante Ramirez                                 #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :C-04-2006-14                Fecha:12/06/2014             #
# Modificaci�n        :Intercambio de im�genes PROSA    			#
#-----------------------------------------------------------------------------  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificaci�n        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.prosa.sql.Database"%>
<%@page import="com.prosa.io.HTML"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess" />
<%!
    /**
    * Database
    */
    Database db;
    String sessionID;
    String BANCOS;
    String query;
    ArrayList<String> fiidAL;
    Iterator it;
	String ambiente=null;
    /**
    * HTML
    */
    HTML html;
    String fiid;
    String role;
%>

<%
	if (!ga.AccessGranted(session, "grantAccess")) 
        {
		response.sendRedirect("login.jsp");
	}

	/**
	 * Database
	 */
	 ambiente=(String)session.getAttribute("ambiente");
	 
	 sessionID = request.getRequestedSessionId();
	 System.out.println("ambiente" + ambiente);
	 if(ambiente.equals("Nacional"))
         {

	    	 db = (Database)session.getAttribute(sessionID + "db");
   	 
         }
        else if(ambiente.equals("Internacional"))
         {

                db = (Database)session.getAttribute(sessionID + "db_int");
         }
	 
	 //db = (Database)session.getAttribute(sessionID + "db");
	 BANCOS = (String)session.getAttribute("BANCOS");
	 System.out.println("BANCOS:" + BANCOS);
	 /**************************************************************************/
	 
	 /**
	 * HTML
	 */
	 html = new HTML();
	
	 fiid = (String)session.getAttribute("fiid");
	 role = (String)session.getAttribute("role");
         System.out.println("role:" + role);
     //System.out.println("SUBSTRING fiid:" + fiid.substring(1,4));
	
	 query = "SELECT DISTINCT CODIGO_BANCO FROM " + BANCOS;
         
	 // 
/*
---------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0163-09 Inicia la Modificacion   01/06/2009 -
---------------------------------------------------------------------------------
*/
	 if((role.equals("banco") && fiid.equals("S044")) || (role.equals("banco") && fiid.equals("L060")) || (role.equals("banco") && fiid.equals("P044")))
         {
		 query=query+  " WHERE CODIGO_BANCO = '" + fiid + "'";
	 }
         else if(role.equals("banco"))
         {
			/*
			---------------------------------------------------------------------------------
			-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   30/07/2014 -
			---------------------------------------------------------------------------------
			*/	
			 if(fiid.equals("B072"))
                         {
				//System.out.println("Desplegar 3 fiids");
		
				 query="SELECT TABLA.CODIGO_BANCO_HIJO FROM ("
					  +" SELECT * FROM TBL_SRR_GRUPO_BANCO TSGB" 
					  +" UNION" 
					  +" SELECT TSRRB.CODIGO_BANCO, TSRRB.CODIGO_BANCO FROM TBL_SRR_BANCO TSRRB" 
					  +" )TABLA "
					  +" WHERE CODIGO_BANCO LIKE '%" + fiid.substring(1,4) + "%' AND codigo_banco not in ('S044', 'L060','P044')";
			 }
                         else
                         {
				/*
				---------------------------------------------------------------------------------
				-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   30/07/2014 -
				---------------------------------------------------------------------------------
				*/	
			 	query=query+  " WHERE CODIGO_BANCO LIKE '%" + fiid.substring(1,4) + "%' AND codigo_banco not in ('S044', 'L060','P044')";
			 }
       }
/*	 
---------------------------------------------------------------------------------
-- Marca del Cambio : WELL-JMQ-P-02-0163-09 Finaliza la Modificacion 01/06/2009 -
---------------------------------------------------------------------------------
*/
	 //
	 //System.out.println("Query:" + query);
	 db.setQuerySelect(query);
	 db.executeQuerySelect();
	 fiidAL = db.getRSColsData();
%>

<!DOCTYPE>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://code.highcharts.com/highcharts.js"></script>
        <script src="https://code.highcharts.com/highcharts-3d.js"></script>
        <script src="https://code.highcharts.com/modules/exporting.js"></script>
    </head>
  <body>
      <br/>
      <div class="well well-sm text-center">TRANSACCIONES ADQUIRIENTE STAT06_NT</div>
      <br/><br/>
      <div class="container-fluid">
          <center>
            <form>
              <div class="row">
                  <div class='col-md-5'>
                      <div class="form-group">
                          <label for="dtp_input1" class="col-md-3 control-label">Fecha Incical</label>
                          <div class="input-group date form_date col-md-6" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input1" data-link-format="yyyy-mm-dd">
                              <input class="form-control" size="16" type="text" value="" readonly  name="fechaInicial" id="fechaInicial">
                              <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                              <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                          </div>
                          <input type="hidden" id="dtp_input1" value="" /><br/>
                      </div>
                  </div>
                  <div class='col-md-5'>
                      <div class="form-group">
                          <label for="dtp_input2" class="col-md-3 control-label">Fecha Final</label>
                          <div class="input-group date form_date col-md-6" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                              <input class="form-control" size="16" type="text"  readonly  name="fechaFinal"  id="fechaFinal">
                              <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                              <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                          </div>
                          <input type="hidden" id="dtp_input2" value="" /><br/>
                      </div>
                  </div>
                  <div class='col-md-2'>
                      <div class="form-group">
                          <label for="dtp_input3" class="col-md-6 control-label">Adquirente:</label>   
                           <%
//                                    if(role.equals("banco") || role.equals("bancoint")) {
//                                        out.println(
//                                            "<input name=\"txtfFiid\" type=\"text\" id=\"txtfFiid\" readonly value=\"" + fiid + "\">"
//                                    	);
                                    if(role.equals("bancoint")) 
                                    {
                                        out.println(
                                            "<input name=\"txtfFiid\" type=\"text\" id=\"txtfFiid\" readonly value=\"" + fiid + "\">");
                                    } 
                                    else if(role.equals("banco")) 
                                    {
                                        /*
                                        ---------------------------------------------------------------------------------
                                        -- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   30/07/2014 -
                                        ---------------------------------------------------------------------------------
                                        */
                                    	if(fiid.equals("B072"))
                                        {
                                    		out.println(html.getListBox("cbFiid", fiidAL,"Selecione las fiids a consultar"));
                                    	}
                                        else
                                    	out.println(html.getComboBox("cbFiid", fiidAL));
                                    } 
                                    else if(role.equals("administrador")) 
                                     {
                                            if(fiid.equals("B072"))
                                            {
                                              out.println(html.getListBox("cbFiid", fiidAL,"Selecione las fiids a consultar"));
                                            }
                                          else
                                          out.println(html.createDdlFiids("cbFiid", fiidAL));
                                    }
                                    /*
                                    ---------------------------------------------------------------------------------
                                    -- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   30/07/2014 -
                                    ---------------------------------------------------------------------------------
                                    */
                                %>
                      </div>
                 </div>
               </div>
                <div class="row text-center">
                  <div class='col-md-12'>
                      <button type="submit" class="btn btn-success" id="btnConsultar">Consultar</button>
                  </div>
                </div>
               </form>
                <div  id="main" ></div><!--se despliegan reportes-->
        </center>
      </div>
                      
     <!--                 
    <form action="ControllerServlet?action=numTransAdqMain" method="post"
			enctype="application/x-www-form-urlencoded" name="frmNumTransAdq"
			 id="frmNumTransAdq">
		
		</form>   
     -->
            <script type="text/javascript">
                $('.form_date').datetimepicker(
                 {
                     language:  'es',
                     format: 'dd/mm/yyyy',
                     weekStart: 1,
                     todayBtn:  1,
                     autoclose: 1,
                     todayHighlight: 1,
                     startView: 2,
                     minView: 2,
                     forceParse: 0
                 });
                 
           /**
            * VI@SOLUTIONS MARCA DE CAMBIOS 
            * SE ENVIAN EL FORMULARIO POR FUNCION AJAX Y SE RENDERIZA EN EL DIV MAIN
            * AUTHOR: ABRAHAM VARGAS GARCIA 
            */      
          $(function () 
          {
              $('form').on('submit', function (e) 
              {
                  e.preventDefault();
                    $.ajax({
                      type: "POST",
                      url: "ControllerServlet?action=numTransAdqMain",
                      data: $('form').serialize(),
                      success: function (response) {
                        $('#main').html(response);
                        //$('#dialog').dialog('open');
                     },
                     failure: function (response) {
                        alert(response.responseText);
                     },
                     error: function (response) {
                        alert(response.responseText);
                      }
                   });
               });//Fin de funcion 
             });   
           </script>
	</body>
</html>
