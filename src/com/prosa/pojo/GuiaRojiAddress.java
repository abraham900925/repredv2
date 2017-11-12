/*
 * GuiaRojiAddress.java
 *
 * Created on January 24, 2007, 10:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*###############################################################################
# Nombre del Programa :  GuiaRojiAddrees.java                                   #
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

package com.prosa.pojo;

import java.io.Serializable;

/**
 *
 * @author afibarra
 */
public class GuiaRojiAddress implements Serializable {
    
    // Campos
    private String street;
    private String colony;
    private String municipality;
    private String zip;
    private String link;
    private String idSession;
    private int row;
    private String coordinate;
    
    public String getStreet() {
        
        return this.street;
    }
    public void setStreet( String value ) {
        
        this.street = value;
    }
    
    public String getColony() {
        
        return this.colony;
    }
    public void setColony( String value ) {
        
        this.colony = value;
    }
    
    public String getMunicipality() {
        
        return this.municipality;
    }
    public void setMunicipality( String value ) {
        
        this.municipality = value;
    }
    
    public String getZip() {
        
        return this.zip;
    }
    public void setZip( String value ) {
        
        this.zip = value;
    }
    
    public void setLink( String value ) {
        
        this.link = value;
    }
    
    public String getLink() {
        
        return this.link;
    }
    
    public void setIdSession( String value ) {
        
        this.idSession = value;
    }
    
    public String getIdSession() {
        
        return this.idSession;
    }
    
    public void setRow( int value ) {
        
        this.row = value;
    }
    
    public int getRow() {
        
        return this.row;
    }
    
    public void setCoordinate( String value ) {
        
        this.coordinate = value;
    }
    
    public String getCoordinate() {
        
        return this.coordinate;
    }
    
    /** Creates a new instance of GuiaRojiAddress */
    public GuiaRojiAddress() {
        
        this.street = null;
        this.colony = null;
        this.municipality = null;
        this.link = null;
        this.idSession = null;
        this.row = -1;
        this.coordinate = null;
    }
}
