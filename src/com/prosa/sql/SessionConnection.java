/*###############################################################################
# Nombre del Programa :  SessionConnetion.java                                  #
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

package com.prosa.sql;

import com.prosa.exceptions.WellException;
import java.sql.*;
import javax.servlet.http.*;


public class SessionConnection
  implements HttpSessionBindingListener {

  private Connection connection;

  public SessionConnection() {

    connection = null;
  } 

  public SessionConnection(Connection value) {

    this.connection = value;
  }

  public Connection getConnection() {

    return this.connection;
  }

  public void setConnection(Connection value) throws WellException {

    if (value != null) {
      this.connection = value;
    }
    else {
      throw new WellException("com.sql.setConnection : "
                              + "Parametro NULL");
    }
  }

  public void valueBound(HttpSessionBindingEvent value) {

    if (this.connection != null) {

      System.out.println("Binding a valid connection");
    }
    else {
      System.out.println("Binding a null connection");
    }
  }

  public void valueUnbound(HttpSessionBindingEvent value) {

    if (this.connection != null) {

      System.out.println("Closing the bound connection...");

      try {
        this.connection.close();
      }
      catch (SQLException e) {
        System.out.println(e.toString());
      }
    }
  }
}
