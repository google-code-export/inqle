package com.beyobe.client.beans;

import java.util.Date;

public class Participant {

	private long id;
	private Date created = new Date();
    private Date updated = null;
    private String preferredLang = "en";
    private UnitSystem preferredUnitSystem = UnitSystem.ENGLISH;
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
	public String getPreferredLang() {
		return preferredLang;
	}
	public void setPreferredLang(String preferredLang) {
		this.preferredLang = preferredLang;
	}
	public UnitSystem getPreferredUnitSystem() {
		return preferredUnitSystem;
	}
	public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
		this.preferredUnitSystem = preferredUnitSystem;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}