package org.inqle.data.rdf.jena;

import java.util.ArrayList;
import java.util.List;


import com.hp.hpl.jena.query.QuerySolution;

public class RdfTable {

	protected List<String> varNameList = new ArrayList<String>();
	protected List<QuerySolution> resultList = new ArrayList<QuerySolution>();
	private QueryCriteria queryCriteria = null;
	private Exception exception = null;
	
	/**
	 * Returns the List of QuerySolution s that constitute the data of this table
	 * @return resultList
	 */
	public List<QuerySolution> getResultList() {
		return resultList;
	}
	
	/**
	 * Returns the List of varName (Strings)
	 * @return
	 */
	public List<String> getVarNameList() {
		return varNameList;
	}

	public void setResultList(List<QuerySolution> list) {
		this.resultList = list;
	}

	public void setVarNameList(List<String> resultVars) {
		this.varNameList = resultVars;
	}
	
	public String getQuery() {
		return this.queryCriteria.getQuery();
	}
	
	public void setQueryCriteria(QueryCriteria rs) {
		this.queryCriteria = rs;		
	}
	
	public QueryCriteria getQueryCriteria() {
		return queryCriteria;
	}

	public Exception getError() {
		return this.exception ;
	}

	public void setError(Exception exception) {
		this.exception = exception;
	}

	public int countResults() {
		if (resultList == null) {
			return 0;
		} else {
			return resultList.size();
		}
		
	}
}
