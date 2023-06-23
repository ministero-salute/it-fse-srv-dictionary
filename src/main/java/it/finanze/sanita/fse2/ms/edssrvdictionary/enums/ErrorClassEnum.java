/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorClassEnum {

	/**
	 * Generic class error.
	 */
	GENERIC("/errors", "Generic", "Errore generico", "/generic"),
	VALIDATION_MANDATORY("/errors/fields", "Missing", "Campo obbligatorio non presente", "/mandatory"),
	VALIDATION_WRONG_EXTENSION("/errors/fields", "Valore errato", "Estensione file errata", "/mandatory"),
	VALIDATION_REQUEST("/errors/fields", "Valore errato", "Valorizzare correttamente la request", "/mandatory"),
	VALIDATION_WRONG_TOKEN("/errors/fields", "Token jwt errato", "Token jwt errato", "/mandatory"),
    TIMEOUT("/errors", "Timeout", "Errore di timeout", "/timeout");


	/**
	 * Error type.
	 */
	private final String type;

	/**
	 * Error title, user friendly description.
	 */
	private final String title;

	/**
	 * Error detail, developer friendly description.
	 */
	private final String detail;

	/**
	 * Error instance, URI that identifies the specific occurrence of the problem.
	 */
	private final String instance;

}
