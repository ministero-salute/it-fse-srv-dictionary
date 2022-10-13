package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for Change Set status endpoint response.
 *
 * @author Riccardo Bonesi
 * 
 */
@Getter
@Setter
public class ChangeSetResDTO extends ResponseDTO {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 21641254425604264L; 

	  /**
     * Trace id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String traceID;

    /**
     * Span id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String spanID;
	
	
	private Date lastUpdate;
	private Date timestamp;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> deletions;

	@Schema(minimum = DEFAULT_ARRAY_MIN_SIZE + "", maximum = DEFAULT_ARRAY_MAX_SIZE + "")
	private int totalNumberOfElements;

	public ChangeSetResDTO() {
		super();
	}
	
}
