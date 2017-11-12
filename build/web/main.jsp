<jsp:useBean id="ga" scope="session" class="com.prosa.net.GrantAccess"/>

<%
    if(!ga.AccessGranted(session, "grantAccess")){
        response.sendRedirect("login.jsp");
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
<!--
<style type="text/css">

body {
	background-color: #FFFFFF;
}
body,td,th {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
}

</style>
-->
</head>

<body>
<!--    
 <h4 align=center>BIENVENIDOS AL: </h4>
 <h3 align=center>SISTEMA DE REPORTES RED</h3>
 <br></br>
 <div align=center><img border="0" src="images/atm.jpg" /> </div>-->
</body>
</html>
