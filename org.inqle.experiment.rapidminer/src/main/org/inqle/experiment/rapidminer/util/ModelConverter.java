package org.inqle.experiment.rapidminer.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.inqle.data.rdf.jenabean.Arc;
import org.inqle.data.sampling.ArcTable;
import org.inqle.data.sampling.ArcTableFactory;
import org.inqle.data.sampling.IDataTable;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.example.table.NominalMapping;
import com.rapidminer.example.table.PolynominalMapping;
import com.rapidminer.tools.Ontology;

/**
 * Converts Jena Model into RapidMiner MemoryExampleTable
 * @author David Donohue
 * Oct 10, 2008
 */
public class ModelConverter {

	private OntModel ontModel;
	private Resource subjectClass;

	private static Logger log = Logger.getLogger(ModelConverter.class);
	
	public ModelConverter(OntModel ontModel, Resource subjectClass) {
		this.ontModel = ontModel;
		this.subjectClass = subjectClass;
	}
	
	public MemoryExampleTable createExampleTable() {
		
		ArcTableFactory arcTableFactory = new ArcTableFactory(ontModel);
		ArcTable arcTable = arcTableFactory.createArcTable(subjectClass);
		
		List<Attribute> attributes = getAttributes(arcTable);
		
		MemoryExampleTable table = new MemoryExampleTable(attributes);

		//loop through rows of the DataTable, filling the table
		List<List<Object>> rows = arcTable.getRows();
		for (List<Object> row: rows) {//loop thru each row
//		for (int rowIndex = 0; rowIndex < dataTable.getRows().size(); rowIndex++) {//loop thru each row
			double[] data = new double[attributes.size()];

			//loop thru each column
			for (int colIndex = 0; colIndex < attributes.size(); colIndex++) {
				Attribute attribute = attributes.get(colIndex);
				
				Object cell = row.get(colIndex);

				double val = 0;
				if (attribute.getValueType() == Ontology.REAL) {//Numeric
					try {
						val = Double.parseDouble(cell.toString());
					} catch (Exception e) {
						log.error("Unable to cast datum '" + cell.toString() + "' as a number.", e);
					}
				} else if (attribute.getValueType() == Ontology.SINGLE_VALUE) {
					//do not add anything?
				} else {//Nominal or String
//					log.trace("Get mapping for non-numeric attribute: " + attribute + "; ALL VALUES=" + arcTable.getColumnValues(columnIndex));
					NominalMapping mapping = attribute.getMapping();
					//val = column.getValuesSet().indexOf(cell.toString());
					val = mapping.mapString(cell.toString());
					if (attribute.getValueType() == Ontology.POLYNOMINAL) {
						log.info("Cell val '" + cell + "' maps to " + val);
					}
				}
				//fill with proper data here
				data[colIndex] = val;
			}//loop back to next column
			
			//add data row
			table.addDataRow(new DoubleArrayDataRow(data));
		}//loop back to next row
		
		
		
		
		return table;
	}
	
	private List<Attribute> getAttributes(ArcTable arcTable) {
		List<Attribute> attributes = new ArrayList<Attribute>();
		int columnCount = arcTable.getHeaders().size();
		for (int i=0; i<columnCount; i++) {
			int dataType = arcTable.getDataType(i);
			int columnType = arcTable.getColumnType(i);
			int rapidMinerDataType = Ontology.POLYNOMINAL;
			if (columnType==IDataTable.COLUMN_TYPE_NO_VALUES || dataType==IDataTable.DATA_TYPE_NO_VALUES) {
				rapidMinerDataType = Ontology.SINGLE_VALUE;
			} else if (columnType==IDataTable.COLUMN_TYPE_ONE_VALUE) {
				rapidMinerDataType = Ontology.SINGLE_VALUE;
			} else if (columnType==IDataTable.COLUMN_TYPE_ALL_UNIQUE && dataType==IDataTable.DATA_TYPE_STRING) {
				rapidMinerDataType = Ontology.STRING;
			} else if (dataType==IDataTable.DATA_TYPE_DATE) {
				rapidMinerDataType = Ontology.DATE_TIME;
			} else if(dataType==IDataTable.DATA_TYPE_NUMERIC) {
				rapidMinerDataType = Ontology.REAL;
			}
			
			if (rapidMinerDataType != Ontology.REAL) {
				PolynominalMapping mapping = new PolynominalMapping();
				LinkedHashSet<Object> valuesSet = arcTable.getColumnDistinctValues(i);
				int j=0;
				for (Object strObj: valuesSet) {
					String strVal = strObj.toString();
					log.debug("Mapping " + i + ": " + strVal);
					mapping.mapString(strVal.trim());
					j++;
				}
			}
			
			
			Arc headerArc = arcTable.getColumns().get(i);
			Attribute anAttribute = AttributeFactory.createAttribute(headerArc.toString(), rapidMinerDataType);
			attributes.add(anAttribute);
		}
		return attributes;
	}

}
