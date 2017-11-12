/*###############################################################################
# Nombre del Programa :  TextFile.java                                          #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad, lee / escribe    #
#                        archivos de texto.                                     #
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
import java.text.*;
import java.util.*;
import java.util.regex.*;


/**
 * <p>Title: TextFile Class</p>
 * <p>Descripcion: Clase que lee/escribe archivos de texto</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Compania: Wellcom</p>
 * @author Armando F. Ibarra
 * @version 1.0
 */
public class TextFile {

  //Campos
  private String readFileName;
  private String writeFileName;
  //private ArrayList fileLines;
  //private ArrayList lineTokens;
  private String[] fileLines;
  private String[] lineTokens;
  private String tokenDelimiter;
  private String lineToTokens;

  private File rootDirectory;
  private String directoryPath;

  public TextFile() {

    this.readFileName = null;
    this.writeFileName = null;
    this.fileLines = null;
    this.lineTokens = null;
    this.tokenDelimiter = null;
    this.lineTokens = null;

    this.directoryPath = null;
  }

  public void setDirectoryPath(String value) {

    if (value.length() <= 0) {
      this.directoryPath =
        System.getProperty("user.home");
    }
    else {
      this.directoryPath = value;
    }
  }

  public String getDirectoryPath() {

    if (this.directoryPath != null) {
      return this.directoryPath;
    }
    else {
      return "";
    }

  }

  /**
   * Devuelve el nombre de todos los archivos contenidos
   * en el Directorio especificado
   * @param value boolean - Si se desea que en la misma
   * cadena del nombre del archivo tambien ponga su tamano
   * @return String[]
   */
  public String[] getDirFilesNames(boolean value, String fileExtension) {

    String result[] = null;
    ArrayList tmp = null;
    String tmpStr = null;
    Iterator it = null;
    File f[] = null;
    String size = null;
    DecimalFormat df = null;
    int i;
    final String fe = fileExtension;

    if (this.directoryPath == null) {
      this.rootDirectory =
        new File(System.getProperty("user.dir"));
    }
    else {
      this.rootDirectory =
        new File(this.directoryPath);
    }

    FilenameFilter filter = new FilenameFilter() {
      public boolean accept(File dir, String name) {
        //return name.endsWith( ".txt" );
        return name.endsWith(fe);
      }
    };

    f = this.rootDirectory.listFiles(filter);
    tmp = new ArrayList();
    tmpStr = "";
    for (i = 0; i < f.length; i++) {

      if (f[i].isFile()) {

        tmpStr = f[i].getName();
        if (value) {

          Locale.setDefault(Locale.US);
          df = new DecimalFormat();
          size = df.format(f[i].length());
          tmpStr =
            tmpStr + "\t" + size + " bytes";
        }

        tmp.add(tmpStr);
      }

    }

    tmp.trimToSize();
    result = new String[tmp.size()];
    it = tmp.iterator();
    i = 0;
    while (it.hasNext()) {

      result[i] = (String) it.next();
      i++;
    }

    return result;
  }

  public void setReadFileName(String fn) throws WellException {

    if (fn.length() > 0) {
      this.readFileName = fn;
    }
    else {
      throw new WellException("setreadFileName");
    }
  }

  public String getReadFileName() throws WellException {

    if (this.readFileName == null) {
      throw new WellException("getreadFileName");
    }

    return this.readFileName;
  }

  public void setWriteFileName(String fn) throws WellException {

    if (fn.length() > 0) {
      this.writeFileName = fn;
    }
    else {
      throw new WellException("setreadFileName");
    }
  }

  public String getWriteFileName() throws WellException {

    if (this.writeFileName == null) {
      throw new WellException("getreadFileName");
    }

    return this.writeFileName;
  }

  public void writeFile(String s, boolean apnd) throws WellException {

    if (s.length() <= 0) {
      throw new WellException("writeFile");
    }
    if (this.writeFileName == null) {
      throw new WellException("writeFile");
    }

    PrintWriter out = null;
    try {
      out = new PrintWriter(new BufferedWriter(new FileWriter(
        this.writeFileName, apnd)), true);
      out.print(s);
      out.close();
    }
    catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace(System.err);
    }
  }

  public void writeFile(ArrayList al, boolean apnd) throws WellException {

    if (al == null) {
      throw new WellException("writeFile");
    }
    if (this.writeFileName == null) {
      throw new WellException("writeFile");
    }

    PrintWriter out = null;
    Iterator it = null;
    String line = null;
    try {
      al.trimToSize();
      it = al.iterator();
      out = new PrintWriter(new BufferedWriter(new FileWriter(
        this.writeFileName, apnd)), true);
      while (it.hasNext()) {
        line = (String) it.next();
        out.println(line);
        //System.out.println( line );
      }
      out.close();
    }
    catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace(System.err);
    }
  }

  //public ArrayList getFileLines() throws WellException {
  public String[] getFileLines() throws WellException {

    ArrayList tmpAL = null;
    Iterator it = null;
    int i = -1;

    if (this.readFileName == null) {
      throw new WellException("");
    }

    BufferedReader in = null;
    String line = null;

    try {
      tmpAL = new ArrayList();
      in =
        new BufferedReader(new FileReader(this.readFileName));
      line = null;

      while ( (line = in.readLine()) != null) {

        tmpAL.add(line);
      }

      in.close();
      tmpAL.trimToSize();
      it = tmpAL.iterator();
      this.fileLines = new String[tmpAL.size()];
      i = 0;
      while (it.hasNext()) {
        this.fileLines[i] = (String) it.next();
        i++;
      }
    }
    catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }

    //return fileLines;
    return fileLines;
  }

  public void setTokenDelimiter(String td) throws WellException {

    if (td.length() > 0) {
      this.tokenDelimiter = td;
    }
    else {
      throw new WellException("setTokenDelimiter");
    }
  }

  public void setLineToTokens(String ltk) throws WellException {

    if (ltk.length() > 0) {
      this.lineToTokens = ltk;
    }
    else {
      throw new WellException("setLineToTokens");
    }
  }

  //public ArrayList getLineTokens() throws WellException {
  /*public Object[] getLineTokens() throws WellException {
      ArrayList tmpAL = null;
      Iterator it = null;
      int i = -1;

      if( this.fileLines == null ) {
          throw new WellException( "getLineTokens" );
      }
      if( this.tokenDelimiter == null ) {
          throw new WellException( "getLineTokens" );
      }

      tmpAL = new ArrayList();
      StringTokenizer st = new StringTokenizer( this.lineToTokens,
                                                this.tokenDelimiter );
      while( st.hasMoreElements() ) {
          tmpAL.add( st.nextToken() );
      }
      tmpAL.trimToSize();
      it = tmpAL.iterator();
      this.lineTokens = new String[ tmpAL.size() ];
      i = 0;
      while( it.hasNext() ) {
          this.lineTokens[ i ] = (String)it.next();
          i++;
      }

      //return this.lineTokens;
      return this.lineTokens;
       }*/
  public String[] getLineTokens() throws WellException {

    String result[] = null;

    if (this.fileLines == null) {
      throw new WellException("getLineTokens");
    }
    if (this.tokenDelimiter == null) {
      throw new WellException("getLineTokens");
    }

    result = this.lineToTokens.split(Pattern.compile(this.tokenDelimiter).
                                     pattern());

    return result;
  }

  public String quitCharacter(String orgStr, String ctq) {

    String result = null;

    result = quitCharacter(orgStr, ctq.charAt(0));

    return result;
  }

  public String quitCharacter(String orgStr, char ctq) {

    StringBuffer result = null;
    char tmpChar[] = null;
    int i = -1;

    tmpChar = orgStr.toCharArray();
    result = new StringBuffer();
    for (i = 0; i < tmpChar.length; i++) {

      if (tmpChar[i] != ctq) {
        result.append(tmpChar[i]);
      }
    }

    return result.toString();
  }

  public static void main(String[] args) {

    /*try {
        /*Iterator it = null;

         TextFile tf = new TextFile();
         tf.setReadFileName( "C:\\Temp\\prueba.txt" );
         tf.setWriteFileName( "C:\\Temp\\prueba-salida.txt" );

         tf.writeFile( tf.getFileLines(), false );

         /*it = tf.getFileLines().iterator();
          String line = null;
          while( it.hasNext() ) {
              line = (String)it.next();
              System.out.println( line );
          }

          tf.setLineToTokens( line );
          tf.setTokenDelimiter( " ::" );
          it = tf.getLineTokens().iterator();
          while( it.hasNext() ) {
              line = (String)it.next();
              System.out.println( line );
          }*/
      /*}
               /*catch( Exception e ) {
           System.out.println( e.toString() );
           e.printStackTrace();
                }*/


       TextFile tf = new TextFile();
    String fileNames[] = null;

    tf.setDirectoryPath("C:" + File.separator + "Temp");

    fileNames = tf.getDirFilesNames(true, ".zip");

    for (int i = 0; i < fileNames.length; i++) {
      System.out.println(fileNames[i]);
    }
  }
}
