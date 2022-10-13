package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.*;

/**
 * Model to save terminology.
 */
@Document(collection = "#{@terminologyBean}")
@Data
@NoArgsConstructor
public class TerminologyETY {

	public static final String FIELD_ID = "_id";
	public static final String FIELD_SYSTEM = "system";
	public static final String FIELD_CODE = "code";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_DESCRIPTION = "description";

	@Id
	private String id;
	
	@Field(name = FIELD_SYSTEM)
	private String system;
	
	@Field(name = FIELD_CODE)
	private String code;

	@Field(name = FIELD_VERSION)
	private String version;
	
	@Field(name = FIELD_DESCRIPTION)
	private String description;

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate; 
	
	@Field(name = FIELD_DELETED)
	private boolean deleted;
	
}
