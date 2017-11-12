/*###############################################################################
# Nombre del Programa :  ChartBuilderReportServlet.java                         #
# Autor               :  ARMANDO FLORES	I.                                      #
# Compania            :  WELLCOM S.A. DE C.V.                                   #
# Proyecto/Procliente :  C-08-089-07                 	   FECHA:14/04/2008     #
# Descripcion General :	 Creacion de graficas representavitivas de datos        #
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

package com.prosa.reports;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * <p>Titulo: ChartBuilderReportServlet</p>
 * <p>Descripcion: Permite la creacion de graficas representativas de datos
 * en formatos GIF o PNG</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Compania: Wellcom</p>
 * @author M. en C. Armando F. Ibarra
 */
public class ChartBuilderReportServlet
  extends HttpServlet implements Serializable {

  static final private String CONTENT_TYPE = "image/gif";

  public void init(ServletConfig config) throws ServletException {

    super.init(config);

    /*
     * New headless AWT toolkit:
     * http://java.sun.com/products/java-media/2D/forDevelopers/java2dfaq.html#xvfb
     */
    System.setProperty("java.awt.headless", "true");

    /*
     * Descomentar solo para prop�sitos de debug
     */
    System.out.println("Headless is: " +
                       System.getProperty("java.awt.headless"));
  }

  /**
   * Atiende a las peticiones "GET" y "POST"
   * @param request de �ste par�metro se obtienen los valores necesarios
   * para regresar un archivo de tipo .gif
   * @param response -
   * @throws ServletException
   */
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException {

    try {

      // Stream de salida
      ByteArrayOutputStream bOut;

      String cbrID;
      response.setContentType(CONTENT_TYPE);
      HttpSession session = request.getSession(true);
      String sessionID = session.getId() + "CB";
      cbrID = request.getParameter("sessionIDCB");

      if (sessionID.equals(cbrID)) {

        bOut = (ByteArrayOutputStream) session.getAttribute(cbrID);
        ServletOutputStream out = response.getOutputStream();
        bOut.writeTo(out);
        out.flush();
        out.close();
      }
    }
    catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }

  public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException {

    this.doGet(request, response);
  }
}
