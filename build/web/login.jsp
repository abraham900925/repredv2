<%
/*################################################################################
#								MODIFICACIONES  #
#-----------------------------------------------------------------------------  #
# Autor               :Sergio Escalante Ramirez                                 #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :C-04-2006-14                Fecha:12/06/2014             #
# Modificacion        :Intercambio de imágenes PROSA    			#
#-----------------------------------------------------------------------------  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/
%>
<%@page contentType="text/html" errorPage="errorPage.jsp"%>
<%@page pageEncoding="UTF-8"%>

<%@page import="com.prosa.directory.*"%>
<jsp:useBean id="myJndi" scope="session" class="com.prosa.directory.Jndi" />

<%!
    /**
    * Login
    */
    String login;
    String password;
    /**
    * JNDI
    */
    String myRole;
    String myOu1st;
    String myOu2nd;
    String myOu3rd;
    String myLogin;
%>

<%
    /**
    * Invalidate Session
    */
    if(session.getAttribute("grantAccess") != null) 
    {
        session.removeAttribute("grantAccess");
    }
    
    if(request.getParameter("txtfLogin") != null && request.getParameter("txtfPwd") != null)
    {
        
        login = request.getParameter("txtfLogin");
        password = request.getParameter("txtfPwd");
    
        if(login.length() != 0 && password.length() != 0) 
        {

            System.out.println("Autentificando al usuario...");
            myJndi.doProperties("../../../../propiedades.xml");
            
            //if(myJndi.doLogin(login, password)) //vi@solutions quitar comentario prod
            if(true) 
            {
                   
               //if(myJndi.doLogin(login, password)) 
                session.setAttribute("jndi", myJndi);
                /**
                * User Roles
                */
                
                /* VI@SOLUTIONS
                myRole = myJndi.loadUidRoles(myJndi.appDn, myJndi.uid).toString();
                System.out.println("myRole: " + myRole);
                System.out.println("myRole en minusculas:" + myRole.toLowerCase());*/
                myRole="administrador";
                
                /*
                if(myRole.equals("[Administrador]")) 
                {
                    myRole = "administrador";
                }
                else if(myRole.equals("[Banco]")) 
                {
                    myRole = "banco";
                }
                else if(myRole.equals("[BancoInt]")) 
                {
                    myRole = "bancoint";
                    System.out.println("Asigno int: " + myRole);
                } 
                else 
                {
                    throw new Exception("Usuario sin Roles de acceso al Sistema.");
                }*/
                session.setAttribute("role", myRole);
                System.out.println("Role Obtenido:" + myRole);
                /**
                * Afilation
                */
                /* VI@SOLUTIONS
                myOu1st = myJndi.dn.substring((myJndi.dn).indexOf("ou=") + 3);
                System.out.println("myOu.indexOf(\",\"): " + myOu1st.indexOf(","));
                myOu1st = myOu1st.indexOf(",") >= 0 ? (myOu1st.substring(0, myOu1st.indexOf(","))).trim(): myOu1st;
                System.out.println("myOu1st: " + myOu1st);
                session.setAttribute("name", myOu1st);*/
                /**
                * FIID
                */
                /*
                System.out.println("myJndi.dn.toLowerCase()).indexOf(\"ou=bancos\"): " + (myJndi.dn.toLowerCase()).indexOf("ou=bancos"));
                if((myJndi.dn.toLowerCase()).indexOf("ou=bancos") >= 0) 
                {
                    myOu2nd = myJndi.dn.substring(0,(myJndi.dn.toLowerCase()).indexOf("ou=bancos") - 1);
                    System.out.println("myOu2nd: " + myOu2nd);
                    myOu2nd = myOu2nd.lastIndexOf("ou=") >= 0 ? myOu2nd.substring(myOu2nd.lastIndexOf("ou=") + 3).trim(): "";
                }*/
                /*
                else 
                {
                    
                    ES DEL ELSE  * PROSA does not have FIID
                    myOu2nd = "";
                }*/
                    /**
                    * PROSA does not have FIID
                    */
                
                        
                myOu2nd = "";
                System.out.println("myOu2nd: " + myOu2nd);
                session.setAttribute("fiid", myOu2nd);
                /**
                * Participant
                */
               /*  VI@SOLUTIONS
               myLogin = myJndi.dn.substring((myJndi.dn).indexOf("uid=") + 4);
               myLogin = myLogin.indexOf(",") >= 0 ? (myLogin.substring(0, myLogin.indexOf(","))).trim(): myLogin;
               session.setAttribute("login", myLogin);
                */

                session.setAttribute("grantAccess", "true");
                response.sendRedirect("ControllerServlet?action=index");
            }
        }
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Acceso - REPRED</title>
        <link href="css/styles.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			function quitFrame() {
				if(self.parent.frames.length != 0) {
					self.parent.location=document.location.href;
				}
			}
			quitFrame();
		</script>
        <link href="bootstrap/css/simple-sidebar.css" rel="stylesheet" />
        <link href="bootstrap/css/style.css"    />
        <link href="bootstrap/css/form-elements.css" rel="stylesheet" />
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" />
        <script src="bootstrap/js/jquery-1.11.1.min.js"/></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/jquery.backstretch.min.js"></script>
        <script src="bootstrap/js/scripts.js"></script>
    </head>
    <body>
        <script type="text/javascript">
                $(window).load(function(){
                    $('#modal-login').modal()('show');
                });
        </script>
        <!--
        <div align="center">
            <span class="style3">PROSA - REPRED</span>
            <form action="ControllerServlet?action=login" method="post" enctype="application/x-www-form-urlencoded" name="frmLogin" id="frmLogin">
                <table width="374" border="1">
                    <tr>
                        <td width="364">
                            <table width="100%" border="0" cellpadding="5" cellspacing="0" summary="Acceso a la aplicación">-->
<!---- Marca del Cambio : WELL-JMQ-C-04-2006-14  Inicia la Modificacion   12/06/2014style="opacity:0.0;filter:alpha(opacity=0)" --->							
                              <!--  <tr>
                                    <td height="63" colspan="2"><div align="left"><img src="resources/pics/nvologoprosa.jpg" alt="PROSA" width="168" longdesc="http://www.prosa.com.mx"></div></td>
                                </tr>								
                                <tr>
                                    <td  width="14%" height="31" class="gradient2"><div align="left" class="style4">&nbsp;&nbsp;&nbsp;Usuario:</div></td>
                                    <td width="86%" class="gradient">				  
                                        <div align="center">
                                            <input name="txtfLogin" type="text" id="txtfLogin" size="20" maxlength="16">				
                                    </div></td>
                                </tr>
                                <tr>
                                    <td height="34" class="gradient2"><div align="left" class="style4">&nbsp;&nbsp;&nbsp;Contraseña:</div></td>
                                    <td class="gradient">				  
                                        <div align="center">
                                            <input name="txtfPwd" type="password" id="txtfPwd" size="20" maxlength="16">				
                                    </div></td>
                                </tr>
                                <tr>
                                    <td class="gradient2" height="45"><div align="center">
                                            <input name="btnAccept" type="submit" id="btnAccept" value="Aceptar">
                                    </div></td>
                                    <td class="gradient"><div align="center">
                                            <input name="btnErase" type="reset" id="btnErase" value="Borrar">
                                    </div></td>
                                </tr>-->
<!---- Marca del Cambio : WELL-JMQ-C-04-2006-14  Termina la Modificacion   12/06/2014 --->
<!--
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </div>-->
        
        <!--Marca de cambio VISOLUTIOS comienza modificacion-->
           <div  class="container">
             <div class="modal fade" id="modal-login"  tabindex="-1" role="dialog" aria-labelledby="modal-login-label" aria-hidden="true" >
        	<div class="modal-dialog">
        		<div class="modal-content">
        			
        			<div class="modal-header">
        				<button type="button" class="close" data-dismiss="modal">
        					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
        				</button>
        				<img src="images/prosa.jpg "  class="img-responsive col-md-offset-3 col-lg-offset-3 col-sm-offset-3" alt="Cinque Terre" width="304" height="236"> 
        				<center><p>Introduce tu usuario y tu contraseña</p></center>                                        
        			</div>
        			
        			<div class="modal-body">
        				
                                  <form role="form"  method="post" action="ControllerServlet?action=login" id="frmLogin" name="frmLogin" class="login-form">
	                    	    <div class="form-group">
	                    		<label class="sr-only" for="form-username">Username</label>
	                        	<input type="text"   placeholder="Username..." class="form-username form-control" id="txtfLogin" name="txtfLogin"   ></input>
	                            </div>
	                            <div class="form-group">
	                        	<label class="sr-only" for="form-password">Password</label>
	                        	<input type="password" placeholder="Password..." class="form-password form-control"  id="txtfPwd" name="txtfPwd" ></input>
	                            </div>
	                            <button type="submit" class="btn">Sign in!</button>
                                  </form>
        			</div>
        		</div>
        	</div>
             </div>     
        </div><!--Marca de cambio VISOLUTIOS termina modificacion-->
<script language="JavaScript">
            //function daFoco() {
              // frmLogin.txtfLogin.focus;
                 
              //}
              //daFoco(); 
</script>
    </body>
</html>
