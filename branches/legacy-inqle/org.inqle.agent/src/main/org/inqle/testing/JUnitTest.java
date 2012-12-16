package org.inqle.testing;

public class JUnitTest implements IJUnitTest {

	private String name;
	private String description;
	private Class<?> jUnitTestClass;

	public String getDescription() {
		return description;
	}

	public Class<?> getJUnitTestClass() {
		return jUnitTestClass;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setJUnitTestClass(Class<?> unitTestClass) {
		jUnitTestClass = unitTestClass;
	}

}
