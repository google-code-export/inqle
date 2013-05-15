package com.beyobe.client.beans;

import java.util.Date;

public class ParticipantImpl implements Participant {

	private long id;
	private Date created = new Date();
    private Date updated = null;
    private String lang;
//    private UnitSystem unitSystem = UnitSystem.ENGLISH;
	private String name;
    
   
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#getId()
	 */
	@Override
	public long getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#setId(long)
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#getCreated()
	 */
	@Override
	public Date getCreated() {
		return created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#setCreated(java.util.Date)
	 */
	@Override
	public void setCreated(Date created) {
		this.created = created;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#getUpdated()
	 */
	@Override
	public Date getUpdated() {
		return updated;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#setUpdated(java.util.Date)
	 */
	@Override
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
//	public UnitSystem getPreferredUnitSystem() {
//		return unitSystem;
//	}
//	public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
//		this.unitSystem = preferredUnitSystem;
//	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#getLang()
	 */
	@Override
	public String getLang() {
		return lang;
	}
	/* (non-Javadoc)
	 * @see com.beyobe.client.beans.Participant#setLang(java.lang.String)
	 */
	@Override
	public void setLang(String lang) {
		this.lang = lang;
	}

}