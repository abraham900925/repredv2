/*###############################################################################
# Nombre del Programa :  HeaderExcelProsa.java                                  #
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
# Autor               :                                                         #
# Compania            :                                                         #
# Proyecto/Procliente :                              Fecha:                     #
# Modificacion        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.io;

import java.io.*;
import java.util.*;

public class HeaderExcelProsa {

  public static void main(String args[]) {

    try {
      Excel excel = new Excel();
      ArrayList rowsValues = new ArrayList();
      File img = new File("C:\\Proyectos\\Consulta Comercios\\Fuentes\\"
                          + "Wellcom_Recoded\\prosa.png");

      String row1[] = {
        "MERCADOS INTERNACIONALES"};
      String row2[] = {
        "BBVA Banco - Puerto Rico"};
      String row3[] = {
        "Fecha de Corte"};

      String row4[] = {
        Calendar.getInstance().getTime().toString() };

      String row5[] = {
        "REPORTE DE CONCILIACION DE CIFRAS [ Operacion Adquirente ]"};
      String row6[] = {
        "ATH Vs. PROSA", "", "", "", "", "", ""};

      /**** FILAS EXTRAS !!! ******************/
      String row7[] = {
        "1", "2", "3"};
      String row8[] = {
        "A", "F", "I"};
      String row9[] = {
        "5", "7000", "9"};
      String row10[] = {
        "", ""};
      /****************************************/

      excel.setExcelFile(new FileOutputStream("prueba.xls"));
      excel.newSheet();
      excel.setSheetRowValuesSize(14);
      excel.setSheetRowValuesPos(excel.CELLALIGN_LEFT);

      rowsValues.add(row1);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(2, 1, null, excel.BORDER_NONE, excel.BORDERSTYLE_NONE, false);

      rowsValues.clear();
      rowsValues.add(row2);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(2, 2, null, excel.BORDER_NONE, excel.BORDERSTYLE_NONE, false);

      rowsValues.clear();
      rowsValues.add(row3);
      excel.setSheetRowValuesSize(10);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(5, 1, null, excel.BORDER_ALL, excel.BORDERSTYLE_THIN, false);

      rowsValues.clear();
      rowsValues.add(row4);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(6, 1, null, excel.BORDER_ALL, excel.BORDERSTYLE_THIN, false);

      rowsValues.clear();
      rowsValues.add(row5);
      excel.setSheetRowValuesSize(12);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(2, 4, null, excel.BORDER_NONE, excel.BORDERSTYLE_NONE, false);

      rowsValues.clear();
      rowsValues.add(row6);
      excel.setSheetRowValuesSize(12);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(2, 5, null, excel.BORDER_BOTTOM,
                         excel.BORDERSTYLE_THIN, false);

      rowsValues.clear();
      rowsValues.add(row10);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(0, 1, null, excel.BORDER_NONE, excel.BORDERSTYLE_NONE, false);

      excel.addSheetImage(img, 0, 0, 2, 6);

      /******************* FILAS EXTRAS !!!  ****************************************/
      rowsValues.clear();
      rowsValues.add(row7);
      rowsValues.add(row8);
      rowsValues.add(row9);
      excel.setSheetRowValuesSize(12);
      excel.setSheetRowValues(rowsValues);
      excel.addSheetRows(null, excel.BORDER_BOTTOM, excel.BORDERSTYLE_THIN, false);
      /*******************************************************************************/

      excel.closeFile();
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }
}
