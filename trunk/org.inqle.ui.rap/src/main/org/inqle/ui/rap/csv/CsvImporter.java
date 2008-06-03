package org.inqle.ui.rap.csv;

import java.io.BufferedReader;
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
import org.apache.log4j.Logger;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class CsvImporter {

	public static final int ID_TYPE_SEQUENTIAL = 0;
	public static final int ID_TYPE_UUID = 1;
	public static final int ID_TYPE_CELL_VALUE = 2;
	public static final String[] idTypes = {
		"Sequential",
		"Random UUID",
		"Value from specified column"
	};
	
	private static final String[] DATE_FORMATS_TO_TRY = {
		"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
		"yyyy/MM/dd", "yyyy-MM-dd", "yyyy.MM.dd",
	};
	
	private List<String> columnPredicates = new ArrayList<String>();
	
	private CSVParser csvParser;
	
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
	private int idType = ID_TYPE_SEQUENTIAL;
	
	/**
	 * the string to append before the value in the subject column, 
	 */
	private String subjectPrefix;
	
	private String[][] rawData;
	                 
	public CsvImporter(InputStream inputStream) {
		CSVConfig csvConfig = CSVConfig.guessConfig(inputStream);
		CSVStrategy csvStrategy = new CSVStrategy(csvConfig.getDelimiter(), csvConfig.getValueDelimiter(), CSVStrategy.COMMENTS_DISABLED);;
		csvParser = new CSVParser(new BufferedReader(new InputStreamReader(inputStream)), csvStrategy);
	}

	public long getHeaderIndex() {
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

	public String[][] getRawData() {
		if (rawData == null) {
			try {
				rawData = csvParser.getAllValues();
			} catch (IOException e) {
				log.error("Unable to read file as comma separated values (CSV) file.", e);
				e.printStackTrace();
			}
		}
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

	public int getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = idType;
	}
	
	public void setPredicate(int columnIndex, String predicateUri) {
		columnPredicates.set(columnIndex, predicateUri);
	}
	
	public String getPredicate(int columnIndex) {
		return columnPredicates.get(columnIndex);
	}
	
	/**
	 * Save the CSV file as a number of RDF statements, into the specified Jena model
	 * @param model
	 */
	public void saveStatements(Model model) {
		String[][] data = getRawData();
		model.begin();
		for (int rowIndex = headerIndex + 1; rowIndex < data.length; rowIndex++) {
			String[] row = data[rowIndex];
			String subjectUri = getSubjectUri(row, rowIndex);
			Resource subjectResource = model.createResource(subjectUri);
			Literal objectLiteral = null;
			for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
				String predicateUri = getPredicate(columnIndex);
				//if the predicate is null or blank, continue to next column
				if (predicateUri == null || predicateUri.length() == 0) {
					continue;
				}
				Property predicateProperty = model.createProperty(predicateUri);
				String cell = row[columnIndex];
				//if this cell is null or blank, continue to the next column
				if (cell == null || cell.length() == 0) {
					continue;
				}
				Statement statementForThisCell = null;
				
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
			}
		}
		model.commit();
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
				uriStr += rowIndex;
				break;
			case ID_TYPE_UUID:
				uriStr += UUID.randomUUID().toString();
				break;
			case ID_TYPE_CELL_VALUE:
				try {
					URI uri = new URI(uriStr);
					uri.resolve(row[getSubjectIndex()]);
					uriStr += uri.toString();
					break;
				} catch (URISyntaxException e) {
					log.error("Unable to convert subject prefix to URI:" + uriStr);
				}
				
		}
		return uriStr;
	}
}