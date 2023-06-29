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
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.ClientException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataIntegrityException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.FileExtensionValidationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.RequestValidationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.TokenException;


class ExceptionTest {

	@Test
	void businessExceptionTest() {
		BusinessException exc = new BusinessException("Error"); 
		
		assertEquals(BusinessException.class, exc.getClass()); 
		assertEquals("Error", exc.getError().getDetail()); 
	} 
	
	@Test
	void clientExceptionTest() {
		ClientException exc = new ClientException(new ErrorResponseDTO(), HttpStatus.SC_INTERNAL_SERVER_ERROR); 
		
		assertEquals(ClientException.class, exc.getClass()); 
		assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, exc.getStatusCode()); 
	} 
	
	@Test
	void dataIntegrityExceptionTest() {
		DataIntegrityException exc = new DataIntegrityException("Error"); 
		
		assertEquals(DataIntegrityException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage()); 
	} 
	
	@Test
	void dataProcessingExceptionTest() {
		DataProcessingException exc = new DataProcessingException("Error"); 
		
		assertEquals(DataProcessingException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage()); 
	}
	
	@Test
	void dataProcessingExceptionMsgExcTest() {
		DataProcessingException exc = new DataProcessingException("Error", new RuntimeException()); 
		
		assertEquals(DataProcessingException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage()); 
	}
	
	@Test
	void documentAlreadyPresentExceptionTest() {
		DocumentAlreadyPresentException exc = new DocumentAlreadyPresentException("Error"); 
		
		assertEquals(DocumentAlreadyPresentException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage());
	}
	
	@Test
	void fileExtensionValidationExceptionTest() {
		FileExtensionValidationException exc = new FileExtensionValidationException("Error"); 
		
		assertEquals(FileExtensionValidationException.class, exc.getClass()); 
		assertEquals("Error", exc.getError().getDetail());
	}
	
	@Test
	void invalidContentExceptionTest() {
		InvalidContentException exc = new InvalidContentException("Error", "Field"); 
		
		assertEquals(InvalidContentException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage());
		assertEquals("Field", exc.getField());
	}
	
	@Test
	void requestValidationExceptionTest() {
		RequestValidationException exc = new RequestValidationException("Error"); 
		
		assertEquals(RequestValidationException.class, exc.getClass()); 
		assertEquals("Error", exc.getError().getDetail());
	}
	
	@Test
	void tokenExceptionTest() {
		TokenException exc = new TokenException("Error"); 
		
		assertEquals(TokenException.class, exc.getClass()); 
		assertEquals("Error", exc.getError().getDetail());
	}
} 
