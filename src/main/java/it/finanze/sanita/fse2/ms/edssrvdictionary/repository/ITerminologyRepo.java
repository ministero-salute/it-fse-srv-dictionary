package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import org.bson.types.ObjectId;

import java.util.List;

/**
 *	@author vincenzoingenito
 	@author Riccardo Bonesi
 *
 *	Vocabulary interface repository.
 */
public interface ITerminologyRepo extends IChangeSetRepo<TerminologyETY> {

	/**
	 * Inserts a vocabulary on database.
	 * 
	 * @param ety Vocabulary to insert.
	 * @return Vocabulary inserted.
	 */
	TerminologyETY insert(TerminologyETY ety);
	
	/**
	 * Returns a Vcard identified by its {@code pk}.
	 * 
	 * @param pk Primary key of the Vcard to return.
	 * @return Vcard identified by its {@code pk}.
	 */
	TerminologyETY findById(String pk);

	/**
	 * Inserts all vocabularies on database.
	 * 
	 * @param etys List of vocabularies to insert.
	 */
	void insertAll(List<TerminologyETY> etys);

	/**
	 * Returns all vocabularies.
	 * 
	 * @return List of all vocabularies.
	 */
	List<TerminologyETY> findAll();
	
	Boolean existsBySystem(String system);
	
	List<TerminologyETY> findByInCodeAndSystem(List<String> codes, String system);
	
	void dropCollection();

	/**
     * Retrieves all the not-deleted termonologues
     *
     * @return Any available terminology
     * @throws OperationException If a data-layer error occurs
     */
    List<TerminologyETY> getEveryActiveTerminology() throws OperationException;

	List<TerminologyETY> findByIds(List<ObjectId> ids) throws OperationException;

	TerminologyETY deleteById(String id) throws OperationException;

}
