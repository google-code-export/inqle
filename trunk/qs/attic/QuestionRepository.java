package org.inqle.repository;

import java.util.List;

import org.inqle.domain.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Question.class)
public interface QuestionRepository {

	@Query("select distinct q from Question q, Subscription s " +
//			" where q.priority=?1" +
			" where s.participant.id=?1 and s.question.id = q.id" +
			" order by q.priority asc, s.created desc limit ?2 offset ?3")
	List<Question> getSubscribedQuestions(Integer participantId,  Integer count, Integer offset);
//
//	@Query("select distinct q from Question q where not exists" +
//			" (select * from Subscription s" +
//			" where s.participantId=?1 and s.questionId = q.id)" +
//			" order by q.priority asc limit ?2 offset ?3")
//	List<Question> getUnsubscribedQuestions(Integer participantId,  Integer count, Integer offset);
//	
//	@Query("select distinct q from Question q where not exists" +
//			" (select distinct questionId from Datum d" +
//			" where d.participantId=?1 and d.questionId = q.id)" +
//			" order by q.priority asc limit ?2 offset ?3")
//	List<Question> getUnansweredQuestions(Integer participantId,  Integer count, Integer offset);
//	
//	@Query("select distinct q from Question q" +
//			" where not exists" +
//			" (select distinct questionId from LatestParticipantDatum lpd" +
//			" where lpd.participantId=?1 and lpd.questionId = q.id" +
//			" and lpd.askableAfter > current_timestamp())" +
//			" order by q.priority asc limit ?2 offset ?3")
//	List<Question> getAskableQuestions(Integer participantId,  Integer count, Integer offset);
}
