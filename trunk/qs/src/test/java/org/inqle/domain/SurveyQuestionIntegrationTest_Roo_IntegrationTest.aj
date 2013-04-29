// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.SurveyQuestion;
import org.inqle.domain.SurveyQuestionDataOnDemand;
import org.inqle.domain.SurveyQuestionIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SurveyQuestionIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SurveyQuestionIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SurveyQuestionIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: SurveyQuestionIntegrationTest: @Transactional;
    
    @Autowired
    SurveyQuestionDataOnDemand SurveyQuestionIntegrationTest.dod;
    
    @Test
    public void SurveyQuestionIntegrationTest.testCountSurveyQuestions() {
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", dod.getRandomSurveyQuestion());
        long count = SurveyQuestion.countSurveyQuestions();
        Assert.assertTrue("Counter for 'SurveyQuestion' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testFindSurveyQuestion() {
        SurveyQuestion obj = dod.getRandomSurveyQuestion();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to provide an identifier", id);
        obj = SurveyQuestion.findSurveyQuestion(id);
        Assert.assertNotNull("Find method for 'SurveyQuestion' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SurveyQuestion' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testFindAllSurveyQuestions() {
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", dod.getRandomSurveyQuestion());
        long count = SurveyQuestion.countSurveyQuestions();
        Assert.assertTrue("Too expensive to perform a find all test for 'SurveyQuestion', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SurveyQuestion> result = SurveyQuestion.findAllSurveyQuestions();
        Assert.assertNotNull("Find all method for 'SurveyQuestion' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SurveyQuestion' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testFindSurveyQuestionEntries() {
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", dod.getRandomSurveyQuestion());
        long count = SurveyQuestion.countSurveyQuestions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SurveyQuestion> result = SurveyQuestion.findSurveyQuestionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SurveyQuestion' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SurveyQuestion' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testFlush() {
        SurveyQuestion obj = dod.getRandomSurveyQuestion();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to provide an identifier", id);
        obj = SurveyQuestion.findSurveyQuestion(id);
        Assert.assertNotNull("Find method for 'SurveyQuestion' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySurveyQuestion(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'SurveyQuestion' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testMergeUpdate() {
        SurveyQuestion obj = dod.getRandomSurveyQuestion();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to provide an identifier", id);
        obj = SurveyQuestion.findSurveyQuestion(id);
        boolean modified =  dod.modifySurveyQuestion(obj);
        Integer currentVersion = obj.getVersion();
        SurveyQuestion merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'SurveyQuestion' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", dod.getRandomSurveyQuestion());
        SurveyQuestion obj = dod.getNewTransientSurveyQuestion(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'SurveyQuestion' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'SurveyQuestion' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SurveyQuestionIntegrationTest.testRemove() {
        SurveyQuestion obj = dod.getRandomSurveyQuestion();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SurveyQuestion' failed to provide an identifier", id);
        obj = SurveyQuestion.findSurveyQuestion(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'SurveyQuestion' with identifier '" + id + "'", SurveyQuestion.findSurveyQuestion(id));
    }
    
}