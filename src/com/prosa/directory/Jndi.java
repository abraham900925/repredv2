/*###############################################################################
# Nombre del Programa :  Jndi.java                                              #
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


package com.prosa.directory;

/**
 * Insert the type's description here.
 * Creation date: (1/7/2004 4:34:38 PM)
 * @author: Administrator
 */

import com.prosa.exceptions.WellException;
import com.prosa.io.ResourceFile;
import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;


/**
 * Demonstrates how to create an initial context to an LDAP server
 * using simple authentication.
 *
 * usage: java Simple
 */

public class Jndi
  implements Serializable {
  public DirContext ctxMain;
  public String msg;
  public java.lang.String uid;
  public java.lang.String dn;
  public java.lang.String initialContext;
  public java.lang.String url;
  public java.lang.String factory;
  public java.lang.String authentication;
//    public java.lang.String administratorDn;
//    public java.lang.String operatorDn;
//    public java.lang.String securityDn;
  public java.lang.String peopleDn;
  public java.lang.String appDn;

  /**
   * Ldap constructor comment.
   */
  public Jndi() {
    super();
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/7/2004 5:23:30 PM)
   */

  public static void main(String args[]) {
    Jndi myJndi = new Jndi();
    boolean bol = false;
    String myRole = "", myOu1st = "", myOu2nd = "", myOu3rd = "";
    try {

      myJndi.doProperties("./propiedades.xml");
      System.out.println("doLogin: " +
                         myJndi.doLogin("pmartinez", "dsfdsa"));

      /*
              String[] attrIDs = {"entrydn", "uniquemember"};
              String filter = "(&(cn=*))";
       printSearchEnumeration(myJndi.searchSubtree(myJndi.appDn, attrIDs, filter));
       */
      System.out.println("key: " + myJndi.loadKey(myJndi.dn, "sn"));
      String[] attrIDs = {
        "entrydn", "sn", "cn"};
      System.out.println("attrs: " + myJndi.loadAttrs(myJndi.dn, attrIDs));
      System.out.println("OuRoles: " + myJndi.loadOuRoles(myJndi.appDn));
      System.out.println("UidsByRol: " +
                         myJndi.loadUidsByRol(myJndi.appDn, "Comercio"));
      System.out.println("loadUidsByRolReport: " +
                         myJndi.loadUidsByRolReport(myJndi.appDn, "Comercio"));
      System.out.println("UidsByOu: " + myJndi.loadUidsByOu(myJndi.peopleDn));
      System.out.println("UidsByOu: " + myJndi.loadUidsByOu(""));
      System.out.println("UidRoles: " +
                         myJndi.loadUidRoles(myJndi.appDn, myJndi.uid));

      //Roles del Sistema
      myRole = myJndi.loadUidRoles(myJndi.appDn, myJndi.uid).toString();
      if ( (myRole.toLowerCase()).indexOf("administrador") >= 0) {
        myRole = "administrador";
      }
      else if ( (myRole.toLowerCase()).indexOf("banco") >= 0) {
        myRole = "banco";
      }
      else if ( (myRole.toLowerCase()).indexOf("corporativo") >= 0) {
        myRole = "corporativo";
      }
      else {
        myRole = "comercio";
      }
      System.out.println("myRole: " + myRole);

      //Numero Afiliacion
      myOu1st = myJndi.dn.substring( (myJndi.dn.toLowerCase()).indexOf("ou=") +
                                    3);
      System.out.println("myOu.indexOf(\",\"): " + myOu1st.indexOf(","));
      myOu1st = myOu1st.indexOf(",") >= 0 ?
        (myOu1st.substring(0, myOu1st.indexOf(","))).trim() : myOu1st;
      System.out.println("myOu1st: " + myOu1st);

      //FIID
      if ( (myJndi.dn.toLowerCase()).indexOf("ou=bancos") >= 0) {
        myOu2nd = myJndi.dn.substring(0,
                                      (myJndi.dn.toLowerCase()).indexOf("ou=bancos") -
                                      1);
        System.out.println("myOu2nd: " + myOu2nd);
        myOu2nd = myOu2nd.lastIndexOf("ou=") >= 0 ?
          myOu2nd.substring(myOu2nd.lastIndexOf("ou=") + 3).trim() : "PROSA";
      }
      else {
        myOu2nd = "PROSA";
      }
      System.out.println("myOu2nd: " + myOu2nd);


    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/7/2004 4:42:20 PM)
   */
  public DirContext doContext(String dn, String password) throws Exception {

    // Get LDAP Configuration
    //damePropiedades();
    // Set up environment for creating initial context
    DirContext ctx = null;
    Hashtable env = new Hashtable(11);
    // factory = "com.sun.jndi.ldap.LdapCtxFactory"
    env.put(Context.INITIAL_CONTEXT_FACTORY,
            factory);
    //url = "ldap://localhost:390/o=wellcom,c=mx");
    env.put(Context.PROVIDER_URL, url + initialContext);
    //authentication = "simple"
    env.put(Context.SECURITY_AUTHENTICATION, authentication);
    //userDN = "cn=S. User, ou=NewHires, o=JNDITutorial");
    env.put(Context.SECURITY_PRINCIPAL, uid);
    //password = "*****";
    env.put(Context.SECURITY_CREDENTIALS, password);

    try {

      System.out.println("initialContext: " + initialContext);
      System.out.println("dn: " + dn);

      // Create initial context
      ctx = new InitialDirContext(env);
    }
    catch (NamingException e) {
//		e.printStackTrace();
//		throw new WellException("com.directory.Ldap.dameConexion: "+"[ERRORLDAP4].Credenciales Invalidas."+ url +", userDN="+ dn +" | "+ e);
//		throw new WellException("com.directory.Ldap.dameConexion|" + e.toString());
      throw e;

    }
    catch (Exception e) {
//		e.printStackTrace();
//	 	throw new WellException("com.directory.Ldap.dameConexion|" + e.toString());
      throw e;
    }

    return ctx;
  }

  /**
   * Insert the method's description here.
   * Creation date: (4/5/2004 11:54:13 PM)
   */
  public ArrayList loadKey(String ou, String key) throws WellException {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();
    try {

      String[] attrIDs = {
        key};
      String filter = "(&(" + key + "=*))";

      NamingEnumeration answer = searchObject(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
//System.out.println(">>>" + sr.getName());
        attrsList = doAttrsList(sr.getAttributes());
        myArrayList.addAll(attrsList);
//System.out.println(">>>" + myArrayList);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }

  public ArrayList loadAttrs(String ou, String[] attrIDs) throws WellException {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();
    try {

      String filter = "(&(" + "entrydn" + "=*))";

      NamingEnumeration answer = searchObject(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
//System.out.println(">>>" + sr.getName());
        attrsList = doAttrsList(sr.getAttributes());
        myArrayList.addAll(attrsList);
//System.out.println(">>>" + myArrayList);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/7/2004 4:37:22 PM)
   */
  static void printAttrs(Attributes attrs) throws WellException {
    int i = 0;

    if (attrs == null) {
      System.out.println("No attributes");
    }
    else {
      /* Print each attribute */
      try {

        for (NamingEnumeration ae = attrs.getAll();
             ae.hasMore(); ) {
          Attribute attr = (Attribute) ae.next();
          System.out.println("attribute: " + attr.getID());

          /* print each value */
          for (NamingEnumeration e = attr.getAll();
               e.hasMore();
               System.out.println("value: " + e.next())) {
            ;
          }
        }

      }
      catch (NamingException e) {
//        e.printStackTrace();
        throw new WellException("com.directory.Ldap.imprimeAtributos|" +
                                e.toString());

      }
      catch (Exception e) {
//		e.printStackTrace();
        throw new WellException("com.directory.Ldap.imprimeAtributos|" +
                                e.toString());
      }
    } // end if
  }

  public ArrayList doAttrsList(Attributes attrs) throws WellException {
    int i = 0;
    ArrayList myArrayList = new ArrayList();
    if (attrs == null) {
      System.out.println("No attributes");
    }
    else {
      /* For each attribute */
      try {

        for (NamingEnumeration ae = attrs.getAll();
             ae.hasMore(); ) {
          Attribute attr = (Attribute) ae.next();
//System.out.println("attribute: " + attr.getID());

          /* For each value */
          for (NamingEnumeration e = attr.getAll();
               e.hasMore();
               myArrayList.add(e.next())) {
            ;
          }
        }
      }
      catch (NamingException e) {
//        e.printStackTrace();
        throw new WellException("com.directory.Ldap.imprimeAtributos|" +
                                e.toString());

      }
      catch (Exception e) {
//		e.printStackTrace();
        throw new WellException("com.directory.Ldap.imprimeAtributos|" +
                                e.toString());
      }
    } // end if
    return myArrayList;

  }

  /**
   * Insert the method's description here.
   * Creation date: (5/7/2004 2:41:07 PM)
   */
  public boolean doLogin(String login, String password) throws Exception {
      
    try {

      dn = getDn(login);
      uid = getUid();
      ctxMain = doContext(dn, password);
      
      return true;
    }

    catch (Exception e) {
      throw new WellException("com.prosa.directory.jndi.doLogin|" +
                              e.toString());
    }
  }

  public NamingEnumeration searchSubtree(String ou, String[] attrIDs,
                                         String filter) {

    NamingEnumeration answer;
    // Set up the environment for creating the initial context
    Hashtable env = new Hashtable(11);
    env.put(Context.INITIAL_CONTEXT_FACTORY,
            factory);
    env.put(Context.PROVIDER_URL, url + initialContext);

    try {
      // Create initial context
      DirContext ctx = new InitialDirContext(env);

      // Specify the ids of the attributes to return
      // String[] attrIDs = {"sn", "telephonenumber", "golfhandicap", "mail"};
      SearchControls ctls = new SearchControls();
      ctls.setReturningAttributes(attrIDs);
      ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      // Specify the search filter to match
      // String filter = "(&(sn=Contreras)(mail=*))";

      // Search subtree for objects using filter
      // NamingEnumeration answer = ctx.search("", filter, ctls);
      answer = ctx.search(ou, filter, ctls);

      // Print the answer
      // printSearchEnumeration(answer);

      // Close the context when we're done
      ctx.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      answer = null;
    }
    return answer;

  }

  public NamingEnumeration searchObject(String ou, String[] attrIDs,
                                        String filter) {

    NamingEnumeration answer;
    // Set up the environment for creating the initial context
    Hashtable env = new Hashtable(11);
    env.put(Context.INITIAL_CONTEXT_FACTORY,
            factory);
    env.put(Context.PROVIDER_URL, url + initialContext);

    try {
      // Create initial context
      DirContext ctx = new InitialDirContext(env);

      // Specify the ids of the attributes to return
      // String[] attrIDs = {"sn", "telephonenumber", "golfhandicap", "mail"};
      SearchControls ctls = new SearchControls();
      ctls.setReturningAttributes(attrIDs);
      ctls.setSearchScope(SearchControls.OBJECT_SCOPE);

      // Specify the search filter to match
      // String filter = "(&(sn=Contreras)(mail=*))";

      // Search subtree for objects using filter
      // NamingEnumeration answer = ctxMain.search("", filter, ctls);
      answer = ctx.search(ou, filter, ctls);

      // Print the answer
      // printSearchEnumeration(answer);

      // Close the context when we're done
      ctx.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      answer = null;
    }
    return answer;

  }

  static void printSearchEnumeration(NamingEnumeration enumn) throws
    WellException {
    try {
      while (enumn.hasMore()) {
        SearchResult sr = (SearchResult)enumn.next();
        System.out.println(">>>" + sr.getName());
        printAttrs(sr.getAttributes());
      }
    }
    catch (NamingException e) {
      e.printStackTrace();
    }
  }

  public ArrayList loadOuRoles(String ou) {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();
    try {

      String[] attrIDs = {
        "cn"};
      String filter = "(&(cn=*))";

      NamingEnumeration answer = searchSubtree(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
//System.out.println(">>>" + sr.getName());
        attrsList = doAttrsList(sr.getAttributes());
        myArrayList.addAll(attrsList);
//System.out.println(">>>" + myArrayList);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }

  public ArrayList loadUidsByRol(String ou, String rol) {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();

    try {

      String[] attrIDs = {
        "uniquemember"};
      String filter = "(&(cn=*" + rol + "*))";

      NamingEnumeration answer = searchSubtree(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
//System.out.println(">>>" + sr.getName());
        attrsList = doAttrsList(sr.getAttributes());
        myArrayList.addAll(attrsList);
//System.out.println(">>>" + myArrayList);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }


  public ArrayList loadUidsByRolReport(String ou, String rol) {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();

    try {


      String[] attrIDs = {
        "uniquemember"};
      String filter = "(&(cn=*" + rol + "*))";

      NamingEnumeration answer = searchSubtree(ou, attrIDs, filter);

      // Print the answer
//      printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
        attrsList = doAttrsList(sr.getAttributes());
      }

      Iterator it = attrsList.iterator();

      String[] UserAttrIDs = {
        "entrydn", "sn", "cn"};
      String myUid, myLogin, myOu1st, myOu2nd;
      while (it.hasNext()) {

          myUid = (String) it.next();
System.out.println("myUid: " + myUid );
          //Login
          myLogin = myUid.substring(4, (myUid.toLowerCase()).indexOf("ou=") - 1);

          //Numero Afiliacion
          myOu1st = myUid.substring( (myUid.toLowerCase()).indexOf("ou=") + 3);
          myOu1st = myOu1st.indexOf(",") >= 0 ?
                    (myOu1st.substring(0, myOu1st.indexOf(","))).trim() : myOu1st;
System.out.println("myOu1st: " + myOu1st);

          //FIIDt
          if ( (myUid.toLowerCase()).indexOf("ou=bancos") >= 0) {
            myOu2nd = myUid.substring(0, (myUid.toLowerCase()).indexOf("ou=bancos") - 1);
            System.out.println("myOu2nd: " + myOu2nd);
            myOu2nd = myOu2nd.lastIndexOf("ou=") >= 0 ?
                      myOu2nd.substring(myOu2nd.lastIndexOf("ou=") + 3).trim() : "PROSA";
          }
          else {
            myOu2nd = "PROSA";
          }

System.out.println("myOu2nd: " + myOu2nd);

        /* Cambio para Carga de HTML.getComboBox() */
        String[] rsColumnsData = null;
        rsColumnsData = new String[7];
        rsColumnsData[0] = myLogin;
        rsColumnsData[1] = rol;
        rsColumnsData[2] = "";
        rsColumnsData[3] = "";
        rsColumnsData[4] = myOu1st;
        rsColumnsData[5] = "";
        rsColumnsData[6] = myOu2nd;
        myArrayList.add(rsColumnsData);
}
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }


  public ArrayList loadUidsByOu(String ou) {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();
    Iterator it = null;

    try {

      String[] attrIDs = {
        "uid"};
      String filter = "(&(uid=*))";

      NamingEnumeration answer = searchSubtree(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
        //System.out.println(">>>" + sr.getName());

        attrsList = doAttrsList(sr.getAttributes());

        /* Version Anterior
                     myArrayList.addAll(attrsList);
                     System.out.println(attrsList.toString());
         */
//System.out.println("attrsList.toString(): " + attrsList.toString());


        /* Cambio para Carga de HTML.getComboBox() */
        String[] rsColumnsData = null;
        rsColumnsData = new String[2];
        String login = "";
        login = attrsList.toString().replace('[', ' ').replace(']', ' ').trim();
        rsColumnsData[0] = login;
        rsColumnsData[1] = login;
        myArrayList.add(rsColumnsData);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }

  public ArrayList loadUidRoles(String ou, String uid) {

    ArrayList myArrayList = new ArrayList();
    ArrayList attrsList = new ArrayList();

    try {

      String[] attrIDs = {
        "cn"};
      String filter = "(&(uniquemember=*" + uid + "*))";

      NamingEnumeration answer = searchSubtree(ou, attrIDs, filter);

      // Print the answer
      //printSearchEnumeration(answer);

      while (answer.hasMore()) {
        SearchResult sr = (SearchResult) answer.next();
//System.out.println(">>>" + sr.getName());
        attrsList = doAttrsList(sr.getAttributes());
        myArrayList.addAll(attrsList);
//System.out.println(">>>" + myArrayList);

      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return myArrayList;
  }

  public void doProperties(String filePath) throws WellException {
    try {
      ResourceFile rs = new ResourceFile();
      if (filePath.length() == 0) {
        throw new WellException("com.directory.ldap.setFilePath"
                                + "Argumento null.");
      }

      rs.setFilePath(filePath);

      url = rs.getXMLKey("URL");
      initialContext = rs.getXMLKey("INITIALCONTEXT");
      factory = rs.getXMLKey("FACTORY");
      authentication = rs.getXMLKey("AUTHENTICATION");
      peopleDn = rs.getXMLKey("PEOPLE");
      appDn = rs.getXMLKey("APPLICATION");
//            administratorDN = rs.getXMLKey( "ADMINISTRADORES" );
//            operatorDN = rs.getXMLKey( "USERS" );
//            securityDN = rs.getXMLKey( "SEGURIDAD" );
    }
    catch (Exception e) {
      throw new WellException(
        "com.directory.ldap.doProperties|"
        + e.toString());
    }
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/11/2004 3:23:25 PM)
   * @return java.lang.String
   */
  public java.lang.String getAuthentication() {
    return authentication;
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/11/2004 3:23:25 PM)
   * @return java.lang.String
   */
  public java.lang.String getInitialContext() {
    return initialContext;
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/11/2004 3:23:25 PM)
   * @return java.lang.String
   */
  public java.lang.String getFactory() {
    return factory;
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/11/2004 3:23:25 PM)
   * @return java.lang.String
   */
  public java.lang.String getPeopleDn() {
    return peopleDn;
  }

  /**
   * Insert the method's description here.
   * Creation date: (1/11/2004 3:23:25 PM)
   * @return java.lang.String
   */
  public java.lang.String getUrl() {
    return url;
  }

  /**
   * Insert the method's description here.
   * Creation date: (5/7/2004 2:41:07 PM)
   */
  public String getUid() throws Exception {

    uid = String.valueOf(String.valueOf( (new StringBuffer(String.valueOf(
      String.valueOf(dn)))).append(",").append(initialContext)));
    /*
                peopleDn = peopleDn != null ? peopleDn.trim() : "";
                if (peopleDn != "" && peopleDn != null) {
                    uid = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(dn)))).append(",").append(peopleDn).append(", ").append(initialContext)));
                } else {
                    uid = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(dn)))).append(",").append(initialContext)));
                }
     */

    System.out.println("getUid: " + uid);

    return uid;
  }

  /**
   * Insert the method's description here.
   * Creation date: (5/7/2004 2:54:12 PM)
   */
  public String getDn(String login)

    throws Exception {
    boolean bBan = false;

    String sRuta = "";

    DirContext ctx = new InitialDirContext();

//		sRuta = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(Propiedades.getProperty("URL"))))).append(Propiedades.getProperty("PEOPLE")).append(",").append(Propiedades.getProperty("INITIALCONTEXT")).append("??sub?(uid=").append(uid).append(")")));

    peopleDn = peopleDn != null ? peopleDn.trim() : "";
    if (peopleDn.length() > 0) {
      sRuta = String.valueOf(String.valueOf( (new StringBuffer(String.valueOf(
        String.valueOf(url)))).append(peopleDn).append(",").append(
        initialContext).append("??sub?(uid=").append(login).append(")")));
    }
    else {
      sRuta = String.valueOf(String.valueOf( (new StringBuffer(String.valueOf(
        String.valueOf(url)))).append(initialContext).append("??sub?(uid=").
                                            append(login).append(")")));
    }

    System.out.println("sRuta: " + sRuta);

    for (NamingEnumeration answer = ctx.search(sRuta, "", null);
         answer.hasMoreElements(); ) {
      SearchResult sr = (SearchResult) answer.next();
      sr.setRelative(false);
      dn = sr.getName();
      System.out.println("for NamingEnumeration.dn: " + dn);
      bBan = true;
    }

    if (peopleDn.length() > 0) {
      dn = String.valueOf(String.valueOf( (new StringBuffer(String.valueOf(
        String.valueOf(dn)))).append(",").append(peopleDn)));
    }

    System.out.println("getDn: " + dn);

    if (bBan) {
      ctx.close();
    }

    return dn;
  }

}
