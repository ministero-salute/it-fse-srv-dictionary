package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;


import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ProfileUtility;
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
	private MongoTemplate mongoTemplate;

	@Autowired
	private ProfileUtility profileUtility;
	
	@Override
	public TerminologyETY insert(final TerminologyETY ety) throws OperationException {
		TerminologyETY out;
		try {
			out = mongoTemplate.insert(ety);
		}catch (MongoException ex) {
			throw new OperationException(Logs.ERR_REP_UNABLE_INSERT_ENTITY, ex);
		}
		return out;
	}

	@Override
	public TerminologyETY findById(String pk) throws OperationException {
		TerminologyETY out;
		try {
			out = mongoTemplate.findById(new ObjectId(pk), TerminologyETY.class);
		}catch (MongoException ex) {
			throw new OperationException(Logs.ERROR_UNABLE_FIND_TERMINOLOGIES, ex);
		}
		return out;
	}

	@Override
	public Collection<TerminologyETY> insertAll(List<TerminologyETY> etys) throws OperationException {
		Collection<TerminologyETY> entities;
		try {
			entities = mongoTemplate.insertAll(etys);
		}catch (MongoException ex) {
			throw new OperationException(Logs.ERR_REP_UNABLE_INSERT_ENTITY, ex);
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
			output = mongoTemplate.exists(query, TerminologyETY.class);
 		} catch(MongoException ex) {
			throw new OperationException(Logs.ERR_REP_UNABLE_CHECK_SYSTEM, ex);
		}
		return output;
	}

	@Override
	public List<TerminologyETY> findByInCodeAndSystem(final List<String> codes, final String system) {
		List<TerminologyETY> output;
		try {
			Query query = new Query();
			query.addCriteria(where("code").in(codes).and(FIELD_SYSTEM).is(system));
			output = mongoTemplate.find(query, TerminologyETY.class);
		} catch(Exception ex) {
			log.error("Error while execute find by in code and system :" , ex);
			throw new BusinessException("Error while execute find by in code and system :" , ex);
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
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName());
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Logs.ERROR_UNABLE_FIND_INSERTIONS, e);
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
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName());
        } catch (MongoException e) {
            throw new OperationException(Logs.ERROR_UNABLE_FIND_DELETIONS, e);
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
			obj = mongoTemplate.findById(new ObjectId(id), SnapshotETY.class);
		} catch (MongoException e) {
			throw new OperationException("Unable to retrieve the requested snapshot document", e);
		}
		return obj;
	}

	@Override
	public SnapshotETY insertSnapshot(SnapshotETY entity) throws OperationException {
		SnapshotETY obj;
		try {
			obj = mongoTemplate.insert(entity);
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
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName()); 
        } catch (MongoException e) {
            throw new OperationException(Logs.ERROR_UNABLE_FIND_TERMINOLOGIES, e);
        }
        return objects;
    }

	@Override
	public List<TerminologyETY> findByIds(List<ObjectId> ids) throws OperationException {
		List<TerminologyETY> objects;
		Query q = Query.query(where(FIELD_ID).in(ids));
		try {
			objects = mongoTemplate.find(q, TerminologyETY.class);
		}catch (MongoException e) {
			throw new OperationException("Unable to retrieve documents by multiple ids", e);
		}
		return objects;
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
			mongoTemplate.updateFirst(query, update, TerminologyETY.class);
		}catch (MongoException ex) {
			throw new OperationException("Unable to delete document", ex);
		}
		// Retrieve update entity
		return findById(id);
	}

	public String getCollectionName() {
		return profileUtility.isTestProfile() ?  Constants.Profile.TEST_PREFIX + Constants.ComponentScan.Collections.TERMINOLOGY : Constants.ComponentScan.Collections.TERMINOLOGY; 
	} 
	
}
