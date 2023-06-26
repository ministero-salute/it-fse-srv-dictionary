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

import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.RequestDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.FormatEnum;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.FileExtensionValidationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.RequestValidationException;

public class ValidationUtility {
	
	private ValidationUtility() {
		// Constructor intentionally empty.
	} 
	
	public static final int DEFAULT_STRING_MIN_SIZE = 0;
	public static final int DEFAULT_STRING_MAX_SIZE = 100;
	public static final int DEFAULT_ARRAY_MIN_SIZE = 0;
	public static final int DEFAULT_ARRAY_MAX_SIZE = 10000;
	public static final int DEFAULT_BINARY_MIN_SIZE = 0;
	public static final int DEFAULT_BINARY_MAX_SIZE = 10000;
	
	public static void validateFileExtension(FormatEnum format, MultipartFile file) {
	    String originalFilename = file.getOriginalFilename();
	    if (originalFilename != null) {
	        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
	        if (!format.getFileExtension().contains(fileExtension)) {
	        	throw new FileExtensionValidationException("Estensione non valida rispetto a quanto dichiarato nel Format");
	        }
	    } else {
	    	throw new FileExtensionValidationException("File name non valido");
	    }
	}
	
	public static void validRequiredReqBody(FormatEnum format, RequestDTO request) {
		if(FormatEnum.FHIR_R4_JSON.equals(format) || FormatEnum.FHIR_R4_XML.equals(format)) {
			if(request!=null) {
				throw new RequestValidationException("Se si è scelto come format fhir la request deve essere null");
			}
		} 
	}
}
