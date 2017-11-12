/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.prosa.service;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.prosa.dto.Stat06ntDto;
import com.prosa.dao.Stat06ntImp;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author abraham
 */
public class ReporteService 
{
    
    private Workbook                 wb;
    private Sheet                    sheet;
    private CellStyle                cellStyle;
    private List<Stat06ntDto>         listaReporte;
    private InputStream              fileIn;
    private Stat06ntImp              stat06Imp;
    
    
      public ReporteService() throws IOException 
    {
        fileIn=ReporteService.class.getClassLoader().getResourceAsStream("/stat06nt.xlt");
        this.wb = new HSSFWorkbook(fileIn);//Creamos libro 
        this.sheet=this.wb.getSheetAt(0);//obtiene la hoja actual 
        //createRowsAndCells(sheet);
        listaReporte = new ArrayList<Stat06ntDto>();
        stat06Imp = new Stat06ntImp();
    }
     //CHANGE TO THE MODEL 
    public Workbook createReportStat06Nt(String query)
    {
        
        stat06Imp.buildReport(query);
       int i=0;
       for(Stat06ntDto rep :stat06Imp.getListStat06Nt())
        {  
            Row row = this.sheet.createRow(i+1);
            Cell cellFecha = row.createCell(0);
            cellFecha.setCellValue(rep.getFecha());
            
            Cell cellBanco = row.createCell(1);
            cellBanco.setCellValue(rep.getCodBcoEmis());

            
            Cell cellRetMn = row.createCell(2);
            cellRetMn.setCellValue(rep.getRetiroMn());
           
            Cell cellRetDlls = row.createCell(3);
            cellRetDlls.setCellValue(rep.getRetiroDlls());
            
            
            Cell cellVenGen = row.createCell(4);
            cellVenGen.setCellValue(rep.getVtaGenerica());
            
           
            Cell cellPagoElec = row.createCell(5);
            cellPagoElec.setCellValue(rep.getPagoElec());
            
           
            Cell cellConsultas = row.createCell(6);
            cellConsultas.setCellValue(rep.getConsultas());           
            
            Cell cellCambioNip = row.createCell(7);
            cellCambioNip.setCellValue(rep.getCambioNip());
           
            Cell cellRech = row.createCell(8);
            cellRech.setCellValue(rep.getRechazos());
           
            Cell cellRetenidas = row.createCell(9);
            cellRetenidas.setCellValue(rep.getRetenidas());            
            
            Cell cellTransfer = row.createCell(10);
            cellTransfer.setCellValue(rep.getTransfer());
           
            Cell cellDep = row.createCell(11);
            cellDep.setCellValue(rep.getDeposito());
            
            
            Cell cellUltMov = row.createCell(12);
            cellUltMov.setCellValue(rep.getUltMovs());
            
           
            Cell cellSurcharge = row.createCell(13);
            cellSurcharge.setCellValue(rep.getTranfee());

        
        }
       
       return this.wb;
        
    }

    public Workbook getWb() {
        return wb;
    }

    public void setWb(Workbook wb) {
        this.wb = wb;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public List<Stat06ntDto> getListaReporte() {
        return listaReporte;
    }

    public void setListaReporte(List<Stat06ntDto> listaReporte) {
        this.listaReporte = listaReporte;
    }  
      
      
    
}
