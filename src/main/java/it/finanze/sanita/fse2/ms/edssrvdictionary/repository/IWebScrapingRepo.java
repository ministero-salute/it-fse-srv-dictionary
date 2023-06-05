package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;

public interface IWebScrapingRepo {

	WebScrapingETY insertWebScraping(WebScrapingETY entity) throws OperationException;
	
	List<WebScrapingETY> insertMultiWebScraping(List<WebScrapingETY> etys) throws OperationException;

	List<WebScrapingETY> deleteBySystem(String system) throws OperationException, DataIntegrityException;

	List<WebScrapingETY> deleteMultiWebScraping(List<WebScrapingETY> etys) throws OperationException, DataIntegrityException;

	boolean existsBySystem(final String system) throws OperationException;

	List<WebScrapingETY> findBySystem(String system) throws OperationException;
}
