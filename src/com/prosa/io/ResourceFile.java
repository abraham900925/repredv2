/*###############################################################################
# Nombre del Programa :  ResourceFile.java                                      #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad, metodos para     #
#                        lectura archivos .xml y properties.      				#
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
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 *
 * <p>Título: Clase XMLFile</p>
 * <p>Descripción: Provee métodos para la lectura
 * de archivos .xml y Properties</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compañía: Wellcom</p>
 * @author Armando F. Ibarra
 *
 */
public class ResourceFile
  implements Serializable {

  public ResourceFile() {

    this.document = null;
    this.docFactory = null;
    this.docBuilder = null;
    this.rootNode = null;

    this.properties = null;
    this.inputStreamFile = null;
    this.filePath = "";

    this.nodeMap = null;

  }

  public ResourceFile(InputStream value) {

    this.document = null;
    this.docFactory = null;
    this.docBuilder = null;
    this.rootNode = null;

    this.properties = null;
    this.inputStreamFile = value;
    this.filePath = "";

    this.nodeMap = null;
  }

  public ResourceFile(Properties value) {

    this.document = null;
    this.docFactory = null;
    this.docBuilder = null;
    this.rootNode = null;

    this.properties = value;
    this.inputStreamFile = null;
    this.filePath = "";

    this.nodeMap = null;
  }

  /**
   * Establece la trayectoria del archivo del cual
   * se desean leer las propiedades
   * @param value cadena que contiene la trayectoria del archivo del cual
   * se desean leer las propiedades
   * @throws WellException
   */
  public void setFilePath(String value) throws WellException {

    if (value.length() > 0) {

      this.filePath = value;

      this.properties = null;
      this.inputStreamFile = null;
      this.document = null;
    }
    else {
      throw new WellException("com.io.XMLFile.setFilePath: "
                              + "Argumento null.");
    }
  }

  /**
   * Regresa el valor contenido en la llave especificada
   * por el parámetro <em>value</em>
   * @param value cadena que especifica el nombre de llave
   * @return valor del contenido en la llave
   * @throws WellException
   */
  public String getFileProperty(String value) throws WellException {

    String result = "";

    if (value.length() > 0) {
      try {

        if (this.properties == null) {

          if (this.filePath.length() <= 0) {
            throw new WellException(
              "com.io.ResourceFile: "
              + "No ha especificado un filePath.");
          }

          this.inputStreamFile =
            (       com.prosa.io.ResourceFile.class).
            getResourceAsStream(this.filePath);

          this.properties = new Properties();
          this.properties.load(this.inputStreamFile);
        }

        result = (String)this.properties.getProperty(value);
      }
      catch (Exception ex) {
        throw new WellException("com.io.ResourceFile: "
                                + ex.toString());
      }
    }
    else {
      throw new WellException(
        "com.io.ResourceFile.getFileProperty: "
        + "Parámetro null.");
    }

    return result;
  }

  /**
   * Regresa el valor contenido en la llave especificada
   * por el parámetro <em>value</em>
   * @param value cadena que especifica el nombre de llave
   * @return valor del contenido en la llave
   * @throws WellException
   */
  public String getXMLKey(String value) throws WellException {

    String result = "";

    if (value.length() > 0) {

      if (this.inputStreamFile == null) {

        if (this.filePath.length() <= 0) {
          throw new WellException("com.io.ResourceFile: "
                                  + "No ha especificado un filePath.");
        }
        this.inputStreamFile =(com.prosa.io.ResourceFile.class).getResourceAsStream("/propiedades.xml");
          //getResourceAsStream(this.filePath);
      }

      if (this.document == null) {
        this.prepareDocument();
      }
      this.nodeMap = new HashMap();
      this.buildMap(this.nodeMap, this.rootNode);

      result = (String)this.nodeMap.get(value);
    }
    else {
      throw new WellException(
        "com.io.ResourceFile.getXMLProperty: "
        + "Parámetro null.");
    }

    return result;
  }

  public String getPropertiesKey(String key) throws WellException {

//	FileInputStream file=null;
    InputStream file = null;

    try {
      if (properties == null) {
        synchronized (com.prosa.io.ResourceFile.class) {
          if (properties == null) {
            //file= new FileInputStream("../webapps/vcifras/WEB-INF/Propiedades.properties");
            file = (    com.prosa.io.ResourceFile.class).getResourceAsStream(this.
              filePath);
            properties = new java.util.Properties();
            properties.load(file);
          }
        }
      }
      return (String) properties.getProperty(key);
    }
    catch (Exception er) {
//		 er.printStackTrace();
      throw new WellException("com.directory.File.getPropertiesKey|" +
                              er.toString());
    }

  }

  /*
   * Métodos de utileria
   */
  private void buildMap(Map map, Node rootNode) {

    for (Node n = rootNode.getFirstChild();
         n != null; n = n.getNextSibling()) {

      if (n.getNodeType() != 1) {
        continue;
      }
      if (n.hasChildNodes()) {
        buildMap(map, n);
      }
      else {

        Element e = (Element) n;
        String value = e.getAttribute("value");
        nodeMap.put(e.getNodeName(), String.valueOf(value));
      }
    }
  }

  private void prepareDocument() throws WellException {

    try {

      this.docFactory = DocumentBuilderFactory.newInstance();
      this.docFactory.setValidating(false);
      this.docBuilder = this.docFactory.newDocumentBuilder();
      this.document = this.docBuilder.parse(this.inputStreamFile);
      this.rootNode = this.document.getFirstChild();
    }
    catch (Exception ex) {

      throw new WellException("com.io.XMLFile.prepareDocument: "
                              + ex.toString());
    }
  }

  /**
   * Insert the method's description here.
   * Creation date: (6/16/2004 11:13:32 AM)
   */
  public String getClassPath(java.lang.Object obj) {

    String className = "" + obj.getClass().getName().replace('.', '/');
    String classJar = "" + obj.getClass().getResource(
      "/" + className + ".class").toString();

    if (classJar.startsWith("jar:")) {
      System.out.println("*** running from jar!");
    }

    return classJar;

  }

  /**
   * Insert the method's description here.
   * Creation date: (6/16/2004 11:13:32 AM)
   */
  public String getThisClassPath(java.lang.Object obj) {

    /*    System.out.println(obj.getClass().getName() + " is loaded from " +
          obj.getClass().getProtectionDomain().getCodeSource().getLocation());
     */
    String classPath = "" +
      obj.getClass().getProtectionDomain().getCodeSource().getLocation();

    return classPath;

  }

  /**
   * Insert the method's description here.
   * Creation date: (6/16/2004 12:21:05 PM)
   */
  public String getJavaRuntimePath() {

    Class theClass = java.lang.Object.class;
    java.net.URL u = theClass.getResource("");
//    System.out.println("The Java RunTime used is located at : " + u);

    return u.toString();

  }

  /*
   * Campos
   */
  public Document document;
  private DocumentBuilderFactory docFactory;
  private DocumentBuilder docBuilder;
  private Node rootNode;

  private InputStream inputStreamFile;
  private String filePath;
  private Properties properties;

  private Map nodeMap;

  /*
   * Función main para probar la clase
   */
  public static void main(String args[]) {

    try {

      ResourceFile rs = new ResourceFile();
      String sout = "";
      sout = rs.getThisClassPath(rs);

      System.out.println("getClassPath sout: " + sout);
      sout = rs.getClassPath(rs);
      System.out.println("getClassLoc sout: " + sout);

      //rs.setFilePath( "../../../xml/tbl_users.xml" );
      rs.setFilePath("tbl_users.xml");
      System.out.println(rs.getXMLKey("name"));
      System.out.println(rs.getXMLKey("type"));
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }
}
