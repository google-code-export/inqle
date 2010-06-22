package org.inqle.qa.gae;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.inqle.qa.QueryCriteria;

import com.google.inject.Inject;

@Deprecated
public class GaeQueryFactory {

	private PersistenceManagerFactory pmf;

	@Inject
	public GaeQueryFactory(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}
	
	public Query getQuery(QueryCriteria qc) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Query q = pm.newQuery(qc.getClassToQuery(), getFilterClause(qc));
		return q;
	}

//	private String getFilterClause(QueryCriteria qc) {
//		String clause = "";
//		Collection<Filter> filters = getFilter
//	}
	
	
}
