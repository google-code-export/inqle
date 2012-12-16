package org.inqle.qa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Deprecated
public class QueryCriteria {

	private class Filter {
		
		private String param;
		private String operator;
		private Collection<String> values;
		private String type;

		public Filter(String param, String operator, Collection<String> values, String type) {
			this.param = param;
			this.operator = operator;
			this.values = values;
			this.type = type;
		}

		public String getParam() {
			return param;
		}

		public String getOperator() {
			return operator;
		}

		public Collection<String> getValues() {
			return values;
		}

		public String getType() {
			return type;
		}
	}

	private static final int DEFAULT_COUNT = 0;

	private Class<?> classToQuery;

	private List<Filter> filters = new ArrayList<Filter>();
	private String sort;
	private int count = DEFAULT_COUNT;
	private int offset = 0;
	
	public Class<?> getClassToQuery() {
		return classToQuery;
	}

	public QueryCriteria setClassToQuery(Class<?> classToQuery) {
		this.classToQuery = classToQuery;
		return this;
	}

	public QueryCriteria addFilter(Filter filter) {
		filters.add(filter);
		return this;
	}
	
	public QueryCriteria addFilter(String param, String operator, List<String> values, String type) {
		filters.add(new Filter(param, operator, values, type));
		return this;
	}
	
	public QueryCriteria setFilters(List<Filter> filters) {
		this.filters = filters;
		return this;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public QueryCriteria setSort(String sort) {
		this.sort = sort;
		return this;
	}

	public String getSort() {
		return sort;
	}

	public int getCount() {
		return count;
	}

	public QueryCriteria setCount(int count) {
		this.count = count;
		return this;
	}

	public int getOffset() {
		return offset;
	}

	public QueryCriteria setOffset(int offset) {
		this.offset = offset;
		return this;
	}
	
	

	
}
