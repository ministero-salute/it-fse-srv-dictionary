package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
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
	public GetTermsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
		return new GetTermsResDTO(getLogTraceInfo(), terminologySRV.findById(id));
	}

	@Override
	public PostTermsResDTO uploadTerminologies(MultipartFile file, String version) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
		return new PostTermsResDTO(getLogTraceInfo(), terminologySRV.uploadTerminologyXml(file, version));
	}

	@Override
	public DelTermsResDTO deleteTerminologies(String system) throws OperationException, DocumentNotFoundException, DataIntegrityException {
		return new DelTermsResDTO(getLogTraceInfo(), terminologySRV.deleteTerminologiesBySystem(system));
	}

	@Override
	public DelTermsResDTO deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		return new DelTermsResDTO(getLogTraceInfo(), terminologySRV.deleteTerminologyById(id));
	}
}
