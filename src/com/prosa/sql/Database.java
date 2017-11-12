/*###############################################################################
# Nombre del Programa :  Database.java                                          #
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
# Modificaci�n        :                                                         #
#-----------------------------------------------------------------------------  #
# Numero de Parametros: 0                                                       #
###############################################################################*/

package com.prosa.sql;

import com.prosa.exceptions.WellException;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;


/**
 *
 * <p>T�tulo: Clase Database</p>
 * <p>Descripci�n: Clase que permite realizar operaciones en bases de datos</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compa��a: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public class Database
  implements Serializable {

  public Database() {

    this.userName = "";
    this.password = "";
    this.driver = "";
    this.url = "";
    this.dataSourceName = "";

    this.queryDelete = "";
    this.queryInsert = "";
    this.querySelect = "";
    this.queryUpdate = "";
    this.queryBatch = null;
    this.storeProcedure = "";

    this.connection = null;
    this.connectionType = "jdbc";
    this.isATransaction = false;

    this.resultSet = null;
    this.rsMetaData = null;

    this.rsColumnsTitles = null;
    this.rsColumnsData = null;
    this.colsDataAsArray = false;
    this.rsColsDataArray = null;
    this.rsColsDataIterator = null;

    //this.numRowsRS = -5;
    this.numRowsRS = 0;
  }

  /**
   * Destructor - Libera recursos
   * @throws WellException
   */
  protected void finalize() throws WellException {

    try {
      if (this.resultSet != null) {
        this.resultSet.close();
      }

      if ( (this.connection != null) && (!this.connection.isClosed())) {
        this.connection.close();
      }
    }
    catch (Exception ex) {
      throw new WellException("com.sql.Database.finalize: "
                              + ex.toString());
    }
  }

  /**
   * Regresa el nombre de las columnas obtenidas al ejecutar la
   * instrucci�n SQL establecida en la propiedad <em>querySelect</em>
   * @return conjunto de cadenas con los nombres de las columnas obtenidas
   * al ejecutar la instrucci�n SQL establecida en
   * la propiedad <em>querySelect</em>
   * @throws WellException
   */
  public String[] getRSColsTitles() throws WellException {

    int numCols;

    try {

      if (this.resultSet != null) {

        this.rsMetaData = this.resultSet.getMetaData();
        numCols = this.rsMetaData.getColumnCount();
        this.rsColumnsTitles = new String[numCols];
        for (int i = 1; i <= numCols; i++) {
          this.rsColumnsTitles[i - 1] =
            this.rsMetaData.getColumnName(i);
        }
      }
      else {
        throw new WellException(
          "com.sql.Database.getRSColumnsNames: "
          + "this.resultSet = null");
      }
    }
    catch (SQLException sqlEx) {
      throw new WellException(
        "com.sql.Database.getRSColumnsNames: "
        + sqlEx.toString());
    }

    return this.rsColumnsTitles;
  }

  /**
   * Regresa los datos contenidos en las columnas obtenidas al ejecutar
   * la instrucci�n SQL establecida en la propiedad <em>querySelect</em>
   * @return tipo de dato <em>ArrayList</em> que contienen los datos en las
   * columnas obtenidas al ejecutar la instrucci�n SQL establecida
   * en la propiedad <em>querySelect</em>
   * @throws WellException
   */
  public ArrayList getRSColsData() throws WellException {

    int numRows, numCols;

    try {

      if (this.resultSet != null) {

        this.rsMetaData = this.resultSet.getMetaData();
        numCols = this.rsMetaData.getColumnCount();
        this.resultSet.last();
        numRows = this.resultSet.getRow();

        /*
         * Procurar que siempre se indique en el
         * constructor del ArrayList el tama�o deseado
         */
        this.rsColsDataArray = new ArrayList(numRows);

        this.rsColumnsData = new String[numRows][numCols];
        this.resultSet.first();
        for (int i = 0; i < numRows; i++) {

          for (int j = 0; j < numCols; j++) {
            this.setRSData(
              this.rsMetaData.getColumnType(j + 1), i, j);
          }

          this.rsColsDataArray.add(this.rsColumnsData[i]);

          this.resultSet.next();
        }

        this.rsColsDataIterator = this.rsColsDataArray.listIterator();
        this.rsColumnsData = null;
      }
      else {
        throw new WellException(
          "com.sql.Database.getRSColumnsData: "
          + "this.resultSet = null");
      }
    }
    catch (Exception ex) {
      new WellException("com.sql.Database.getRSColsAsArray: "
                        + ex.toString());
    }

    return this.rsColsDataArray;
  }

  /**
   * Regresa el n�mero de filas obtenidas de los datos de las columnas
   * al ejecutar la instrucci�n almacenanda en la
   * propiedad <em>querySelect</em>
   * @return entero con el n�mero de filas obtenidas de los datos de las
   * columnas al ejecutar la instrucci�n almacenada
   * en la propiedad <em>querySelect</em>
   * @throws WellException
   */
  public int getRSColsDataSize() throws WellException {

    int size = 0;

    if (this.rsColsDataArray == null) {
      this.getRSColsData();
    }

    size = this.rsColsDataArray.size();
    if (size > 0) {
      return size;
    }
    else {
      throw new WellException(
        "com.slq.Database.getRSColsDataSize: "
        + "La tabla no contiene datos.");
    }
  }


  /**
   * Regresa el n�mero de filas especificadas en el par�metro <em>value</em>
   * de los datos de las columnas al ejecutar la instrucci�n almacenada
   * en la propiedad <em>querySelect</em>
   * @param value entero que especifica el n�mero de filas a devolver
   * @return n�mero de filas con los datos de las columnas
   * @throws WellException
   */
  public ArrayList getNextRSColsData(int value) throws WellException {

    int i = 0;
    int lastRows = 0;
    String row[] = null;
    ArrayList list = new ArrayList();

    try {
      if (this.rsColsDataArray == null) {
        this.getRSColsData();
      }

      if (this.rsColsDataArray.size() > 0) {

        if (!rsColsDataIterator.hasNext()) {

          lastRows = this.rsColsDataArray.size() % value;
          while (this.rsColsDataIterator.hasPrevious() &&
                 (i < (lastRows == 0 ? value : lastRows))) {

            this.rsColsDataIterator.previous();
            i++;
          }
        }

        i = 0;
        while (this.rsColsDataIterator.hasNext() && (i < value)) {

          row = (String[])this.rsColsDataIterator.next();
          list.add(row);
          i++;
        }
      }
      else {
        throw new WellException(
          "com.sql.Database.getNextRSColsData: "
          + "La tabla no contiene datos.");
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.getNextRSColsData: "
        + ex.toString());
    }

    return list;
  }

  // Agregado el 19/Jul/06
  /**
   * Regresa el numero de filas especificadas en el parametro <em>value</em>
   * a partir de la posicion actual del cursor del ResultSet
   * @param value int - Especifica el numero de filas que se desea obtener
   * @return ArrayList - Numero de filas con los datos de las columnas
   * @throws WellException
   */
  public ArrayList getNextResultSetData(int value) throws WellException {

    int i = 0;
    int j = 0;
    int numCols = 0;
    int numRows = 0;
    int currentRow = 0;
    int lastRows = 0;

    if (this.resultSet == null) {
      throw new WellException(
        "com.sql.Database.getNextResultSetData: "
        + "ResultSet = null "
        );
    }

    try {

      //Obtener numero de columnas
      this.rsMetaData = this.resultSet.getMetaData();
      numCols = this.rsMetaData.getColumnCount();

      if (this.resultSet.isBeforeFirst()) {
        this.resultSet.first();
      }

      //Posicion actual del cursor
      currentRow = this.resultSet.getRow();

      /*
      //Obtener numero de filas
      this.resultSet.last();
      //Posicion actual del cursor que en este caso nos
      //indica el numero total de registros
      numRows = this.resultSet.getRow();
      //Se establece al cursor en su posicion original
      if (currentRow > 0) {
        this.resultSet.absolute(currentRow);
      }
      */
      //Obtener numero de filas
      numRows = this.getNumRowsRS();

      //ResultSet - Contiene datos?
      if (numRows > 0) {

        //Se establece el tamanio del ArrayList de resultado
        this.rsColsDataArray = new ArrayList(value);
        this.rsColumnsData = new String[value][numCols];

        //El cursor se encuentra en el ultimo o
        //despues del ultimo registro?
        //if ((this.resultSet.isLast()) || (this.resultSet.isAfterLast())) {
        if (this.resultSet.isAfterLast()) {

          //value es multiplo de rowNums?
          lastRows = numRows % value;

          if (lastRows == 0) {
            //for (i = 1; i < value; i++) {
            for (i = 0; i < value; i++) {
              this.resultSet.previous();
            }
          }
          else if (lastRows != 0) {
            //for (i = 1; i < lastRows; i++) {
            for (i = 0; i < lastRows; i++) {
              this.resultSet.previous();
            }
          }
        }

        //Mientras no se alcance el ultimo registro, desplegar datos.
        for (i = 0; i < value; i++) {

          if (!this.resultSet.isAfterLast()) {
            for (j = 0; j < numCols; j++) {
              this.setRSData(
                this.rsMetaData.getColumnType(j + 1), i, j
              );
            }

            this.rsColsDataArray.add(this.rsColumnsData[i]);
            this.resultSet.next();
          }
        }
        this.rsColsDataArray.trimToSize();
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.getNextResultSetData: "
        + ex.toString()
        );
    }

    return this.rsColsDataArray;
  }

  // Agregado el 19/Jul/06
  /**
   * Regresa el numero de filas especificadas en el parametro <em>value</em>
   * a partir de la posicion actual del cursor del ResultSet
   * @param value int - Especifica el numero de filas que se desea obtener
   * @return ArrayList - Numero de filas con los datos de las columnas
   * @throws WellException
   */
  public ArrayList getPrevResultSetData(int value) throws WellException {

    int i = 0;
    int j = 0;
    int numCols = 0;
    int numRows = 0;
    int currentRow = 0;
    int lastRows = 0;

    if (this.resultSet == null) {
      throw new WellException(
        "com.sql.Database.getPrevResultSetData: "
        + "ResultSet = null "
        );
    }

    try {

      //Obtener numero de columnas
      this.rsMetaData = this.resultSet.getMetaData();
      numCols = this.rsMetaData.getColumnCount();

      currentRow = this.resultSet.getRow();

      //El cursor se encuentra en el ultimo o
      //despues del ultimo registro?
      //if ( (this.resultSet.isLast()) || (this.resultSet.isAfterLast())) {
      if (this.resultSet.isAfterLast()) {

        //Se posiciona el cursor en el ultimo registro
        this.resultSet.last();
        /*
        //Se obtiene y almacena la posicion actual del cursor,
        //que en este caso nos indica el numero de registros
        numRows = this.resultSet.getRow();
        */
        //Se obtiene el numero de registros
        numRows = this.getNumRowsRS();

        //value es multiplo de rowNums?
        lastRows = numRows % value;

        if (lastRows == 0) {
          //for (i = 1; i < ((value * 2) + 1); i++) {
          for (i = 1; i < (value * 2); i++) {
            this.resultSet.previous();
          }
        }
        else if (lastRows != 0) {
          for (i = 1; i < (value + lastRows); i++) {
            this.resultSet.previous();
          }
        }
        currentRow = this.resultSet.getRow();
      }
      //La posicion del cursor es distinta a la posicion del ultimo registro?
      else {
        currentRow = this.resultSet.getRow();
        this.resultSet.last();
        numRows = this.resultSet.getRow();
        this.resultSet.absolute( currentRow );

        for(i = 1;i < ((value * 2) + 1);i++) {
        //for(i = 1;i < (value * 2);i++) {
          if(!this.resultSet.isBeforeFirst()) {
            this.resultSet.previous();
          }
        }
      }
      currentRow = this.resultSet.getRow();
      if( this.resultSet.isBeforeFirst() ) {
        this.resultSet.first();
      }

      //ResultSet - Contiene datos?
      if (numRows > 0) {

        //Se establece el tamanio del ArrayList de resultado
        this.rsColsDataArray = new ArrayList(value);
        this.rsColumnsData = new String[value][numCols];

        //Mientras no se alcance el ultimo registro, desplegar datos.
        for (i = 0; i < value; i++) {

          if (!this.resultSet.isAfterLast()) {
            for (j = 0; j < numCols; j++) {
              this.setRSData(
                this.rsMetaData.getColumnType(j + 1), i, j
              );
            }

            this.rsColsDataArray.add(this.rsColumnsData[i]);
            this.resultSet.next();
          }
        }
        this.rsColsDataArray.trimToSize();
        currentRow = this.resultSet.getRow();
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.getPrevResultSetData: "
        + ex.toString()
      );
    }

    return this.rsColsDataArray;
  }

  /**
   * Regresa el numero de filas especificadas en el parametro <em>value</em>
   * de los datos de las columnas al ejecutar la instruccion almacenada
   * en la propiedad <em>querySelect</em>
   * @param value entero que especifica el numero de filas a devolver
   * @return numero de filas con los datos de las columnas
   * @throws WellException
   */
  public ArrayList getPrevRSColsData(int value) throws WellException {

    int i = 0;
    int lastRows = 0;
    String row[] = null;
    ArrayList list = new ArrayList();

    try {
      if (this.rsColsDataArray == null) {
        this.getRSColsData();
      }

      if (this.rsColsDataArray.size() > 0) {

        if (!this.rsColsDataIterator.hasNext()) {
          lastRows = (this.rsColsDataArray.size() % value == 0) ? (value * 2) :
            (value + (this.rsColsDataArray.size() % value));
        }
        else {
          lastRows = value * 2;
        }

        while (this.rsColsDataIterator.hasPrevious() && (i < lastRows)) {

          this.rsColsDataIterator.previous();
          i++;
        }

        i = 0;
        while (this.rsColsDataIterator.hasNext() && (i < value)) {

          row = (String[])this.rsColsDataIterator.next();
          list.add(row);
          i++;
        }
      }
      else {
        throw new WellException(
          "com.sql.Database.getPrevRSColsData: "
          + "La tabla no contiene datos.");
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.getPrevRSColsData: "
        + ex.toString());
    }

    return list;
  }

  /**
   * Regresa el indice a la primera fila del conjunto de filas
   * obtenidas de los datos de las columnas al ejecutar la instruccion
   * almacenada en la propiedad <em>querySelect</em>
   * @throws WellException
   */
  public void rstRSColsDataIdx() throws WellException {

    try {
      if (this.rsColsDataArray == null) {
        this.getRSColsData();
      }

      if (this.rsColsDataArray.size() > 0) {
        this.rsColsDataIterator = this.rsColsDataArray.listIterator();
      }
      else {
        throw new WellException(
          "com.sql.Database.rstRSColsDataIdx: "
          + "La tabla no contiene datos.");
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.rstRSColsDataIdx: "
        + ex.toString());
    }
  }

  /**
   * Regresa la fila a la cual esta actualmente apuntando el �ndice
   * del conjunto de filas obtenidas de los datos de las columnas
   * al ejecutar la instruccion almacenada
   * en la propiedad <em>querySelect</em>
   * @return la fila a la cual esta actualmente apuntando el indice
   * @throws WellException
   */
  public String[] getCurrRSColsData() throws WellException {

    String row[] = null;

    try {
      if (this.rsColsDataArray == null) {
        this.getRSColsData();
      }

      if (this.rsColsDataArray.size() > 0) {

        int currIdx = this.rsColsDataIterator.nextIndex();
        row = (String[])this.rsColsDataArray.get(currIdx - 1);
      }
      else {
        throw new WellException(
          "com.sql.Database.getCurrRSColsData: "
          + "La tabla no contiene datos.");
      }
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.getCurrRSColsData: "
        + ex.toString());
    }

    return row;
  }

  /**
   * Establece el nombre de usuario para la identificacion en la base de datos
   * @param value cadena que contiene el nombre de usuario para la
   * identificacion en la base de datos
   */
  public void setUserName(String value) {

    if (value.length() > 0) {
      this.userName = value;
    }
  }

  /**
   * Establece el password para la identificaci�n en la base de datos
   * @param value cadena que contiene el password para la identificacion
   * en la base de datos
   */
  public void setPassword(String value) {

    if (value.length() > 0) {
      this.password = value;
    }
  }

  /**
   * Establece el controlador JDBC apropiado
   * para conectarse a la base de datos
   * @param value cadena que contiene el controlador JDBC apropiado para
   * conectarse a la base de datos
   */
  public void setDriver(String value) {

    if (value.length() > 0) {
      this.driver = value;
    }
  }

  /**
   * Establece el URL apropiado para conectarse a la base de datos
   * @param value cadena que contiene el URL apropiado
   * para conectarse a la base de datos
   */
  public void setUrl(String value) {

    if (value.length() > 0) {
      this.url = value;
    }
  }

  /**
   * Establece el nombre de la fuente de datos
   * @param value cadena que contiene el nombre de la fuente de datos
   */
  public void setDataSourceName(String value) {

    if (value.length() > 0) {
      this.dataSourceName = value;
    }
  }

  /**
   * Establece una consulta de tipo <em>DELETE</em>
   * @param value cadena que contiene una consulta de tipo <em>DELETE</em>
   */
  public void setQueryDelete(String value) {

    if (value.length() > 0) {
      this.queryDelete = value;
    }
  }

  /**
   * Establece una consulta de tipo <em>INSERT</em>
   * @param value cadena que contiene una consulta de tipo <em>INSERT</em>
   */
  public void setQueryInsert(String value) {

    if (value.length() > 0) {
      this.queryInsert = value;
    }
  }

  /**
   * Establece una consulta de tipo <em>SELECT</em>
   * @param value cadena que contiene una consulta de tipo <em>SELECT</em>
   */
  public void setQuerySelect(String value) {

    if (value.length() > 0) {
      this.querySelect = value;
    }
  }

  /**
   * Establece el Store Procedure que se desea ejecutar
   * @param value String
   */
  public void setStoreProcedure(String value) {

    if (value.length() > 0) {
      this.storeProcedure = value;
    }
  }

  /**
   * Establece una consulta de tipo <em>UPDATE</em>
   * @param value cadena que contiene una consulta de tipo <em>UPDATE</em>
   */
  public void setQueryUpdate(String value) {

    if (value.length() > 0) {
      this.queryUpdate = value;
    }
  }

  /**
   * Establece el tipo de conexion a la base de datos
   * @param value cadena que contiene el tipo de conexion a la base de datos
   * <br><br><b>Tipos de conexion: </b>
   * <br><br>1. "jdbc" - JDBC<br>
   * 2. "ds" - DataSource ( Pool de conexiones )
   */
  public void setConnectionType(String value) {

    if (value.length() > 0) {
      this.connectionType = value;
    }
  }

  /**
   * Ejecuta el Store Procedure definido en la propiedad storeProcedure
   * @throws WellException
   */
  public CallableStatement getCallableStatement() throws WellException {

    CallableStatement cs = null;

    try {

      if (this.storeProcedure.length() <= 0) {
        throw new WellException(
          "com.sql.Database.getCallableStatement: "
          + "No ha establecido un storeProcedure.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        cs = this.connection.prepareCall(this.storeProcedure);
      }
    }
    catch (Exception e) {
      throw new WellException(
        "com.sql.Database.getCallableStatement: "
        + e);
    }

    return cs;
  }

  /**
   * Ejecuta la instrucci�n SQL definida en la propiedad querySelect
   * @throws WellException
   */
  public void executeQuerySelect() throws WellException {

    try {
      if (this.querySelect.length() <= 0) {
        throw new WellException(
          "com.sql.Database.executeQuerySelect: "
          + "No ha establecido un querySelect.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        if (this.isATransaction) {
          this.connection.setAutoCommit(false);
        }

        /*Statement query = this.connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT );*/
        Statement query = this.connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

        // Agregado el 28/Mar/2007
        this.numRowsRS = 0;
        
        this.resultSet = query.executeQuery(this.querySelect);

        //Modificado el 13/Sep/06 por AFI
        //Obtener el numero de resultados
        if( this.resultSet != null ) {

          if( this.resultSet.last() ) {
            this.numRowsRS = this.resultSet.getRow();
            this.resultSet.beforeFirst();
          }
        }
        if (this.isATransaction) {

          this.connection.commit();
          this.connection.setAutoCommit(true);
        }
      }
    }
    catch (SQLException sqlEx) {

      try {
        if (this.connection != null) {
          if (this.isATransaction) {
            this.connection.rollback();
          }
        }
      }
      catch (SQLException sqlEx2) {
        throw new WellException(
          "com.sql.Database.executeQuerySelect.rollback: "
          + sqlEx.toString());
      }

      throw new WellException(
        "com.sql.Database.executeQuerySelect: "
        + sqlEx.toString());
    }
  }

  /**
   * Ejecuta la instrucci�n SQL definida en la propiead queryDelete
   * @throws WellException
   */
  public void executeQueryDelete() throws WellException {

    try {

      if (this.queryDelete.length() <= 0) {
        throw new WellException(
          "com.sql.Database.executeQueryDelete: "
          + "No ha establecido un queryDelete.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        /*if (this.isATransaction) {
          this.connection.setAutoCommit(false);
        }*/

        /*Statement query = this.connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT );*/
        Statement query = this.connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

        query.executeUpdate(this.queryDelete);

        //if (this.isATransaction) {

          this.connection.commit();
          //this.connection.setAutoCommit(true);
        //}
      }
    }
    catch (SQLException sqlEx) {

      try {
        if (this.connection != null) {
          if (this.isATransaction) {
            this.connection.rollback();
          }
        }
      }
      catch (SQLException sqlEx2) {
        throw new WellException(
          "com.sql.Database.executeQueryDelete.rollback: "
          + sqlEx.toString());
      }

      throw new WellException(
        "com.sql.Database.executeQueryDelete: "
        + sqlEx.toString());
    }
  }

  /**
   * Ejecuta la instruccion SQL definida en la propiead queryInsert
   * @throws WellException
   */
  public void executeQueryInsert() throws WellException {

    try {

      if (this.queryInsert.length() <= 0) {
        throw new WellException(
          "com.sql.Database.executeQueryInsert: "
          + "No ha establecido un queryInsert.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        if (this.isATransaction) {
          this.connection.setAutoCommit(false);
        }

        /*Statement query = this.connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT );*/
        Statement query = this.connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

        query.executeUpdate(this.queryInsert);

        if (this.isATransaction) {

          this.connection.commit();
          this.connection.setAutoCommit(true);
        }
      }
    }
    catch (SQLException sqlEx) {

      try {
        if (this.connection != null) {
          if (this.isATransaction) {
            this.connection.rollback();
          }
        }
      }
      catch (SQLException sqlEx2) {
        throw new WellException(
          "com.sql.Database.executeQueryInsert.rollback: "
          + sqlEx.toString());
      }

      throw new WellException(
        "com.sql.Database.executeQueryInsert: "
        + sqlEx.toString());
    }
  }

  /**
   * Ejecuta la instrucci�n SQL definida en la propiead queryUpdate
   * @throws WellException
   */
  public void executeQueryUpdate() throws WellException {

    try {

      if (this.queryUpdate.length() <= 0) {
        throw new WellException(
          "com.sql.Database.executeQueryUpdate: "
          + "No ha establecido un queryUpdate.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        if (this.isATransaction) {
          this.connection.setAutoCommit(false);
        }

        /*Statement query = this.connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT );*/
        Statement query = this.connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

        query.executeUpdate(this.queryUpdate);

        if (this.isATransaction) {

          this.connection.commit();
          this.connection.setAutoCommit(true);
        }
      }
    }
    catch (SQLException sqlEx) {

      try {
        if (this.connection != null) {
          if (this.isATransaction) {
            this.connection.rollback();
          }
        }
      }
      catch (SQLException sqlEx2) {
        throw new WellException(
          "com.sql.Database.executeQueryUpdate.rollback: "
          + sqlEx.toString());
      }

      throw new WellException(
        "com.sql.Database.executeQueryUpdate: "
        + sqlEx.toString());
    }
  }

  /**
   * Regresa el <em>ResultSet</em> obtenido al ejecutar la instruccion SQL
   * definida en la propiedad querySelect
   * @return tipo de dato <em>ResultSet</em> obtenido al ejecutar
   * la instruccion SQL definida en la propiedad querySelect
   * @throws WellException
   */
  public ResultSet getResultSet() throws WellException {

    if (this.resultSet != null) {
      return this.resultSet;
    }
    else {
      throw new WellException("com.Database.getResultSet: " +
                              "this.resultSet = null");
    }
  }

  /**
   * Cierra todas las instancias del objeto <em>ResultSet</em>
   * para liberar recursos
   * @throws WellException
   */
  public void closeResultSet() throws WellException {

    try {
      if (this.resultSet != null) {
        this.resultSet.close();
      }
    }
    catch (SQLException sqlEx) {
      throw new WellException("com.sql.Database.closeResultSet: "
                              + sqlEx.toString());
    }
  }

  /**
   * Cierra la conexi�n con la base de datos
   * @throws WellException
   */
  public void closeConnection() throws WellException {

    try {
      if (this.resultSet != null) {
        this.resultSet.close();
      }

      if ( (this.connection != null) && (!this.connection.isClosed())) {
        this.connection.close();
      }
    }
    catch (SQLException sqlEx) {
      throw new WellException(
        "com.sql.Database.closeConnection: "
        + sqlEx.toString());
    }
  }

  /**
   * Establece un conjunto de cadenas de consulta a ser ejecutadas
   * en la base de datos
   * @param value conjunto de cadenas a ser ejecutadas en la base de datos
   * @throws WellException
   */
  public void setQueryBatch(String value[]) throws WellException {

    if (value.length <= 0) {
      throw new WellException("com.sql.Database.setQueryBatch: "
                              + "Par�metro null.");
    }
    else {
      this.queryBatch = value;
    }
  }

  /**
   * Ejecuta el conjunto de cadenas de consulta almacenadas
   * en la propiedad queryBatch
   * @throws WellException
   */
  public void executeQueryBatch() throws WellException {

    try {

      if (this.queryBatch == null) {
        throw new WellException(
          "com.sql.Database.executeQueryBatch: "
          + "No ha establecido un queryBatch.");
      }

      if (this.connection == null) {
        this.doConnection();
      }

      if (this.connection != null) {

        this.connection.setAutoCommit(false);

        /*Statement query = this.connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT );*/
        Statement query = this.connection.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

        for (int i = 0; i < this.queryBatch.length; i++) {
          query.addBatch(this.queryBatch[i]);
        }

        query.executeBatch();

        this.connection.commit();
        this.connection.setAutoCommit(true);
      }
    }
    catch (SQLException sqlEx) {

      try {
        if (this.connection != null) {
          this.connection.rollback();
        }
      }
      catch (SQLException sqlEx2) {
        throw new WellException(
          "com.sql.Database.executeQueryBatch.rollback: "
          + sqlEx.toString());
      }

      throw new WellException(
        "com.sql.Database.executeQueryBatch: "
        + sqlEx.toString());
    }
  }

  /**
   * Recarga los datos de la tabla la cual ha sufrido alguna
   * operacion de tipo <em>update</em> como: update, insert, delete
   * @throws WellException
   */
  public void reloadRSColsData() throws WellException {

    try {

      this.executeQuerySelect();
      this.getRSColsData();
    }
    catch (Exception ex) {
      throw new WellException(
        "com.sql.Database.reloadRSColsData:"
        + ex.toString());
    }
  }

  public void doConnection() throws WellException {

    try 
    {
       // se agrega properties
      Properties props = new Properties(); 
      props.put("user", "root");         
      props.put("password", "root");
      props.put("useUnicode", "true");
      props.put("useServerPrepStmts", "false"); // use client-side prepared statement
      props.put("characterEncoding", "UTF-8"); // ensure charset is utf8 here
      
      this.url="jdbc:mysql://localhost:3306/REPRED";
        
      //if (this.connectionType.equals("jdbc")) 
      if (true) 
      {

        Class.forName(this.driver).newInstance();
        if (this.connection == null) {
          this.connection =
            DriverManager.getConnection(this.url,props);
        }
      }
      else if (this.connectionType.equals("ds")) 
      {

        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup(this.dataSourceName);
        if (this.connection == null) {
          this.connection = ds.getConnection();
        }
      }
    }
    catch (Exception ex) {
      throw new WellException("com.sql.DataBase.doConnection: "
                              + ex.toString() + this.url + "|"
                              + this.userName + "|"
                              + this.dataSourceName);
    }
  }

  public Connection getConnection() {

    return this.connection;
  }

  public int getNumRowsRS() throws WellException {

    if(this.resultSet == null) {
      throw new WellException( "com.sql.Database.getNumRowsRS: "
                               + "ResultSet nulo." );
    }

    return this.numRowsRS;
  }

  /*
   * Metodos de Utileria
   */
  private void getRSColsDataAsString() throws WellException {

    int numRows, numCols;

    try {

      if (this.resultSet != null) {

        this.rsMetaData = this.resultSet.getMetaData();
        numCols = this.rsMetaData.getColumnCount();
        this.resultSet.last();
        numRows = this.resultSet.getRow();

        /*
         * Procurar que siempre se indique en el
         * constructor del ArrayList el tamano deseado
         */
        if (this.colsDataAsArray) {
          this.rsColsDataArray = new ArrayList(numRows);
        }

        this.rsColumnsData = new String[numRows][numCols];
        this.resultSet.first();
        for (int i = 0; i < numRows; i++) {
          for (int j = 0; j < numCols; j++) {
            this.setRSData(
              this.rsMetaData.getColumnType(j + 1), i, j);
          }

          if (this.colsDataAsArray) {
            this.rsColsDataArray.add(this.rsColumnsData[i]);
          }

          this.resultSet.next();
        }
      }
      else {
        throw new WellException(
          "com.sql.Database.getRSColumnsData: "
          + "this.resultSet = null");
      }
    }
    catch (SQLException sqlEx) {
      throw new WellException(
        "com.sql.Database.getRSColumnsData: "
        + sqlEx.toString());
    }
  }

  private void setRSData(int type, int rowIdx, int colIdx) throws WellException {

    try {

      switch (type) {

        case java.sql.Types.CHAR:
        case java.sql.Types.VARCHAR:
        case java.sql.Types.LONGVARCHAR:
          if (this.resultSet.getString(colIdx + 1) != null) {
            this.rsColumnsData[rowIdx][colIdx] =
              this.resultSet.getString(colIdx + 1);
          }
          else {
            this.rsColumnsData[rowIdx][colIdx] = "";
          }
          break;

        case java.sql.Types.NUMERIC:
        case java.sql.Types.DECIMAL:
          if (this.resultSet.getBigDecimal(colIdx + 1) != null) {
            this.rsColumnsData[rowIdx][colIdx] =
              resultSet.getBigDecimal(colIdx + 1).toString();
          }
          else {
            this.rsColumnsData[rowIdx][colIdx] = "";
          }
          break;

        case java.sql.Types.BIT:
          this.rsColumnsData[rowIdx][colIdx] =
            String.valueOf(this.resultSet.
                           getBoolean(colIdx + 1));
          break;

        case java.sql.Types.TINYINT:
          this.rsColumnsData[rowIdx][colIdx] =
            new Byte(this.resultSet.
                     getByte(colIdx + 1)).toString();
          break;

        case java.sql.Types.SMALLINT:
          this.rsColumnsData[rowIdx][colIdx] =
            new Short(this.resultSet.
                      getShort(colIdx + 1)).toString();
          break;

        case java.sql.Types.INTEGER:
          this.rsColumnsData[rowIdx][colIdx] =
            String.valueOf(this.resultSet.
                           getInt(colIdx + 1));
          break;

        case java.sql.Types.BIGINT:
          this.rsColumnsData[rowIdx][colIdx] =
            String.valueOf(this.resultSet.
                           getLong(colIdx + 1));
          break;

        case java.sql.Types.FLOAT:
          this.rsColumnsData[rowIdx][colIdx] =
            String.valueOf(this.resultSet.
                           getFloat(colIdx + 1));
          break;

        case java.sql.Types.DOUBLE:
          this.rsColumnsData[rowIdx][colIdx] =
            String.valueOf(this.resultSet.
                           getDouble(colIdx + 1));
          break;

        case java.sql.Types.DATE:
          if (this.resultSet.getDate(colIdx + 1) != null) {
            this.rsColumnsData[rowIdx][colIdx] =
              this.resultSet.getDate(colIdx + 1).toString();
          }
          else {
            this.rsColumnsData[rowIdx][colIdx] = "";
          }
          break;

        case java.sql.Types.TIME:
          if (this.resultSet.getTime(colIdx + 1) != null) {
            this.rsColumnsData[rowIdx][colIdx] =
              this.resultSet.getTime(colIdx + 1).toString();
          }
          else {
            this.rsColumnsData[rowIdx][colIdx] = "";
          }
          break;

        case java.sql.Types.TIMESTAMP:
          if (resultSet.getTimestamp(colIdx + 1) != null) {
            this.rsColumnsData[rowIdx][colIdx] =
              resultSet.getTimestamp(colIdx + 1).toString();
          }
          else {
            this.rsColumnsData[rowIdx][colIdx] = "";
          }
          break;

        case java.sql.Types.NULL:
          this.rsColumnsData[rowIdx][colIdx] = "";
      }
    }
    catch (SQLException sqlEx) {
      throw new WellException("com.sql.Database.setRSData: "
                              + sqlEx.toString());
    }
    catch (Exception ex) {
      throw new WellException("com.sql.Database.setRSData: "
                              + ex.toString());
    }
  }

  /*
   * Campos
   */
  private String userName;
  private String password;
  private String driver;
  private String url;
  private String dataSourceName;

  private String queryDelete;
  private String queryInsert;
  private String querySelect;
  private String queryUpdate;
  private String queryBatch[];
  private String storeProcedure;

  private boolean isATransaction;

  private Connection connection;
  private String connectionType;

  private ResultSet resultSet;
  private ResultSetMetaData rsMetaData;

  private String rsColumnsTitles[];
  private String rsColumnsData[][];
  private boolean colsDataAsArray;
  private ArrayList rsColsDataArray;
  private ListIterator rsColsDataIterator;

  private int numRowsRS;

  /*
   * Funcion main de prueba
   */
  
  
  public static void main(String args[]) throws WellException, SQLException
  {
          
    
    Database db = new Database();
    db.setDriver( "com.mysql.jdbc.Driver" );
    db.doConnection();    
    db.setQuerySelect("SELECT * FROM TBL_SRR_REP_STAT06_NT;");
    
    db.executeQuerySelect();
    ResultSet rs = db.getResultSet();
    
    while(rs.next())
    {
        System.out.println("CODIGO_BANCO_EMI---CODIGO_BANCO_ADQ---CODIGO_TIPO_OPERACION---NOMBRE---CTA_CHEQUES---CTA_AHORRO");
        System.out.println("----"+rs.getString("CODIGO_BANCO_EMI")+"----");    
    }
    
    
    
  
  
  
  }
 
  public  void main2(String args[]) 
  {

    Database db = new Database();
    db.setDriver( "com.mysql.jdbc.Driver" );
    db.setUrl( "jdbc:mysql://localhost:3306/ranking_libros" );
    db.setConnectionType( "jdbc" );
    db.setUserName("root");
    db.setPassword("mysqls4ur0n");

    
    
    
    
    
    /*db.setConnectionType("jdbc");
    db.setDriver("oracle.jdbc.driver.OracleDriver");
    db.setUrl("jdbc:oracle:thin:@localhost:1521:WELLDB");
    db.setUserName("AFI");
    db.setPassword("password");*/

    String query =
        "LOAD DATA LOCAL INFILE '"
        + "/home/ranking_libros/data/Educal/040000906.dat"
        + "' INTO TABLE tbl_rkl_carga_ventas FIELDS TERMINATED BY '|' (Fecha_Venta, Codigo_Barras, ISBN, Titulo, Autor, Editorial, Unidades, Precio_Unitario, Importe, Id_Sucursal, Clasificacion, Tamano, Id_Colaborador, Ciudad, Registro, Fecha_Periodo)\n"
          + "SET Fecha_Carga = Now();";

    int i = -1;

    try {

      /*db.setQuerySelect(
        "SELECT PSCONTRACT FROM AFI.SFEHVAL WHERE ROWNUM < 15");*/
      db.setQueryUpdate( query );
      db.executeQueryUpdate();

      /*String colTitles[] = db.getRSColsTitles();
             for (i = 0; i < colTitles.length; i++) {
        System.out.println(colTitles[i]);
             }
             System.out.println("-----o-----");*/

      //ArrayList colsValues = db.getRSColsData();
      ArrayList colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next");

      colsValues = null;
      colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next2");

      colsValues = null;
      colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next3");

      colsValues = null;
      colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next4");

      colsValues = null;
      colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next5");

      colsValues = null;
      colsValues = db.getPrevResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Prev");

      colsValues = null;
      colsValues = db.getPrevResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Prev2");

      colsValues = null;
      colsValues = db.getNextResultSetData(5);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Next6");

      /*colsValues = null;
      colsValues = db.getPrevResultSetData(8);
      for (i = 0; i < colsValues.size(); i++) {

        String row[] = (String[]) colsValues.get(i);

        for (int j = 0; j < row.length; j++) {
          System.out.println(row[j]);
        }
        //System.out.println("-----o-----");
      }
      System.out.println("Se acabo el Prev3");*/

      /*colsValues = null;
                   colsValues = db.getNextResultSetData( 100 );
                   for( i = 0; i < colsValues.size(); i++ ) {

          String row[] = (String[])colsValues.get( i );

          for( int j = 0; j < row.length; j++ )
              System.out.println( row[ j ] );
          System.out.println( "-----o-----" );
                   }

                   colsValues = null;
                   colsValues = db.getNextResultSetData( 100 );
                   for( i = 0; i < colsValues.size(); i++ ) {

          String row[] = (String[])colsValues.get( i );

          for( int j = 0; j < row.length; j++ )
              System.out.println( row[ j ] );
          System.out.println( "-----o-----" );
                   }*/

      db.closeConnection();

      //System.out.println("NumRows = " + i);
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }
}
