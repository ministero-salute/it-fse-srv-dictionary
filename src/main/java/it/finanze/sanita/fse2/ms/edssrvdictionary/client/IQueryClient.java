package it.finanze.sanita.fse2.ms.edssrvdictionary.client;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.GetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;

public interface IQueryClient {

	PostDocsResDTO callUploadTerminology(FormatEnum format, RequestDTO creationInfo, MultipartFile file) throws IOException;
	
	MetadataResourceResponseDTO  callMetadataResourceEp(List<SystemUrlDTO> systemUrlDTO);
	
	GetResponseDTO getTerminology(String oid, String version);
	
}
