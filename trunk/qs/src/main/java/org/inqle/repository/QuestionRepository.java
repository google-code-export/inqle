package org.inqle.repository;

import java.util.List;

import org.inqle.domain.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = Question.class)
public interface QuestionRepository {
	@Transactional(readOnly=true)
	@Query("select distinct q from Question q, Subscription s " +
			" where s.participant.id=?1 and s.question.id = q.id" +
			" order by q.priority asc, s.created desc "
			)
	List<Question> getSubscribedQuestions(Long participantId);
}
