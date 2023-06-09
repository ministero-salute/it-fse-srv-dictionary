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
		log.debug("Query Client - Calling query client");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<?> entity = new HttpEntity<>(systemUrlDTO, headers);
		String fullUrl = msUrlCFG.getQueryHost() + "/v1/metadata-resource";
		return restTemplate.postForObject(fullUrl , entity, MetadataResourceResponseDTO.class);
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

		return restTemplate.postForObject(url, entity, PostDocsResDTO.class);
	}

	@Override
	public void deleteTerminology(String resourceId) {
		String url = msUrlCFG.getQueryHost() + "/v1/terminology";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.DELETE, URI.create(url + "/" + resourceId));

		restTemplate.exchange(requestEntity, Object.class);
	}


	public GetResponseDTO getTerminology(String oid, String version) {
		String url = msUrlCFG.getQueryHost() + "/v1/terminology/" + oid + "/" + version;
		ResponseEntity<GetResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, null, GetResponseDTO.class);
		return response.getBody();
	}



}
