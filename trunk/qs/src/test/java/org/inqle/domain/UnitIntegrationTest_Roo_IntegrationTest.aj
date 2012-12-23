// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.Unit;
import org.inqle.domain.UnitDataOnDemand;
import org.inqle.domain.UnitIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UnitIntegrationTest_Roo_IntegrationTest {
    
    declare @type: UnitIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: UnitIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: UnitIntegrationTest: @Transactional;
    
    @Autowired
    UnitDataOnDemand UnitIntegrationTest.dod;
    
    @Test
    public void UnitIntegrationTest.testCountUnits() {
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", dod.getRandomUnit());
        long count = Unit.countUnits();
        Assert.assertTrue("Counter for 'Unit' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void UnitIntegrationTest.testFindUnit() {
        Unit obj = dod.getRandomUnit();
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Unit' failed to provide an identifier", id);
        obj = Unit.findUnit(id);
        Assert.assertNotNull("Find method for 'Unit' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Unit' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void UnitIntegrationTest.testFindAllUnits() {
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", dod.getRandomUnit());
        long count = Unit.countUnits();
        Assert.assertTrue("Too expensive to perform a find all test for 'Unit', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Unit> result = Unit.findAllUnits();
        Assert.assertNotNull("Find all method for 'Unit' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Unit' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void UnitIntegrationTest.testFindUnitEntries() {
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", dod.getRandomUnit());
        long count = Unit.countUnits();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Unit> result = Unit.findUnitEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Unit' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Unit' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void UnitIntegrationTest.testFlush() {
        Unit obj = dod.getRandomUnit();
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Unit' failed to provide an identifier", id);
        obj = Unit.findUnit(id);
        Assert.assertNotNull("Find method for 'Unit' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUnit(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Unit' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UnitIntegrationTest.testMergeUpdate() {
        Unit obj = dod.getRandomUnit();
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Unit' failed to provide an identifier", id);
        obj = Unit.findUnit(id);
        boolean modified =  dod.modifyUnit(obj);
        Integer currentVersion = obj.getVersion();
        Unit merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Unit' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UnitIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", dod.getRandomUnit());
        Unit obj = dod.getNewTransientUnit(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Unit' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Unit' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Unit' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void UnitIntegrationTest.testRemove() {
        Unit obj = dod.getRandomUnit();
        Assert.assertNotNull("Data on demand for 'Unit' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Unit' failed to provide an identifier", id);
        obj = Unit.findUnit(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Unit' with identifier '" + id + "'", Unit.findUnit(id));
    }
    
}
