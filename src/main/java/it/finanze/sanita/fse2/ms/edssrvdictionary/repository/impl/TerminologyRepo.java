/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;


import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_CHANGESET_DELETE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_CHANGESET_INSERT;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_COUNT_ACTIVE_DOC;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DEL_DOCS_BY_SYS;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DEL_MISMATCH;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_DOCS_NOT_FOUND;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_EVERY_ACTIVE_DOC;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_UNABLE_CHECK_SYSTEM;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_REP_UNABLE_INSERT_ENTITY;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY.FIELD_SYSTEM;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.StringUtility;

/**
 *
 *	Vocabulary repository.
 */
@Repository
public class TerminologyRepo implements ITerminologyRepo {

	@Autowired
	private MongoTemplate mongo;
	 
	@Override
	public TerminologyETY findById(String pk) throws OperationException {
		TerminologyETY out;
		try {
			out = mongo.findById(new ObjectId(pk), TerminologyETY.class);
		}catch (MongoException ex) {
			throw new OperationException(ERR_REP_DOCS_NOT_FOUND, ex);
		}
		return out;
	}

	@Override
	public List<TerminologyETY> insertAll(List<TerminologyETY> insertions) throws OperationException {
		List<TerminologyETY> entities;
		try {
			entities = new ArrayList<>(mongo.insertAll(insertions));
		}catch (MongoException ex) {
			throw new OperationException(ERR_REP_UNABLE_INSERT_ENTITY, ex);
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
			output = mongo.exists(query, TerminologyETY.class);
 		} catch(MongoException ex) {
			throw new OperationException(ERR_REP_UNABLE_CHECK_SYSTEM, ex);
		}
		return output;
	}

	/**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<TerminologyETY> getInsertions(Date lastUpdate) throws OperationException {
        // Working var
        List<TerminologyETY> objects;
        // Create query
        Query q = Query.query(
            where(FIELD_INSERTION_DATE).gt(lastUpdate).and(FIELD_DELETED).ne(true)
        );
        try {
            // Execute
            objects = mongo.find(q, TerminologyETY.class);
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(ERR_REP_CHANGESET_INSERT, e);
        }
        return objects;
    }

    /**
     * Retrieves the latest deletions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<TerminologyETY> getDeletions(Date lastUpdate) throws OperationException {
        // Working var
        List<TerminologyETY> objects;
        // Create query
        Query q = Query.query(
            where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and(FIELD_DELETED).is(true)
        );
        try {
            objects = mongo.find(q, TerminologyETY.class);
        } catch (MongoException e) {
            throw new OperationException(ERR_REP_CHANGESET_DELETE, e);
        }
        return objects;
    }

	/**
	 * Retrieves a snapshot instance according to the given id
	 *
	 * @param id The snapshot id
	 * @return The snapshot instance
	 * @throws OperationException If a data-layer error occurs
	 */
	@Override
	public SnapshotETY getSnapshot(String id) throws OperationException {
		SnapshotETY obj;
		try {
			obj = mongo.findById(new ObjectId(id), SnapshotETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to retrieve the requested snapshot document", e);
		}
		return obj;
	}

	@Override
	public SnapshotETY insertSnapshot(SnapshotETY entity) throws OperationException {
		SnapshotETY obj;
		try {
			obj = mongo.insert(entity);
		} catch (MongoException e) {
			throw new OperationException("Unable to insert the given snapshot document", e);
		}
		return obj;
	}

	@Override
	public long getActiveDocumentCount() throws OperationException {
		// Working var
		long size;
		// Create query
		Query q = query(where(FIELD_DELETED).ne(true));
		try {
			// Execute count
			size = mongo.count(q, TerminologyETY.class);
		}catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_COUNT_ACTIVE_DOC, e);
		}
		return size;
	}

	/**
     * Retrieves all the not-deleted extensions with their data
     *
     * @return Any available terminology
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<TerminologyETY> getEveryActiveTerminology() throws OperationException {
        List<TerminologyETY> objects;
        Query q = Query.query(where(FIELD_DELETED).ne(true));
        
        try {
            objects = mongo.find(q, TerminologyETY.class);
        } catch (MongoException e) {
            throw new OperationException(ERR_REP_EVERY_ACTIVE_DOC, e);
        }
        return objects;
    }

	@Override
	public List<TerminologyETY> findByIds(List<ObjectId> ids) throws OperationException {
		List<TerminologyETY> objects;
		Query q = Query.query(where(FIELD_ID).in(ids));
		try {
			objects = mongo.find(q, TerminologyETY.class);
		}catch (MongoException e) {
			throw new OperationException("Unable to retrieve documents by multiple ids", e);
		}
		return objects;
	}

	@Override
	public List<TerminologyETY> findBySystem(String system) throws OperationException {
		// Init empty collection
		List<TerminologyETY> out;
		// Create query
		Query query = new Query();
		query.addCriteria(
			where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false)
		);
		try {
			// Execute
			out = mongo.find(query, TerminologyETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_DOCS_NOT_FOUND, e);
		}
		// Return data
		return out;
	}

	@Override
	public List<TerminologyETY> deleteBySystem(String system) throws OperationException, DataIntegrityException {
		// Working vars
		List<TerminologyETY> entities;
		UpdateResult result;
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false));
		// Create update definition
		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set(FIELD_DELETED, true);
		// Get docs to remove
		entities = findBySystem(system);
		try {
			// Execute
			result = mongo.updateMulti(query, update, TerminologyETY.class);
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
	public List<TerminologyETY> updateBySystem(String system,String version,Date releaseDate, List<TerminologyETY> entities) throws OperationException, DocumentNotFoundException {
		List<TerminologyETY> output;
		boolean result = deleteBySystemForUpdate(system, version, releaseDate);
		if(result) {
			output = insertAll(entities);
		} else {
			throw new DocumentNotFoundException("Attenzione, nessun documento trovato con i parametri forniti in input");
		}
		return output;
	}

	@Override
	public Page<TerminologyETY> getBySystem(String system, Pageable page) throws OperationException {
		// Working vars
		List<TerminologyETY> entities;
		long count;
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false));
		try {
			// Get count
			count = mongo.count(query, TerminologyETY.class);
			// Retrive slice with pagination
			entities = mongo.find(query.with(page), TerminologyETY.class);
		} catch (MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_DOCS_NOT_FOUND, e);
		}
		// Return data
		return new PageImpl<>(entities, page, count);
	}

	@Override
	public boolean existsBySystemVersionAndRelease(String system, String version, Date releaseDate) throws OperationException {
		// Working var
		boolean output;
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false));
		
		List<Criteria> orCriteria = new ArrayList<>();
		
		if(!StringUtility.isNullOrEmpty(version)) {
			orCriteria.add(Criteria.where("version").is(version));
		}
		
		if(releaseDate!=null) {
			orCriteria.add(Criteria.where("release_date").is(releaseDate)); 
		}
		
		if (!orCriteria.isEmpty()) {
			Criteria or = new Criteria().orOperator(orCriteria);
			query.addCriteria(or);
		}
		
		try {
			output = mongo.exists(query, TerminologyETY.class);
		} catch(MongoException ex) {
			throw new OperationException(ERR_REP_UNABLE_CHECK_SYSTEM, ex);
		}
		return output;
	}

	
	private boolean deleteBySystemForUpdate(final String system, final String version, final Date releaseDate) throws OperationException {
		boolean output;
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_SYSTEM).is(system).and(FIELD_DELETED).is(false));
		
		if(!StringUtility.isNullOrEmpty(version)) {
			query.addCriteria(Criteria.where("version").is(version));
		}
		
		if(releaseDate!=null) {
			query.addCriteria(Criteria.where("release_date").is(releaseDate));
		}
		
		// Create update definition
		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set(FIELD_DELETED, true);
		try {
			// Execute
			UpdateResult result = mongo.updateMulti(query, update, TerminologyETY.class);
			output = result.getModifiedCount()>0;
		} catch(MongoException e) {
			// Catch data-layer runtime exceptions and turn into a checked exception
			throw new OperationException(ERR_REP_DEL_DOCS_BY_SYS , e);
		}
		return output;
	}
}
