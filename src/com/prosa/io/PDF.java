/*###############################################################################
# Nombre del Programa :  PDF.java                                               #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Creacion de archivos PDF        			            #
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
# Modificaci�n        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.io;

import java.io.*;
import java.util.*;

import java.awt.image.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.prosa.exceptions.WellException;
import com.prosa.sql.Database;

/**
 *
 * <p>T�tulo: Clase PDF</p>
 * <p>Descripci�n: Permite la creaci�n de archivos PDF</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compa��a: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public class PDF
  implements Serializable {

  public PDF() {

    this.pdfFile = null;

    this.pageSize = "Letter";
    this.pageOrientation = "P";
    this.pageAuthor = "Wellcom";
    this.pageTitle = "Reporte";
    this.hasPageTitle = false;

    this.pageHeader = "Encabezado";
    this.pageFooter = "";
    this.hasPageHeader = false;
    this.hasPageFooter = false;
    this.hasHeaderBorder = true;
    this.hasFooterBorder = true;
    this.hasPageNumeration = false;

    this.pageImageAlignment = "L";

    this.table = null;
    this.tableNumberOfCols = 1;
    this.tableColTitles = null;
    this.tableColTitlesPos = "C";
    this.tableColTitleSize = 10;
    this.tableColTextSize = 10;
    this.tableRowValues = null;
    this.isGrayedTable = false;

    this.tableBorderWidth = 1;
    this.rtb = null;
    this.borderType = 0;

    this.pagingSize = 0;
    this.query = null;
    this.db = null;
  }

  /**
   * Establece el n�mero de columnas de la tabla
   * @param value entero que especifica el n�mero de filas de la tabla
   */
  public void setTableNumberOfCols(int value) {

    if (value > 0) {
      this.tableNumberOfCols = value;
    }
    else {
      this.tableNumberOfCols = 1;
    }
  }

  /**
   * Establece los t�tulos de las columnas de la tabla
   * @param value conjunto de cadenas utilizada para establecer los titulos
   * de las columnas de la tabla
   */
  public void setTableColsTitles(String value[]) {

    this.tableColTitles = value;
  }

  /**
   * Establece la posici�n de los t�tulos de las columnas de la tabla
   * @param value cadena que especifica la posici�n de los t�tulos de
   * las columnas de la tabla<br><br>
   * <b>Posici�n de los t�tulos:</b><br><br>
   * 1. "L" - Left<br>
   * 2. "C" - Center<br>
   * 3. "R" - Right<br><br>
   * <b>Ejemplo:</b>
   * setColTitlesPos( "C" )
   */
  public void setTableColTitlesPos(String value) {

    if (value.length() > 0) {
      this.tableColTitlesPos = value;
    }
    else {
      this.tableColTitlesPos = "C";
    }
  }

  /**
   * Establece el tama�o de la fuente para los t�tulos de las columnas
   * @param value entero que especif�ca el tama�o de la fuente para
   * los t�tulos de las columnas
   */
  public void setTableColTitleSize(int value) {

    if (value > 0) {
      this.tableColTitleSize = value;
    }
    else {
      this.tableColTitleSize = 10;
    }
  }

  /**
   * Establece el tama�o de la fuente para el texto de las columnas
   * @param value entero que especifica el tama�o de la fuente para
   * el texto de las columnas
   */
  public void setTableColTextSize(int value) {

    if (value > 0) {
      this.tableColTextSize = value;
    }
    else {
      this.tableColTextSize = 10;
    }
  }

  /**
   * Establece los valores que contendr�n las filas de la tabla
   * @param value tipo de dato <em>ArrayList</em> que contiene una conjunto
   * de objetos del tipo <em>String[]</em>, donde cada uno de �stos arryas de
   * tipo <em>String</em> representan los valores de una de las filas de
   * la tabla
   */
  public void setTableRowValues(ArrayList value) {

    if (value != null) {
      this.tableRowValues = value;
    }
    else {
      String values[] = new String[1];
      this.tableRowValues = new ArrayList(1);
      values[0] = "value";
      this.tableRowValues.add(values);
    }
  }

  /**
   * Agrega una tabla al documento
   * @throws WellException
   */
  public void addPageTable() throws WellException {

    if ( (this.document != null) && (this.tableRowValues != null)) {

      try {
        this.prepareTable();
        this.document.add(this.table);
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.addPageTable: " +
                                ex.toString());
      }
    }
  }

  /**
   * Agrega los datos obtenidos de la consulta a la base datos al documento
   * @throws WellException
   */
  public void addDBPageTable() throws WellException {

    int numRows = 0;
    int repeat = 0;
    int restRows = 0;

    /*if ( (this.document != null) && (this.tableRowValues != null)
         && (this.db != null)) {*/
    if ( (this.document != null) && (this.db != null)) {

      try {
        //this.db.setQuerySelect( this.query );
        this.db.executeQuerySelect();
        // Cuantas veces se obtendran datos?
        numRows = this.db.getNumRowsRS();
        restRows = numRows % this.pagingSize;
        repeat = numRows / this.pagingSize;
        if( restRows > 0 ) {
          repeat += 1;
        }
        //this.setDBColsTitles();
        // Obtener registros "repeat" veces
        for( int i = 0;i < repeat;i++ ) {

          if(i < repeat) {
            this.setTableRowValues(this.db.getNextResultSetData(
              this.pagingSize));
          }
          else if((i >= repeat) && (restRows > 0)) {
            this.setTableRowValues(this.db.getNextResultSetData(restRows));
          }
          //this.addPageTable();
          this.setDBRowValues();
          //this.document.add(this.table);
        }
        this.document.add(this.table);
      }
      catch( Exception ex ) {
        throw new WellException("com.io.PDF.addDBPageTable:  "
                                + ex.toString());
      }
    }
  }

  /**
   * Establece si las filas de la tabla se ir�n intercalando una gris,
   * una blanca
   * @param value booleano que especifica si las filas de la tabla se ir�n
   * intercalando una gris, una blanca
   */
  public void setIsGrayedTable(boolean value) {

    this.isGrayedTable = value;
  }

  /**
   * Establece el tama�o de la p�gina
   * @param value cadena que especifica el tama�o de la p�gina
   * <br><br><b>Tama�os de p�gina:</b><br><br>
   * 1. Letter<br>
   * 2. A4<br><br>
   * <b>Ejemplo:</b><br><br>
   * setPageSize( "A4" );
   */
  public void setPageSize(String value) {

    if (value.length() > 0) {
      this.pageSize = value;
    }
    else {
      this.pageSize = "Letter";
    }
  }

  /**
   * Establece la orientaci�n de la p�gina
   * @param value cadena que especifica la orientaci�n de la p�gina
   * <br><br><b>Orientaci�n de p�gina:</b><br><br>
   * 1. P - Portrait<br>
   * 2. L - Landscape<br><br>
   * <b>Ejemplo:</b><br><br>
   * setPageOrientation( "L" );
   */
  public void setPageOrientation(String value) {

    if (value.length() > 0) {
      this.pageOrientation = value;
    }
    else {
      this.pageOrientation = "P";
    }
  }

  /**
   * Establece el autor del documento
   * @param value cadena que especifica el autor del documento
   */
  public void setPageAuthor(String value) {

    if (value.length() > 0) {
      this.pageAuthor = value;
    }
    else {
      this.pageAuthor = "Wellcom, S.A. de C.V.";
    }
  }

  /**
   * Establece el t�tulo del documento
   * @param value cadena que especifica el t�tulo del documento
   */
  public void setPageTitle(String value) {

    if (value.length() > 0) {
      this.pageTitle = value;
    }
    else {
      this.pageTitle = "T�tulo";
    }
  }

  /**
   * Establece si la p�gina tendr� un titulo o no
   * @param value booleano qu especifica si la p�gina tendr� un t�tulo o no
   */
  public void setHasPageTitle(boolean value) {

    this.hasPageTitle = value;
  }

  /**
   * Establece el encabezado de las p�ginas del documento
   * @param value cadena que especifica el encabezado de las p�ginas del
   * documento
   */
  public void setPageHeader(String value) {

    if (value.length() > 0) {
      this.pageHeader = value;
    }
    else {
      this.pageHeader = "Encabezado";
    }
  }

  /**
   * Establece si la p�gina contendr� un encabezado
   * @param value booleano que especifica si la p�gina contendr� un encabezado
   */
  public void setHasPageHeader(boolean value) {

    this.hasPageHeader = value;
  }

  /**
   * Establece si el encabezado de la p�gina estar� limitado por
   * un par de l�neas horizontales
   * @param value booleano que establece si el encabezado de la p�gina estar�
   * limitado por un par de l�neas horizontales
   */
  public void setHasHeaderBorder(boolean value) {

    this.hasHeaderBorder = value;
  }

  /**
   * Establece el pie de pagina de las p�ginas del documento
   * @param value cadena que especifica el pie de p�gina de las p�ginas del
   * documento
   */
  public void setPageFooter(String value) {

    if ( (value.length() > 0) && this.hasPageNumeration) {
      this.pageFooter = "\t\t\t\t\t|\t\t\t\t\t" + value;
    }
    else if ( (value.length() > 0) && !this.hasPageNumeration) {
      this.pageFooter = value;
    }
    else {
      this.pageFooter = "Pie de p�gina";
    }
  }

  /**
   * Establece si la pagina contendra un pie de pagina
   * @param value booleano que establece si la pagina tendra un pie de pagina
   */
  public void setHasPageFooter(boolean value) {

    this.hasPageFooter = value;
  }

  /**
   * Establece si el pie de p�gina estar� limitado por
   * un par de l�neas horizontales
   * @param value booleano que establece si el pie de p�gina estar�
   * limitado por un par de l�neas horizontales
   */
  public void setHasFooterBorder(boolean value) {

    this.hasFooterBorder = value;
  }

  /**
   * Establece si aparecera el numero de pagina en el pie de pagina
   * @param value booleano que especifica si aparecera el numero de pagina
   * en el pie de pagina
   */
  public void setHasPageNumeration(boolean value) {

    this.hasPageNumeration = value;
    if ( (this.hasPageNumeration) && this.hasPageFooter) {
      String tmpStr = this.pageFooter;
      this.pageFooter = "\t\t\t\t\t|\t\t\t\t\t" + tmpStr;
    }
    else if ( (this.hasPageNumeration) && !this.hasPageFooter) {
      this.hasPageFooter = true;
    }
  }

  /**
   * Establece el flujo de salida donde se crear� el documento PDF
   * @param value flujo de salida donde se crear� el documento PDF
   */
  public void setPdfFile(OutputStream value) {

    if (value != null) {
      this.pdfFile = value;
    }
  }

  /**
   * Agrega lineas de texto al texto del documento
   * @param value cadena representado una linea de texto para ser agregada al
   * texto del documento
   * @throws WellException
   */
  public void addPageText(String value) throws WellException {

    if ( (this.document != null) && (value.length() > 0)) {

      try {
        this.document.add(new Paragraph(value));
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.addPageText: " +
                                ex.toString());
      }
    }
  }

  /**
   * Agrega una im�gen al documento
   * @param value tipo de dato <em>java.awt.image.BufferedImage</em> que
   * contiene a la im�gen que se desea agregar al documento
   * @throws WellException
   */
  public void addPageImage(BufferedImage value) throws WellException {

    if ( (this.document != null) && (value != null)) {

      try {
        Image img = Image.getInstance(value, null, false);

        if (this.pageImageAlignment.equals("L")) {
          img.setAlignment(Image.LEFT);
        }
        else if (this.pageImageAlignment.equals("C")) {
          img.setAlignment(Image.MIDDLE);
        }
        else if (this.pageImageAlignment.equals("R")) {
          img.setAlignment(Image.RIGHT);
        }
        else {
          img.setAlignment(Image.LEFT);
        }

        this.document.add(img);
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.addPageImage: " +
                                ex.toString());
      }
    }
  }

  /**
   * Agrega una imagen al documento
   * @param value tipo de dato <em>com.lowagie.text.Image</em> que
   * contiene a la imagen que se desea agregar al documento
   * @param ia Alineacion de la imagen
   * <br><br>
   * 1. IA_LEFT - Left <br>
   * 2. IA_MIDDLE - Middle <br>
   * 3. IA_RIGHT - Right
   * @throws WellException
   */
  public void addPageImage(Image value, int ia) throws WellException {

    if ( (this.document != null) && (value != null)) {

      if ( (ia < 0) || (ia > 2)) {
        ia = 0;
      }

      try {

        value.setAlignment(ia);
        this.document.add(value);
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.addPageImage: " +
                                ex.toString());
      }
    }
  }

  /**
   * Agrega una im�gen al documento
   * @param value tipo de dato <em>java.awt.image.BufferedImage</em> que
   * contiene a la im�gen que se desea agregar al documento
   * @param ia Alineacion de la imagen
   * <br><br>
   * 1. IA_LEFT - Left <br>
   * 2. IA_MIDDLE - Middle <br>
   * 3. IA_RIGHT - Right
   * @throws WellException
   */
  public void addPageImage(BufferedImage value, int ia) throws WellException {

    if ( (this.document != null) && (value != null)) {

      if ( (ia < 0) || (ia > 2)) {
        ia = 0;
      }

      try {
        Image img = Image.getInstance(value, null, false);

        img.setAlignment(ia);

        this.document.add(img);
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.addPageImage: " +
                                ex.toString());
      }
    }
  }

  /**
   * Establece la posici�n en el documento de la im�gen que se desea agregar
   * @param value cadena que representa la posici�n en el documento
   * de la im�gen que se desea agregar<br>
   * <br><b>Posiciones: </b><br><br>
   * 1. "L" - Left
   * 2. "C" - Center
   * 3. "R" - Right
   * @deprecated Utilizar addPageImage( BufferedImage value, int pa )
   */
  public void setPageImageAlignment(String value) {

    if (value.length() > 0) {
      this.pageImageAlignment = value;
    }
    else {
      this.pageImageAlignment = "L";
    }
  }

  /**
   * Abre el documento para poder insertar datos ( texto e im�genes )
   * @throws WellException
   */
  public void openDocument() throws WellException {

    try {
      if (this.document != null) {
        this.document.open();
      }
      else {

        this.preparePDFFile();
        this.document.open();
      }
    }
    catch (Exception ex) {
      throw new WellException("com.io.PDF.openDocument(): "
                              + ex.toString());
    }
  }

  /**
   * Cierra el documento
   */
  public void closeDocument() {

    if (this.document != null) {
      this.document.close();
    }
  }

  /**
   * Agrega una nueva p�gina al documento
   * @throws WellException
   */
  public void newPage() throws WellException {

    if (this.document != null) {

      try {
        this.document.newPage();
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.newPage: " +
                                ex.toString());
      }
    }
  }

  /**
   * Inserta una linea en blanco al documento
   */
  public void insertBlankRow() {

    try {
      this.addPageText(" ");
    }
    catch (Exception e) {
      System.out.println("com.io.PDF.insertBlankRow "
                         + e.toString());
    }
  }

  /**
   * Establece el ancho del borde de las filas
   * @param value int Ancho del borde de las filas
   */
  public void setTableBorderWidth(int value) {

    if (value < 0) {
      value = 0;
    }

    this.tableBorderWidth = value;
  }

  /**
   * Establece que filas llevaran borde
   * @param value int[] Array de enteros que establece el numero de filas
   * que llevaran borde
   * @throws WellException
   */
  //public void setRowsWithBorder(int[] value) throws WellException {
  public void setRowsWithBorder(int[] value) {

    /*if (value == null) {
      throw new WellException("com.io.PDF.setRowsWithBorder: "
                              + "Argumento null");
    }*/

    this.rtb = value;
  }

  /**
   * Establece el tipo de borde de las filas
   * @param value int Tipo de borde de las filas
   * <br><br>
   * - SIMPLE_BORDER
   * - DOUBLE_BORDER
   */
  public void setRowBorderType(int value) {

    if (value < 0) {
      value = 0;
    }

    this.borderType = value;
  }

  public void setPagingSize( int value ) {

    if( value <= 0 ) {
      this.pagingSize = 1;
    }
    else {
      this.pagingSize = value;
    }
  }

  public void setQuery( String value ) throws WellException {

    if(value == null) {
      throw new WellException( "com.io.PDF.setQuery: "
                               + "Parametro nulo." );
    }
    else {
      this.query = value;
    }
  }
  
  /*
   * Agregado el 07/Mar/2007 por AFI
   */
  public String getQuery() {
      return this.query;
  }

  public void setDB( Database value ) throws WellException {

    if(value == null) {
      throw new WellException( "com.io.PDF.setDB: "
                               + "Parametro nul." );
    }
    else {
      this.db = value;
    }
  }

  /*
   * M�todos de utileria
   */

  /**
   * Prepara el archivo PDF en base a los par�metros establecidos en sus
   * propiedades
   * @throws WellException
   */
  private void preparePDFFile() throws WellException {

    if (this.pdfFile != null) {

      HeaderFooter header = null;
      HeaderFooter footer = null;

      /*
       * Tama�o y orientaci�n de la p�gina
       */
      if (this.pageSize.equals("Letter")) {
        if (this.pageOrientation.equals("L")) {
          document = new Document(PageSize.LETTER.rotate());
        }
        else {
          document = new Document(PageSize.LETTER);
        }
      }
      else if (this.pageSize.equals("A4")) {
        if (this.pageOrientation.equals("L")) {
          document = new Document(PageSize.A4.rotate());
        }
        else {
          document = new Document(PageSize.A4);
        }
      }
      else {
        if (this.pageOrientation.equals("L")) {
          document = new Document(PageSize.LETTER.rotate());
        }
        else {
          document = new Document(PageSize.LETTER);
        }
      }

      try {
        PdfWriter.getInstance(document, this.pdfFile);

        /*
         * Autor y t�tulo del documento
         */
        document.addAuthor(this.pageAuthor);
        if (this.hasPageTitle) {
          document.addTitle(this.pageTitle);
        }

        /*
         * Encabezados y pie de p�gina
         */
        if (this.hasPageHeader) {

          header =
            new HeaderFooter(
              new Phrase(this.pageHeader), false);

          if (!this.hasHeaderBorder) {
            header.setBorder(Rectangle.NO_BORDER);
          }

          document.setHeader(header);
        }

        if (this.hasPageFooter) {

          if (!this.hasPageNumeration) {
            footer =
              new HeaderFooter(new Phrase(this.pageFooter),
                               false);
          }
          else {
            footer =
              new HeaderFooter(new Phrase("P�gina: "),
                               new Phrase(this.pageFooter));
          }

          if (!this.hasFooterBorder) {
            footer.setBorder(Rectangle.NO_BORDER);
          }

          document.setFooter(footer);
        }
      }
      catch (Exception ex) {
        throw new WellException("com.io.PDF.preparePDFFile: " +
                                ex.toString());
      }
    }
  }

  /**
   * Prepara una tabla en base a los parametros establecidos en sus
   * propiedades
   * @throws WellException
   */
  private void prepareTable() throws WellException {

    int i, j = 0;
    int rowNumber = 0;
    PdfPCell cell = null;
    Phrase phrase = null;
    Chunk chunk = null;

    try {
      this.table = new PdfPTable(this.tableNumberOfCols);

      /*
       * N�mero y ancho de las columnas
       */
      int colTitlesWidth[] = new int[this.tableNumberOfCols];
      for (i = 0; i < this.tableNumberOfCols; i++) {
        colTitlesWidth[i] = 100 / this.tableNumberOfCols;
      }
      this.table.setWidths(colTitlesWidth);
      this.table.setWidthPercentage(100);

      /*
       * T�tulos de las columnas
       */
      if (this.tableColTitles != null) {

        for (i = 0; i < this.tableNumberOfCols; i++) {

          phrase =
            new Phrase(this.tableColTitles[i],
                       FontFactory.getFont(
                         FontFactory.TIMES_BOLD,
                         this.tableColTitleSize));

          cell = new PdfPCell(phrase);
          cell.setBorderWidth(this.tableBorderWidth);
          cell.setGrayFill(0.7f);

          if (this.tableColTitlesPos.equals("L")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_LEFT);
          }
          else if (this.tableColTitlesPos.equals("C")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_CENTER);
          }
          else if (this.tableColTitlesPos.equals("R")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_RIGHT);
          }
          else {
            cell.setHorizontalAlignment(
              Element.ALIGN_LEFT);
          }

          this.table.addCell(cell);
        }

        this.table.setHeaderRows(1);
      }

      /*
       * Datos de la tabla
       */

      /*
       * Se obtiene el n�mero de filas y sus valores
       */
      Iterator it = this.tableRowValues.iterator();
      String rvalues[] = null;
      j = 1;

      //Se especificaron filas para ponerles un borde?
      if (this.rtb != null) {
        Arrays.sort(this.rtb);
      }

      while (it.hasNext()) {

        rowNumber++;

        rvalues = (String[]) it.next();
        for (i = 0; i < rvalues.length; i++) {

          //Se especificaron filas para ponerles un borde?
          if (this.rtb != null) {

            if (Arrays.binarySearch(this.rtb, rowNumber) >= 0) {

              chunk =
                new Chunk(rvalues[i],
                          new Font(
                            Font.TIMES_ROMAN,
                            this.tableColTextSize));
              chunk.setUnderline(0.1f, -2f);

              if (this.borderType == DOUBLE_BORDER) {
                chunk.setUnderline(0.1f, -4f);
              }
              phrase = new Phrase(chunk);
            }
            else {
              phrase =
                new Phrase(rvalues[i],
                           FontFactory.getFont(
                             FontFactory.TIMES,
                             this.tableColTextSize)
                );
            }
          }
          else {
            phrase =
              new Phrase(rvalues[i],
                         FontFactory.getFont(
                           FontFactory.TIMES,
                           this.tableColTextSize));
          }

          cell = new PdfPCell(phrase);
          cell.setBorderWidth(this.tableBorderWidth);

          if (this.isGrayedTable) {
            if (j % 2 == 1) {
              cell.setGrayFill(0.9f);
            }
            else {
              cell.setGrayFill(0.7f);
            }
          }

          this.table.addCell(cell);

          chunk = null;
          phrase = null;
        }

        j++;
      }
    }
    catch (Exception ex) {
      throw new WellException("com.io.PDF.prepareTable: " +
                              ex.toString());
    }
  }

  public void setDBColsTitles( String[] value ) throws WellException {

    int i, j = 0;
    PdfPCell cell = null;
    Phrase phrase = null;

    try {
      this.table = new PdfPTable(this.tableNumberOfCols);

      /*
       * N�mero y ancho de las columnas
       */
      int colTitlesWidth[] = new int[this.tableNumberOfCols];
      for (i = 0; i < this.tableNumberOfCols; i++) {
        colTitlesWidth[i] = 100 / this.tableNumberOfCols;
      }
      this.table.setWidths(colTitlesWidth);
      this.table.setWidthPercentage(100);

      this.tableColTitles = value;

      /*
       * T�tulos de las columnas
       */
      if (this.tableColTitles != null) {

        for (i = 0; i < this.tableNumberOfCols; i++) {

          phrase =
            new Phrase(this.tableColTitles[i],
                       FontFactory.getFont(
                         FontFactory.TIMES_BOLD,
                         this.tableColTitleSize));

          cell = new PdfPCell(phrase);
          cell.setBorderWidth(this.tableBorderWidth);
          cell.setGrayFill(0.7f);

          if (this.tableColTitlesPos.equals("L")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_LEFT);
          }
          else if (this.tableColTitlesPos.equals("C")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_CENTER);
          }
          else if (this.tableColTitlesPos.equals("R")) {
            cell.setHorizontalAlignment(
              Element.ALIGN_RIGHT);
          }
          else {
            cell.setHorizontalAlignment(
              Element.ALIGN_LEFT);
          }

          this.table.addCell(cell);
        }

        this.table.setHeaderRows(1);
      }
    }
    catch (Exception ex) {
      throw new WellException("com.io.PDF.setDBColsTitles: " +
                              ex.toString());
    }
  }

  private void setDBRowValues() throws WellException {
    int i, j = 0;
    int rowNumber = 0;
    PdfPCell cell = null;
    Phrase phrase = null;
    Chunk chunk = null;


    try {
      /*
       * Datos de la tabla
       */

      /*
       * Se obtiene el n�mero de filas y sus valores
       */
      Iterator it = this.tableRowValues.iterator();
      String rvalues[] = null;
      j = 1;

      //Se especificaron filas para ponerles un borde?
      if (this.rtb != null) {
        Arrays.sort(this.rtb);
      }

      while (it.hasNext()) {

        rowNumber++;

        rvalues = (String[]) it.next();
        for (i = 0; i < rvalues.length; i++) {

          //Se especificaron filas para ponerles un borde?
          if (this.rtb != null) {

            if (Arrays.binarySearch(this.rtb, rowNumber) >= 0) {

              chunk =
                new Chunk(rvalues[i],
                          new Font(
                            Font.TIMES_ROMAN,
                            this.tableColTextSize));
              chunk.setUnderline(0.1f, -2f);

              if (this.borderType == DOUBLE_BORDER) {
                chunk.setUnderline(0.1f, -4f);
              }
              phrase = new Phrase(chunk);
            }
            else {
              phrase =
                new Phrase(rvalues[i],
                           FontFactory.getFont(
                             FontFactory.TIMES,
                             this.tableColTextSize)
                );
            }
          }
          else {
            phrase =
              new Phrase(rvalues[i],
                         FontFactory.getFont(
                           FontFactory.TIMES,
                           this.tableColTextSize));
          }

          cell = new PdfPCell(phrase);
          cell.setBorderWidth(this.tableBorderWidth);

          if (this.isGrayedTable) {
            if (j % 2 == 1) {
              cell.setGrayFill(0.9f);
            }
            else {
              cell.setGrayFill(0.7f);
            }
          }

          this.table.addCell(cell);

          chunk = null;
          phrase = null;
        }

        j++;
      }
    }
    catch (Exception ex) {
      throw new WellException("com.io.PDF.setDBRowValues: " +
                              ex.toString());
    }
  }

  /*
   * Campos
   */
  private Document document;
  private OutputStream pdfFile;

  private String pageSize;
  private String pageOrientation;
  private String pageAuthor;
  private String pageTitle;
  private boolean hasPageTitle;

  private String pageHeader;
  private String pageFooter;
  private boolean hasPageHeader;
  private boolean hasPageFooter;
  private boolean hasHeaderBorder;
  private boolean hasFooterBorder;
  private boolean hasPageNumeration;

  private String pageImageAlignment;

  private PdfPTable table;
  private int tableNumberOfCols;
  private String tableColTitles[];
  private String tableColTitlesPos;
  private int tableColTitleSize;
  private int tableColTextSize;
  private ArrayList tableRowValues;
  private boolean isGrayedTable;

  // Added 22/05/05
  public static final int IA_LEFT = Image.LEFT;
  public static final int IA_MIDDLE = Image.MIDDLE;
  public static final int IA_RIGHT = Image.RIGHT;

  private int tableBorderWidth;
  private int[] rtb;
  public static final int SIMPLE_BORDER = 0;
  public static final int DOUBLE_BORDER = 1;
  private int borderType;

  // Added 13/09/06
  private int pagingSize;
  private String query;
  private Database db;

  /*
   * Funci�n main para prueba.
   */
  public static void main(String args[]) {

    try {
      PDF pdf = new PDF();
      pdf.setPdfFile(new FileOutputStream("prueba_mmi.pdf"));
      pdf.setHasPageHeader(true);
      pdf.setPageHeader("Wellcom");
      pdf.setHasPageNumeration(true);
      pdf.setHasPageFooter(true);
      pdf.setPageFooter("AFI");
      pdf.setPageOrientation("L");
      pdf.setPageSize("A4");
      pdf.setIsGrayedTable(true);
      pdf.openDocument();

      //INSERTA REGISTROS DE UNA BASE DE DATOS
      pdf.newPage();

      String dbColTitles[] = { "NOMBRE", "IMPORTE" };
      pdf.setTableNumberOfCols(2);
      //pdf.setTableColsTitles(dbColTitles);
      pdf.setDBColsTitles(dbColTitles);
      pdf.setTableColTitleSize(10);
      pdf.setTableColTitlesPos("L");
      pdf.setRowsWithBorder(null);

      Database db = new Database();
      db.setConnectionType("jdbc");
      db.setDriver("oracle.jdbc.driver.OracleDriver");
      db.setUrl("jdbc:oracle:thin:@localhost:1521:WELLDB");
      db.setUserName("afibarra");
      db.setPassword("password");
      //db.setQuerySelect("SELECT PSCONTRACT FROM AFI.SFEHVAL WHERE ROWNUM < 10001");
      db.setQuerySelect("SELECT * FROM TBL_TMP_SUBTOTALES");
      pdf.setDB(db);
      pdf.setPagingSize(20000);
      //pdf.setIsGrayedTable(false);
      pdf.addDBPageTable();
      //pdf.addPageTable();

      pdf.closeDocument();
    }
    catch(Exception e) {

      System.out.println( e.getMessage() );
    }
  }
}
