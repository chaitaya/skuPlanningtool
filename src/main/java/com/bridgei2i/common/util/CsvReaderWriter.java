package com.bridgei2i.common.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.taglibs.standard.tag.el.core.OutTag;

import com.bridgei2i.common.exception.*;
import com.bridgei2i.common.constants.*;
import com.bridgei2i.controller.UploadDataController;

import au.com.bytecode.opencsv.CSVReader;


public class CsvReaderWriter {

      private final static Logger logger = Logger.getLogger(CsvReaderWriter.class
                              .getName());
      


      private final Connection con;
      private char seprator;

      /**
       * This Constructor will help to build this class object with DB connection.
       *
       */
      public CsvReaderWriter(Connection connection) {
                  this.con = connection;
                  this.seprator = ','; // It's a default separator
      }

      public char getSeprator() {
                  return seprator;
      }

      public void setSeprator(char seprator) {
                  this.seprator = seprator;
      }

      /**
       * This method will help to Parse CSV file using OpenCSV library and load
       * the CSV data in given database table.
       *
       * @param String
       *            csvFilename:- Name of the CSV file
       * @param String
       *            dbTableName:- Name of the Database table where we want to
       *            insert data.
       * @param boolean deleteTableDataBeforeLoad:- Delete the table data before inserting
       *        new records.
       * @throws Exception
       */
      

      public  String[] readCSVHeader(String csvFilename, String dbTableName,
              boolean deleteTableDataBeforeLoad, Integer referenceId, String referenceColumnName,int calleeFunctionType ) throws Exception {
    	  CSVReader csvReader = null;
		  if (null == this.con) {
		              throw new Exception("Not a valid database connection.");
		  }
		  try {
		
		              csvReader = new CSVReader(new FileReader(csvFilename),
		                                      this.seprator);
		
		  } catch (Exception e) {
		              logger.severe(e.getMessage());
		              throw new Exception("Error occured while executing file. "
		                                      + e.getMessage());
		  }
		  String[] headerRow;
		  headerRow = csvReader.readNext();
		  if (null == headerRow) {
		      throw new FileNotFoundException(
		                              "No header column found in given CSV file."
		                                                      + "Please modify properly the CSV file format and provide the Header Column.");
		  }
		  return headerRow;
      }
      public  ArrayList<String[]> readWriteCSV(String csvFilename, String dbTableName,
              boolean deleteTableDataBeforeLoad, Integer referenceId, String referenceColumnName,int calleeFunctionType) throws Exception {
		
		  CSVReader csvReader = null;
		  if (null == this.con) {
		              throw new Exception("Not a valid database connection.");
		  }
		  try {
		
		              csvReader = new CSVReader(new FileReader(csvFilename),
		                                      this.seprator);
		
		  } catch (Exception e) {
		              logger.severe(e.getMessage());
		              throw new Exception("Error occured while executing file. "
		                                      + e.getMessage());
		  }
		
		  String[] headerRow = csvReader.readNext();
		  if (null == headerRow) {
		      throw new FileNotFoundException(
		                              "No header column found in given CSV file."
		                                                      + "Please modify properly the CSV file format and provide the Header Column.");
		  }
		  
		 if(calleeFunctionType==1){
			 if (headerRow.length<31){
				 
					// TODO Auto-generated catch block
				 throw new Exception("The number of columns in the uploaded CSV file doesn't match the required column count(33)");
				 
			 }
		 }
		 if(calleeFunctionType==2){
			 if (headerRow.length!=13){
				 
					// TODO Auto-generated catch block
				 throw new Exception("The number of columns in the uploaded CSV file doesn't match the required column count(11)");
				 
			 }
		 }
		   /*String [] expectedFields={"Order Week (BV)","Sales Office","Sales Centers","Agent Functional Group","Product Line","Product Hierarchy I","Product Hierarchy II","Product Hierarchy IV","Product Number","PL Class","Product Description","Gross Dollars","Less Discounts","Number of Orders","Quantity","ESC (Final)","ESC/Unit","Product Margin","Product Category",
                                     "Model Roll Up","Model","Processor","Week Range","Op System","ASP","Display","Price Band","Business","PL Roll Up","Scorecard Roll up","Planogram"};
			 
		   for(int i=0;i<expectedFields.length;i++)
		   {
			   if(expectedFields[i].equalsIgnoreCase(headerRow[i])){
				   continue;
			   }
			   else{
				  
				   throw new Exception("The Column name in the given CSV file does match the expected value : Found:\t"+headerRow[i]+"\tExpected:\t"+expectedFields[i]);
			   } 
		   }*/
		   
		   String[] nextLine;
		   ArrayList<String[]> excelData=new ArrayList<String[]>();
		   while ((nextLine = csvReader.readNext()) != null) {
			   
			   excelData.add(nextLine);
		   }
 return excelData;
}

      
      public String [] saveVariableNames(String csvFilename, String dbTableName,
              boolean deleteTableDataBeforeLoad, Integer referenceId, String referenceColumnName) throws Exception {

			  CSVReader csvReader = null;
			  Connection con = null;
			  String query=null;
			  
			  if (null == this.con) {
			              throw new Exception("Not a valid database connection.");
			  }
			  try {
			
			              csvReader = new CSVReader(new FileReader(csvFilename),
			                                      this.seprator);
			
			  } catch (Exception e) {
			              logger.severe(e.getMessage());
			              throw new Exception("Error occured while executing file. "
			                                      + e.getMessage());
			  }
			 
				  String[] headerRow = csvReader.readNext();
				  
				  if (null == headerRow) {
				      throw new FileNotFoundException(
				                              "No header column found in given CSV file."
				                                                      + "Please modify  the CSV file format properly and provide the Header Column.");
				}
				  
				  csvReader.close();
				  logger.info("Variable Insertion Query: " + query);

				  return headerRow;
	
		}
}
