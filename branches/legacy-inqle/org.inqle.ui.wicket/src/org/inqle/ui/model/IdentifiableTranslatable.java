package org.inqle.ui.model;

import java.util.UUID;

/**
 * @author  Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class IdentifiableTranslatable extends BaseTranslatable implements IDentifiable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	/**
	 * 
	 */
	public IdentifiableTranslatable() {
	}

	public String getId() {
		if (id==null) {
			// this isn't necessarily unique??!!
			id = UUID.randomUUID().toString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifiableTranslatable other = (IdentifiableTranslatable) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
