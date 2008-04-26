package org.inqle.testing;

public interface IJUnitTest {
	
	public static final String ID = "org.inqle.testing.IJUnitTest";
	public static final String TEST_CLASS_ATTRIBUTE = "test_class";

	public Class<?> getJUnitTestClass();
	
	public String getName();
	
	public String getDescription();
	
	public void setName(String name);
	
	public void setDescription(String description);
	
}
