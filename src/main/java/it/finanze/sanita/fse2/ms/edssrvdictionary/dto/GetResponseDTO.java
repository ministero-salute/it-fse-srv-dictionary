package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GetResponseDTO extends ResponseDTO {

	private boolean isPresent;
	
	private String id;
	
	public GetResponseDTO(final LogTraceInfoDTO traceInfo, final boolean inIsPresent, final String inId) {
		super(traceInfo);
		isPresent = inIsPresent;
		id = inId;
	}
	
}
