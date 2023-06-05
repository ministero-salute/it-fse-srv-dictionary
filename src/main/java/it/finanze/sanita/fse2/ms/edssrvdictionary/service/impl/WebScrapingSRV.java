package it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_FILE_NOT_VALID;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_SYSTEM_ALREADY_EXISTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_SRV_SYSTEM_NOT_EXISTS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.ErrorInstance.Fields.FILE;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.WebScrapingDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IWebScrapingRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IWebScrapingSRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.FileUtility;

@Service
public class WebScrapingSRV implements IWebScrapingSRV {

    @Autowired
    IWebScrapingRepo repository;

    @Override
    public WebScrapingDTO insertWebScraping(String system, String url) throws OperationException, DocumentAlreadyPresentException {
        WebScrapingETY ety = new WebScrapingETY();
        ety.setSystem(system);
        ety.setUrl(url);
        if(repository.existsBySystem(system)) {
            throw new DocumentAlreadyPresentException(
				String.format(ERR_SRV_SYSTEM_ALREADY_EXISTS, system)
			);
        }
        WebScrapingETY response = repository.insertWebScraping(ety);
        return new WebScrapingDTO(response.getId(), response.getSystem(), response.getUrl(), false);
    }

    @Override
    public int insertMultiWebScraping(MultipartFile file) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, InvalidContentException {
        // Check file integrity
		if(file == null || file.isEmpty()) throw new InvalidContentException(ERR_SRV_FILE_NOT_VALID, FILE);
		// Extract binary content
		byte[] raw = FileUtility.throwIfEmpty(file);
		// Parse entities
		List<WebScrapingETY> entities = WebScrapingETY.fromCSV(raw);
        for(WebScrapingETY ety : entities) {
            if(repository.existsBySystem(ety.getSystem())) {
                throw new DocumentAlreadyPresentException(
                    String.format(ERR_SRV_SYSTEM_ALREADY_EXISTS, ety.getSystem())
                );
            }
        }
		// Insert
		Collection<WebScrapingETY> insertions = repository.insertMultiWebScraping(entities);
		// Return size
		return insertions.size();
    }

    @Override
    public int deleteWebScraping(String system) throws DocumentNotFoundException, OperationException, DataIntegrityException {
        // Check system exists
		if(!repository.existsBySystem(system)) {
			// Let the caller know about it
			throw new DocumentNotFoundException(String.format(ERR_SRV_SYSTEM_NOT_EXISTS, system));
		}
		// Delete any matching document system (then return size)
		return repository.deleteBySystem(system).size();
    }

    @Override
    public int deleteMultiWebScraping(MultipartFile file) throws InvalidContentException, DataProcessingException, OperationException, DocumentNotFoundException, DataIntegrityException {
        // Check file integrity
		if(file == null || file.isEmpty()) throw new InvalidContentException(ERR_SRV_FILE_NOT_VALID, FILE);
		// Extract binary content
		byte[] raw = FileUtility.throwIfEmpty(file);
		// Parse entities
		List<WebScrapingETY> entities = WebScrapingETY.fromCSV(raw);
        // Check systems exist
        for(WebScrapingETY ety : entities) {
            if(!repository.existsBySystem(ety.getSystem())) {
                throw new DocumentNotFoundException(
                    String.format(ERR_SRV_SYSTEM_NOT_EXISTS, ety.getSystem())
                );
            }
        }
		// Delete
		Collection<WebScrapingETY> deletions = repository.deleteMultiWebScraping(entities);
		// Return size
		return deletions.size();
    }
    
}
