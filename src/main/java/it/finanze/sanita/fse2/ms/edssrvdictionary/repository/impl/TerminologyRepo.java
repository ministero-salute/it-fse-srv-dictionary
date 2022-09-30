package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl;
 

import java.util.Date;
import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ProfileUtility;
import lombok.extern.slf4j.Slf4j;

/**
 *	@author vincenzoingenito
	@author Riccardo Bonesi
 *
 *	Vocabulary repository.
 */
@Slf4j
@Repository
public class TerminologyRepo extends AbstractMongoRepo<TerminologyETY, String> implements ITerminologyRepo {
	
	private static final String SYSTEM_STRING = "system";

	@Autowired
	private transient MongoTemplate mongoTemplate;

	@Autowired
	private transient ProfileUtility profileUtility; 
	
	@Override
	public TerminologyETY insert(final TerminologyETY ety) {
		return super.insert(ety); 
	}

	@Override
	public TerminologyETY findById(String pk) {
		return super.findByID(pk);
	}

	@Override
	public void insertAll(List<TerminologyETY> etys) {
		super.insertAll(etys);
	}

	@Override
	public List<TerminologyETY> findAll() {
		return super.findAll();
	}

	public Integer upsertByCode(final TerminologyETY ety) {
		Integer output = 0;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("code").is(ety.getCode()).and(SYSTEM_STRING).is(ety.getSystem()));
			
			Update update = new Update();
			update.set(SYSTEM_STRING, ety.getSystem());
			update.set("description", ety.getDescription());
			update.set("code", ety.getCode());
			UpdateResult uResult = mongoTemplate.upsert(query, update, TerminologyETY.class);
			output = (int)uResult.getModifiedCount();
		} catch(Exception ex) {
			log.error("Error upserting ety schema " , ex);
			throw new BusinessException("Error inserting ety schema ", ex);
		}
		return output;
	}

	@Override
	public Boolean existsBySystem(final String system) {
		boolean output = false;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where(SYSTEM_STRING).is(system));
			output = mongoTemplate.exists(query, TerminologyETY.class);
 		} catch(Exception ex) {
			log.error("Error while execute exists by system :" , ex);
			throw new BusinessException("Error while execute exists by system :" , ex);
		}
		return output;
	}

	@Override
	public List<TerminologyETY> findByInCodeAndSystem(final List<String> codes, final String system) {
		List<TerminologyETY> output = null;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("code").in(codes).and(SYSTEM_STRING).is(system));
			output = mongoTemplate.find(query, TerminologyETY.class);
		} catch(Exception ex) {
			log.error("Error while execute find by in code and system :" , ex);
			throw new BusinessException("Error while execute find by in code and system :" , ex);
		}
		return output;
	}
 
	@Override
	public void dropCollection() {
		try {
			mongoTemplate.dropCollection(TerminologyETY.class);
		} catch(Exception ex) {
			log.error("Error while execute exists by version query " + getClass() , ex);
			throw new BusinessException("Error while execute exists by version query " + getClass(), ex);
		}
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
            Criteria.where(FIELD_INSERTION_DATE).gt(lastUpdate).and(FIELD_DELETED).ne(true)
        );
        try {
            // Execute
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName());
        } catch (MongoException e) {
            // Catch data-layer runtime exceptions and turn into a checked exception
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_INSERTIONS, e);
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
            Criteria.where(FIELD_LAST_UPDATE).gt(lastUpdate)
                .and(FIELD_INSERTION_DATE).lte(lastUpdate)
                .and(FIELD_DELETED).is(true)
        );
        try {
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName());
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_DELETIONS, e);
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
		throw new UnsupportedOperationException("Not implemented yet");
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
        Query q = Query.query(Criteria.where(FIELD_DELETED).ne(true)); 
        
        try {
            objects = mongoTemplate.find(q, TerminologyETY.class, getCollectionName()); 
        } catch (MongoException e) {
            throw new OperationException(Constants.Logs.ERROR_UNABLE_FIND_TERMINOLOGIES, e);
        }
        return objects;
    }

	public String getCollectionName() {
		return profileUtility.isTestProfile() ?  Constants.Profile.TEST_PREFIX + Constants.ComponentScan.Collections.TERMINOLOGY : Constants.ComponentScan.Collections.TERMINOLOGY; 
	} 
	
}
