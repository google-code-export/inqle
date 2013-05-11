// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.ChoiceTranslation;
import org.inqle.domain.ChoiceTranslationDataOnDemand;
import org.inqle.domain.ChoiceTranslationIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ChoiceTranslationIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ChoiceTranslationIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ChoiceTranslationIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ChoiceTranslationIntegrationTest: @Transactional;
    
    @Autowired
    ChoiceTranslationDataOnDemand ChoiceTranslationIntegrationTest.dod;
    
    @Test
    public void ChoiceTranslationIntegrationTest.testCountChoiceTranslations() {
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", dod.getRandomChoiceTranslation());
        long count = ChoiceTranslation.countChoiceTranslations();
        Assert.assertTrue("Counter for 'ChoiceTranslation' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testFindChoiceTranslation() {
        ChoiceTranslation obj = dod.getRandomChoiceTranslation();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to provide an identifier", id);
        obj = ChoiceTranslation.findChoiceTranslation(id);
        Assert.assertNotNull("Find method for 'ChoiceTranslation' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ChoiceTranslation' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testFindAllChoiceTranslations() {
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", dod.getRandomChoiceTranslation());
        long count = ChoiceTranslation.countChoiceTranslations();
        Assert.assertTrue("Too expensive to perform a find all test for 'ChoiceTranslation', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ChoiceTranslation> result = ChoiceTranslation.findAllChoiceTranslations();
        Assert.assertNotNull("Find all method for 'ChoiceTranslation' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ChoiceTranslation' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testFindChoiceTranslationEntries() {
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", dod.getRandomChoiceTranslation());
        long count = ChoiceTranslation.countChoiceTranslations();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ChoiceTranslation> result = ChoiceTranslation.findChoiceTranslationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ChoiceTranslation' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ChoiceTranslation' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testFlush() {
        ChoiceTranslation obj = dod.getRandomChoiceTranslation();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to provide an identifier", id);
        obj = ChoiceTranslation.findChoiceTranslation(id);
        Assert.assertNotNull("Find method for 'ChoiceTranslation' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyChoiceTranslation(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ChoiceTranslation' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testMergeUpdate() {
        ChoiceTranslation obj = dod.getRandomChoiceTranslation();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to provide an identifier", id);
        obj = ChoiceTranslation.findChoiceTranslation(id);
        boolean modified =  dod.modifyChoiceTranslation(obj);
        Integer currentVersion = obj.getVersion();
        ChoiceTranslation merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ChoiceTranslation' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", dod.getRandomChoiceTranslation());
        ChoiceTranslation obj = dod.getNewTransientChoiceTranslation(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ChoiceTranslation' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ChoiceTranslation' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ChoiceTranslationIntegrationTest.testRemove() {
        ChoiceTranslation obj = dod.getRandomChoiceTranslation();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ChoiceTranslation' failed to provide an identifier", id);
        obj = ChoiceTranslation.findChoiceTranslation(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ChoiceTranslation' with identifier '" + id + "'", ChoiceTranslation.findChoiceTranslation(id));
    }
    
}