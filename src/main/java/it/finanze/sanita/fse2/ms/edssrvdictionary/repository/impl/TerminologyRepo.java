/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;


import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY.FIELD_SYSTEM;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *	@author vincenzoingenito
	@author Riccardo Bonesi
 *
 *	Vocabulary repository.
 */
@Slf4j
@Repository
public class TerminologyRepo implements ITerminologyRepo {

	@Autowired
	private MongoTemplate mongo;
	
	@Override
	public TerminologyETY insert(final TerminologyETY ety) throws OperationException {
		TerminologyETY out;
		try {
			out = mongo.insert(ety);
		}catch (MongoException ex) {
			throw new OperationException(ERR_REP_UNABLE_INSERT_ENTITY, ex);
		}
		return out;
	}

	@Override
	public TerminologyETY findById(String pk) throws OperationException {
		TerminologyETY out;
		try {
			out = mongo.findById(new ObjectId(pk), TerminologyETY.class);
		}catch (MongoException ex) {
			throw new OperationException(ERROR_UNABLE_FIND_TERMINOLOGIES, ex);
		}
		return out;
	}

	@Override
	public Collection<TerminologyETY> insertAll(List<TerminologyETY> etys) throws OperationException {
		Collection<TerminologyETY> entities;
		try {
			entities = mongo.insertAll(etys);
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

	@Override
	public List<TerminologyETY> findByInCodeAndSystem(final List<String> codes, final String system) throws OperationException {
		List<TerminologyETY> output;
		try {
			Query query = new Query();
			query.addCriteria(where("code").in(codes).and(FIELD_SYSTEM).is(system));
			output = mongo.find(query, TerminologyETY.class);
		} catch(MongoException ex) {
			throw new OperationException("Unable to retrieve by code and system", ex);
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
            throw new OperationException(ERROR_UNABLE_FIND_INSERTIONS, e);
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
            throw new OperationException(ERROR_UNABLE_FIND_DELETIONS, e);
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
            throw new OperationException(ERROR_UNABLE_FIND_TERMINOLOGIES, e);
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
	public TerminologyETY deleteById(String id) throws OperationException {
		// Create query
		Query query = new Query();
		query.addCriteria(where(FIELD_ID).is(id));
		query.addCriteria(where(FIELD_DELETED).is(false));
		// Create update definition
		Update update = new Update();
		update.set(FIELD_LAST_UPDATE, new Date());
		update.set(FIELD_DELETED, true);
		// Execute
		try {
			mongo.updateFirst(query, update, TerminologyETY.class);
		}catch (MongoException ex) {
			throw new OperationException("Unable to delete document", ex);
		}
		// Retrieve update entity
		return findById(id);
	}

	@Override
	public Collection<TerminologyETY> deleteBySystem(String system) throws OperationException, DataIntegrityException {
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

}
