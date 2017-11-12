/*###############################################################################
# Nombre del Programa :  WellException.java                                     #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Manejo de Excepciones				                    #
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

package com.prosa.exceptions;

/**
 *
 * <p>T�tulo: Clase WellException</p>
 * <p>Descripci�n: Manejo de excepciones</p>
 * <p>Compa��a: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public class WellException
  extends Exception {

  public WellException() {

    super();
  }

  public WellException(String value) {

    super(value);
  }
}
