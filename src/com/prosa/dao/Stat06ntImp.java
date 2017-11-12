/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.prosa.dao;

import com.prosa.exceptions.WellException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.prosa.dto.Stat06ntDto;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import java.lang.StringBuffer;
import com.prosa.sql.Database;
/**
 *
 * @author abraham
 */
public class Stat06ntImp implements Reportes
{

    private Database db ;
    private List<Stat06ntDto> listStat06Nt;
    public Stat06ntImp()
    {
      this.db = new Database();
      listStat06Nt = new ArrayList<Stat06ntDto>();
    }

    @Override
    public void buildReport(String query) 
    {
        ResultSet    rs=null;
        Stat06ntDto  stat06;
        try
        {
            //@Input parameters wb,
            //@Return List<Stat06NtDto>
            //Hacer la conexion a la bd y guardar los registros en la lista dto
            
            db.doConnection();//Hacemos conexion 
            db.setQuerySelect(query);//ejecutamos query
            rs=db.getResultSet();//regresamos resultado

          while(rs.next())
          {
              stat06 = new Stat06ntDto();
              stat06.setFecha(rs.getDate(1));
              stat06.setCodBcoEmis(rs.getString(2));
              stat06.setRetiroMn(rs.getDouble(3));
              stat06.setRetiroDlls(rs.getDouble(4));
              stat06.setVtaGenerica(rs.getDouble(5));
              stat06.setPagoElec(rs.getDouble(6));
              stat06.setConsultas(rs.getDouble(7));
              stat06.setCambioNip(rs.getDouble(8));
              stat06.setRechazos(rs.getDouble(9));
              stat06.setRetenidas(rs.getDouble(10));
              stat06.setTransfer(rs.getDouble(11));
              stat06.setDeposito(rs.getDouble(12));
              stat06.setUltMovs(rs.getDouble(13));
              stat06.setTranfee(rs.getDouble(14));
              this.listStat06Nt.add(stat06);
          }
          
          
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Stat06ntImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (WellException ex) 
        {
            Logger.getLogger(Stat06ntImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String buildTableReport(ResultSet rs) 
    {
        
       StringBuffer table = new StringBuffer();
       table.append("<div class=\"text-left\">");
         table.append("<form action=\"ExcelServlet\"  method=\"post\" name=\"frmExcel\" id=\"frmExcel\" >");
          table.append("<button type=\"submit\" class=\"btn btn-primary\">Excel</button>");
         table.append("</form>");
          table.append("<button type=\"button\" class=\"btn btn-primary\">Pdf</button>");
       table.append("</div>");
       table.append("<br/>");
       table.append("<br/>");
       table.append("<table id=\"example\" class=\"table table-striped table-bordered\" cellspacing=\"0\" width=\"100%\">");
       table.append("<thead>");
       table.append("<tr>");
          table.append("<th>Fecha</th>");
          table.append("<th>BANCO</th>");
          table.append("<th>Retiro Mn</th>");        
          table.append("<th>Retiro Ddls</th>");
          table.append("<th>Ven Gen</th>");
          table.append("<th>Pago Elec</th>");
          table.append("<th>Consultas</th>");
          table.append("<th>Cambio Nip</th>");
          table.append("<th>Rechazos</th>");
          table.append("<th>Retenidas</th>");
          table.append("<th>Transfer</th>");
          table.append("<th>Deposito</th>");
          table.append("<th>Ult Movs</th>");
          table.append("<th>Surcharge</th>");
       table.append("</tr>");
       table.append("</thead>");
       table.append("<tbody>");   
          try
        {
            while(rs.next())
            {
                
                table.append("<tr>"); 
                table.append("<td>"+rs.getString(1)+"</td>");
                table.append("<td>"+rs.getString(2)+"</td>");
                table.append("<td>"+rs.getString(3)+"</td>");
                table.append("<td>"+rs.getString(4)+"</td>");
                table.append("<td>"+rs.getString(5)+"</td>");
                table.append("<td>"+rs.getString(6)+"</td>");
                table.append("<td>"+rs.getString(7)+"</td>");
                table.append("<td>"+rs.getString(8)+"</td>");
                table.append("<td>"+rs.getString(9)+"</td>");
                table.append("<td>"+rs.getString(10)+"</td>");
                table.append("<td>"+rs.getString(11)+"</td>");
                table.append("<td>"+rs.getString(12)+"</td>");
                table.append("<td>"+rs.getString(13)+"</td>");
                table.append("<td>"+rs.getString(14)+"</td>");
                table.append("</tr>");
                
            }
            
            
          table.append("</tbody>");
          table.append("</table>");
        }
        catch (SQLException ex) 
        {
            ex.getCause();
        }
           
       
        return table.toString();
    }

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public List<Stat06ntDto> getListStat06Nt() {
        return listStat06Nt;
    }

    public void setListStat06Nt(List<Stat06ntDto> listStat06Nt) {
        this.listStat06Nt = listStat06Nt;
    }
    
    
}

