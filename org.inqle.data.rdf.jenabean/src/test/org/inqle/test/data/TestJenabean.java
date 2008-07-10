package org.inqle.test.data;

import java.util.Arrays;
import java.util.List;

import org.inqle.data.rdf.jenabean.JenabeanWriter;
import org.junit.Test;

import thewebsemantic.Bean2RDF;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestJenabean {

	@Test
	public void saveAndLoadBeanWithCollections() {
		BeanWithCollections beanWithCollections = new BeanWithCollections();
		beanWithCollections.setId("beanWithCollections");
		Model memoryModel = ModelFactory.createDefaultModel();
		List<String> stringList1 = Arrays.asList(new String[] {"1", "2", "3"});
		beanWithCollections.setStrings1(stringList1);
		List<String> stringList2 = Arrays.asList(new String[] {"A", "B", "C"});
		beanWithCollections.setStrings2(stringList2);
		Bean2RDF writer = new Bean2RDF(memoryModel);
		writer.save(beanWithCollections);
		System.out.println("Saved BeanWithCollections:" + JenabeanWriter.toString(beanWithCollections));
		
		RDF2Bean reader = new RDF2Bean(memoryModel);
		BeanWithCollections beanWithCollectionsReloaded = null;
		try {
			beanWithCollectionsReloaded = (BeanWithCollections)reader.loadDeep(BeanWithCollections.class, "beanWithCollections");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded BeanWithCollections:" + JenabeanWriter.toString(beanWithCollectionsReloaded));
		//assertEquals(beanWithCollections, beanWithCollectionsReloaded);
	}
	
	@Test
	public void saveAndLoadBeanWithInterface() {
		BeanWithInterface beanWithInterface = new BeanWithInterface();
		beanWithInterface.setId("beanWithInterface");
		Model memoryModel = ModelFactory.createDefaultModel();
		
		InterfaceBean interfaceBean = new InterfaceBean();
		interfaceBean.setId("interfaceBean");
		interfaceBean.setMessage("I am interfaceBean.");
		beanWithInterface.setInterfaceBean(interfaceBean);
		
		Bean2RDF writer = new Bean2RDF(memoryModel);
		writer.save(beanWithInterface);
		System.out.println("Saved beanWithInterface:" + JenabeanWriter.toString(beanWithInterface));
		
		RDF2Bean reader = new RDF2Bean(memoryModel);
		BeanWithInterface beanWithInterfaceReloaded = null;
		try {
			beanWithInterfaceReloaded = (BeanWithInterface)reader.loadDeep(BeanWithInterface.class, "beanWithInterface");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded beanWithInterfaceReloaded:" + JenabeanWriter.toString(beanWithInterfaceReloaded));
		//assertEquals(beanWithInterface, beanWithInterfaceReloaded);
	}
	
	@Test
	public void writeGlobalJenabean() {
		GlobalJenabeanObject globalJenabeanObject = new GlobalJenabeanObject();
		JenabeanWriter.toString(globalJenabeanObject);
	}
	
	@Test
	public void testJenabeanArraysOfJenabeans() {
		BeanWithString bean1 = new BeanWithString();
		bean1.setId("bean1");
		bean1.setString("string1");
		BeanWithString bean2 = new BeanWithString();
		bean2.setId("bean2");
		bean2.setString("string2");
		BeanWithString[] beanArray = new BeanWithString[] {
				bean1,
				bean2
		};
		
		BeanWithArrays beanWithArrays = new BeanWithArrays();
		beanWithArrays.setId("beanWithArrays");
		beanWithArrays.setBeanArray(beanArray);
		
		//save the beanWithArrays
		Model memoryModel = ModelFactory.createDefaultModel();
		
		Bean2RDF writer = new Bean2RDF(memoryModel);
		writer.save(beanWithArrays);
		System.out.println("Saved beanWithArrays:" + JenabeanWriter.toString(beanWithArrays));
		
		//load the beanWithArrays
		RDF2Bean reader = new RDF2Bean(memoryModel);
		BeanWithArrays beanWithArraysReloaded = null;
		try {
			beanWithArraysReloaded = (BeanWithArrays)reader.loadDeep(BeanWithArrays.class, "beanWithArrays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded beanWithArraysReloaded:" + JenabeanWriter.toString(beanWithArraysReloaded));
	}
}
