package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.GetTerminologyResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.TerminologyResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;



/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
@Slf4j
public class TerminologyCTL extends AbstractCTL implements ITerminologyCTL{

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992440464600570L; 

    @Autowired
    private ITerminologySRV terminologySRV;


    @Override
	public ResponseEntity<GetTerminologyResDTO> getTerminologyById(HttpServletRequest request,  String id) throws OperationException, DocumentNotFoundException {
		log.info(Constants.Logs.CALLED_GET_TERMINOLOGY_BY_ID); 
		TerminologyDocumentDTO doc = terminologySRV.findById(id); 
		return new ResponseEntity<GetTerminologyResDTO>(new GetTerminologyResDTO(getLogTraceInfo(), doc), null, HttpStatus.SC_OK); 

	}


	@Override
	public ResponseEntity<TerminologyResponseDTO> uploadTerminologyFile(HttpServletRequest request, MultipartFile file) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		Integer uploadItems = terminologySRV.uploadTerminologyFile(file);
		if(uploadItems!=0) {
			return new ResponseEntity<TerminologyResponseDTO>(new TerminologyResponseDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_CREATED); 
		} else {
			return new ResponseEntity<TerminologyResponseDTO>(new TerminologyResponseDTO(getLogTraceInfo(),uploadItems), null, HttpStatus.SC_OK); 
		}
	}
    

    
}
