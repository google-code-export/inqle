// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.List;
import org.inqle.domain.Formula;
import org.inqle.domain.FormulaDataOnDemand;
import org.inqle.domain.FormulaIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FormulaIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FormulaIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FormulaIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FormulaIntegrationTest: @Transactional;
    
    @Autowired
    FormulaDataOnDemand FormulaIntegrationTest.dod;
    
    @Test
    public void FormulaIntegrationTest.testCountFormulas() {
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", dod.getRandomFormula());
        long count = Formula.countFormulas();
        Assert.assertTrue("Counter for 'Formula' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FormulaIntegrationTest.testFindFormula() {
        Formula obj = dod.getRandomFormula();
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Formula' failed to provide an identifier", id);
        obj = Formula.findFormula(id);
        Assert.assertNotNull("Find method for 'Formula' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Formula' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FormulaIntegrationTest.testFindAllFormulas() {
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", dod.getRandomFormula());
        long count = Formula.countFormulas();
        Assert.assertTrue("Too expensive to perform a find all test for 'Formula', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Formula> result = Formula.findAllFormulas();
        Assert.assertNotNull("Find all method for 'Formula' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Formula' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FormulaIntegrationTest.testFindFormulaEntries() {
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", dod.getRandomFormula());
        long count = Formula.countFormulas();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Formula> result = Formula.findFormulaEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Formula' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Formula' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FormulaIntegrationTest.testFlush() {
        Formula obj = dod.getRandomFormula();
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Formula' failed to provide an identifier", id);
        obj = Formula.findFormula(id);
        Assert.assertNotNull("Find method for 'Formula' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFormula(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Formula' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FormulaIntegrationTest.testMergeUpdate() {
        Formula obj = dod.getRandomFormula();
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Formula' failed to provide an identifier", id);
        obj = Formula.findFormula(id);
        boolean modified =  dod.modifyFormula(obj);
        Integer currentVersion = obj.getVersion();
        Formula merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Formula' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FormulaIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", dod.getRandomFormula());
        Formula obj = dod.getNewTransientFormula(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Formula' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Formula' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Formula' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FormulaIntegrationTest.testRemove() {
        Formula obj = dod.getRandomFormula();
        Assert.assertNotNull("Data on demand for 'Formula' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Formula' failed to provide an identifier", id);
        obj = Formula.findFormula(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Formula' with identifier '" + id + "'", Formula.findFormula(id));
    }
    
}
