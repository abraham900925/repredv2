<%-- 
    Document   : pruebaAjax
    Created on : 15/10/2017, 01:37:43 AM
    Author     : abraham
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <center>
      <div class="container-fluid">
         <div class="row">
          <div class='col-md-6'>
              <div class="form-group">
                <label for="dtp_input1" class="col-md-3 control-label">Fecha Incical</label>
                <div class="input-group date form_date col-md-5" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input1" data-link-format="yyyy-mm-dd">
                    <input class="form-control" size="16" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
		   <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
	          <input type="hidden" id="dtp_input1" value="" /><br/>
              </div>
          </div>
          <div class='col-md-6'>
              <div class="form-group">
                <label for="dtp_input2" class="col-md-3 control-label">Fecha Final</label>
                <div class="input-group date form_date col-md-5" data-date="" data-date-format="dd MM yyyy" data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                    <input class="form-control" size="16" type="text" value="" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
		   <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
	          <input type="hidden" id="dtp_input2" value="" /><br/>
              </div>
          </div>
        </div>
      </div>
<script type="text/javascript">
   $('.form_date').datetimepicker({
        language:  'fr',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });
</script>
</center>
    </body>
</html>
