package it.finanze.sanita.fse2.ms.edssrvdictionary.config.mongo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 *	@author vincenzoingenito
 *
 *	Mongo properties configuration.
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)  
public class MongoPropertiesCFG implements Serializable {
  
	/**
	 *  Serial version uid
	 */
	private static final long serialVersionUID = -7936473659737067416L;
 
	@Value("${data.mongodb.uri}")
	private String uri;
}
