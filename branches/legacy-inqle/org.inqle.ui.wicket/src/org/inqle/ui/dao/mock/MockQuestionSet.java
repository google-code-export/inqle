/**
 * 
 */
package org.inqle.ui.dao.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.inqle.ui.model.IAnswer;
import org.inqle.ui.model.IntegerRangeAnswer;
import org.inqle.ui.model.ListQuestionsSet;
import org.inqle.ui.model.MultipleChoiceAnswer;
import org.inqle.ui.model.Option;
import org.inqle.ui.model.Question;
import org.inqle.ui.model.SingleChoiceAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class MockQuestionSet extends ListQuestionsSet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MockQuestionSet.class);
	
	/**
	 * @param questions
	 */
	public MockQuestionSet() {
		super(new ArrayList<Question>());

		BufferedReader reader = new BufferedReader(new InputStreamReader(MockQuestionSet.class.getResourceAsStream("questions.txt")));
		String line;
		try {
			line = reader.readLine();
			while (line !=  null) {
				StringTokenizer tk = new StringTokenizer(line, "|");
				String[] q = {tk.nextToken(), tk.nextToken()};
				Question question = new Question();
				question.setTranslationKey(q[0]);
				System.out.println(question.getTranslationKey());
				question.setAnswer(getAnswer(q[1].trim()));
				add(question);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			LOGGER.error("IOException", e);
		}		
	}
	
	private IAnswer getAnswer(String sanswer) {
		if(sanswer.startsWith("MC")) {
			sanswer = sanswer.substring(3, sanswer.length()-1);
			MultipleChoiceAnswer answer = new MultipleChoiceAnswer();
			StringTokenizer tk = new StringTokenizer(sanswer, ",");
			while(tk.hasMoreTokens()) {
				Option option = new Option(tk.nextToken());
				answer.addOption(option);
			}
			return answer;			
		}
		if(sanswer.startsWith("SC")) {
			sanswer = sanswer.substring(3, sanswer.length()-1);
			SingleChoiceAnswer answer = new SingleChoiceAnswer();
			StringTokenizer tk = new StringTokenizer(sanswer, ",");
			while(tk.hasMoreTokens()) {
				Option option = new Option(tk.nextToken());
				answer.addOption(option);
			}
			return answer;			
		} 
		if(sanswer.startsWith("IR")) {
			sanswer = sanswer.substring(3, sanswer.length()-1);
			IntegerRangeAnswer answer = new IntegerRangeAnswer();
			StringTokenizer tk = new StringTokenizer(sanswer, ",");
			answer.setMinimumResponse(Integer.parseInt(tk.nextToken().trim()));
			answer.setMaximumResponse(Integer.parseInt(tk.nextToken().trim()));
			return answer;			
		}
		
		return null;
	}

}
