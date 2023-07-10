package it.finanze.sanita.fse2.ms.edssrvdictionary.changeset.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryDeleteDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO.HistoryInsertDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceItemDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO.ResourceMetaDTO;

public abstract class AbstractChangeset extends AbstractChunkResources{

    protected HistoryInsertDTO createInsertion(ResourceTypeTest type) {
        HistoryInsertDTO insertion = new HistoryInsertDTO();
        insertion.setId(generateResourceId());
        insertion.setVersion(generateVersion());
        insertion.setType(type.getValue());
        return insertion;
    }

    protected HistoryDeleteDTO createDeletion(String id, String omit, ResourceTypeTest type) {
        HistoryDeleteDTO deletion = new HistoryDeleteDTO();
        deletion.setId(id);
        deletion.setOmit(omit);
        deletion.setType(type.getValue());
        return deletion;
    }
    
    protected HistoryDTO createHistory(List<HistoryInsertDTO> insertions, List<HistoryDeleteDTO> deletions) {
        HistoryDTO history = new HistoryDTO();
        history.setInsertions(insertions);
        history.setDeletions(deletions);
        history.setTimestamp(new Date());
        history.setLastUpdate(new Date());
        return history;
    }

    protected HistoryResourceDTO createHistoryResource(HistoryInsertDTO insertion, List<ResourceItemDTO> items) {
        ResourceMetaDTO meta = new ResourceMetaDTO();
        meta.setOid(generateOid());
        meta.setReleased(new Date());
        meta.setType(insertion.getType());
        meta.setWhitelist(items.isEmpty());
        
        HistoryResourceDTO resource = new HistoryResourceDTO();
        resource.setMeta(meta);
        resource.setResourceId(insertion.getId());
        resource.setItems(items);
        return resource;
    }

    protected List<ResourceItemDTO> createItemsList() {
        // Build resource item
        ResourceItemDTO item = new ResourceItemDTO();
        item.setCode("test_code");
        item.setDisplay("test_display");
        // Build list of resources (with one item)
        List<ResourceItemDTO> items = new ArrayList<ResourceItemDTO>();
        items.add(item);
        return items;
    }
}
