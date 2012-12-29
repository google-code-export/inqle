package org.inqle.domain;

import static org.junit.Assert.*;

import java.util.List;

import org.inqle.repository.QuestionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = Question.class)
public class QuestionIntegrationTest {

	@Autowired
    private QuestionRepository questionRepository;

	@Test
    public void testMarkerMethod() {
		if (questionRepository==null) System.out.println("questionRepository is null");
		Concept c1 = new Concept();
		c1.setConceptkey("Weight");
		c1.persist();
    	Question q1 = new Question();
    	q1.setTag("Weight");
    	q1.setText("What is your weight?");
    	q1.setConcept(c1);
    	questionRepository.save(q1);
    	List<Question> allQuestions = questionRepository.findAll();
    	assertEquals(1, allQuestions.size());
    	System.out.println("q1=" + q1);
    	Question q2 = questionRepository.findOne(q1.getId());
    	assertEquals(q1, q2);
    	q1.setAbbreviation("wt");
    	questionRepository.save(q1);
    	q2 = questionRepository.findOne(q2.getId());
    	assertEquals("wt", q2.getAbbreviation());
    }
}
