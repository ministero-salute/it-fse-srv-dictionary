package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelTermsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.GetTermsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostTermsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
@Slf4j
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL {

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992440464600570L; 

    @Autowired
    private ITerminologySRV terminologySRV;

	@Override
	public GetTermsInsDTO getTermsByChunkIns(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException {
		return new GetTermsInsDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkIns(id, idx));
	}

	@Override
	public GetTermsDelDTO getTermsByChunkDel(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, OperationException {
		return new GetTermsDelDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkDel(id, idx));
	}

	@Override
	public GetTermsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
		return new GetTermsResDTO(getLogTraceInfo(), terminologySRV.findById(id));
	}

	@Override
	public PostTermsResDTO uploadTerminologyXml(MultipartFile file, String version) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
		return new PostTermsResDTO(getLogTraceInfo(), terminologySRV.uploadTerminologyXml(file, version));
	}

	@Override
	public DelTermsResDTO deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		terminologySRV.deleteTerminologyById(id);
		return new DelTermsResDTO(getLogTraceInfo(), 1);
	}
}
