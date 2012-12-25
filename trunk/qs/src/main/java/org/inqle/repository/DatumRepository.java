package org.inqle.repository;

import java.util.List;

import org.inqle.domain.Datum;
import org.inqle.domain.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

@RooJpaRepository(domainType = Datum.class)
public interface DatumRepository {
	@Query("select distinct d from LatestParticipantDatum lpd, Datum d where lpd.participantId=?1" +
			" and lpd.datumId = d.id order by lpd.created desc limit ?2 offset ?3")
	List<Question> getLatestData(Integer participantId,  Integer count, Integer offset);
}
