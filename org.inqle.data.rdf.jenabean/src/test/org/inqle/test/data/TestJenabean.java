package org.inqle.test.data;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.inqle.rdf.RDF;
import org.inqle.rdf.beans.util.JenabeanWriter;

import thewebsemantic.Bean2RDF;
import thewebsemantic.NotFoundException;
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
	
	@Test
	public void testUriField() {
		BeanWithUri beanWithUri = new BeanWithUri();
		beanWithUri.setId("1234");
		URI aUri = null;
		try {
			aUri = new URI(RDF.FOAF + "Person");
		} catch (URISyntaxException e) {
			System.out.println("Error in testing code: URISyntaxException creating URI" + e);
			e.printStackTrace();
			assertTrue(false);
		}
		beanWithUri.setUriField(aUri);
		
		//save the beanWithUri
		Model memoryModel = ModelFactory.createDefaultModel();
		
		Bean2RDF writer = new Bean2RDF(memoryModel);
		writer.save(beanWithUri);
		System.out.println("Saved beanWithUri:" + JenabeanWriter.toString(beanWithUri));
	
		RDF2Bean reader = new RDF2Bean(memoryModel);
		try {
			BeanWithUri reconstitutedBeanWithUri = (BeanWithUri)reader.load(BeanWithUri.class, "1234");
			System.out.println("Loaded beanWithUri:" + JenabeanWriter.toString(reconstitutedBeanWithUri));
		} catch (NotFoundException e) {
			System.out.println("Error reconstituting BeanWithUri");
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	
	@Test
	public void testUriCollectionField() {
		BeanWithCollectionOfUris beanWithCollectionOfUris = new BeanWithCollectionOfUris();
		beanWithCollectionOfUris.setId("1234");
		Collection<URI> uriCollection = new ArrayList<URI>();
		URI personUri = URI.create(RDF.FOAF + "Person");
		uriCollection.add(personUri);
		URI documentUri = URI.create(RDF.DC + "Document");
		uriCollection.add(documentUri);
		beanWithCollectionOfUris.setUriCollection(uriCollection);
		
		//save the beanWithUri
		Model memoryModel = ModelFactory.createDefaultModel();
		
		Bean2RDF writer = new Bean2RDF(memoryModel);
		writer.save(beanWithCollectionOfUris);
		System.out.println("Saved beanWithCollectionOfUris:" + JenabeanWriter.toString(beanWithCollectionOfUris));
	
		RDF2Bean reader = new RDF2Bean(memoryModel);
		BeanWithCollectionOfUris reconstitutedBeanWithCollectionOfUris = null;
		try {
			reconstitutedBeanWithCollectionOfUris = (BeanWithCollectionOfUris)reader.load(BeanWithCollectionOfUris.class, "1234");
			System.out.println("Loaded beanWithCollectionOfUris:" + JenabeanWriter.toString(reconstitutedBeanWithCollectionOfUris));
		} catch (NotFoundException e) {
			System.out.println("Error reconstituting BeanWithCollectionOfUris");
			e.printStackTrace();
			assertTrue(false);
		}
		
		Collection<URI> reconstitutedUris = reconstitutedBeanWithCollectionOfUris.getUriCollection();
		System.out.println("reconstituted Collection<URI>=" + reconstitutedUris);
		assertTrue(reconstitutedUris.size()==2);
		assertTrue(reconstitutedUris.contains(personUri));
		assertTrue(reconstitutedUris.contains(documentUri));
	}
	
}
