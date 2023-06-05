package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DEL_DOCS_BY_SYS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DEL_MISMATCH;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DOCS_NOT_FOUND;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_UNABLE_CHECK_SYSTEM;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY.FIELD_DELETED;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.WebScrapingETY.FIELD_SYSTEM;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
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
	public List<WebScrapingETY> insertMultiWebScraping(List<WebScrapingETY> etys) throws OperationException {
		List<WebScrapingETY> entities;
		try {
			entities = new ArrayList<>(mongoTemplate.insertAll(etys));
		} catch (MongoException e) {
			throw new OperationException("Unable to insert the given web scraping document", e);
		}
		return entities;
	}

	@Override
	public boolean existsBySystem(final String system) throws OperationException {
		// Working var
		boolean output;
		// Create query
		Query query = new Query();
		query.addCriteria(
			where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false)
		);
		try {
			output = mongoTemplate.exists(query, WebScrapingETY.class);
 		} catch(MongoException ex) {
			throw new OperationException(ERR_REP_UNABLE_CHECK_SYSTEM, ex);
		}
		return output;
	}

	@Override
	public List<WebScrapingETY> deleteBySystem(String system) throws OperationException, DataIntegrityException {
		// Working vars
		List<WebScrapingETY> entities;
		UpdateResult result;
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false));
		// Create update definition
		Update update = new Update();
		update.set(FIELD_DELETED, true);
		// Get docs to remove
		entities = findBySystem(system);
		try {
			// Execute
			result = mongoTemplate.updateMulti(query, update, WebScrapingETY.class);
		} catch(MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_DEL_DOCS_BY_SYS , e);
		}
		// Assert we modified the expected data size
		if(entities.size() != result.getMatchedCount() || entities.size() != result.getModifiedCount()) {
			throw new DataIntegrityException(String.format(ERR_REP_DEL_MISMATCH, result.getModifiedCount(), entities.size()));
		}
		// Return modified entities
		return entities;
	}

	@Override
	public List<WebScrapingETY> deleteMultiWebScraping(List<WebScrapingETY> etys) throws OperationException, DataIntegrityException {
		// Working vars
		List<WebScrapingETY> out = new ArrayList<>();
		List<WebScrapingETY> entities;
		UpdateResult result;
		for(WebScrapingETY ety : etys) {
			// Create query
			Query query = new Query();
			query.addCriteria(where(FIELD_SYSTEM).is(ety.getSystem()).and(FIELD_DELETED).is(false));
			// Create update definition
			Update update = new Update();
			update.set(FIELD_DELETED, true);
			// Get docs to remove
			entities = findBySystem(ety.getSystem());
			try {
				// Execute
				result = mongoTemplate.updateMulti(query, update, WebScrapingETY.class);
			} catch(MongoException e) {
				// Catch data-layer runtime exceptions and turn into a checked exception
				throw new OperationException(ERR_REP_DEL_DOCS_BY_SYS , e);
			}
			// Assert we modified the expected data size
			if(entities.size() != result.getMatchedCount() || entities.size() != result.getModifiedCount()) {
				throw new DataIntegrityException(String.format(ERR_REP_DEL_MISMATCH, result.getModifiedCount(), entities.size()));
			}
			out.addAll(entities);
		}
		// Return modified entities
		return out;
	}

	@Override
	public List<WebScrapingETY> findBySystem(String system) throws OperationException {
		// Init empty collection
		List<WebScrapingETY> out;
		// Create query
		Query query = new Query();
		query.addCriteria(
			where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false)
		);
		try {
			// Execute
			out = mongoTemplate.find(query, WebScrapingETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_DOCS_NOT_FOUND, e);
		}
		// Return data
		return out;
	}
}
