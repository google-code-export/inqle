// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.Survey;
import org.inqle.domain.SurveyDataOnDemand;
import org.inqle.domain.SurveyIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SurveyIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SurveyIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SurveyIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: SurveyIntegrationTest: @Transactional;
    
    @Autowired
    SurveyDataOnDemand SurveyIntegrationTest.dod;
    
    @Test
    public void SurveyIntegrationTest.testCountSurveys() {
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", dod.getRandomSurvey());
        long count = Survey.countSurveys();
        Assert.assertTrue("Counter for 'Survey' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SurveyIntegrationTest.testFindSurvey() {
        Survey obj = dod.getRandomSurvey();
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Survey' failed to provide an identifier", id);
        obj = Survey.findSurvey(id);
        Assert.assertNotNull("Find method for 'Survey' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Survey' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SurveyIntegrationTest.testFindAllSurveys() {
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", dod.getRandomSurvey());
        long count = Survey.countSurveys();
        Assert.assertTrue("Too expensive to perform a find all test for 'Survey', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Survey> result = Survey.findAllSurveys();
        Assert.assertNotNull("Find all method for 'Survey' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Survey' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SurveyIntegrationTest.testFindSurveyEntries() {
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", dod.getRandomSurvey());
        long count = Survey.countSurveys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Survey> result = Survey.findSurveyEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Survey' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Survey' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SurveyIntegrationTest.testFlush() {
        Survey obj = dod.getRandomSurvey();
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Survey' failed to provide an identifier", id);
        obj = Survey.findSurvey(id);
        Assert.assertNotNull("Find method for 'Survey' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySurvey(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Survey' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SurveyIntegrationTest.testMergeUpdate() {
        Survey obj = dod.getRandomSurvey();
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Survey' failed to provide an identifier", id);
        obj = Survey.findSurvey(id);
        boolean modified =  dod.modifySurvey(obj);
        Integer currentVersion = obj.getVersion();
        Survey merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Survey' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SurveyIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", dod.getRandomSurvey());
        Survey obj = dod.getNewTransientSurvey(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Survey' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Survey' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Survey' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SurveyIntegrationTest.testRemove() {
        Survey obj = dod.getRandomSurvey();
        Assert.assertNotNull("Data on demand for 'Survey' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Survey' failed to provide an identifier", id);
        obj = Survey.findSurvey(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Survey' with identifier '" + id + "'", Survey.findSurvey(id));
    }
    
}
