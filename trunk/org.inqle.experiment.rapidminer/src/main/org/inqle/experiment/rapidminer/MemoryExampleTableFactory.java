package org.inqle.experiment.rapidminer;

import org.inqle.data.sampling.ArcTable;
import org.inqle.data.sampling.IDataTable;
import org.inqle.experiment.rapidminer.util.ArcTableToMemoryExampleTableConverter;

import com.rapidminer.example.table.MemoryExampleTable;

public class MemoryExampleTableFactory {

	public MemoryExampleTable createExampleTable(IDataTable dataTable) {
		MemoryExampleTable exampleTable = null;
		if (dataTable instanceof ArcTable) {
			exampleTable = ArcTableToMemoryExampleTableConverter.createExampleTable((ArcTable)dataTable);
		}
		return exampleTable;
	}

}
