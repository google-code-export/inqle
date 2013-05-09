package com.beyobe.client.beans;

import java.util.Date;

public class Participant {

	private long id;
	private Date created = new Date();
    private Date updated = null;
    private String lang;
//    private UnitSystem unitSystem = UnitSystem.ENGLISH;
	private String name;
    
   
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
//	public UnitSystem getPreferredUnitSystem() {
//		return unitSystem;
//	}
//	public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
//		this.unitSystem = preferredUnitSystem;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

}