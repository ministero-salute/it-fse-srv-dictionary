package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ITerminologyCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.GetTerminologyResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.TerminologyResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.OperationLogEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.ResultLogEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.logging.ElasticLoggerHelper;
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
	private ElasticLoggerHelper elasticLogger; 

    @Autowired
    private ITerminologySRV terminologySRV;


    @Override
	public GetTerminologyResDTO getTerminologyById(HttpServletRequest request,  String id) throws OperationException, DocumentNotFoundException {
		log.info(Constants.Logs.CALLED_GET_TERMINOLOGY_BY_ID); 
		elasticLogger.info(Constants.Logs.CALLED_GET_TERMINOLOGY_BY_ID, OperationLogEnum.QUERY_TERMINOLOGY_BY_ID, ResultLogEnum.OK, new Date()); 
		TerminologyDocumentDTO doc = terminologySRV.findById(id); 
		return new GetTerminologyResDTO(getLogTraceInfo(), doc);

	}


	@Override
	public TerminologyResponseDTO uploadTerminologyFile(HttpServletRequest request, MultipartFile file) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		terminologySRV.uploadTerminologyFile(file);
		return new TerminologyResponseDTO(getLogTraceInfo());
		
	}
    

    
}
