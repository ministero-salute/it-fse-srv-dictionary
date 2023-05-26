package it.finanze.sanita.fse2.ms.edssrvdictionary.client.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.MicroservicesURLCFG;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueryClient implements IQueryClient {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MicroservicesURLCFG msUrlCFG;

	@Override
	public MetadataResourceResponseDTO  callMsQuery(List<SystemUrlDTO> systemUrlDTO) {
		MetadataResourceResponseDTO out = null;
		log.debug("Query Client - Calling query client");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<?> entity = new HttpEntity<>(systemUrlDTO, headers);
		String fullUrl = msUrlCFG.getQueryHost() + "/v1/metadata-resource";
		try {
			out = restTemplate.postForObject(fullUrl , entity, MetadataResourceResponseDTO.class);
		} catch(HttpClientErrorException ex) {
			//TODO - Manage errors
		}
		
		return out;
	}

}
