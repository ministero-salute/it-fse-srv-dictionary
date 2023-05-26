package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IWebScrapingRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY;

@Repository
public class WebScrapingRepo implements IWebScrapingRepo {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public WebScrapingETY insertWebScraping(WebScrapingETY entity) throws OperationException {
		WebScrapingETY obj;
		try {
			obj = mongoTemplate.insert(entity);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert the given web scraping document", e);
		}
		return obj;
	}
	
	@Override
	public void insertAllWebScraping(List<WebScrapingETY> etys) throws OperationException {
		try {
			mongoTemplate.insertAll(etys);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert the given web scraping document", e);
		}
	}

}
