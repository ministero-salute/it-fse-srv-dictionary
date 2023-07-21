/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.TokenException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.CfUtility;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.StringUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JWTTokenDTO {

	private String sub;

	private String file_hash;

	private String oid;

	private String version;


	public static void deleteTerminologyValidatePayload(final JWTTokenDTO jwtToken, final String oid, final String version) {
		genericValidatePayload(jwtToken,oid,version);
	}

	public static void uploadTerminologyValidatePayload(final JWTTokenDTO jwtToken, RequestDTO creationInfo, MultipartFile multipartFile) {

		String oid = creationInfo!=null ? creationInfo.getOid() : "";
		String version = creationInfo!=null ? creationInfo.getVersion() : ""; 
		genericValidatePayload(jwtToken,oid, version);

		try {
			if(StringUtility.isNullOrEmpty(jwtToken.getFile_hash())) {
				log.info("L'hash del file risulta essere non valorizzato nel jwt");
				throw new TokenException("L'hash del file risulta essere non valorizzato nel jwt");
			}

			if(!jwtToken.getFile_hash().equals(StringUtility.encodeSHA256(multipartFile.getBytes()))) {
				log.info("L'hash dichiarato nel jwt risulta essere differente rispetto al file in input");
				throw new TokenException("L'hash dichiarato nel jwt risulta essere differente rispetto al file in input");
			}

		} catch(Exception ex) {
			throw new BusinessException("Generic error upload");
		}
	}


	public static void genericValidatePayload(final JWTTokenDTO jwtToken, String oid, String version) {

		if(StringUtility.isNullOrEmpty(jwtToken.getSub())) {
			throw new TokenException("Il sub nel jwt non può essere null");
		}

		if(!CfUtility.isValidCf(jwtToken.getSub())) {
			throw new TokenException("Il cf nel jwt risulta essere non valido");
		}

		if(StringUtility.isNullOrEmpty(jwtToken.getOid())) {
			throw new TokenException("L'oid nel jwt non può essere null");
		}

		if(StringUtility.isNullOrEmpty(jwtToken.getVersion())) {
			throw new TokenException("La version nel jwt non può essere null");
		}

		if(!StringUtility.isNullOrEmpty(oid) && !jwtToken.getOid().equals(oid)) {
			log.info("Oid del token diverso dalla request");
			throw new TokenException("Oid del token diverso dalla request");
		}

		if(!StringUtility.isNullOrEmpty(version) && !jwtToken.getOid().equals(version)) {
			log.info("Version del token diverso dalla request");
			throw new TokenException("Version del token diverso dalla request");
		}
	}


	public static JWTTokenDTO extractPayload(final Object httpRequest,boolean fromDockerEnvironment) {

		HttpServletRequest req = (HttpServletRequest)httpRequest;

		String jwtGovwayHeaderB64 = "";
		if(fromDockerEnvironment) {
			jwtGovwayHeaderB64 = req.getHeader(Constants.Headers.JWT_BUSINESS_HEADER);
		} else {
			jwtGovwayHeaderB64 = req.getHeader(Constants.Headers.JWT_GOVWAY_HEADER);
		}

		if(StringUtility.isNullOrEmpty(jwtGovwayHeaderB64)) {
			throw new TokenException("Il jwt non può essere vuoto");
		}

		String[] chunks = jwtGovwayHeaderB64.split("\\.");

		String payload = "";
		if (Boolean.FALSE.equals(fromDockerEnvironment) || chunks.length==1) {
			payload = new String(Base64.getDecoder().decode(chunks[0]));
		} else {
			payload = new String(Base64.getDecoder().decode(chunks[1]));
		}

		return JWTTokenDTO.extractPayload(payload);

	}

	private static JWTTokenDTO extractPayload(final String json) {

		JWTTokenDTO jwtPayload = null;
		try {
			jwtPayload = StringUtility.fromJSON(json, JWTTokenDTO.class);
			if(jwtPayload==null) {
				throw new TokenException("Il jwt risulta essere vuoto o malformato");
			}
		} catch (Exception e) {
			log.error("Error while validating JWT payload DTO");
			throw new TokenException("Il jwt risulta essere vuoto o malformato");
		}

		return jwtPayload;
	}
}
