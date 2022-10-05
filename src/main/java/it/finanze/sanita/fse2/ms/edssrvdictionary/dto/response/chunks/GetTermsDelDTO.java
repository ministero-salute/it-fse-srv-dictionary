package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.ResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * DTO use to return a document as response to getDocumentByChunk and getTerminologyById request
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetTermsDelDTO extends ResponseDTO {

    @ArraySchema(schema = @Schema(implementation = TerminologyDocumentDTO.class))
    private List<ObjectId> documents;

    public GetTermsDelDTO(LogTraceInfoDTO traceInfo, List<ObjectId> data) {
        super(traceInfo);
        this.documents = data;
    }
}
