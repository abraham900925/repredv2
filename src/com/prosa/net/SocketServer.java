/*
 * SocketServer.java
 *
 * Created on March 30, 2007, 10:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*###############################################################################
# Nombre del Programa :  SocketServer. Java                                     #
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

package com.prosa.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author afibarra
 */
class SingleSocket extends Thread {
    
    /**
     * Fields
     */
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    /** Creates a new instance of SocketServer */
    public SingleSocket() {
    }
    
    public SingleSocket(Socket s) throws IOException {
        
        this.socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }
    
    public void run() {
        try {
            while(true) {
                String str = in.readLine();
                if(str.equals("END")) {
                    break;
                }
                System.out.println("Echoing: " + str);
                out.println(str);
            }
            System.out.println("closing...");
        } catch(IOException ioe) {
            System.out.println(ioe.toString());
        } finally {
            try {
                socket.close();
            } catch(IOException ioe) {
                System.err.println("Socket not closed");
            }
        }
    }
}

public class SocketServer {

    static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Server Started");
        
        try {
            
            while(true) {
                Socket socket = server.accept();
                try {
                    new SingleSocket(socket);
                } catch(IOException ioe) {
                    socket.close();
                }
            }
            
        } finally {
            server.close();
        }
    }
}
