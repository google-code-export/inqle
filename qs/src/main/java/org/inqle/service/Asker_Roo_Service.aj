// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.service;

import java.util.List;
import org.inqle.domain.Question;
import org.inqle.service.Asker;

privileged aspect Asker_Roo_Service {
    
    public abstract long Asker.countAllQuestions();    
    public abstract void Asker.deleteQuestion(Question question);    
    public abstract Question Asker.findQuestion(Long id);    
    public abstract List<Question> Asker.findAllQuestions();    
    public abstract List<Question> Asker.findQuestionEntries(int firstResult, int maxResults);    
    public abstract void Asker.saveQuestion(Question question);    
    public abstract Question Asker.updateQuestion(Question question);    
}
