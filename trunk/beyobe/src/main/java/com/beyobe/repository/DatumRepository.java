package com.beyobe.repository;

import java.util.List;

import com.beyobe.domain.Datum;
import com.beyobe.domain.Question;

import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RooJpaRepository(domainType = Datum.class)
public interface DatumRepository {
	
	/**
	 * Get a list of questions to which this user has subscribed
	 * @param userId
	 * @return list of questions, ordered by question priority then creation date
	 */
	@Query("select distinct d from Datum d " +
			" where d.userId=?1 " +
			" order by d.effectiveDate desc "
			)
	@Transactional(readOnly=true)
	List<Datum> getUserData(String userId);
	
	/**
	 * Get a list of questions to which this user has subscribed
	 * @param userId
	 * @return list of questions, ordered by question priority then creation date
	 */
	@Query("select distinct d from Datum d " +
			" where d.userId=?1 and d.questionId=?2" +
			" order by d.effectiveDate desc "
			)
	@Transactional(readOnly=true)
	List<Datum> getUserDataForQuestion(String userId, String questionId);
	
	@Query("select count(d) from Datum d " +
			" where d.userId=?1 "
			)
	@Transactional(readOnly=true)
	Long countUserData(String userId);
//	/**
//	 * Get all latest answered data for this user
//	 * @param userId
//	 * @return
//	 */
//	@Query("select d from LatestParticipantDatum lpd, Datum d " +
//			" where lpd.user.id=?1" +
//			" and lpd.datum.id = d.id" +
//			" order by lpd.created desc")
//	List<Datum> getLatestData(Integer userId);
//	
//	/**
//	 * Get latest datum for this user, by concept key
//	 * @param userId
//	 * @return
//	 */
//	@Query("select d from LatestParticipantDatum lpd, Datum d " +
//			" where lpd.user.id=?1" +
//			" and lpd.datum.id = d.id" +
//			" and lpd.concept.conceptkey = ?2" +
//			" order by lpd.created desc limit 1")
//	Datum getLatestDatum(Integer userId, String conceptkey);
//	
//	/**
//	 * Get data by concept ID
//	 * @param userId
//	 * @return
//	 */
//	@Query("select d from Datum d " +
//			" where d.user.id = ?1" +
//			" and d.status >= 0 " +
//			" and d.concept.id = ?2" +
//			" order by d.created desc")
//	List<Datum> getDataByConcept(Integer userId, Integer conceptId);

}
