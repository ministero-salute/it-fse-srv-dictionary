package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.base.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.chunks.ChangeSetChunkDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsDelDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.chunks.GetTermsInsDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.ChunkOutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL{

    /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -805992420464600570L;

    @Autowired
    private transient ITerminologySRV terminologySRV;

    @Override
    public GetTermsInsDTO getTermsByChunkIns(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, DataIntegrityException, OperationException {
        return new GetTermsInsDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkIns(id, idx));
    }

    @Override
    public GetTermsDelDTO getTermsByChunkDel(String id, int idx) throws ChunkOutOfRangeException, DocumentNotFoundException, OperationException {
        return new GetTermsDelDTO(getLogTraceInfo(), terminologySRV.getTermsByChunkDel(id, idx));
    }

    /**
     * @param lastUpdate The last update date
     * @return The changeset for the current time according to the last update
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public ChangeSetResDTO changeSet(Date lastUpdate) throws OperationException {

        List<ChangeSetDTO> insertions = terminologySRV.getInsertions(lastUpdate);
        List<ChangeSetDTO> deletions = terminologySRV.getDeletions(lastUpdate);

        ChangeSetResDTO response = new ChangeSetResDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setTotalNumberOfElements(insertions.size() + deletions.size());

        return response;
    }



    /**
     * @param lastUpdate the last updated date
     * 
     * @return the changeset chunks of the current time according to the last update
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public ChangeSetChunkDTO changeSetChunks(Date lastUpdate) throws OperationException {

        ChunksDTO chunks = terminologySRV.createChunks(lastUpdate);

        ChangeSetChunkDTO response = new ChangeSetChunkDTO();
        LogTraceInfoDTO info = getLogTraceInfo();
        response.setSpanID(info.getSpanID());
        response.setTraceID(info.getTraceID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setChunks(chunks);
        response.setTotalNumberOfElements(
            chunks.getInsertions().getChunksItems() + chunks.getDeletions().getChunksItems()
        );

        return response;
    }

}
