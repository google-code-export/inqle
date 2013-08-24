package com.beyobe.client;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.Session;
import com.beyobe.client.data.BeanMaker;
import com.beyobe.client.util.UUID;
import com.beyobe.client.widgets.AnswerForm;
import com.google.gwt.user.client.Random;

public class Maker {
	
//	public static Participant p() {
//		Participant p = BeanMaker.makeParticipant();
//		p.setId(UUID.uuid());
//		p.setCreated(new Date());
//		p.setRole(UserRole.ROLE_BASIC);
//		p.setUsername(RandomStringUtils.random(Random.nextInt(4)));
//		p.
//	}
	
	public static Question q(Session s) {
		Question q = BeanMaker.makeQuestion();
		q.setId(UUID.uuid());
		q.setCreated(new Date());
		q.setCreatedBy(s.getUserUid());
		q.setOwnerId(s.getUserUid());
		int abbrevLen = Random.nextInt(AnswerForm.MAXIMUM_LENGTH_SHORT_TEXT -1) + 1;
		q.setAbbreviation(RandomStringUtils.random(abbrevLen));
		int longFormLen = Random.nextInt(AnswerForm.MAXIMUM_LENGTH_LONG_TEXT -1) + 1;
		q.setLongForm(RandomStringUtils.random(longFormLen));
		int dataTypeIndex = Random.nextInt(2);
		q.setDataType(DataType.values()[dataTypeIndex]);
//		q.setMinValue(Random.nextInt(5);
		return q;
	}

}
