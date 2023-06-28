/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

/**
 * Constants application.
 */
public final class Constants {

	/**
	 * Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.finanze.sanita.fse2.ms.edssrvdictionary";

		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.finanze.sanita.fse2.ms.edssrvdictionary.config.mongo";

		private ComponentScan() {
			// This method is intentionally left blank.
		}

	}

	public static final class Collections {

		public static final String CHUNKS_INDEX = "chunks_index_eds";

		public static final String CHUNKS = "chunks_eds";
		
		public static final String WEB_SCRAPING = "web_scraping";

		private Collections() {
			// Private constructor to hide the implicit public one.
		}
	}

	public static final class Profile {

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

		public static final String ERR_VAL_UNABLE_CONVERT = "Impossibile convertire %s nel tipo %s";
		public static final String ERR_VAL_MISSING_PART = "Il campo richiesto '%s' non è presente";
		public static final String ERR_VAL_MISSING_PARAMETER = "Il parametro richiesto '%s' non è presente";
		public static final String ERR_VAL_SYSTEM_BLANK = "Il system non può essere vuoto";
		public static final String ERR_VAL_URL_BLANK = "L'url non può essere vuoto";
		public static final String ERR_VAL_FUTURE_DATE = "La data del ultimo aggiornamento non può essere nel futuro";
		public static final String ERR_SRV_SYSTEM_ALREADY_EXISTS = "System %s già presente nel database";
		public static final String ERR_SRV_DOCUMENT_NOT_EXIST = "Il documento richiesto non esiste";
		public static final String ERR_SRV_DOCUMENT_CHUNK_NOT_EXIST = "Il chunk richiesto non esiste, l'indice potrebbe essere malformato";
		public static final String ERR_SRV_INIT_ENGINE = "Inizializzazione engine dei chunks in corso";
		public static final String ERR_VAL_IDX_CHUNK_NOT_VALID = "L'indice chunk non è valido (minore di zero o maggiore dei chunk disponibili)";
		public static final String ERR_SRV_FILE_NOT_VALID = "Il documento è nullo oppure vuoto";
		public static final String ERR_REP_UNABLE_CHECK_SYSTEM = "Impossibile verificare l'esistenza del system";
		public static final String ERR_UTLS_IO_ERROR = "Impossibile estrarre i dati dal file";
		public static final String ERR_UTLS_IO_EMPTY = "Il file risulta essere vuoto";
		public static final String ERR_REP_DOCS_NOT_FOUND = "Impossibile recuperare i documenti del system richiesto";
		public static final String ERR_REP_DEL_DOCS_BY_SYS = "Impossibile cancellare i documenti del system richiesto";


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
