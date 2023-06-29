package it.finanze.sanita.fse2.ms.edssrvdictionary.client;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.GetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IQueryClient {

	PostDocsResDTO callUploadTerminology(FormatEnum format, RequestDTO creationInfo, MultipartFile file) throws IOException;
	
	MetadataResourceResponseDTO  callMetadataResourceEp(List<SystemUrlDTO> systemUrlDTO);
	
	GetResponseDTO getTerminology(String oid, String version);
	
	void deleteTerminology(String resourceId);

	HistoryDTO getHistory(Date lastUpdate);

	HistoryResourceDTO getResource(String resourceId, String versionId);

	HistorySnapshotDTO getSnapshot();

}
