package org.inqle.ui.rap.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.csv.writer.CSVConfig;
import org.apache.commons.csv.writer.CSVConfigGuesser;
import org.apache.log4j.Logger;

/**
 * Reads a CSV file, captures header, column, and row info.
 * 
 * Assumes that the headers are in the first row.
 * Assumes that the data begins in the row after the header.
 * 
 * If the cell cannot be cast to any of these then it is stored as a String data type.
 * @author David Donohue
 * Jun 3, 2008
 */
public class CsvReader {
	
	private static final String PREFERRED_HEADER_DELIMITER = ", ";

	private CSVParser csvParser;
	
	private static Logger log = Logger.getLogger(CsvReader.class);
	
	/**
	 * By default, the header is the first row
	 */
	private int headerIndex = 0;
	
	private String[][] rawData;
	private File file;
	private Exception error;

	public CsvReader(File file) {
		//first convert Max line breaks (\r) to Unix line breaks (\n)
		log.info("Creating CSV Importer, using file=" + file + "...");    
    
		String fileText = "";
//		try {
//			log.info("loading file using readFileToString..." + file + "; can read?" + file.canRead());
//			fileText = FileUtils.readFileToString(file);
//			log.info("loaded file.");
//		} catch (Exception e) {
//			log.error("Error loading file " + file, e);
//			this.error = e;
//			return;
//		}
		
		log.info("loading file using readFileToString..." + file + "; can read?" + file.canRead());
		fileText = CsvReader.readFileToString(file);
		log.info("loaded file.");
		
		//load and save the file, to fix any problems with CSV files saved using older Mac method 
		//(which used \r as line feed)
		log.info("Loaded CSV file, retrieved text:\n" + fileText);
//		fileText.replaceAll("\\\\r\\\\n", "\n");
//		fileText.replaceAll("\\\\r", "\n");
//		log.info("Replaced Carriage Return characters.  CSV file text=\n" + fileText);
		try {
			CsvReader.writeStringToFile(file, fileText);
		} catch (Exception e) {
			log.error("Error writing file " + file.getAbsolutePath(), e);
			this.error = e;
			return;
		}
		
		this.file = file;
		log.info("Creating CSVConfigGuesser for file:" + file);
		//CSVConfig csvConfig = CSVConfig.guessConfig(inputStream);
		try {
			FileInputStream fis = new FileInputStream(file);
			CSVConfigGuesser configGuesser = new CSVConfigGuesser(fis);
			log.trace("Guessing config...");
			CSVConfig csvConfig = configGuesser.guess();
			log.trace("Creating CSV strategy for config:" + csvConfig);
			CSVStrategy csvStrategy = new CSVStrategy(csvConfig.getDelimiter(), csvConfig.getValueDelimiter(), CSVStrategy.COMMENTS_DISABLED);
			log.trace("Creating CSV parser...");
			FileReader fileReader = new FileReader(file);
			csvParser = new CSVParser(new BufferedReader(fileReader), csvStrategy);
			log.trace("Created CSVParser:" + csvParser);
			//populate rawData
			//try {
			rawData = csvParser.getAllValues();
			
			fileReader.close();
			fis.close();
			//} catch (IOException e) {
				//log.error("Unable to read file as comma separated values (CSV) file.", e);
			//}
			
		} catch (Exception e) {
			log.error("Error creating CSVParser:", e);
			this.error = e;
		}
		
	}
	
	public int getHeaderIndex() {
		return headerIndex;
	}

	public void setHeaderIndex(int headerIndex) {
		this.headerIndex = headerIndex;
	}

	/**
	 * Get the data of the CSV, as an array rows, where each row is an array of Strings.
	 * @return
	 */
	public String[][] getRawData() {
		return rawData;
	}

	public File getFile() {
		return file;
	}

	public Exception getError() {
		return error;
	}
	
	public void cleanUp() {
		file.delete();
	}
	
	public static String readFileToString(File file) {
		 StringBuffer contents = new StringBuffer();
     BufferedReader reader = null;

     try
     {
         reader = new BufferedReader(new FileReader(file));
         String text = null;

         // repeat until all lines is read
         while ((text = reader.readLine()) != null)
         {
             contents.append(text)
                 .append(System.getProperty("line.separator"));
         }
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         try {
             if (reader != null) {
                 reader.close();
             }
         } catch (IOException e) {
             log.error("Error closing file " + file);
         }
     }
     return contents.toString();
	}
	
	public static void writeStringToFile(File file, String string) {
		log.trace("Saving string to file " + file + "\n" + string);
		try {
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter buffWriter = new BufferedWriter(fileWriter);
			buffWriter.write(string);
			buffWriter.close();
			fileWriter.close();
		} catch (Exception e) {
			log.error("Error saving file " + file, e);
		}
	}
	
	public String[] getHeaders() {
		String[][] data = getRawData();
		return data[getHeaderIndex()];
	}

	public String getHeaderString() {
		String str = "";
		int i = 0;
		for (String header: getHeaders()) {
			if (i > 0) str += PREFERRED_HEADER_DELIMITER;
			str += header;
			i++;
		}
		return str;
	}
	
	public int getColumnIndex(String header) {
		int i=0;
		for (String colHeader: getHeaders()) {
			if (colHeader == null) continue;
			if (colHeader.toLowerCase().trim().equals(header)) return i;
			i++;
		}
		//no match
		return -1;
	}
}