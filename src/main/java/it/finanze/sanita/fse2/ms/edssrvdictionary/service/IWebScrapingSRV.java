package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.WebScrapingDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;

public interface IWebScrapingSRV {
    
    WebScrapingDTO insertWebScraping(String system, String url) throws OperationException, DocumentAlreadyPresentException;

    int insertMultiWebScraping(MultipartFile file) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, InvalidContentException;

    int deleteWebScraping(String system) throws DocumentNotFoundException, OperationException, DataIntegrityException;

    int deleteMultiWebScraping(MultipartFile file) throws OperationException, DataProcessingException, InvalidContentException, DataIntegrityException, DocumentNotFoundException;
}
