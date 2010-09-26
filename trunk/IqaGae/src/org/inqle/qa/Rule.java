package org.inqle.qa;

import java.util.List;

public class Rule {

	private String entityKey;
	private String id;
	private String name;
	private String description;
	private List<Rule> andRules;
	private List<Rule> orRules;
	private String type;
	private String property;
	private String operator;
	private String values;
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public String getEntityKey() {
		return entityKey;
	}
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAndRules(List<Rule> andRules) {
		this.andRules = andRules;
	}
	public void setOrRules(List<Rule> orRules) {
		this.orRules = orRules;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
}
