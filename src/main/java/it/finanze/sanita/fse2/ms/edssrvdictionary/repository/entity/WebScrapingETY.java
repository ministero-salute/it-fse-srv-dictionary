package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save terminology.
 */
@Document(collection = "#{@webScrapingBean}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebScrapingETY {

	@Id
	private String id;
	
	@Field(name = "system")
	private String system;
	
	@Field(name = "url")
	private String url;
	
 
}
