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
package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

/**
 * Constants application.
 */
public final class Constants {


	public static final class App {
		public static final String SHA_ALGORITHM = "SHA-256";

		private App() {
			//This method is intentionally left blank.
		}
	}

	public static final class Headers {

		/**
		 * JWT header field of GovWay.
		 */
		public static final String JWT_BUSINESS_HEADER = "FSE-JWT-Authoring";

		/**
		 * JWT header field of GovWay.
		 */
		public static final String JWT_GOVWAY_HEADER = "GovWay-ModI-Info";


		private Headers() {
			//This method is intentionally left blank.
		}

	}


	public static final class Collections {

		public static final String TERMINOLOGY = "terminology_eds";

		public static final String SNAPSHOT = "snapshot_eds";

		public static final String WEB_SCRAPING = "web_scraping";

		private Collections() {
			// Private constructor to hide the implicit public one.
		}
	}

	public static final class Profile {

		public static final String DOCKER = "docker";

		public static final String DEV = "dev";

		public static final String TEST = "test";

		public static final String TEST_PREFIX = "test_";

		/**
		 * Constructor.
		 */
		private Profile() {
			// This method is intentionally left blank.
		}

	}

	public static final class Logs {

		public static final String ERR_VAL_ID_BLANK = "L'identificatore documento non può essere vuoto";
		public static final String ERR_VAL_ID_NOT_VALID = "L'identificatore documento non è valido";
		public static final String ERR_VAL_UNABLE_CONVERT = "Impossibile convertire %s nel tipo %s";
		public static final String ERR_VAL_MISSING_PART = "Il campo richiesto '%s' non è presente";
		public static final String ERR_VAL_MISSING_PARAMETER = "Il parametro richiesto '%s' non è presente";
		public static final String ERR_VAL_SYSTEM_BLANK = "Il system non può essere vuoto";
		public static final String ERR_VAL_URL_BLANK = "L'url non può essere vuoto";
		public static final String ERR_VAL_IDX_CHUNK_NOT_VALID = "L'indice chunk non è valido (minore di zero o maggiore dei chunk disponibili)";
		public static final String ERR_VAL_FUTURE_DATE = "La data del ultimo aggiornamento non può essere nel futuro";
		public static final String ERR_VAL_FUTURE_RELEASE_DATE = "La data di rilascio non può essere nel futuro";
		public static final String ERR_SRV_SYSTEM_ALREADY_EXISTS = "System %s già presente nel database";
		public static final String ERR_SRV_DOCUMENT_NOT_EXIST = "Il documento richiesto non esiste";
		public static final String ERR_SRV_FILE_NOT_VALID = "Il documento è nullo oppure vuoto";
		public static final String ERR_SRV_SYSTEM_NOT_EXISTS = "System %s non presente nel database";
		public static final String ERR_SRV_PAGE_NOT_EXISTS = "La pagina richiesta non esiste, range valido da <%d> a <%d>";
		public static final String ERR_SRV_PAGE_IDX_LESS_ZERO = "L'indice pagina non può essere minore di zero";
		public static final String ERR_SRV_CHUNK_MISMATCH = "Il numero dei documenti richiesti <%d> non coincide con quelli ottenuti <%d>";
		public static final String ERR_SRV_PAGE_LIMIT_LESS_ZERO = "Il limite pagina non può essere minore o uguale a zero";
		public static final String ERR_REP_UNABLE_RETRIVE_FILENAME = "Impossibile recuperare il nome del file corrente";
		public static final String ERR_REP_UNABLE_CHECK_SYSTEM = "Impossibile verificare l'esistenza del system";
		public static final String ERR_REP_UNABLE_CHECK_SYSTEM_VERSION = "Impossibile verificare l'esistenza del system con version";
		public static final String ERR_UTLS_IO_ERROR = "Impossibile estrarre i dati dal file";
		public static final String ERR_UTLS_IO_EMPTY = "Il file risulta essere vuoto";
		public static final String ERR_REP_UNABLE_INSERT_ENTITY = "Impossibile caricare l'entità nel database";
		public static final String ERR_REP_DOCS_NOT_FOUND = "Impossibile recuperare i documenti del system richiesto";
		public static final String ERR_REP_DEL_MISMATCH = "Il numero delle cancellazioni eseguite <%d> non coincide con quelle richieste <%d>";
		public static final String ERR_REP_DEL_DOCS_BY_SYS = "Impossibile cancellare i documenti del system richiesto";
		public static final String ERR_REP_CHANGESET_INSERT = "Impossibile recuperare il change-set degli inserimenti";
		public static final String ERR_REP_CHANGESET_DELETE = "Impossibile recuperare il change-set delle cancellazioni";
		public static final String ERR_REP_EVERY_ACTIVE_DOC = "Impossibile recuperare ogni terminologia attiva con i relativi documenti";
		public static final String ERR_REP_COUNT_ACTIVE_DOC = "Impossibile conteggiare ogni estensione attiva";

		private Logs() {
			// This method is intentionally left blank.
		}
	}

	/**
	 * Constants.
	 */
	private Constants() {
		// This method is intentionally left blank.
	}

}
