<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>PROSA :: REPRED</title>
<%
	System.out.println("AMB= "+request.getParameter("AMB"));    
	if(request.getParameter("AMB")!=null)
        {
    	String amb=request.getParameter("AMB");
		System.out.println("AMB= "+amb);
		session.setAttribute("ambiente",amb); 
	}
%>
    

   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menu</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"></link>
        <link href="bootstrap/css/simple-sidebar.css" rel="stylesheet"></link>
        <script src="bootstrap/js/jquery.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <link href="bootstrap/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet"/>
        <link href="bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>
        <link href="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.9/themes/blitzer/jquery-ui.css"rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.9/jquery-ui.js" type="text/javascript"></script>
        <script src="bootstrap/js/bootstrap-datetimepicker.js"></script>
        <script src="bootstrap/js/bootstrap-datetimepicker.fr.js"></script>
        
        


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
                    <ul class="sidebar-nav" id="userMenu">
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
                            <a href="javascript:;" class="link details" id="numTransAdq" >Stat06-NT</a>     
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
                         <li>
                             <a  href="javascript:;" class="link details" id="btn11"  >-prueba</a>               
                        </li>
                         <li>
                            <a  href="javascript:;" class="link details" id="btn2" >-prueba2</a>               
                        </li>

                    </ul>
                </div><!--slider wrapper-->
            </div><!--fin wrapper-->
            <div  id="dialog" ></div><!--se despliegan reportes-->
        </div><!-- /.container-fluid -->        
      
        <!--style="display: none"-->
        <script>
        $("#menu-toggle").click(function(e) 
        {
            e.preventDefault();
            $("#wrapper").toggleClass("toggled");
        });
        
         $(function () 
         {
             /*
            $("#dialog").dialog({
                autoOpen: false,
                modal: true,
                title: "View Details"
                
            });//fin de funcions*/
        
            $("#userMenu").children('li').click(function () 
            {              
                    var id = $(this).children('a').attr('id');
                    var urlP;
                 
                 //para reporte 
                 if(id.valueOf()=="numTransAdq")
                 {
                     urlP="numTransAdqFiltros";
                     
                 }   
                  if(id.valueOf()=="btn11")
                 {
                     urlP="pruebaAjax.jsp";
                 } 
                  if(id.valueOf()=="btn11")
                 {
                     urlP="numTransAdqHeader.jsp";
                 } 
                 
                 if(urlP!=null)
                 {
                    $.ajax({
                      type: "POST",
                      url: "ControllerServlet?action="+urlP,
                      contentType: "application/json; charset=utf-8",
                      dataType: "html",
                      success: function (response) {
                        $('#dialog').html(response);
                        //$('#dialog').dialog('open');
                     },
                     failure: function (response) {
                        alert(response.responseText);
                     },
                     error: function (response) {
                        alert(response.responseText);
                      }
                   });
                 }
            });//Fin de funcion 
        });   
       </script>
       <script src="bootstrap/dataTableJs/jquery.dataTables.min.js"></script>
       <script src="bootstrap/dataTableJs/dataTables.bootstrap.min.js"></script>
    </body>
<!--  
<frameset rows="80,*" cols="*" frameborder="no" border="0" framespacing="0">
	<frame src="top.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" />
	<frameset rows="*" cols="155,*" framespacing="0" frameborder="no" border="0">
  		<frame src="left.jsp" name="leftFrame" scrolling="no" noresize="noresize" id="leftFrame" title="leftFrame" />
  		<frame src="main.jsp" name="mainFrame" id="mainFrame" title="mainFrame" />
	</frameset>
</frameset>
---------------------------------------------------------------
-->





<!--
<frameset rows="99" frameborder="no" border="0" framespacing="0">
   <frameset cols="*" frameborder="no" border="0" framespacing="0">
    <frameset rows="*" framespacing="0" frameborder="no" border="0">
     <frameset cols="145,*" framespacing="0" frameborder="no" border="0" >
         <frame src="visolutions/left1.jsp" name="leftFrame" scrolling=No noresize="noresize" id="leftFrame" title="leftFrame"></frame>
         <frame src="visolutions/main1.jsp" name="mainFrame" id="mainFrame" title="PROSA - Infraestructura"></frame>
     </frameset>
    </frameset>
   </frameset>
</frameset>
<noframes>
    <body>
    
</body>
</noframes>-->
</html>
<!-- SE VA A GENERAR UN MENU DINAMICO DONDE UN DIV QUEDA ESTATICO Y OTRO DINAMICO
     QUE ES DONDE SE IRAN INSERTANDO LOS REPORTES A VISUALIZAR
>


