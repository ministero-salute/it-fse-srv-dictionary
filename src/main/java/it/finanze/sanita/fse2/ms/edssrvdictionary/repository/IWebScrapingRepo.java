package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;

public interface IWebScrapingRepo {

	WebScrapingETY insertWebScraping(WebScrapingETY entity) throws OperationException;
	
	void insertAllWebScraping(List<WebScrapingETY> etys) throws OperationException;
}
