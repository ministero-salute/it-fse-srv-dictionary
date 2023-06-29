/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.utility;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class StringUtility {

	private static final String ERROR_MSG = "Errore in fase di calcolo sha";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private StringUtility() {
		// Constructor intentionally empty.
	}

	/**
	 * Returns {@code true} if the String passed as parameter is null or empty.
	 * 
	 * @param str	String to validate.
	 * @return		{@code true} if the String passed as parameter is null or empty.
	 */
	public static boolean isNullOrEmpty(final String str) {
		return str == null || str.isEmpty();
	}

	public static <T> List<T> fromJsonForList(String jsonString, TypeReference<List<T>> typeReference) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(jsonString, typeReference);
		} catch (IOException e) {
			log.info("Error while parse list to json",e);
	        throw new BusinessException("Error while parse list to json");
		}
	}
	
	/**
	 * Transformation from Json to Object.
	 * 
	 * @param <T>	Generic type of return
	 * @param json	json
	 * @param cls	Object class to return
	 * @return		object
	 */
	public static <T> T fromJSON(final String json, final Class<T> cls) {
		return new Gson().fromJson(json, cls);
	}
	
	/**
	 * Returns the encoded String of the SHA-256 algorithm represented in base 64.
	 * 
	 * @param objectToEncode String to encode.
	 * @return String Encoded.
	 */
	public static String encodeSHA256(final byte[] objectToEncode) {
		try {
		    final MessageDigest digest = MessageDigest.getInstance(Constants.App.SHA_ALGORITHM);
		    return Hex.encodeHexString(digest.digest(objectToEncode));
		} catch (final Exception e) {
			log.error(ERROR_MSG, e);
			throw new BusinessException(ERROR_MSG);
		}
	}
}
