/*###############################################################################
# Nombre del Programa :  HTML.java                                              #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad 					#                                                                              #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Acceder al sistema                                     #
#                                                                               #
#                                                                               #
# Dias de ejecucion   :  A Peticion del web, se pueden ejecutar n instancias    #
#################################################################################
# Autor               :JOAQUIN MOJICA                                           #
# Compania            :WELLCOM SA DE CV                                         #
# Proyecto/Procliente :P-02-0472-13                  Fecha:04/08/2014           #
# Modificaci?n        :Consolidacion de fiids para Banorte						#
#-----------------------------------------------------------------------------  #
#								MODIFICACIONES                                  #
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.io;

import com.prosa.exceptions.WellException;
import java.util.*;

import org.apache.commons.validator.*;
import java.text.NumberFormat;

public class HTML {
    
    public HTML() {
        
        //this.comboBox = "";
        this.fieldsToValidate = null;
        
        this.cssTable = "";
        this.cssTRHeaderTable = "";
        this.cssTRDataTable = "";
        
        this.genericValidator = null;
    }
    
    public double getTotalSum(ArrayList rowValues, int idx) throws WellException {
        try {
            if (rowValues == null) {
                throw new WellException(
                    "com.io.HTML.getTotalSum: Parametros invalidos");
            }
            
            double result = 0.00;
            String rv[] = null;
            
            Iterator it = rowValues.iterator();
            while (it.hasNext()) {
                
                rv = (String[]) it.next();
                if (rv[idx] != null) {
                    Double rslt = Double.valueOf(rv[idx]);
                    result += rslt.doubleValue();
                }
            }
            return result;
            
        } catch (Exception ex) {
            System.out.println("result: ");
            return 0;
            
        }
    }
    
    /**
     * Marca de cambio Author: Ing. Abraham Vargas VI@SOLUTIONS
     */
    
    public String createDdlFiids(String name, ArrayList<String> values)throws WellException {
        
        if ( (name.length() <= 0) || (values == null)) 
        {
            throw new WellException(
                "com.io.HTML.getComboBox: Par�metros inv�lidos");
        }
        String ddlFiids="<select name=\"selectBost\" class=\"form-control\" id=\"dtp_input3\">";
        for(Object g : values)
        {
           String [] h= (String[])g;
          ddlFiids+="<option value="+h[0]+">"+h[0]+"</option>";                 
        
        }
        
        ddlFiids+="</select>";
        
        return ddlFiids;
    
    }
     /**
     * Fin Marca de cambio Author: Ing. Abraham Vargas VI@SOLUTIONS
     */
    
    
    
    /**
     * Se obtiene un "combobox" a partir de un conjunto de valores
     * @param name variable de tipo "String" que establece el nombre del
     * combobox
     * @param values variable de tipo "ArrayList" que contiene los valores
     * que mostrara el "combobox"
     * @return una cadena que describe un "combobox" con ciertos valores
     * @throws WellException
     */
    public String getComboBox(String name, ArrayList values) throws WellException {
        
        if ( (name.length() <= 0) || (values == null)) {
            throw new WellException(
                "com.io.HTML.getComboBox: Par�metros inv�lidos");
        }
        
        String result = "<select name=\"" + name + "\" id=\"cbTipoTar\">";
        
        Iterator it = values.iterator();
        String cbValues[] = null;
        while (it.hasNext()) 
        {
            
            cbValues = (String[]) it.next();
            
            result += "<option value=\""
                + cbValues[0] + "\">";
                
            if(cbValues.length == 1)
            {
                result += cbValues[0] + "</option>";
            }
            else 
            {
                result += cbValues[1] + "</option>";
            }
        }
        
        result += "<option value=\"None\" selected></option>";
        result += "</select>";
        
        return result;
    }
    
    public String getComboBox(String name, ArrayList values, String defaultValue) throws
        WellException {
        
        if ( (name.length() <= 0) || (values == null)) {
            throw new WellException(
                "com.io.HTML: Par�metros inv�lidos");
        }
        
        String result = "<select name=\"" + name + "\" id=\"cbTipoTar\">";
        String selected = "";
        
        Iterator it = values.iterator();
        String cbValues[] = null;
        result += "<option value=\"None\" ></option>";
        while (it.hasNext()) {
            
            cbValues = (String[]) it.next();
            selected =
                cbValues[0].toString().equals(defaultValue) ? " SELECTED " : "";
            
            result += "<option value=\""
                + cbValues[0] + "\">";
                
            if(cbValues.length == 1) {
                result += cbValues[0] + "</option>";
            } else {
                result += cbValues[1] + "</option>";
            }
            
            //result += "<option value=\""
                //+ cbValues[0]
                //+ "\"" + selected + ">" + cbValues[1]
                //+ "</option>";
        }
        
        result += "</select>";
        return result;
    }
    
    /**
     * Valida los campos de la Forma HTML especificados por el usuario con la
     * funci�n <em>addFieldToValidate</em>
     * @return Un arreglo de cadenas que especifica los campos inv�lidos. En
     * caso de que todos sean v�lidos el resultado es null.
     * @throws WellException
     */
    public ArrayList validateFields() throws WellException {
        ArrayList invalidFields = new ArrayList(0);
        
        if (this.fieldsToValidate == null) {
            return invalidFields;
        }
    /*            throw new WellException(
     "com.io.HTML.validateFields: No ha especificado "
                        + "ning�n campo para validar." );
     */
        Set set = this.fieldsToValidate.entrySet();
        Iterator it = set.iterator();
        String fieldValue = "";
        
        while (it.hasNext()) {
            
            Map.Entry me = (Map.Entry) it.next();
            fieldValue = (String) me.getValue();
      /*
       * Modificado el 08/Mar/07 por AFI
       */
            //if (!fieldValue.equals("") && !fieldValue.equals("None")) {
            if(!fieldValue.equals("")
                && !fieldValue.equals("None")) {
                it.remove();
            } else {
                invalidFields.add( (String) me.getKey());
            }
        }
        
        return invalidFields;
    }
    
    /**
     * Agrega un nuevo elemento a la lista de campos que ser�n validados de
     * una Forma HTML
     * @param value cadena que especifica el nombre del campo a ser validado
     * @throws WellException
     */
    public void addFieldToValidate(String fieldName, String value) throws
        WellException {
        
        if ( (fieldName.length() <= 0)) {
            throw new WellException(
                "com.io.HTML.AddFieldToValidate: "
                + "Par�metros inv�lidos.");
        }
        
        if (this.fieldsToValidate == null) {
            this.fieldsToValidate = new HashMap(1);
        }
        
        this.fieldsToValidate.put(fieldName, value);
    }
    
    /**
     * Limpia la lista de campos a validar
     */
    public void clearFieldsToValidate() {
        
        if (this.fieldsToValidate != null) {
            this.fieldsToValidate.clear();
        }
    }
    
    public void setCSSTable(String value) {
        
        if (value.length() > 0) {
            this.cssTable = value;
        }
    }
    
    public void setCSSTRHeaderTable(String value) {
        
        if (value.length() > 0) {
            this.cssTRHeaderTable = value;
        }
    }
    
    public void setCSSTRDataTable(String value) {
        
        if (value.length() > 0) {
            this.cssTRDataTable = value;
        }
    }
    
    public void setCSSTRTable(String value) {
        
        if (value.length() > 0) {
            this.cssTRDataTable = value;
        }
    }
    
    /**
     * Genera una Tabla HTML
     * @param colTitles Arreglo de cadenas que contiene
     * los t�tulos de las columnas
     * @param rowValues variable que contiene los valores de
     * las filas de la tabla
     * @return Una cadena que espec�fica una Tabla HTML
     * @throws WellException
     */
    public String getTable(String[] colTitles, ArrayList rowValues) throws
        WellException {
        
        String result = "";
        String rowValue = "";
        
        if ( (colTitles == null) || (rowValues == null)) {
            throw new WellException(
                "com.io.HTML.getTable: "
                + "Par�metros inv�lidos.");
        }
        
        //Modificado el 24/Oct/2006 por Armando F. Ibarra
        result =
            "<table class=\"" + this.cssTable + "\" width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">"
            + "<tr class=\"" + this.cssTRHeaderTable + "\">";
        
        //Encabezados de Columna
        int numColTitles = colTitles.length;
        for (int i = 0; i < numColTitles; i++) {
            
            //Agregado el 27/Abr/2006 por Armando F. Ibarra
            colTitles[i] = colTitles[i].replaceAll("\r\n|\n", "");
            
            result += "<td><div align=\"center\">" + colTitles[i] + "</div></td>";
        }
        result += "</tr>";
        
        //Filas
        Iterator it = rowValues.iterator();
        NumberFormat n = NumberFormat.getCurrencyInstance( Locale.US );
        
        while (it.hasNext()) {
            
            String rowVals[] = (String[]) it.next();
            int rowLength = rowVals.length;
            result += "<tr class=\"" + this.cssTRDataTable + "\">";
            
            for (int i = 0; i < rowLength; i++) {
                //Modificado el 06/Nov/06 por Armando F. Ibarra
                rowValue = rowVals[i];
                rowValue = rowValue.replaceAll("\r\n|\n", "");
                //Modificado el 07/Dic/06 por Armando F. Ibarra
        /*if( genericValidator.isDouble( rowValue ) ||
            genericValidator.isFloat( rowValue ) ) {
         
            result += "<td><div align=\"right\">" + n.format(Double.parseDouble(rowValue)) + "</div></td>";
        }*/
                if( genericValidator.isDate( rowValue, "yyyy-MM-dd", true ) ) {
                    result += "<td><div align=\"center\">" + rowValue + "</div></td>";
                } else {
                    result += "<td><div align=\"left\">" + rowValue + "</div></td>";
                }
            }
            result += "</tr>";
        }
        result += "</table>";
        
        return result;
    }
    
    /**
     * Genera una Tabla HTML
     * @param colTitles Arreglo de cadenas que contiene
     * los t�tulos de las columnas
     * @param rowValues variable que contiene los valores de
     * las filas de la tabla
     * @return Una cadena que espec�fica una Tabla HTML
     * @throws WellException
     */
    public String getTableRadioBtn(String[] colTitles, ArrayList rowValues) throws
        WellException {
        
        String result = "";
        int j = 0;
        
        if ( (colTitles == null) || (rowValues == null)) {
            throw new WellException(
                "com.io.HTML.getTable: "
                + "Par�metros inv�lidos.");
        }
        
        result =
            "<table width=\"95%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">"
            + "<tr class=\"" + this.cssTable + "\">";
        
        int numColTitles = colTitles.length;
        for (int i = 0; i < numColTitles; i++) {
            
            //Agregado el 27/Abr/2006 por Armando F. Ibarra
            colTitles[i] = colTitles[i].replaceAll("\r\n|\n", "");
            
            result += "<td><div align=\"center\">" + colTitles[i] + "</div></td>";
        }
        result += "</tr>";
        
        Iterator it = rowValues.iterator();
        while (it.hasNext()) {
            
            String rowVals[] = (String[]) it.next();
            int rowLength = rowVals.length;
            result += "<tr class=\"" + this.cssTRDataTable + "\">";
            
            for (int i = 0; i < rowLength; i++) {
                //Agregado el 27/Abr/2006 por Armando F. Ibarra
                rowVals[i].replaceAll("\r\n|\n", "");
                result += "<td>" + rowVals[i] + "</td>";
                //Agregado el 03/Sep/2006 por Armando F. Ibarra
                if (i == (rowLength - 1)) {
                    //result += "<td>" + "<input name=\"" + j + "\" type=\"radio\" value=\"" + j + "\">" + "</td>";
                    result += "<td>" + "<input name=\"rbtn\" type=\"radio\" value=\"" + j +
                        "\">" + "</td>";
                    
                    j++;
                }
            }
            result += "</tr>";
        }
        result += "</table>";
        
        return result;
    }
	/*---------------------------------------------------------------------------------
	-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   04/08/2014 -
	---------------------------------------------------------------------------------*/
     /**-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Inicia la Modificacion   30/07/2014
     * Se obtiene un "ListBox" a partir de un conjunto de valores
     * @param name variable de tipo "String" que establece el nombre del
     * combobox
     * @param values variable de tipo "ArrayList" que contiene los valores
     * que mostrar� el "combobox"
     * @return una cadena que describe un "combobox" con ciertos valores
     * @throws WellException
     */
    public String getListBox(String name, ArrayList values, String label) throws WellException {
        
        if ( (name.length() <= 0) || (values == null)) {
            throw new WellException(
                "com.io.HTML.getComboBox: Par�metros inv�lidos");
        }
        
        String result = "<select name=\"" + name + "\" id=\"" + name + "\" multiple=\"multiple\">";
        result += "<option value=\"None\" selected>"+ label + "</option>";
        Iterator it = values.iterator();
        String cbValues[] = null;
        while (it.hasNext()) {
            
            cbValues = (String[]) it.next();
            
            result += "<option value=\""
                + cbValues[0] + "\">";
                
            if(cbValues.length == 1) {
                result += cbValues[0] + "</option>";
            } else {
                result += cbValues[1] + "</option>";
            }
        }       
        result += "</select>";
        return result;
    }
	/*---------------------------------------------------------------------------------
	-- Marca del Cambio : WELL-JMQ-P-02-0472-13 Termina la Modificacion   04/08/2014 -
	---------------------------------------------------------------------------------*/
	
    public static void main(String[] args) {
        HTML HTML1 = new HTML();
    }
    
  /*
   * Campos
   */
    //private String comboBox;
    private HashMap fieldsToValidate;
    
    private String cssTable;
    private String cssTRHeaderTable = "";
    private String cssTRDataTable = "";
    
    private org.apache.commons.validator.GenericValidator genericValidator;
}
