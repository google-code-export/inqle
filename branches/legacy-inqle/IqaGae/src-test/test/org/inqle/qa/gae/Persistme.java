package test.org.inqle.qa.gae;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

	@PersistenceCapable
	class Persistme {
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.UUIDSTRING)
		private String id;
		
		String val;
		
		Persistme(String val) {
			this.val = val;
		}
		
		public String toString() {
			return "id=" + id + "; val=" + val;
		}
	}
