package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.IWebScrapingCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.webscraping.PostSingleDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IWebScrapingSRV;

@RestController
public class WebScrapingCTL extends AbstractCTL implements IWebScrapingCTL {

    @Autowired
    private IWebScrapingSRV service;

    @Override
    public PostSingleDTO insertWebScraping(String system, String url) throws OperationException, DocumentAlreadyPresentException {
        return new PostSingleDTO(getLogTraceInfo(), service.insertWebScraping(system, url));
    }

    @Override
    public PostDocsResDTO insertMultiWebScraping(MultipartFile file) throws DataProcessingException, DocumentAlreadyPresentException, OperationException, InvalidContentException {
        return new PostDocsResDTO(getLogTraceInfo(), service.insertMultiWebScraping(file));
    }

    @Override
    public DelDocsResDTO deleteWebScraping(String system) throws DataIntegrityException, DocumentNotFoundException, OperationException {
        return new DelDocsResDTO(getLogTraceInfo(), service.deleteWebScraping(system));
    }

    @Override
    public DelDocsResDTO deleteMultiWebScraping(MultipartFile file) throws InvalidContentException, DataProcessingException, OperationException, DocumentNotFoundException, DataIntegrityException {
        return new DelDocsResDTO(getLogTraceInfo(), service.deleteMultiWebScraping(file));
    }
    
}
