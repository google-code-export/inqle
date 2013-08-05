package com.beyobe.repository;

import java.util.List;

import com.beyobe.client.beans.SubscriptionType;
import com.beyobe.domain.Question;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = Question.class)
public interface QuestionRepository {
	
	
	/**
	 * Get a list of questions to which this participant has subscribed
	 * @param participantId
	 * @return list of questions, ordered by question priority then creation date
	 */
	@Query(
		"select distinct q from Question q, Subscription s " +
		" where s.participant.id=?1 and s.questionId = q.id and s.subscriptionType=?2 "
//		" order by s.created asc "
	)
	@Transactional(readOnly=true)
	List<Question> getSubscribedQuestions(String participantId, SubscriptionType subscriptionType);
	
	//TODO filter for only those questions published
	/**
	 * Get some questions matching the query term
	 * Be sure to add % at end and/or beginning of the term
	 */
	@Query(
			"SELECT distinct q from Question q WHERE (q.privacyType='PUBLIC' OR q.ownerId=?1) AND (LOWER(q.abbreviation) LIKE LOWER(?2) OR LOWER(q.longForm) LIKE LOWER(?2)) " +
			"AND q.id not in (select s.questionId from Subscription s where s.participant.id = ?1 )"
//			limit 10"
	)
	@Transactional(readOnly=true)
	List<Question> searchForNewQuestionsUsingSql(String participantId, String term);
	
//	/**
//	 * Get a list of questions to which this participant has subscribed, that have not
//	 * been answered too recently
//	 * @param participantId
//	 * @return list of questions, ordered by question priority then creation date
//	 */
//	@Query("select distinct q from Question q, Subscription s " +
//			" where s.participant.id=?1 and s.question.id = q.id" +
//			" and not exists" +
//				" (select lpd.id from LatestParticipantDatum lpd" +
//				" where lpd.participant.id=?1 and lpd.datum.question.id = q.id" +
//				" and lpd.askableAfter > current_timestamp())" +
//			" order by q.priority asc, s.created desc "
//			)
//	List<Question> getAvailableSubscribedQuestions(Long participantId);
//	
//	/**
//	 * Get all questions to which this participant has not subscribed
//	 * @param participantId
//	 * @return list of questions, ordered by question priority
//	 */
//	@Query("select distinct q from Question q where not exists" +
//		" (select s.id from Subscription s" +
//		" where s.participant.id=?1 and s.question.id = q.id)" +
//		" order by q.priority asc")
//	List<Question> getUnsubscribedQuestions(Long participantId);
//	
//	/**
//	 * Get all questions to which this participant has not subscribed
//	 * @param participantId
//	 * @return list of questions, ordered by question priority
//	 */
//	@Query("select distinct q from Question q where q.lang = ?2 and not exists" +
//		" (select s.id from Subscription s" +
//		" where s.participant.id=?1 and s.question.id = q.id)" +
//		" order by q.priority asc")
//	List<Question> getUnsubscribedQuestionsOfLanguage(Long participantId, String lang);
//	
//	/**
//	 * Get a list of questions that this participant has never answered
//	 * @param participantId
//	 * @return list of questions, ordered by question priority
//	 */
//	@Query("select distinct q from Question q where not exists" +
//		" (select d.id from Datum d" +
//		" where d.participant.id=?1 and d.question.id = q.id)" +
//		" order by q.priority asc")
//	List<Question> getNeveransweredQuestions(Long participantId);
//	
//	/**
//	 * Get all questions that have not already been answered within the question's 
//	 * latency period
//	 * @param participantId
//	 * @return list of questions, ordered by question priority
//	 */
//	@Query("select distinct q from Question q" +
//		" where not exists" +
//		" (select lpd.id from LatestParticipantDatum lpd" +
//		" where lpd.participant.id=?1 and lpd.datum.question.id = q.id" +
//		" and lpd.askableAfter > current_timestamp())" +
//		" order by q.priority asc")
//	List<Question> getAvailableQuestions(Long participantId);
//	
//	/**
//	 * Get all questions that have not already been answered within the question's 
//	 * latency period
//	 * @param participantId
//	 * @return list of questions, ordered by question priority
//	 */
//	@Query("select distinct q from Question q" +
//		" where q.lang = ?2 and not exists" +
//		" (select lpd.id from LatestParticipantDatum lpd" +
//		" where lpd.participant.id=?1 and lpd.datum.question.id = q.id" +
//		" and lpd.askableAfter > current_timestamp())" +
//		" order by q.priority asc")
//	List<Question> getAvailableQuestionsOfLanguage(Long participantId, String lang);
	
}
