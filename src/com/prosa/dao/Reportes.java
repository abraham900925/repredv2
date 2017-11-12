package com.prosa.dao;


import com.prosa.dto.Stat06ntDto;
import org.apache.poi.ss.usermodel.Workbook;
import java.sql.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author abraham
 */
public  interface Reportes 
{
    public void buildReport(String query);
    public String buildTableReport(ResultSet rs);
    
    
}
