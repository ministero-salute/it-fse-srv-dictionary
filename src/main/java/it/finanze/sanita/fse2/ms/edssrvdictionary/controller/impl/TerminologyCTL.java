package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;


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
    private ITerminologySRV service;

	@Override
	public GetTermsResDTO getTerminologyById(String id) throws OperationException, DocumentNotFoundException {
		return new GetTermsResDTO(getLogTraceInfo(), service.findById(id));
	}

	@Override
	public PostTermsResDTO uploadTerminologies(MultipartFile file, String version) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
		return new PostTermsResDTO(getLogTraceInfo(), service.uploadTerminologyXml(file, version));
	}

	@Override
	public PutTermsResDTO updateTerminologies(MultipartFile file, String version) throws OperationException, DocumentNotFoundException, DataProcessingException, DataIntegrityException, DocumentAlreadyPresentException {
		return new PutTermsResDTO(getLogTraceInfo(), service.updateTerminologyXml(file, version));
	}

	@Override
	public DelTermsResDTO deleteTerminologies(String system) throws OperationException, DocumentNotFoundException, DataIntegrityException {
		return new DelTermsResDTO(getLogTraceInfo(), service.deleteTerminologiesBySystem(system));
	}

	@Override
	public DelTermsResDTO deleteTerminologyById(String id) throws DocumentNotFoundException, OperationException {
		return new DelTermsResDTO(getLogTraceInfo(), service.deleteTerminologyById(id));
	}
	@Override
	public ResponseEntity<PostTermsResDTO> uploadTerminologyFile(HttpServletRequest request, MultipartFile file) throws IOException, OperationException {
		Integer uploadItems = service.uploadTerminologyFile(file);
		if(uploadItems!=0) {
			return new ResponseEntity<>(new PostTermsResDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_CREATED);
		} else {
			return new ResponseEntity<>(new PostTermsResDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_OK);
		}
	}

	@Override
	public GetTermsPageResDTO getTerminologies(String system, int page, int limit) throws OperationException, DocumentNotFoundException, OutOfRangeException {
		// Retrieve Pair<Page, Entities>
		SimpleImmutableEntry<Page<TerminologyETY>, List<TerminologyDocumentDTO>> slice = service.getTerminologies(page, limit, system);
		// When returning, it builds the URL according to provided values
		return new GetTermsPageResDTO(getLogTraceInfo(), slice.getValue(), system, slice.getKey());
	}
}
