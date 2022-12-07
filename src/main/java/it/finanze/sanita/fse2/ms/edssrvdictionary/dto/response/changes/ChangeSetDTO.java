/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO implements Serializable {

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 8943366877983853446L; 
	

	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String id; 

	Payload description;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload implements Serializable {
    	
        /**
		 * Serial Version UID 
		 */
		private static final long serialVersionUID = -525579157952607584L; 
		
		@Size(max = DEFAULT_STRING_MAX_SIZE)
        String system;
        @Size(max = DEFAULT_STRING_MAX_SIZE)
        String code;
    }

}