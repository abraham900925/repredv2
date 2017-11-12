/*###############################################################################
# Nombre del Programa :  Excel.java                                             #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad, escritura de     #
#                        Archivos .xls     										#
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
# Modificaci�n        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.io;

import com.prosa.exceptions.WellException;
import com.prosa.sql.Database;
import java.io.*;
import java.util.*;

import jxl.*;
import jxl.write.*;

/**
 *
 * <p>Titulo: Excel</p>
 * <p>Descripcion: Clase que permite la escritura de archivos .xls</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compania: Wellcom</p>
 * @author Armando F. Ibarra
 *
 */
public class Excel
    implements Serializable {
    
    public Excel() {
        
        this.excelFile = null;
        this.excelOutputStream = null;
        
        this.writableWorkbook = null;
        this.writableSheet = null;
        
        this.sheetColTitles = null;
        this.sheetColTitlesPos = CELLALIGN_CENTRE;
        this.sheetColTitlesSize = 10;
        this.sheetRowValues = null;
        //this.sheetRowValuesBordered = null;
        this.sheetRowValuesPos = CELLALIGN_CENTRE;
        this.sheetRowValuesSize = 10;
        this.sheetPageOrientation = LANDSCAPE;
        this.sheetPageSize = LETTER;
        
        this.currentRow = 0;
        this.numOfSheet = 0;
        
        this.pagingSize = 0;
        this.query = "";
        this.db = null;
    }
    
    /**
     * Establece el t�tulo de las columnas de la hoja de c�lculo actual
     * @param value conjunto de cadenas utilizadas para establecer los
     * t�tulos de las columnas de la hoja de c�lculo
     * @throws WellException
     */
    public void setSheetColsTitles(String value[]) throws WellException {
        
        if (value != null) {
            this.sheetColTitles = value;
        } else {
            throw new WellException("com.io.Excel.setSheetColsTitles:"
                + "Argumento null.");
        }
    }
    
    /**
     * Establece el t�tulo de las columnas de la hoja de c�lculo actual
     * @param value conjunto de cadenas utilizadas para establecer los
     * t�tulos de las columnas de la hoja de c�lculo
     * @throws WellException
     */
    public void setDBSheetColsTitles(String value[]) throws WellException {
        
        this.setSheetColsTitles(value);
    }
    
    /**
     * Establece el t�tulo de las columnas de la hoja de c�lculo actual
     * @param value cadena utilizada para establecer los
     * t�tulos de las columnas de la hoja de c�lculo
     * @throws WellException
     */
    public void setSheetColsTitles(String value) throws WellException {
        
        if (value != null) {
            String tmpStr[] = new String[1];
            tmpStr[0] = value;
            this.setSheetColsTitles(tmpStr);
        } else {
            throw new WellException("com.io.Excel.setSheetColsTitles:"
                + "Argumento null.");
        }
    }
    
    /**
     * Establece los valores que contendr�n las filas de la hoja de c�lculo
     * @param value tipo de dato <em>ArrayList</em> que contiene una conjunto
     * de objetos del tipo <em>String[]</em>, donde cada uno de �stos arryas de
     * tipo <em>String</em> representan los valores de una de las filas de
     * la hoja de c�lculo
     * @throws WellException
     */
    public void setSheetRowValues(ArrayList value) throws WellException {
        
        if (value != null) {
            this.sheetRowValues = value;
        } else {
            throw new WellException("com.io.Excel.setSheetRowValues: "
                + "Argumento null.");
        }
    }
    
    /**
     * Establece los valores que contendr�n las filas de la hoja de c�lculo
     * @param value tipo de dato <em>String</em> que contiene un texto
     * a agregar en una de las hojas de c�lculo.
     * @throws WellException
     */
    public void setSheetRowValues(String value) throws WellException {
        
        if (value != null) {
            
            String tmpTxt[] = new String[1];
            tmpTxt[0] = value;
            ArrayList tmpAR = new ArrayList(1);
            tmpAR.add(tmpTxt);
            setSheetRowValues(tmpAR);
        } else {
            throw new WellException("com.io.Excel.setSheetRowValues: "
                + "Argumento null.");
        }
    }
    
    /**
     * Establece el flujo de salida donde se crear� el archivo .xls
     * @param value tipo de dato <em>File</em> donde se crear� el archivo .xls
     * @throws WellException
     */
    public void setExcelFile(File value) throws WellException {
        
        if (value != null) {
            
            try {
                
                this.excelFile = value;
                this.writableWorkbook =
                    Workbook.createWorkbook(this.excelFile);
            } catch (Exception ex) {
                throw new WellException("com.io.Excel.setExcelFile: "
                    + ex.toString());
            }
        } else {
            throw new WellException("com.io.Excel.setExcelFile: "
                + "Argumento null.");
        }
    }
    
    /**
     * Establece el flujo de salida donde se crear� el archivo .xls
     * @param value tipo de dato <em>OutputStream</em> donde
     * se crear� el archivo .xls
     * @throws WellException
     */
    public void setExcelFile(OutputStream value) throws WellException {
        
        if (value != null) {
            
            try {
                this.excelOutputStream = value;
                this.writableWorkbook =
                    Workbook.createWorkbook(this.excelOutputStream);
            } catch (Exception ex) {
                throw new WellException("com.io.Excel.setExcelFile: "
                    + ex.toString());
            }
        } else {
            throw new WellException("com.io.Excel.setExcelFile: "
                + "Argumento null.");
        }
    }
    
    /**
     * Crea una nueva p�gina de hoja de c�lculo
     * @throws WellException
     */
    public void newSheet() throws WellException {
        
        if (this.writableWorkbook != null) {
            
            this.writableSheet =
                this.writableWorkbook.createSheet(
                "P�gina " + this.numOfSheet, this.numOfSheet++);
            this.currentRow = 0;
        } else {
            throw new WellException("com.io.Excel.newSheet: "
                + "No ha creado ning�n documento.");
        }
    }
    
    /**
     * Crea una nueva p�gina de hoja de c�lculo
     * @param sheetName establece el nombre de la nueva p�gina
     * @param sheetNumber establece el n�mero de la nueva p�gina
     * @throws WellException
     */
    public void newSheet(String sheetName, int sheetNumber) throws WellException {
        
        if ( (sheetName.length() <= 0) || (sheetNumber < 0)) {
            throw new WellException("com.io.Excel.newSheet: "
                + "Par�metro null y/o incorrecto.");
        }
        
        if (this.writableWorkbook != null) {
            
            this.writableSheet =
                this.writableWorkbook.createSheet(sheetName, sheetNumber);
            /*
             * Agregado el 08/Mar/2007 por AFI
             */
            this.currentRow = 0;
        } else {
            throw new WellException("com.io.Excel.newSheet: "
                + "No ha creado ning�n documento.");
        }
    }
    
    /**
     * Agrega los datos establecidos en la propiedad
     * <em>sheetColTitles</em>
     * @throws WellException
     */
    public void addSheetColsTitles() throws WellException {
        
        /***************CAMPOS*********************/
        WritableFont timesBoldFont = null;
        WritableCellFormat timesBoldFormat = null;
        Label lblTitle = null;
        /******************************************/
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetColsTitles: "
                + "No ha creado ning�n documento.");
        }
        if (this.writableSheet == null) {
            throw new WellException("com.io.Excel.addSheetColsTitles: "
                + "No ha creado ninguna hoja de c�lculo.");
        }
        
        this.writableSheet.setPageSetup(this.sheetPageOrientation,
            this.sheetPageSize,
            1,
            1);
        
    /*
     * T�tulos de las columnas de la hoja de c�lculo
     */
        if (this.sheetColTitles != null) {
            
            timesBoldFont =
                new WritableFont(WritableFont.TIMES,
                this.sheetColTitlesSize,
                WritableFont.BOLD);
            
            timesBoldFormat = new WritableCellFormat(timesBoldFont);
            
            try {
                // Posicion y tipo de borde
                timesBoldFormat.setBorder(this.BORDER_ALL,
                    this.BORDERSTYLE_THIN);
                
                // Alineacion de las celdas
                timesBoldFormat.setAlignment(this.sheetColTitlesPos);
                
                // Color de fondo de la celda
                //timesBoldFormat.setBackground(jxl.write.Colour.GRAY_25);
                timesBoldFormat.setBackground(jxl.format.Colour.GRAY_25);
                
                for (int col = 0; col < this.sheetColTitles.length; col++) {
                    
                    lblTitle =
                        new Label(col,
                        currentRow,
                        sheetColTitles[col],
                        timesBoldFormat);
                    
                    this.writableSheet.addCell(lblTitle);
                }
                
                currentRow++;
            } catch (Exception ex) {
                throw new WellException("com.io.Excel.addSheetData: "
                    + ex.toString());
            }
        }
    }
    
    /**
     * Agrega los datos establecidos en la propiedad
     * <em>sheetColTitles</em>
     * @throws WellException
     */
    public void addDBSheetColsTitles() throws WellException {
        
        this.addSheetColsTitles();
    }
    
    
    public void addSheetRows(int c,
        int r,
        int[] rtb,
        jxl.format.Border border,
        jxl.format.BorderLineStyle borderStyle,
        boolean merged) throws WellException {
        
        /********************* CAMPOS *********************************/
        WritableFont timesFont = null;
        WritableCellFormat timesFormat = null;
        WritableCellFormat numberFormat = null;
        
        Iterator it = null;
        String rvalues[] = null;
        
        int i = 0;
        //int row = 0;
        int rowsBordered = 0;
        int rowsBorderedLength = 0;
        boolean borderFounded = false;
        /*************************************************************/
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ning�n documento.");
        }
        if (this.writableSheet == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ninguna hoja de c�lculo.");
        }
        
        this.writableSheet.setPageSetup(this.sheetPageOrientation,
            this.sheetPageSize,
            1,
            1);
        
    /*
     *  Datos de la hoja de c�lculo
     */
        if (this.sheetRowValues != null) {
            
            try {
                
                /* Celda tipo Cadena */
                timesFont = new WritableFont(WritableFont.TIMES,
                    this.sheetRowValuesSize);
                timesFormat = new WritableCellFormat(timesFont);
                timesFormat.setAlignment(this.sheetRowValuesPos);
                /* Celda tipo Numero */
                numberFormat =
                    new WritableCellFormat(timesFont,
                    jxl.write.NumberFormats.FORMAT4);
                
                // Bordes de las celdas
        /* Si no fueron especificadas las celdas que se desean
           formatear, se formatean todas igual */
                if (rtb == null) {
                    timesFormat.setBorder(border, borderStyle);
                    numberFormat.setBorder(border, borderStyle);
                } else {
                    rowsBordered = 1;
                    rowsBorderedLength = rtb.length;
                }
                
                // Iterador para recorrer los valores del ArrayList
                it = this.sheetRowValues.iterator();
                
                // Se verifica si las celdas de la fila tendran borde
                if (rtb != null) {
                    Arrays.sort(rtb);
                }
                
                /* Se recorren los valores del ArrayList */
                while (it.hasNext()) {
                    
                    rvalues = (String[]) it.next();
                    
                    // Se verifica si las celdas de la fila tendran borde
                    if (rtb != null) {
                        
                        timesFont = null;
                        timesFont = new WritableFont(WritableFont.TIMES,
                            this.sheetRowValuesSize);
                        timesFormat = null;
                        timesFormat = new WritableCellFormat(timesFont);
                        timesFormat.setAlignment(this.sheetRowValuesPos);
                        numberFormat = null;
                        numberFormat =
                            new WritableCellFormat(timesFont,
                            jxl.write.NumberFormats.FORMAT4);
                        
                        if (Arrays.binarySearch(rtb, rowsBordered) >= 0) {
                            borderFounded = true;
                        }
                        
                        if (borderFounded) {
                            
                            timesFormat.setBorder(border, borderStyle);
                            numberFormat.setBorder(border, borderStyle);
                            
                            borderFounded = false;
                        } else {
                            timesFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                            numberFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                        }
                    }
                    
                    for (i = 0; i < rvalues.length; i++) {
                        
            /* Se verifica si la celda contiene una cadena
               o un numero. */
                        if (!Format.isNumber(rvalues[i])) {
                            Label lblRow = new Label(c,
                                r,
                                rvalues[i],
                                timesFormat);
                            this.writableSheet.addCell(lblRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(c,
                                    r,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        } else {
                            jxl.write.Number nmbRow =
                                new jxl.write.Number(c,
                                r,
                                Format.stringToDouble(rvalues[i]),
                                numberFormat);
                            this.writableSheet.addCell(nmbRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(c,
                                    r,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        }
                        //Columna siguiente
                        c++;
                    }
                    
                    //Fila siguiente
                    r++;
                    //currentRow++;
                    rowsBordered++;
                }
            } catch (Exception ex) {
                throw new WellException("com.io.Excel.addSheetRows: "
                    + ex.toString());
            }
        }
    }
    
    /**
     * Agrega registros a la hoja de calculo
     * @param rtb Indica los registros que se desea que tengan un formato
     * especifico.
     * @param border Posicion del borde
     * @param borderStyle Estilo del borde
     * @param merged Establece si se desea que se unan las celdas
     * @throws WellException
     * <br><br><b>Posicion del borde:</b><br><br>
     * 1. BORDER_ALL<br>
     * 2. BORDER_BOTTOM<br>
     * 3. BORDER_LEFT<br>
     * 4. BORDER_NONE<br>
     * 5. BORDER_RIGHT<br>
     * 6. BORDER_TOP<br><br>
     * <br><b>Tipo del borde:</b><br><br>
     * 1. BORDERSTYLE_DOUBLE<br>
     * 2. BORDERSTYLE_NONE<br>
     * 3. BORDERSTYLE_THICK<br>
     * 4. BORDERSTYLE_THIN<br><BR>
     * <b>Ejemplo:</b><br><br>
     * addSheetRows( null, BORDER_BOTTOM, BORDERSTYLE_DOUBLE );
     * <br><br>Todas las celdas de los registros tendran en su parte
     * inferior un borde doble
     */
    public void addSheetRows(int[] rtb,
        jxl.format.Border border,
        jxl.format.BorderLineStyle borderStyle,
        boolean merged) throws WellException {
        
        /********************* CAMPOS *********************************/
        WritableFont timesFont = null;
        WritableCellFormat timesFormat = null;
        WritableCellFormat numberFormat = null;
        
        Iterator it = null;
        String rvalues[] = null;
        
        int i = 0;
        int row = 0;
        int rowsBordered = 0;
        int rowsBorderedLength = 0;
        boolean borderFounded = false;
        /*************************************************************/
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ning�n documento.");
        }
        if (this.writableSheet == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ninguna hoja de c�lculo.");
        }
        
        this.writableSheet.setPageSetup(this.sheetPageOrientation,
            this.sheetPageSize,
            1,
            1);
        
    /*
     *  Datos de la hoja de c�lculo
     */
        if (this.sheetRowValues != null) {
            
            try {
                
                /* Celda tipo Cadena */
                timesFont = new WritableFont(WritableFont.TIMES,
                    this.sheetRowValuesSize);
                timesFormat = new WritableCellFormat(timesFont);
                timesFormat.setAlignment(this.sheetRowValuesPos);
                /* Celda tipo Numero */
                numberFormat =
                    new WritableCellFormat(timesFont,
                    jxl.write.NumberFormats.FORMAT4);
                
                // Bordes de las celdas
        /* Si no fueron especificadas las celdas que se desean
           formatear, se formatean todas igual */
                if (rtb == null) {
                    timesFormat.setBorder(border, borderStyle);
                    numberFormat.setBorder(border, borderStyle);
                } else {
                    rowsBordered = 1;
                    rowsBorderedLength = rtb.length;
                }
                
                // Iterador para recorrer los valores del ArrayList
                it = this.sheetRowValues.iterator();
                
                // Se verifica si las celdas de la fila tendran borde
                if (rtb != null) {
                    Arrays.sort(rtb);
                }
                
                /* Se recorren los valores del ArrayList */
                while (it.hasNext()) {
                    
                    rvalues = (String[]) it.next();
                    
                    // Se verifica si las celdas de la fila tendran borde
                    if (rtb != null) {
                        
                        timesFont = null;
                        timesFont = new WritableFont(WritableFont.TIMES,
                            this.sheetRowValuesSize);
                        timesFormat = null;
                        timesFormat = new WritableCellFormat(timesFont);
                        timesFormat.setAlignment(this.sheetRowValuesPos);
                        numberFormat = null;
                        numberFormat =
                            new WritableCellFormat(timesFont,
                            jxl.write.NumberFormats.FORMAT4);
                        
            /*for( i = 0; i < rowsBorderedLength; i++ )
                if(  rtb[ i ] == rowsBordered )
                    borderFounded = true;*/
                        if (Arrays.binarySearch(rtb, rowsBordered) >= 0) {
                            borderFounded = true;
                        }
                        
                        if (borderFounded) {
                            
                            timesFormat.setBorder(border, borderStyle);
                            numberFormat.setBorder(border, borderStyle);
                            
                            borderFounded = false;
                        } else {
                            timesFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                            numberFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                        }
                    }
                    
                    for (int col = 0; col < rvalues.length; col++) {
                        
            /* Se verifica si la celda contiene una cadena
               o un numero. */
                        if (!Format.isNumber(rvalues[col])) {
                            Label lblRow = new Label(col,
                                currentRow,
                                rvalues[col],
                                timesFormat);
                            this.writableSheet.addCell(lblRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(0,
                                    currentRow,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        } else {
                            jxl.write.Number nmbRow =
                                new jxl.write.Number(col,
                                currentRow,
                                Format.stringToDouble(rvalues[col]),
                                numberFormat);
                            this.writableSheet.addCell(nmbRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(0,
                                    currentRow,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        }
                    }
                    
                    currentRow++;
                    rowsBordered++;
                }
            } catch (Exception ex) {
                throw new WellException("com.io.Excel.addSheetRows: "
                    + ex.toString());
            }
        }
    }
    
    /**
     * Agrega registros a la hoja de calculo
     * @param rtb Indica los registros que se desea que tengan un formato
     * especifico.
     * @param border Posicion del borde
     * @param borderStyle Estilo del borde
     * @param merged Establece si se desea que se unan las celdas
     * @throws WellException
     * <br><br><b>Posicion del borde:</b><br><br>
     * 1. BORDER_ALL<br>
     * 2. BORDER_BOTTOM<br>
     * 3. BORDER_LEFT<br>
     * 4. BORDER_NONE<br>
     * 5. BORDER_RIGHT<br>
     * 6. BORDER_TOP<br><br>
     * <br><b>Tipo del borde:</b><br><br>
     * 1. BORDERSTYLE_DOUBLE<br>
     * 2. BORDERSTYLE_NONE<br>
     * 3. BORDERSTYLE_THICK<br>
     * 4. BORDERSTYLE_THIN<br><BR>
     * <b>Ejemplo:</b><br><br>
     * addSheetRows( null, BORDER_BOTTOM, BORDERSTYLE_DOUBLE );
     * <br><br>Todas las celdas de los registros tendran en su parte
     * inferior un borde doble
     */
    public void addDBSheetRows(int[] rtb,
        jxl.format.Border border,
        jxl.format.BorderLineStyle borderStyle,
        boolean merged) throws WellException {
        
        /********************* CAMPOS *********************************/
        WritableFont timesFont = null;
        WritableCellFormat timesFormat = null;
        WritableCellFormat numberFormat = null;
        
        Iterator it = null;
        String rvalues[] = null;
        
        int i = 0;
        int row = 0;
        int rowsBordered = 0;
        int rowsBorderedLength = 0;
        boolean borderFounded = false;
        
        int restRows = 0;
        int numRows = 0;
        int repeat = 0;
        int rowsAdded = 0;
        /*************************************************************/
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ning�n documento.");
        }
        if (this.writableSheet == null) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + "No ha creado ninguna hoja de c�lculo.");
        }
        
        this.writableSheet.setPageSetup(this.sheetPageOrientation,
            this.sheetPageSize,
            1,
            1);
        
    /*
     *  Datos de la hoja de c�lculo
     */
        try {
            
            /* Celda tipo Cadena */
            timesFont = new WritableFont(WritableFont.TIMES,
                this.sheetRowValuesSize);
            timesFormat = new WritableCellFormat(timesFont);
            timesFormat.setAlignment(this.sheetRowValuesPos);
            /* Celda tipo Numero */
            numberFormat =
                new WritableCellFormat(timesFont,
                jxl.write.NumberFormats.FORMAT4);
            
            // Bordes de las celdas
      /* Si no fueron especificadas las celdas que se desean
         formatear, se formatean todas igual */
            if (rtb == null) {
                timesFormat.setBorder(border, borderStyle);
                numberFormat.setBorder(border, borderStyle);
            } else {
                rowsBordered = 1;
                rowsBorderedLength = rtb.length;
            }
            
            // Se verifica si las celdas de la fila tendran borde
            if (rtb != null) {
                Arrays.sort(rtb);
            }
            
            //Base de Datos
            this.db.executeQuerySelect();
            //Numero de registros/"Recorrido" de registros
            numRows = this.db.getNumRowsRS();
            restRows = numRows % this.pagingSize;
            repeat = numRows / this.pagingSize;
            if(restRows > 0) {
                repeat += 1;
            }
            
            for(i = 0;i < repeat;i++) {
                
                if(i < repeat) {
                    this.setSheetRowValues(this.db.getNextResultSetData(this.pagingSize));
                } else if((i >= repeat) && (restRows > 0)) {
                    this.setSheetRowValues(this.db.getNextResultSetData(restRows));
                }
                
                // Iterador para recorrer los valores del ArrayList
                it = this.sheetRowValues.iterator();
                
                /* Se recorren los valores del ArrayList */
                while (it.hasNext()) {
                    
                    rvalues = (String[]) it.next();
                    
                    // Se verifica si las celdas de la fila tendran borde
                    if (rtb != null) {
                        
                        timesFont = null;
                        timesFont = new WritableFont(WritableFont.TIMES,
                            this.sheetRowValuesSize);
                        timesFormat = null;
                        timesFormat = new WritableCellFormat(timesFont);
                        timesFormat.setAlignment(this.sheetRowValuesPos);
                        numberFormat = null;
                        numberFormat =
                            new WritableCellFormat(timesFont,
                            jxl.write.NumberFormats.FORMAT4);
                        
            /*for( i = 0; i < rowsBorderedLength; i++ )
                if(  rtb[ i ] == rowsBordered )
                    borderFounded = true;*/
                        if (Arrays.binarySearch(rtb, rowsBordered) >= 0) {
                            borderFounded = true;
                        }
                        
                        if (borderFounded) {
                            
                            timesFormat.setBorder(border, borderStyle);
                            numberFormat.setBorder(border, borderStyle);
                            
                            borderFounded = false;
                        } else {
                            timesFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                            numberFormat.setBorder(BORDER_NONE,
                                BORDERSTYLE_NONE);
                        }
                    }
                    
                    for (int col = 0; col < rvalues.length; col++) {
                        
            /* Se verifica si la celda contiene una cadena
               o un numero. */
                        if (!Format.isNumber(rvalues[col])) {
                            Label lblRow = new Label(col,
                                currentRow,
                                rvalues[col],
                                timesFormat);
                            this.writableSheet.addCell(lblRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(0,
                                    currentRow,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        } else {
                            jxl.write.Number nmbRow =
                                new jxl.write.Number(col,
                                currentRow,
                                Format.stringToDouble(rvalues[col]),
                                numberFormat);
                            this.writableSheet.addCell(nmbRow);
                            
                            if (merged) {
                                this.writableSheet.mergeCells(0,
                                    currentRow,
                                    rvalues.length - 1,
                                    currentRow);
                            }
                        }
                    }
                    
                    currentRow++;
                    rowsBordered++;
                    
                    //Agregado el 21/09/06
                    rowsAdded++;
                    if(rowsAdded >= 65000) {
                        rowsAdded = 0;
                        this.newSheet();
                        this.addDBSheetColsTitles();
                    }
                }
            }
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.addSheetRows: "
                + ex.toString());
        }
    }
    
    /**
     * Agrega los datos establecidos en las propiedaes
     * <em>sheetColTitles</em> y <em>sheetRowValues</em>
     * @throws WellException
     */
    public void addSheetData() throws WellException {
        
        try {
            addSheetColsTitles();
            addSheetRows(null, this.BORDER_ALL, this.BORDERSTYLE_THIN, false);
        } catch (Exception e) {
            throw new WellException("com.io.Excel.addSheetData");
        }
    }
    
    /**
     * Agrega los datos establecidos en las propiedaes
     * <em>sheetColTitles</em> y <em>sheetRowValues</em>
     * @throws WellException
     */
    public void addDBSheetData() throws WellException {
        
        try {
            addDBSheetColsTitles();
            addDBSheetRows(null, this.BORDER_ALL, this.BORDERSTYLE_THIN, false);
        } catch (Exception e) {
            throw new WellException("com.io.Excel.addDBSheetData");
        }
    }
    
    public void setDB(Database value) throws WellException {
        
        if(value == null) {
            throw new WellException("com.io.Excel.setDB: "
                + "Parametro nulo.");
        } else {
            this.db = value;
        }
    }
    
    /**
     * Agrega una im�gen a la hoja de c�lculo
     * @param value tipo de dato <em>byte[]</em> que contiene una im�gen
     * @throws WellException
     */
    public void addSheetImage(byte[] value) throws WellException {
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "No ha creado ning�n documento.");
        }
        
        if (value == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "Argumento null.");
        }
        
        try {
            
            WritableImage wi =
                new WritableImage(0, 0, 10, 25, value);
            this.writableSheet.addImage(wi);
            
            this.currentRow = 27;
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + ex.toString());
        }
    }
    
    /**
     * Agrega una im�gen a la hoja de c�lculo
     * @param value tipo de dato <em>byte[]</em> que contiene una im�gen
     * @throws WellException
     */
    public void addSheetImage(File value) throws WellException {
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "No ha creado ning�n documento.");
        }
        
        if (value == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "Argumento null.");
        }
        
        try {
            
            WritableImage wi =
                new WritableImage(0, 0, 10, 25, value);
            this.writableSheet.addImage(wi);
            
            this.currentRow = 27;
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + ex.toString());
        }
    }
    
    /**
     * Agrega una im�gen a la hoja de c�lculo
     * @param image tipo de dato <em>byte[]</em> que contiene una im�gen
     * @param col Numero de columna en la que se pondra la imagen
     * @param row Numero de fila en la que se pondra la imagen
     * @param width Ancho ( en numero de columnas ) de la imagen
     * @param height Altura ( en numero de filas ) de la imagen
     * @throws WellException
     */
    public void addSheetImage(byte[] image, int col, int row,
        int width, int height) throws WellException {
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "No ha creado ning�n documento.");
        }
        
        if (image == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "Argumento null.");
        }
        
        try {
            
            WritableImage wi =
                new WritableImage(col, row, width, height, image);
            this.writableSheet.addImage(wi);
            
            this.currentRow = height + 2;
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + ex.toString());
        }
    }
    
    /**
     * Agrega una im�gen a la hoja de c�lculo
     * @param image tipo de dato <em>File</em> que contiene una im�gen
     * @param col Numero de columna en la que se pondra la imagen
     * @param row Numero de fila en la que se pondra la imagen
     * @param width Ancho ( en numero de columnas ) de la imagen
     * @param height Altura ( en numero de filas ) de la imagen
     * @throws WellException
     */
    public void addSheetImage(File image, int col, int row,
        int width, int height) throws WellException {
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "No ha creado ning�n documento.");
        }
        
        if (image == null) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + "Argumento null.");
        }
        
        try {
            
            WritableImage wi =
                new WritableImage(col, row, width, height, image);
            this.writableSheet.addImage(wi);
            
            this.currentRow = height + 2;
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.addSheetImage: "
                + ex.toString());
        }
    }
    
    /**
     * Cierra el documento
     * @throws WellException
     */
    public void closeFile() throws WellException {
        
        if (this.writableWorkbook == null) {
            throw new WellException("com.io.Excel.closeFile: "
                + "No ha creado ning�n documento.");
        }
        
        try {
            
            this.writableWorkbook.write();
            this.writableWorkbook.close();
        } catch (Exception ex) {
            throw new WellException("com.io.Excel.closeFile: "
                + ex.toString());
        }
    }
    
    /**
     * Establece la alineaci�n del texto de los t�tulos de las columnas
     * @deprecated
     * @param value cadena que especifica la alineaci�n del texto de
     * los t�tulos de las columnas<br><br>
     * 1. "L" - Left<br>
     * 2. "C" - Center<br>
     * 3. "R" - Right<br><br>
     * <b>Ejemplo:</b>
     * setSheetColTitlesPos( "C" )
     */
    public void setSheetColTitlesPos(String value) {
        
        if (value.length() > 0) {
            
            if (value.equals("L")) {
                this.sheetColTitlesPos = this.CELLALIGN_LEFT;
            } else if (this.sheetColTitlesPos.equals("C")) {
                this.sheetColTitlesPos = this.CELLALIGN_CENTRE;
            } else if (this.sheetColTitlesPos.equals("R")) {
                this.sheetColTitlesPos = this.CELLALIGN_RIGHT;
            }
        }
        
        else {
            this.sheetColTitlesPos = this.CELLALIGN_CENTRE;
        }
    }
    
    /**
     * Establece la alineaci�n del texto de los t�tulos de las columnas
     * @param value cadena que especifica la alineaci�n del texto de
     * los t�tulos de las columnas<br><br>
     * 1. CELLALIGN_LEFT<br>
     * 2. CELLALIGN_CENTRE<br>
     * 3. CELLALIGN_RIGHT<br><br>
     * <b>Ejemplo:</b>
     * setSheetColTitlesPos( CELLALIGN_CENTRE )
     */
    public void setSheetColTitlesPos(jxl.format.Alignment value) {
        
        if (value != null) {
            this.sheetColTitlesPos = value;
        }
        
        else {
            this.sheetColTitlesPos = this.CELLALIGN_CENTRE;
        }
    }
    
    /**
     * Establece el tama�o de la fuente de los t�tulos de las columnas
     * @param value entero que especifica el tama�o de la fuente de
     * los t�tulos de las columnas
     */
    public void setSheetColTitlesSize(int value) {
        
        if (value > 0) {
            this.sheetColTitlesSize = value;
        } else {
            this.sheetColTitlesSize = 10;
        }
    }
    
    /**
     * Establece el tama�o de la fuente de los t�tulos de las columnas
     * @param value entero que especifica el tama�o de la fuente de
     * los t�tulos de las columnas
     */
    public void setDBSheetColTitlesSize(int value) {
        
        this.setSheetColTitlesSize(value);
    }
    
    /**
     * Establece la alineaci�n del texto de las celdas
     * @param value cadena que especifica la alineaci�n del texto de
     * los t�tulos de las columnas<br><br>
     * 1. CELLALIGN_LEFT<br>
     * 2. CELLALIGN_CENTRE<br>
     * 3. CELLALIGN_RIGHT<br><br>
     * <b>Ejemplo:</b>
     * setSheetRowValuesPos( CELLALIGN_CENTRE )
     */
    public void setSheetRowValuesPos(jxl.format.Alignment value) {
        
        if (value != null) {
            this.sheetRowValuesPos = value;
        }
        
        else {
            this.sheetRowValuesPos = this.CELLALIGN_CENTRE;
        }
    }
    
    /**
     * Establece el tama�o de la fuente del texto de las columnas
     * @param value entero que especifica el tama�o de la fuente
     * del texto de las columnas
     */
    public void setSheetRowValuesSize(int value) {
        
        if (value > 0) {
            this.sheetRowValuesSize = value;
        } else {
            this.sheetRowValuesSize = 10;
        }
    }
    
    /**
     * Establece el tama�o de la fuente del texto de las columnas
     * @param value entero que especifica el tama�o de la fuente
     * del texto de las columnas
     */
    public void setDBSheetRowValuesSize(int value) {
        
        this.setSheetRowValuesSize(value);
    }
    
    /**
     * Inserta una fila en blanco
     * @throws WellException
     */
    public void insertBlankRow() throws WellException {
        
        Label label = null;
        
        if (this.writableSheet != null) {
            
            try {
                label = new Label(0, this.currentRow, "");
                this.writableSheet.addCell(label);
                this.currentRow++;
            } catch (Exception e) {
                throw new WellException("com.io.Excel.insertBlankRow: "
                    + e.toString());
            }
            
        } else {
            throw new WellException("com.io.Excel.insertBlankRow: "
                + "No ha creado ninguna hoja de calculo");
        }
    }
    
    /**
     * Establece la orientaci�n de la p�gina
     * @param value Orientacion de la pagina
     * <br><br><b>Orientaci�n de p�gina:</b><br><br>
     * 1. PAGE_LANDSCAPE<br>
     * 2. PAGE_PORTRAIT<br><br>
     * <b>Ejemplo:</b><br><br>
     * setSheetPageOrientation( PAGE_PORTRAIT );
     */
    public void setSheetPageOrientation(jxl.format.PageOrientation value) {
        
        if (value != null) {
            this.sheetPageOrientation = value;
        } else {
            this.sheetPageOrientation = this.LANDSCAPE;
        }
    }
    
    /**
     * Establece el tama�o de la p�gina
     * @param value cadena que especifica el tama�o de la p�gina
     * <br><br><b>Tama�os de p�gina:</b><br><br>
     * 1. LETTER<br>
     * 2. A4<br><br>
     * <b>Ejemplo:</b><br><br>
     * setSheetPageSize( LETTER );
     */
    public void setSheetPageSize(jxl.format.PaperSize value) {
        
        if (value != null) {
            this.sheetPageSize = value;
        } else {
            this.sheetPageSize = LETTER;
        }
    }
    
    public void setPagingSize(int value) throws WellException {
        
        if(value <= 0) {
            throw new WellException("com.io.Excel.setPagingSize: "
                + "Valor menor o igual a cero.");
        } else {
            this.pagingSize = value;
        }
    }
    
    /**
     * Agregado el 07/Mar/2007 por AFI
     */
    public String getQuery() {
        
        return this.query;
    }
    public void setQuery(String value) {
        
        if(value == null) {
            value = "";
        }
        
        this.query = value;
    }
    
  /*
   * Campos
   */
    private File excelFile;
    private OutputStream excelOutputStream;
    
    private WritableWorkbook writableWorkbook;
    private WritableSheet writableSheet;
    
    private int currentRow;
    private int numOfSheet;
    
    // Posicion de los bordes de las celdas
    public final static jxl.format.Border BORDER_NONE = jxl.format.Border.NONE;
    public final static jxl.format.Border BORDER_ALL = jxl.format.Border.ALL;
    public final static jxl.format.Border BORDER_TOP = jxl.format.Border.TOP;
    public final static jxl.format.Border BORDER_BOTTOM =
        jxl.format.Border.BOTTOM;
    public final static jxl.format.Border BORDER_LEFT = jxl.format.Border.LEFT;
    public final static jxl.format.Border BORDER_RIGHT =
        jxl.format.Border.RIGHT;
    
    // Tipo de bordes de las celdas
    public final static jxl.format.BorderLineStyle BORDERSTYLE_NONE =
        jxl.format.BorderLineStyle.NONE;
    public final static jxl.format.BorderLineStyle BORDERSTYLE_THIN =
        jxl.format.BorderLineStyle.THIN;
    public final static jxl.format.BorderLineStyle BORDERSTYLE_THICK =
        jxl.format.BorderLineStyle.THICK;
    public final static jxl.format.BorderLineStyle BORDERSTYLE_DOUBLE =
        jxl.format.BorderLineStyle.DOUBLE;
    
    // Orientacion de la pagina
    public final static jxl.format.PageOrientation LANDSCAPE =
        jxl.format.PageOrientation.LANDSCAPE;
    public final static jxl.format.PageOrientation PORTRAIT =
        jxl.format.PageOrientation.PORTRAIT;
    
    // Alineacion de las celdas
    public final static jxl.format.Alignment CELLALIGN_CENTRE =
        //jxl.write.Alignment.CENTRE;
        jxl.format.Alignment.CENTRE;
    public final static jxl.format.Alignment CELLALIGN_LEFT =
        //jxl.write.Alignment.LEFT;
        jxl.format.Alignment.LEFT;
    public final static jxl.format.Alignment CELLALIGN_RIGHT =
        //jxl.write.Alignment.RIGHT;
        jxl.format.Alignment.RIGHT;
    
    // Tamano de la pagina
    public final static jxl.format.PaperSize A4 =
        jxl.format.PaperSize.A4;
    public final static jxl.format.PaperSize LETTER =
        jxl.format.PaperSize.LETTER;
    
    private String sheetColTitles[];
    private jxl.format.Alignment sheetColTitlesPos;
    private int sheetColTitlesSize;
    private ArrayList sheetRowValues;
    private jxl.format.Alignment sheetRowValuesPos;
    private int sheetRowValuesSize;
    //private int[] sheetRowValuesBordered;
    private jxl.format.PageOrientation sheetPageOrientation;
    private jxl.format.PaperSize sheetPageSize;
    
    //Agregado el 21/09/06
    private int pagingSize;
    private String query;
    private Database db;
    
  /*
   * M�todo main de prueba
   */
    public static void main(String args[]) {
        
        try {
            
            Excel excel = new Excel();
            excel.setExcelFile(new FileOutputStream("prueba.xls"));
            
            String colsTitles[] = { "PSCONTRACT" };
            
            Database db = new Database();
            db.setConnectionType("jdbc");
            db.setDriver("oracle.jdbc.driver.OracleDriver");
            db.setUrl("jdbc:oracle:thin:@localhost:1521:WELLDB");
            db.setUserName("AFI");
            db.setPassword("password");
            db.setQuerySelect("SELECT PSCONTRACT FROM AFI.SFEHVAL WHERE ROWNUM < 150001");
            excel.setDB(db);
            excel.setPagingSize(20000);
            
            //Se crea una nueva hoja de excel
            excel.newSheet();
            excel.setDBSheetColsTitles(colsTitles);
            excel.setDBSheetColTitlesSize(8);
            excel.setDBSheetRowValuesSize(8);
            excel.addDBSheetData();
            
            excel.closeFile();
        } catch(Exception e) {
            
            System.out.println( e.getMessage() );
        }
    }
}
