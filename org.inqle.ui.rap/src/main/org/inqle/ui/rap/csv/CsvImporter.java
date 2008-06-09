package org.inqle.ui.rap.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.apache.commons.csv.writer.CSVConfig;
import org.apache.commons.csv.writer.CSVConfigGuesser;
import org.apache.log4j.Logger;
import org.inqle.data.rdf.RDF;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * Performs importation of a CSV file into RDF.
 * 
 * Assumes that the headers are in the first row.
 * Assumes that the data begins in the row after the header.
 * 
 * Supports 3 methods of naming the subject:
 * Sequential number, starting with 1
 * Random UUID a 16 digit hexadecimal number.
 * Value from specified column (converted into form that is acceptable to be appended onto a URI)
		
 * Parses each cell to see if it matches any of these data types:
 * Integer
 * Double (real number)
 * Date, using these date patterns: "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", 
 * "yyyy.MM.dd HH:mm:ss",	"yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd"
 * 
 * If the cell cannot be cast to any of these then it is stored as a String data type.
 * @author David Donohue
 * Jun 3, 2008
 */
public class CsvImporter {

	public static final int ID_TYPE_SEQUENTIAL = 0;
	public static final int ID_TYPE_UUID = 1;
	public static final int ID_TYPE_CELL_VALUE = 2;
	public static final String[] ID_TYPES = {
		"Sequential (use only if you use a unique subject prefix)",
		"Random UUID (always safe)",
		"Value from specified column (gets converted into a URI-safe format)"
	};
	
	private static final String[] DATE_FORMATS_TO_TRY = {
		"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
		"yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd"
	};
	
	//the final list of predicate URIs
	private List<String> columnPredicateUris = new ArrayList<String>();
	
	private CSVParser csvParser;
	
	private int countSavedStatements = 0;
	private int countSavedRows = 0;
	
	private static Logger log = Logger.getLogger(CsvImporter.class);
	
	/**
	 * By default, the header is the first row
	 */
	private int headerIndex = 0;
	
	/**
	 * By default, the subject column is the first column
	 */
	private int subjectIndex = 0;
	
	/**
	 * 
	 */
	private int idType = ID_TYPE_UUID;
	
	/**
	 * the string to append before the value in the subject column.
	 * Default to UNKNOWN_SUBJECT
	 */
	private String subjectClassUri = RDF.UNKNOWN_SUBJECT;
	private String subjectPrefix = subjectClassUri + "/";
	
	private String[][] rawData;
	private File file;
	private Exception error;
	
	                 
//	public CsvImporter(InputStream inputStream) {
//		log.info("Creating CSVConfigGuesser for inputStream:" + inputStream);
//		//CSVConfig csvConfig = CSVConfig.guessConfig(inputStream);
//		CSVConfigGuesser configGuesser = new CSVConfigGuesser(inputStream);
//		log.info("Guessing config...");
//		CSVConfig csvConfig = configGuesser.guess();
//		log.info("Creating CSV strategy for config:" + csvConfig);
//		CSVStrategy csvStrategy = new CSVStrategy(csvConfig.getDelimiter(), csvConfig.getValueDelimiter(), CSVStrategy.COMMENTS_DISABLED);
//		log.info("Creating CSV parser...");
//		csvParser = new CSVParser(new BufferedReader(new InputStreamReader(inputStream)), csvStrategy);
//		log.info("Created CSVParser:" + csvParser);
//	}

	public CsvImporter(File file) {
		this.file = file;
		log.trace("Creating CSVConfigGuesser for file:" + file);
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

	public int getSubjectIndex() {
		return subjectIndex;
	}

	public void setSubjectIndex(int subjectIndex) {
		this.subjectIndex = subjectIndex;
	}

	/**
	 * Get the data of the CSV, as an array rows, where each row is an array of Strings.
	 * @return
	 */
	public String[][] getRawData() {
		//if (rawData == null) {
//		String[][] rawData = null;
//			try {
//				rawData = csvParser.getAllValues();
//			} catch (IOException e) {
//				log.error("Unable to read file as comma separated values (CSV) file.", e);
//				e.printStackTrace();
//			}
		//}
		return rawData;
	}

	/**
	 * The prefix to use for the subject for each row
	 * @return
	 */
	public String getSubjectPrefix() {
		return subjectPrefix;
	}

	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	/**
	 * The ID type is the method for generating the ID.  Options include:
	 * ID_TYPE_SEQUENTIAL = 0;	use a sequential number, starting with 1
	 * ID_TYPE_UUID = 1;
	public static final int ID_TYPE_CELL_VALUE = 2;
	 * @return
	 */
	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}
	
	public void setPredicateUri(int columnIndex, String predicateUri) {
		columnPredicateUris.set(columnIndex, predicateUri);
	}
	
	public String getPredicateUri(int columnIndex) {
		return columnPredicateUris.get(columnIndex);
	}
	
	/**
	 * Save the CSV file as a number of RDF statements, into the specified Jena model
	 * @param model
	 */
	public boolean saveStatements(Model model) {
		countSavedStatements = 0;
		countSavedRows = 0;
		String[][] data = getRawData();
		model.begin();
		
		try {
			for (int rowIndex = headerIndex + 1; rowIndex < data.length; rowIndex++) {
				log.info("Storing row #" + (rowIndex + 1) + " ...");
				String[] row = data[rowIndex];
				String subjectUri = getSubjectUri(row, rowIndex);
				Resource subjectResource = model.createResource(subjectUri);
				String predicateUri = RDF.TYPE;
				Property predicateProperty = model.createProperty(predicateUri);
				Resource subjectClass = model.createResource(getSubjectClassUri());
				
				Statement subjectTypeStatement = model.createStatement(subjectResource, predicateProperty, subjectClass);
				model.add(subjectTypeStatement);
				
				Literal objectLiteral = null;
				for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
					predicateUri = getPredicateUri(columnIndex);
					//if the predicate is null or blank, continue to next column
					if (predicateUri == null || predicateUri.length() == 0) {
						continue;
					}
					//TODO support for qnames here
					//TODO add another wizard page to map any new/unknown prefixes.
					predicateProperty = model.createProperty(predicateUri);
					String cell = row[columnIndex];
					log.info("Cell for row #" + rowIndex + ", col #" + columnIndex + " = " + cell);
					//if this cell is null or blank, continue to the next column
					if (cell == null || cell.length() == 0) {
						continue;
					}
					
					//zero the objects to be created for this cell
					Statement statementForThisCell = null;
					objectLiteral = null;
					
					//if integer, store integer
					try {
						Integer cellInteger = Integer.parseInt(cell);
						objectLiteral = ResourceFactory.createTypedLiteral(cellInteger);
					} catch (NumberFormatException e) {
						//leave null
					}
					if (objectLiteral == null) {
						try {
							Double cellDouble = Double.parseDouble(cell);
							objectLiteral = ResourceFactory.createTypedLiteral(cellDouble);
						} catch (NumberFormatException e) {
							//leave null
						}
					}
					if (objectLiteral == null) {
						XSDDateTime cellDate = tryToParseDate(cell);
						if (cellDate != null) {
							objectLiteral = ResourceFactory.createTypedLiteral(cellDate);
						}
					}
					
					//if unable to parse, use the string value
					if (objectLiteral == null) {
						objectLiteral = ResourceFactory.createTypedLiteral(cell);
					}
					
					statementForThisCell = model.createStatement(subjectResource, predicateProperty, objectLiteral);
					model.add(statementForThisCell);

					countSavedStatements++;
					log.info("#" + countSavedStatements + " Saved:" + statementForThisCell);
				}
				countSavedRows++;
			}
			model.commit();
		} catch (Exception e) {
			model.abort();
			this.error = e;
			log.error("Error inserting statements", e);
			return false;
		}
		return true;
	}

	public String getSubjectClassUri() {
		return subjectClassUri;
	}

	private XSDDateTime tryToParseDate(String putativeDateString) {
		XSDDateTime xsdDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		for (String dateMask: DATE_FORMATS_TO_TRY) {
			dateFormat.applyPattern(dateMask);
			Date parsedDate;
			try {
				parsedDate = dateFormat.parse(putativeDateString);
			} catch (ParseException e) {
				continue;
			}
			Calendar calendarVal = Calendar.getInstance();
			calendarVal.setTime(parsedDate);
			xsdDate = new XSDDateTime(calendarVal);
			if (xsdDate != null) {
				break;
			}
		}
		return xsdDate;
	}

	private String getSubjectUri(String[] row, int rowIndex) {
		String uriStr = getSubjectPrefix();
		switch (idType) {
			case ID_TYPE_SEQUENTIAL:
				uriStr += (rowIndex + 1);
				break;
			case ID_TYPE_UUID:
				uriStr += UUID.randomUUID().toString();
				break;
			case ID_TYPE_CELL_VALUE:
				try {
//					URI uri = new URI(uriStr);
//					uri.resolve(row[getSubjectIndex()]);
					URI uri = new URI(row[getSubjectIndex()]);
					uri.resolve(uriStr);
					uriStr += uri.toString();
					break;
				} catch (URISyntaxException e) {
					log.error("Unable to convert subject prefix to URI:" + uriStr);
				}
				
		}
		return uriStr;
	}

	public int getCountSavedStatements() {
		return countSavedStatements;
	}

	public int getCountSavedRows() {
		return countSavedRows;
	}

	public File getFile() {
		return file;
	}

	public Exception getError() {
		return error;
	}

	public List<String> getColumnPredicateUris() {
		return columnPredicateUris;
	}

	public void setColumnPredicateUris(List<String> columnPredicateUris) {
		this.columnPredicateUris = columnPredicateUris;
	}

	public void setSubjectClassUri(String subjectClassUri) {
		this.subjectClassUri = subjectClassUri;
	}
	
	public void cleanUp() {
		file.delete();
	}
}