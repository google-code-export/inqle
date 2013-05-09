package org.inqle.repository;

import java.util.List;

import org.inqle.domain.Datum;
import org.inqle.domain.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Datum.class)
public interface DatumRepository {
	
	/**
	 * Get all latest answered data for this participant
	 * @param participantId
	 * @return
	 */
	@Query("select d from LatestParticipantDatum lpd, Datum d " +
			" where lpd.participant.id=?1" +
			" and lpd.datum.id = d.id" +
			" order by lpd.created desc")
	List<Datum> getLatestData(Integer participantId);
	
	/**
	 * Get latest datum for this participant, by concept key
	 * @param participantId
	 * @return
	 */
	@Query("select d from LatestParticipantDatum lpd, Datum d " +
			" where lpd.participant.id=?1" +
			" and lpd.datum.id = d.id" +
			" and lpd.concept.conceptkey = ?2" +
			" order by lpd.created desc limit 1")
	Datum getLatestDatum(Integer participantId, String conceptkey);
	
	/**
	 * Get data by concept ID
	 * @param participantId
	 * @return
	 */
	@Query("select d from Datum d " +
			" where d.participant.id = ?1" +
			" and d.status >= 0 " +
			" and d.concept.id = ?2" +
			" order by d.created desc")
	List<Datum> getDataByConcept(Integer participantId, Integer conceptId);
	
}