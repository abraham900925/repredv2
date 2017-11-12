/*###############################################################################
# Nombre del Programa :  ISBNSearch.java                                        #
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

import com.prosa.exceptions.WellException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;


/**
 * <p>Titulo: </p>
 * <p>Descripcion: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Compania: Wellcom</p>
 * @author Armando F. Ibarra
 */
public class ISBNSearch {

  //Constantes
  private static final int SM_ISBNDB = 1;
  private static final int SM_Amazon = 2;
  private static final int SM_AmazonUK = 3;
  private static final int SM_Cuspide = 4;
  private static final int SM_ALL = 5;

  //Campos
  private String urlFile;
  private URL url;
  private String ISBN;
  //Para ser utilizadas en un futuro en la opcion ISBNDB
  private String searchType;
  private String searchValue;
  private int searchEngine;

  private void setURLFile(String value) throws WellException {

    if (value.length() <= 0) {
      throw new WellException("setURLFile");
    }

    this.urlFile = value;
  }

  public String getURLFile() {

    return this.urlFile;
  }

  public void setSearchType(String value) throws WellException {

    if (value.length() <= 0) {
      throw new WellException("setSearchType");
    }

    this.searchType = value;
  }

  public void setSearchValue(String value) throws WellException {

    if (value.length() <= 0) {
      throw new WellException("setSearchValue");
    }

    this.searchValue = value;
  }

  public void setEngineValue(int value) throws WellException {

    this.searchEngine = value;
  }

  public void setISBN(String value) throws WellException {

    if (value.length() <= 0) {
      throw new WellException("setISBN");
    }

    this.ISBN = value;
  }

  public ArrayList readISBN(String value) throws WellException {

    ArrayList isbnData = null;
    String url = null;

    switch (this.searchEngine) {

      case 1:
        url =
          "http://isbndb.com/api/books.xml?access_key=G5SODULQ"
          + "&index1=isbn&value1=" + value;
        this.urlFile = url;
        isbnData = this.readURL();
        isbnData = this.isbnTitleAuthorPublisher(isbnData, 1);
        break;

      case 2:
        url =
          "http://www.amazon.com/exec/obidos/ISBN=" + value;
        this.urlFile = url;
        isbnData = this.readURL();
        isbnData = this.isbnTitleAuthorPublisher(isbnData, 2);
        break;

      case 3:
        url =
          "http://www.cuspide.com/detalle_libro.php/" + value;
        this.urlFile = url;
        isbnData = this.readURL();
        isbnData = this.isbnTitleAuthorPublisher(isbnData, 3);
        break;

      case 4:
        url =
          "http://www.amazon.co.uk/exec/obidos/ASIN/" + value;
        this.urlFile = url;
        isbnData = this.readURL();
        break;

      case 5:
        isbnData = new ArrayList(1);
        isbnData.add("Nothing implemented yet.");
        break;

      case -1:
        isbnData = new ArrayList(1);
        isbnData.add("Opci�n no v�lida.");
        break;
    }

    return isbnData;
  }

  private ArrayList isbnTitleAuthorPublisher(ArrayList al, int se) throws
    WellException {

    final String isbndb[] = {
      "<Title>",
      "<AuthorsText>",
      "<PublisherText"};

    final String amazon[] = {
      "<div class=\"buying\"><b class=\"sans\">",
      "<a href=\"/exec/obidos/search-handle-url/index=books&field-author-exact=",
      "<li><b>Publisher:</b>"};

    final String cuspide[] = {
      "<tr> <td class=\"titulibro\">",
      "Autor ",
      "Editorial "};

    final String headers[] = {
      "Titulo:",
      "Autor:",
      "Editorial:"};

    ArrayList result = null;
    Iterator it = null;
    Iterator it2 = null;
    Hashtable ht = null;
    String as[] = null;
    String ln = null;
    String tae = null;
    int found = -1;
    boolean foundb = false;
    StringTokenizer st = null;
    String tmp = null;

    ht = new Hashtable(3);
    ht.put("ISBNDB", isbndb);
    ht.put("Amazon", amazon);
    ht.put("Cuspide", cuspide);

    // Iterador para recorrer la consulta
    it = al.iterator();
    result = new ArrayList(3);

    switch (se) {

      case 1:
        tmp = "";

        as = (String[]) ht.get("ISBNDB");
        for (int i = 0; i < 3; i++) {
          tae = as[i];
          do {
            ln = (String) it.next();
            found = ln.indexOf(tae);
          }
          while ((found == -1) && (it.hasNext()));
          it = null;
          it = al.iterator();

          ln = ln.replaceAll(Pattern.compile("</.*").pattern(), "");
          st = new StringTokenizer(ln, ">");

          while (st.hasMoreTokens()) {

            ln = (String) st.nextToken();
            found = ln.indexOf("<");
            if (found == -1) {
              //result.add( headers[ i ] + ln );
              tmp = tmp + " " + ln;
            }
          }
          //result.add( headers[ i ] + tmp );
          result.add(tmp);
          tmp = "";
        }
        break;

      case 2:
        tmp = "";

        as = (String[]) ht.get("Amazon");
        for (int i = 0; i < 3; i++) {
          tae = as[i];
          do {
            ln = (String) it.next();
            found = ln.indexOf(tae);
          }
          while ( (found == -1) && (it.hasNext()));
          it = null;
          it = al.iterator();

          if (i < 2) {
            ln = ln.replaceAll(Pattern.compile("</.").pattern(), "");
          }
          else {
            ln = ln.replaceAll(Pattern.compile("</li.").pattern(),
                               "");
          }
          st = new StringTokenizer(ln, ">");

          while (st.hasMoreTokens()) {

            ln = (String) st.nextToken();
            if ( (ln.indexOf("<")) == -1 &&
                (ln.indexOf("by")) == -1) {
              tmp = tmp + " " + ln;
            }
          }
          //result.add( headers[ i ] + tmp );
          result.add(tmp);
          tmp = "";
        }

        break;

      case 3:

        String patterns[] = {
          "</td",
          "Autor |<br>",
          "Editorial |<br>"
        };

        tmp = "";

        as = (String[]) ht.get("Cuspide");
        for (int i = 0; i < 3; i++) {
          tae = as[i];
          do {
            ln = (String) it.next();
            //found = ln.indexOf( tae );
            foundb = ln.startsWith(tae);
          }
          while (!foundb && it.hasNext());
          it = null;
          it = al.iterator();

          /*if( i < 2 )
           ln = ln.replaceAll( Pattern.compile( "</." ).pattern(), "" );
                                   else
           ln = ln.replaceAll( Pattern.compile( "</li." ).pattern(), "" );*/
          ln = ln.replaceAll(Pattern.compile(patterns[i]).pattern(),
                             "");

          st = new StringTokenizer(ln, ">");

          while (st.hasMoreTokens()) {

            ln = (String) st.nextToken();
            if ( (ln.indexOf("<")) == -1 &&
                (ln.indexOf(", <")) == -1) {
              tmp = tmp + " " + ln;
            }
          }
          //result.add( headers[ i ] + tmp );
          result.add(tmp);
          tmp = "";
        }

        break;
    }

    return result;
  }

  private ArrayList readURL() /*throws WellException*/ {

    File f = null;
    URI u = null;
    ArrayList urlLines = null;
    InputStream is = null;
    BufferedReader br = null;
    String line = null;

    try {
      urlLines = new ArrayList();

      this.url = new URL(this.urlFile);
      is = this.url.openStream();
      br = new BufferedReader( new InputStreamReader( is ) );

      while ( ( line = br.readLine() ) != null ) {

        urlLines.add( line );
      }
    }
    catch (Exception e) {
      urlLines.add("");
    }

    return urlLines;
  }

  public ISBNSearch() {

    this.urlFile = null;
    this.url = null;
    this.ISBN = null;
    this.searchType = null;
    this.searchValue = null;
    this.searchEngine = -1;
  }

  public static void main(String[] args) {

    try {

      ISBNSearch isbnSearch = new ISBNSearch();
      //isbnSearch.setISBN( "8495618605" );
      isbnSearch.setEngineValue(3);
      System.out.println(isbnSearch.readISBN("8495618605").toString());
    }
    catch (Exception e) {
      System.out.println(e.toString());
      //e.printStackTrace( System.out );
    }
  }
}
