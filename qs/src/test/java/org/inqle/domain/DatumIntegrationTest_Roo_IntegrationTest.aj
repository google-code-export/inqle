// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.DatumDataOnDemand;
import org.inqle.domain.DatumIntegrationTest;
import org.inqle.repository.DatumRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DatumIntegrationTest_Roo_IntegrationTest {
    
    declare @type: DatumIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: DatumIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: DatumIntegrationTest: @Transactional;
    
    @Autowired
    DatumDataOnDemand DatumIntegrationTest.dod;
    
    @Autowired
    DatumRepository DatumIntegrationTest.datumRepository;
    
    @Test
    public void DatumIntegrationTest.testCount() {
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", dod.getRandomDatum());
        long count = datumRepository.count();
        Assert.assertTrue("Counter for 'Datum' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void DatumIntegrationTest.testFind() {
        Datum obj = dod.getRandomDatum();
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datum' failed to provide an identifier", id);
        obj = datumRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Datum' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Datum' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void DatumIntegrationTest.testFindAll() {
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", dod.getRandomDatum());
        long count = datumRepository.count();
        Assert.assertTrue("Too expensive to perform a find all test for 'Datum', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Datum> result = datumRepository.findAll();
        Assert.assertNotNull("Find all method for 'Datum' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Datum' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void DatumIntegrationTest.testFindEntries() {
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", dod.getRandomDatum());
        long count = datumRepository.count();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Datum> result = datumRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
        Assert.assertNotNull("Find entries method for 'Datum' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Datum' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void DatumIntegrationTest.testFlush() {
        Datum obj = dod.getRandomDatum();
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datum' failed to provide an identifier", id);
        obj = datumRepository.findOne(id);
        Assert.assertNotNull("Find method for 'Datum' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDatum(obj);
        Integer currentVersion = obj.getVersion();
        datumRepository.flush();
        Assert.assertTrue("Version for 'Datum' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DatumIntegrationTest.testSaveUpdate() {
        Datum obj = dod.getRandomDatum();
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datum' failed to provide an identifier", id);
        obj = datumRepository.findOne(id);
        boolean modified =  dod.modifyDatum(obj);
        Integer currentVersion = obj.getVersion();
        Datum merged = datumRepository.save(obj);
        datumRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Datum' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DatumIntegrationTest.testSave() {
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", dod.getRandomDatum());
        Datum obj = dod.getNewTransientDatum(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Datum' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Datum' identifier to be null", obj.getId());
        datumRepository.save(obj);
        datumRepository.flush();
        Assert.assertNotNull("Expected 'Datum' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void DatumIntegrationTest.testDelete() {
        Datum obj = dod.getRandomDatum();
        Assert.assertNotNull("Data on demand for 'Datum' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datum' failed to provide an identifier", id);
        obj = datumRepository.findOne(id);
        datumRepository.delete(obj);
        datumRepository.flush();
        Assert.assertNull("Failed to remove 'Datum' with identifier '" + id + "'", datumRepository.findOne(id));
    }
    
}
