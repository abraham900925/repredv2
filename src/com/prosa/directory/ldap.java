/*###############################################################################
# Nombre del Programa :  ldap.java                                              #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad					#
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

package com.prosa.directory;

import com.prosa.exceptions.WellException;
import com.prosa.io.ResourceFile;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;


public class ldap {

  public ldap() {

    //this.resourceFile = null;

    this.url = "";
    this.baseDN = "";
    this.factory = "";
    this.authentication = "";
    this.administratorDN = "";
    this.operatorDN = "";
    this.securityDN = "";
    this.peopleDN = "";
    this.ctxMain = null;
    this.msg = "";

    this.filePath = "";
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
    }
    else {
      throw new WellException("com.directory.ldap.setFilePath"
                              + "Argumento null.");
    }

  }

  public DirContext getConnection(String dn, String password) throws
    WellException {

    this.getProperties();
    DirContext ctx = null;
    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
    env.put(Context.PROVIDER_URL, url + baseDN);
    env.put(Context.SECURITY_AUTHENTICATION, authentication);
    env.put(Context.SECURITY_PRINCIPAL, dn);
    env.put(Context.SECURITY_CREDENTIALS, password);

    try {
      ctx = new InitialDirContext(env);
    }
    catch (Exception ex) {
      throw new WellException(
        "com.directory.ldap.getConnection"
        + ex.toString());

    }

    return ctx;
  }

  public String getUserProperty(String login, String password, String key) throws
    WellException {

    String sRuta = "";

    try {
      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);

      sRuta = this.validateUser(login, password);
      if (sRuta.equals("")) {
        return null;
      }

      if (!this.validateAccess(login, password, sRuta)) {
        return null;
      }

      String dn = String.valueOf(String.valueOf( (
        new StringBuffer(
          String.valueOf(String.valueOf(
            sRuta)))).append(",").append(
              rs.getXMLKey("PEOPLE"))));

      Attributes attrs = ctxMain.getAttributes(dn);

      if (attrs == null) {
        return null;
      }

      for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {

        Attribute attr = (Attribute) ae.next();

        if (attr.getID().equals(key)) {

          for (NamingEnumeration e = attr.getAll(); e.hasMore(); ) {
            return attr.getID();
          }
        }
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.directory.ldap.getUserProperty:"
        + ex.toString());
    }

    return null;
  }

  public Attributes getAttributes(String dn) throws WellException {

    Attributes attr = null;
    try {

      attr = ctxMain.getAttributes(dn);

      if (attr == null) {
        return null;
      }

    }
    catch (Exception e) {
      throw new WellException(
        "com.directory.ldap.getAttributes: "
        + e.toString());
    }

    return attr;
  }

  public String getAccess(String login, String password) throws WellException {
    String sRuta = "";

    try {
      sRuta = this.validateUser(login, password);

      if (sRuta.equals("")) {
        msg = "NOEXISTS";
        throw new WellException(
          "com.ldap.getAccess: "
          + "NOEXISTS: El usuario no existe.");
      }

      if (this.validateAccess(login, password, sRuta)) {

        if (this.isAdminRole(login)) {
          msg = "ISADMIN";
        }
        else if (this.isOperatorRole(login)) {
          msg = "ISUSER";
        }
        else if (this.isSecurityRole(login)) {
          msg = "ISAUDIT";
        }
        else {

          msg = "NOGROUP";
          throw new WellException(
            "com.ldap.getAccess: "
            + "NOGROUP: El usuario no tiene un rol asignado.");
        }
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.directory.Ldap.dameAcceso: "
        + ex.toString());
    }
    finally {
      try {
        if (ctxMain != null) {
          ctxMain.close();
        }
      }
      catch (Exception ex) {
        throw new WellException(
          "com.directory.Ldap.dameAcceso: "
          + ex.toString());
      }
    }
    return msg;
  }

  public String getAdministratorDN() {

    return this.administratorDN;
  }

  public String getAuthentication() {

    return this.authentication;
  }

  public String getBaseDN() {

    return this.baseDN;
  }

  public String getFactory() {

    return this.factory;
  }

  public String getOperatorDN() {

    return this.operatorDN;
  }

  public String getPeopleDN() {

    return this.peopleDN;
  }

  public String getSecurityDN() {

    return this.securityDN;
  }

  public String getUrl() {

    return this.url;
  }

  /*
   * Métodos de utileria
   */
  private void getProperties() throws WellException {
    try {
      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);

      url = rs.getXMLKey("URL");
      baseDN = rs.getXMLKey("INITIALCONTEXT");
      factory = rs.getXMLKey("FACTORY");
      authentication = rs.getXMLKey("AUTHENTICATION");
      administratorDN = rs.getXMLKey("ADMINISTRADORES");
      operatorDN = rs.getXMLKey("USERS");
      securityDN = rs.getXMLKey("SEGURIDAD");
      peopleDN = rs.getXMLKey("PEOPLE");
    }
    catch (Exception e) {
      throw new WellException(
        "com.directory.ldap.getProperties: "
        + e.toString());
    }
  }

  private boolean validateAccess(String uid, String password, String sRuta) throws
    WellException {
    boolean result = false;

    try {
      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);
      String dn = String.valueOf(String.valueOf(
        (new StringBuffer(String.valueOf(
          String.valueOf(sRuta)))).append(",").append(
            rs.getXMLKey("PEOPLE")).append(", ").append(
              rs.getXMLKey("INITIALCONTEXT"))));

      ctxMain = this.getConnection(dn, password);

      result = true;
    }
    catch (Exception ex) {
      throw new WellException("com.directory.ldap.validaAcceso:"
                              + ex.toString());
    }

    return result;
  }

  private boolean isAdminRole(String value) throws WellException {

    boolean result = false;

    String attrIDs[] = {
      "uniqueMember"};
    try {

      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);
      Attributes attrs = ctxMain.getAttributes(
        rs.getXMLKey("ADMINISTRADORES"), attrIDs);
      NamingEnumeration ae = attrs.getAll();
      label0:
        do {
        if (ae.hasMore()) {

          Attribute attr = (Attribute) ae.next();
          if (attr.getID().equals("uniqueMember")) {
            continue;
          }
          NamingEnumeration e = attr.getAll();
          String uni = "";
          do {

            if (e.hasMore()) {
              continue label0;
            }
            uni = (String) e.next();
          }
          while (!uni.toLowerCase().startsWith(
            "uid=".concat(String.valueOf(
              String.valueOf(value.toLowerCase())))));
          break;
        }
        else {
          return false;
        }
      }
      while (true);

      return true;
    }
    catch (Exception ex) {
      throw new WellException("com.directory.ldap.isAdminRole: "
                              + ex.toString());
    }
  }

  private boolean isSecurityRole(String value) throws WellException {

    boolean result = false;

    String attrIDs[] = {
      "uniqueMember"};
    try {

      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);
      Attributes attrs = ctxMain.getAttributes(
        rs.getXMLKey("SEGURIDAD"), attrIDs);
      NamingEnumeration ae = attrs.getAll();
      label0:
        do {
        if (ae.hasMore()) {

          Attribute attr = (Attribute) ae.next();
          if (attr.getID().equals("uniqueMember")) {
            continue;
          }
          NamingEnumeration e = attr.getAll();
          String uni = "";
          do {

            if (e.hasMore()) {
              continue label0;
            }
            uni = (String) e.next();
          }
          while (!uni.toLowerCase().startsWith(
            "uid=".concat(String.valueOf(
              String.valueOf(value.toLowerCase())))));
          break;
        }
        else {
          return false;
        }
      }
      while (true);

      return true;
    }
    catch (Exception ex) {
      throw new WellException(
        "com.directory.ldap.isSecurityRole: "
        + ex.toString());
    }
  }

  private boolean isOperatorRole(String value) throws WellException {

    boolean result = false;

    String attrIDs[] = {
      "uniqueMember"};
    try {

      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);
      Attributes attrs = ctxMain.getAttributes(
        rs.getXMLKey("USERS"), attrIDs);
      NamingEnumeration ae = attrs.getAll();
      label0:
        do {
        if (ae.hasMore()) {

          Attribute attr = (Attribute) ae.next();
          if (attr.getID().equals("uniqueMember")) {
            continue;
          }
          NamingEnumeration e = attr.getAll();
          String uni = "";
          do {

            if (e.hasMore()) {
              continue label0;
            }
            uni = (String) e.next();
          }
          while (!uni.toLowerCase().startsWith(
            "uid=".concat(String.valueOf(
              String.valueOf(value.toLowerCase())))));
          break;
        }
        else {
          return false;
        }
      }
      while (true);

      return true;
    }
    catch (Exception ex) {
      throw new WellException(
        "com.directory.ldap.isSecurityRole: "
        + ex.toString());
    }
  }

  private String validateUser(String uid, String password) throws WellException {
    String result = "";

    boolean bBan = false;

    try {
      ResourceFile rs = new ResourceFile();
      rs.setFilePath(this.filePath);
      String sRuta = String.valueOf(
        String.valueOf( (new StringBuffer(String.valueOf(
          String.valueOf(rs.getXMLKey(
            "URL"))))).append(rs.getXMLKey(
              "PEOPLE")).append(",").append(
                rs.getXMLKey(
                  "INITIALCONTEXT")).append(
                    "??sub?(uid=").append(
                      uid).append(")")));

      DirContext ctx = new InitialDirContext();
      for (NamingEnumeration answer = ctx.search(sRuta, "", null);
           answer.hasMoreElements(); ) {
        SearchResult sr = (SearchResult) answer.next();
        sr.setRelative(false);
        result = sr.getName();
        bBan = true;
      }

      if (bBan) {
        ctx.close();
      }

    }
    catch (Exception ex) {
      throw new WellException("com.directory.ldap.validateUser:"
                              + ex.toString());
    }

    return result;
  }

  /*
   *  Campos
   */
  //private ResourceFile resourceFile;

  private String url;
  private String baseDN;
  private String factory;
  private String authentication;
  private String administratorDN;
  private String operatorDN;
  private String securityDN;
  private String peopleDN;
  private DirContext ctxMain;
  private String msg;

  private String filePath;
}
