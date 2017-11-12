/*###############################################################################
# Nombre del Programa :  GrantAccess.java                                       #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Clase para el manejo de la seguridad, GIC           	#
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

package com.prosa.gic;

/**
 * GrantAccess
 */
import javax.servlet.http.HttpSession;

/**
 *
 * <p>Title: PROSA-GIC</p>
 *
 * <p>Description: GIC</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Wellcom S.A. de C.V.</p>
 *
 * @author M. en C. Armando F. Ibarra
 * @version 1.0
 */
public class GrantAccess {
    
    private boolean granted;
    private String sessionVar;
    
    public GrantAccess() {
        this.granted = false;
        this.sessionVar = null;
    }
    
    public final boolean AccessGranted( HttpSession s, String a ) {
        
        sessionVar = ( String ) s.getAttribute( a );
        if( sessionVar == null ) {
            granted = false;
        } else {
            granted = true;
        }
        
        return granted;
    }
}
