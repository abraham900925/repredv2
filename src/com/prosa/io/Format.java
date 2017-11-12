/*###############################################################################
# Nombre del Programa :  Format.java                                            #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad                   #
#                                                                               #
# Programa Dependiente:                                                         #
# Programa Subsecuente:                                                         #
# Cond. de ejecucion  :  Acceder al sistema                                     #
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
import java.text.*;
import java.util.*;


/**
 *
 * <p>Titulo: Clase Format</p>
 * <p>Descripcion: Contiene metodos para diversos propositos de formateo
 * de variables <em>String</em></p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compania: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public final class Format
  implements Serializable {

  public Format() {

    this.country = "MX";
  }

  /**
   * Reemplaza de una cadena un cierto caracter
   * @param string Cadena en la cual se va a reemplazar el caracter
   * @param oldChar Caracter a reemplazar
   * @param newChar Caracter para reemplazar
   * @return result Cadena con el caracter reemplazado
   */
  public static String replaceChar(String string,
                                   char oldChar, char newChar) {

    String result = string.replace(oldChar, newChar);

    return result;
  }

  /**
   * Remueve los espacios en blanco que contenga una cadena
   * @param value Cadena de la cual se remover�n los espacios en blanco
   * @return result Cadena con los espacios en blanco eliminados
   */
  public static String removeBlanks(String value) {

    String result = value.trim();

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>boolean</em>
   * @return si el argumento es <em>true</em> se regresa una cadena
   * <em>"true"</em>; de otro modo se regresa una cadena <em>"false"</em>
   */
  public static String stringValue(boolean value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>char</em>
   * @return Una cadena de longitud 1 que contiene como su �nico caracter
   * al argumento
   */
  public static String stringValue(char value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>Date</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(Date value) {

    return value.toString();
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato
   * @param value variable del tipo <em>Date</em>
   * @param format cadena que especifica un formato de fecha
   * <br><br><b>Tipos de formato:</b><br><br>
   * 1. dd/mm/yy - 25/12/04<br>
   * 2. dd/mm/yyyy - 25/12/2004<br>
   * 3. mm/dd/yy - 12/25/04<br>
   * 4. mm/dd/yyyy - 12/25/2004<br>
   * 5. dd/mmm/yy - 25/dic/04<br>
   * 6. dd/mmm/yyyy - 25/dic/2004<br>
   * 7. mmm/dd/yy - dic/25/04<br>
   * 8. mmm/dd/yyyy - dic/25/2004<br>
   * 9. dd/mmmm/yy - 25/diciembre/04<br>
   * 10. dd/mmmm/yyyy - 25/diciembre/2004<br>
   * 11. mmmm/dd/yy - diciembre/25/04<br>
   * 12. mmmm/dd/yyyy - diciembre/25/2004<br>
   * <br><b>Ejemplo:</b><br>
   * <br><b>stringValue</b>( new Date(), "mmmm/dd/yyyy" )
   * @return Una cadena representando el argumento con un cierto formato
   */
  public static String stringValue(Date value, String format) {

    String result = "";
    SimpleDateFormat sdf = null;

    if (format.equals("dd/mm/yy")) {

      sdf = new SimpleDateFormat("dd/MM/yy");
      result = sdf.format(value);
    }
    else if (format.equals("dd/mm/yyyy")) {

      sdf = new SimpleDateFormat("dd/MM/yyyy");
      result = sdf.format(value);
    }
    else if (format.equals("mm/dd/yy")) {

      sdf = new SimpleDateFormat("MM/dd/yy");
      result = sdf.format(value);
    }
    else if (format.equals("mm/dd/yyyy")) {

      sdf = new SimpleDateFormat("MM/dd/yyyy");
      result = sdf.format(value);
    }
    else if (format.equals("dd/mmm/yy")) {

      sdf = new SimpleDateFormat("dd/MMM/yy");
      result = sdf.format(value);
    }
    else if (format.equals("dd/mmm/yyyy")) {

      sdf = new SimpleDateFormat("dd/MMM/yyyy");
      result = sdf.format(value);
    }
    else if (format.equals("mmm/dd/yy")) {

      sdf = new SimpleDateFormat("MMM/dd/yy");
      result = sdf.format(value);
    }
    else if (format.equals("mmm/dd/yy")) {

      sdf = new SimpleDateFormat("MMM/dd/yy");
      result = sdf.format(value);
    }
    else if (format.equals("dd/mmmm/yy")) {

      sdf = new SimpleDateFormat("dd/MMMM/yy");
      result = sdf.format(value);
    }
    else if (format.equals("dd/mmmm/yyyy")) {

      sdf = new SimpleDateFormat("dd/MMMM/yyyy");
      result = sdf.format(value);
    }
    else if (format.equals("mmmm/dd/yy")) {

      sdf = new SimpleDateFormat("MMMM/dd/yy");
      result = sdf.format(value);
    }
    else if (format.equals("mmmm/dd/yyyy")) {

      sdf = new SimpleDateFormat("MMMM/dd/yyyy");
      result = sdf.format(value);
    }
    else {

      sdf = new SimpleDateFormat("dd/MM/yy");
      result = sdf.format(value);
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>double</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(double value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>float</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(float value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>int</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(int value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>long</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(long value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento
   * @param value variable del tipo <em>short</em>
   * @return Una cadena representando el argumento
   */
  public static String stringValue(short value) {

    String tmpStr = "";
    String result = tmpStr.valueOf(value);

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>int</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedInt(String value) {

    String result = "";

    if (value.length() > 0) {

      int number = Integer.parseInt(value);
      result = this.formatedInt(number);
    }
    else {
      result = "0";
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>int</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedInt(int value) {

    String result = "";

    if (country.equals("MX")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "MX"));
      result = nf.format(value);
    }
    else if (country.equals("US")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("en", "US"));
      result = nf.format(value);
    }
    else if (country.equals("GB")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("en", "GB"));
      result = nf.format(value);
    }
    else if (country.equals("ES")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "ES"));
      result = nf.format(value);
    }
    else {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "MX"));
      result = nf.format(value);
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>int</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedInt(double value) {

    String result = "";

    if (country.equals("MX")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "MX"));
      result = nf.format(value);
    }
    else if (country.equals("US")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("en", "US"));
      result = nf.format(value);
    }
    else if (country.equals("GB")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("en", "GB"));
      result = nf.format(value);
    }
    else if (country.equals("ES")) {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "ES"));
      result = nf.format(value);
    }
    else {

      NumberFormat nf =
        NumberFormat.getIntegerInstance(new Locale("es", "MX"));
      result = nf.format(value);
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>String</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedCurrency(String value) {

    String result = "";
    double currency = 0;

    if (value.length() > 0) {

      currency = Double.parseDouble(value);
      result = this.formatedCurrency(currency);
    }
    else {
      result = "$0.00";
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>double</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedCurrency(double value) {

    String result = "";

    if (country.equals("MX")) {

      NumberFormat nf =
        NumberFormat.getCurrencyInstance(
          new Locale("es", "MX"));
      result = nf.format(value);
    }
    else if (country.equals("US")) {

      NumberFormat nf =
        NumberFormat.getCurrencyInstance(
          new Locale("en", "US"));
      result = nf.format(value);
    }
    else if (country.equals("GB")) {

      NumberFormat nf =
        NumberFormat.getCurrencyInstance(
          new Locale("en", "GB"));
      result = nf.format(value);
    }
    else if (country.equals("ES")) {

      NumberFormat nf =
        NumberFormat.getCurrencyInstance(
          new Locale("es", "ES"));
      result = nf.format(value);
    }
    else {

      NumberFormat nf =
        NumberFormat.getCurrencyInstance(
          new Locale("es", "MX"));
      result = nf.format(value);
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>double</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedPercent(String value) {

    String result = "";

    if (value.length() > 0) {

      double percent = Double.parseDouble(value);
      result = this.formatedPercent(percent);
    }
    else {
      result = "0.00%";
    }

    return result;
  }

  /**
   * Regresa la representaci�n como cadena del argumento con un cierto
   * formato en base a la configuraci�n del pa�s establecido
   * @param value variable del tipo <em>double</em>
   * <br><br><b>C�digos de pa�s:</b><br><br>
   * 1. MX - M�xico<br>
   * 2. US - Estados Unidos de Am�rica<br>
   * 3. GB - Reino Unido<br>
   * 4. ES - Espa�a
   * @return Una cadena representando el argumento con un cierto formato
   * en base a la configuraci�n del pa�s establecido
   */
  public String formatedPercent(double value) {

    String result = "";

    if (country.equals("MX")) {

      NumberFormat nf =
        NumberFormat.getPercentInstance(
          new Locale("es", "MX"));
      result = nf.format(value);
    }
    else if (country.equals("US")) {

      NumberFormat nf =
        NumberFormat.getPercentInstance(
          new Locale("en", "US"));
      result = nf.format(value);
    }
    else if (country.equals("GB")) {

      NumberFormat nf =
        NumberFormat.getPercentInstance(
          new Locale("en", "GB"));
      result = nf.format(value);
    }
    else if (country.equals("ES")) {

      NumberFormat nf =
        NumberFormat.getPercentInstance(
          new Locale("es", "ES"));
      result = nf.format(value);
    }
    else {

      NumberFormat nf =
        NumberFormat.getPercentInstance(
          new Locale("es", "MX"));
      result = nf.format(value);
    }

    return result;
  }

  /**
   * Regresa la representaci�n como fecha del argumento
   * @param value variable del tipo <em>String</em>
   * @return Una fecha representando el argumento
   */
  public static Date dateValue(String value, String format) throws
    WellException {

    Date result = null;
    SimpleDateFormat sdf = null;

    try {
      if (value.length() == 0) {
        value = "01/01/1900";
        format = "dd/MM/yyyy";
      }

      if (format.equals("dd/mm/yy")) {

        sdf = new SimpleDateFormat("dd/MM/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("dd/mm/yyyy")) {

        sdf = new SimpleDateFormat("dd/MM/yyyy");
        result = sdf.parse(value);
      }
      else if (format.equals("mm/dd/yy")) {

        sdf = new SimpleDateFormat("MM/dd/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("mm/dd/yyyy")) {

        sdf = new SimpleDateFormat("MM/dd/yyyy");
        result = sdf.parse(value);
      }
      else if (format.equals("dd/mmm/yy")) {

        sdf = new SimpleDateFormat("dd/MMM/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("dd/mmm/yyyy")) {

        sdf = new SimpleDateFormat("dd/MMM/yyyy");
        result = sdf.parse(value);
      }
      else if (format.equals("mmm/dd/yy")) {

        sdf = new SimpleDateFormat("MMM/dd/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("mmm/dd/yy")) {

        sdf = new SimpleDateFormat("MMM/dd/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("dd/mmmm/yy")) {

        sdf = new SimpleDateFormat("dd/MMMM/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("dd/mmmm/yyyy")) {

        sdf = new SimpleDateFormat("dd/MMMM/yyyy");
        result = sdf.parse(value);
      }
      else if (format.equals("mmmm/dd/yy")) {

        sdf = new SimpleDateFormat("MMMM/dd/yy");
        result = sdf.parse(value);
      }
      else if (format.equals("mmmm/dd/yyyy")) {

        sdf = new SimpleDateFormat("MMMM/dd/yyyy");
        result = sdf.parse(value);
      }
      else {

        sdf = new SimpleDateFormat("dd/MM/yy");
        result = sdf.parse(value);
      }
    }
    catch (Exception ex) {
      throw new WellException("Formato de Fecha Invalido: "
                              + ex.toString());
    }

    return result;

  }

  /**
   * Especifica el pa�s en base al cual se aplicar� el formato
   * a cantidades, n�meros y monedas
   * @param value Cadena que especifica el pa�s en base al cual se aplicar�
   * el formato a cantidades, n�meros y monedas
   */
  public void setCountry(String value) {

    if (value.length() > 0) {
      this.country = value;
    }
    else {
      this.country = "MX";
    }
  }

  /**
   * Convierte una cadena a un valor "double"
   * @param value Cadena que se desea convertir a valor "double"
   * @return La cadena convertida a un valor "double"
   */
  public static double stringToDouble(String value) {

    double result = 0.00;

    if (value.length() > 0) {

      try {
        result = new Double(value).doubleValue();
      }
      catch (NumberFormatException nfb) {
        result = 0.00;
      }
    }
    else {
      result = 0.00;
    }

    return result;
  }

  /**
   * Verifica que el argumento se trate de una variable de tipo entero
   * @param value Cadena de la cual se desea saber si es
   * una variable de tipo entero
   * @return Un valor booleano que indica si el argumento es una
   * variable de tipo entero
   */
  public static boolean isNumber(String value) {

    boolean result = false;
    double number = 0.00;
    //double reminder = 0.00;

    if (value.length() > 0) {
      try {
        //Integer.parseInt( value );
        //Integer.valueOf( value ).intValue();
        number = Double.valueOf(value).doubleValue();
        //reminder =  number % 1;
        if ( (number % 1) != 0.00) {
          result = true;
        }
        else {
          result = false;
        }
      }
      catch (NumberFormatException nfe) {

        result = false;
      }
    }
    else {
      result = false;
    }

    return result;
  }

  /*
   * Campos
   */
  private String country;
}
