package org.inqle.qa.gae;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.inqle.qa.Queryer;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.inject.Inject;
import com.google.appengine.api.datastore.Key;

public class GaeQueryer implements Queryer {

	private PersistenceManagerFactory pmf;

	@Inject
	public GaeQueryer(PersistenceManagerFactory pmf) {
		this.pmf = pmf;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> classToQuery, String filterClause) {
		
		List<T> results = null;
		Query q = null;
		try {
			PersistenceManager pm = pmf.getPersistenceManager();
			q = pm.newQuery(classToQuery, filterClause);
			results = (List<T>) q.execute();
		} finally {
	        q.closeAll();
	    }
		return results;
	}

	public <T> T getObject(Class<T> classToQuery, String id) {
		T result = null;
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Key key = KeyFactory.createKey(classToQuery.getSimpleName(), id);
			result = pm.getObjectById(classToQuery, key);
		} finally {
	        pm.close();
	    }
		return result;
	}

	public <T> void store(T object) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(object);
		} finally {
	        pm.close();
	    }		
	}

	public <T> void store(Collection<T> objects) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			for (T object: objects) {
				pm.makePersistent(object);
			}
		} finally {
	        pm.close();
	    }
	}

}
