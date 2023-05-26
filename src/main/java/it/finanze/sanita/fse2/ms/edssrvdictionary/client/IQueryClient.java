package it.finanze.sanita.fse2.ms.edssrvdictionary.client;

import java.util.List;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;

public interface IQueryClient {

	MetadataResourceResponseDTO callMsQuery(List<SystemUrlDTO> systemUrlDTO);
	
}
