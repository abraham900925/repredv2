/*###############################################################################
# Nombre del Programa :  Participant.java                                       #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad        	        #
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
 * Creation date: (1/9/2004 3:20:13 PM)
 * @author: Administrator
 */

import com.prosa.exceptions.WellException;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;


public class Participant implements Serializable {
	
	private static final long serialVersionUID = 8965230147L;

	public java.lang.String firstName;

	public java.lang.String lastName;

	public java.lang.String commonName;

	public java.lang.String login;

	public java.lang.String email;

	public java.lang.String password;

	public java.lang.String phone;

	public java.lang.String fax;

	public java.lang.String uid;

	public java.lang.String dn;

	public java.lang.String group;

	public ArrayList role;

	public java.lang.String filePath;

	public boolean logged;

	public Jndi jndi;

	private String msg;

	/**
	 * UsuarioProfile constructor comment.
	 */
	public Participant() {
		super();
		jndi = new Jndi();
		role = new ArrayList();
		msg = "";
		firstName = "";
		lastName = "";
		commonName = "";
		login = "";
		email = "";
		password = "";
		phone = "";
		fax = "";
		group = "";
		uid = "";
		dn = "";

	}

	public static void main(String args[]) {
		Participant user = new Participant();
		try {
			user.filePath = "Propiedades.xml";
			user.doLogin("acontreras", "acontreras");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 11:51:03 AM)
	 */

	private void load() throws WellException {

		int x, y;

		try {

			jndi.doProperties(this.filePath);
			jndi.doLogin(this.login, this.password);
			uid = jndi.uid;
			dn = jndi.dn;
			firstName = (jndi.loadKey(jndi.dn, "givenName")).toString();
			lastName = (jndi.loadKey(jndi.dn, "sn")).toString();
			email = (jndi.loadKey(jndi.dn, "mail")).toString();
			// Obtener Numero de Comercio
			commonName = (jndi.loadKey(jndi.dn, "cn")).toString();
			fax = (jndi.loadKey(jndi.dn, "fax")).toString();
			phone = (jndi.loadKey(jndi.dn, "phone")).toString();
			role = jndi.loadUidRoles(jndi.appDn, jndi.uid);
			//Obtener FIID
			x = (uid.indexOf("=", uid.indexOf("=") + 2));
			y = uid.indexOf(",", x);
			if (y > x) {
				group = uid.substring(x, y) != null ? uid.substring(x, y)
						.trim() : "";
			}
			logged = true;

		}

		catch (Exception e) {
			throw new WellException("com.directory.Participant.load|"
					+ e.toString());
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (5/7/2004 3:19:41 PM)
	 */
	public boolean doLogin(String slogin, String spassword) throws Exception {
		if (slogin == null || slogin.equals("") || spassword == null
				|| spassword.equals("")) {
			throw new WellException(
					"com.directory.Participant.doLogin|NODATA: Clave y Contrase�a Obligatorios.");
		}

		login = slogin;
		password = spassword;
		this.load();
		return true;

	}

	/**
	 * Insert the method's description here.
	 * Creation date: (5/7/2004 3:33:56 PM)
	 */
	public boolean doLogin(HttpServletRequest request, HttpSession session)
			throws Exception {
		String slogin = request.getParameter("name");
		String spassword = request.getParameter("password");
		if (slogin == null || slogin.equals("") || spassword == null
				|| spassword.equals("")) {
			throw new WellException(
					"com.directory.Participant.doLogin|NODATA: Clave y Contrase�a Obligatorios.");
		}

		login = slogin;
		password = spassword;
		this.load();
		return true;

	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getCommonName() {
		return commonName;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getEmail() {
		return email;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getFax() {
		return fax;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getLastName() {
		return lastName;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getLogin() {
		return login;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (22/10/2004 7:35:07 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getGroup() {
		return group;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/11/2004 3:21:28 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getPhone() {
		return phone;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (22/10/2004 7:28:08 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getUid() {
		return uid;
	}

	public java.lang.String getDn() {
		return dn;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (5/12/2004 1:18:26 PM)
	 * @return boolean
	 */
	public boolean isLogged() {
		return logged;
	}

	public void setFilePath(String value) throws WellException {

		if (value.length() > 0) {
			this.filePath = value;
		} else {
			throw new WellException(
					"com.directory.Participant.setFilePath"
							+ "Argumento null.");
		}

	}

}
