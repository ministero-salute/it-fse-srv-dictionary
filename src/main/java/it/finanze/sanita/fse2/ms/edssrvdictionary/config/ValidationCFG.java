package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Validation Event properties.
 */
@Configuration
@Getter
public class ValidationCFG implements Serializable {

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 455225540275442943L;

	
	/**
     * Flag to configure validation event persistance policy.
     */
    @Value("${validation.save-error-events-only}")
    private Boolean saveValidationErrorOnly;

    @Value("${validation.transaction-id.generation-strategy}")
    private Integer transactionIDStrategy;

}
