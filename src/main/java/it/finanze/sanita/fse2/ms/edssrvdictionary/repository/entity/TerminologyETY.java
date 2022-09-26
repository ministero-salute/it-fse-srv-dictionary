package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save terminology.
 */
@Document(collection = "#{@terminologyBean}")
@Data
@NoArgsConstructor
public class TerminologyETY {

	@Id
	private String id;
	
	@Field(name = "system")
	private String system;
	
	@Field(name = "code")
	private String code;
	
	@Field(name = "description")
	private String description;

	@Field(name = "insertion_date")
	private Date insertionDate; 
	
	@Field(name = "last_update_date")
	private Date lastUpdateDate; 
	
	@Field(name = "deleted")
	private boolean deleted; 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminologyETY other = (TerminologyETY) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		return result;
	}
	 
	
	
	
}
