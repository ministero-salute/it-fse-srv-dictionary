package it.finanze.sanita.fse2.ms.edssrvdictionary.client.impl;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.client.IQueryClient;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.MicroservicesURLCFG;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.GetResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.SystemUrlDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class QueryClient implements IQueryClient {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MicroservicesURLCFG msUrlCFG;

	@Override
	public MetadataResourceResponseDTO  callMetadataResourceEp(List<SystemUrlDTO> systemUrlDTO) {
		MetadataResourceResponseDTO out = null;
		log.debug("Query Client - Calling query client");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<?> entity = new HttpEntity<>(systemUrlDTO, headers);
		String fullUrl = msUrlCFG.getQueryHost() + "/v1/metadata-resource";
		try {
			out = restTemplate.postForObject(fullUrl , entity, MetadataResourceResponseDTO.class);
		} catch(ResourceAccessException ex) {
			//TODO - Gestisci Timeout
		}

		return out;
	}

	@Override
	public PostDocsResDTO callUploadTerminology(FormatEnum format, RequestDTO creationInfo, MultipartFile file) throws IOException {

		// Crea il corpo della richiesta
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("creationInfo", creationInfo);
		body.add("file", new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		});

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

		String url = msUrlCFG.getQueryHost() + "/v1/terminology/upload/"+format.toString();

		PostDocsResDTO out = null;
		try {
			out = restTemplate.postForObject(url, entity, PostDocsResDTO.class);
		} catch(ResourceAccessException ex) {
			//TODO - Gestisci Timeout
		}
		return out;
	}
	
	@Override
	public void deleteTerminology(String resourceId) {
		String url = msUrlCFG.getQueryHost() + "/v1/terminology";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.DELETE, URI.create(url + "/" + resourceId));

		ResponseEntity<Void> responseEntity = restTemplate.exchange(requestEntity, Void.class);
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			System.out.println("Delete avvenuta con successo");
		} else if (responseEntity.getStatusCode().is4xxClientError()) {
			System.out.println("Bad Request");
		} else if (responseEntity.getStatusCode().is5xxServerError()) {
			System.out.println("Internal Server Error");
		}
	}


	public GetResponseDTO getTerminology(String oid, String version) {
		String url = msUrlCFG.getQueryHost() + "/v1/terminology/" + oid + "/" + version;
		ResponseEntity<GetResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, GetResponseDTO.class);
		return response.getBody();
	}


	
}
