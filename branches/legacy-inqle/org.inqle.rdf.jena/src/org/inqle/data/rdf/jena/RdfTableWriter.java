package org.inqle.data.rdf.jena;

import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;

/**
 * @author David Donohue
 * Jan 1, 2008
 */
public class RdfTableWriter {

	public static String dataTableToString(RdfTable table) {
		String s = "\n";
		s+= "\n=======================================================================";
		s+= "\n==============BEGIN: STRING REPRESENTATION OF RDFTABLE==============";
		s+= "\nQuery=" + table.getQuery();
		s+= varNamesToString(table);
		s+= dataToString(table);
		s+= "\n===============END: STRING REPRESENTATION OF RDFTABLE===============";
		s+= "\n=======================================================================";
		s+= "\n";
		return s;
	}

	public static String dataToString(RdfTable table) {
		String s = "\nDATA";
		List<QuerySolution> querySolutions = table.getResultList();
		int rowNum = 0;
		for (QuerySolution querySolution: querySolutions) {
			s += "\nRow " + rowNum + ": ";
			List<String> varNames = table.getVarNameList();
			int varNum=0;
			for (String varName: varNames) {
				if (varNum > 0) s += " | ";
				s += varNum + "=" + querySolution.get(varName);
				varNum++;
			}
			rowNum++;
		}
		return s;
	}

	public static String varNamesToString(RdfTable table) {
		String s = "\nVAR NAMES";
		List<String> varNames = table.getVarNameList();
		int i=0;
		for (String varName: varNames) {
			s += "\n" + i + ") " + varName;
			i++;
		}
		return s;
	}
	
}