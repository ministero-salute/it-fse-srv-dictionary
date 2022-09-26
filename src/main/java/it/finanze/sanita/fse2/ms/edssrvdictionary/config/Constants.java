package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

/**
 * 
 * @author vincenzoingenito
 *
 * Constants application.
 */
public final class Constants {

	/**
	 *	Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.finanze.sanita.fse2.ms.edssrvdictionary";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.finanze.sanita.fse2.ms.edssrvdictionary.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.finanze.sanita.fse2.ms.edssrvdictionary.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.finanze.sanita.fse2.ms.edssrvdictionary.config";
		
		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.finanze.sanita.fse2.ms.edssrvdictionary.config.mongo";
		
		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.finanze.sanita.fse2.ms.edssrvdictionary.repository";



		public static final class Collections {

			public static final String TERMINOLOGY = "terminology";
	
			private Collections() {
	
			}
		}
		 
		
		private ComponentScan() {
			//This method is intentionally left blank.
		}

	}
 
	public static final class Profile {
		public static final String TEST = "test";

		public static final String TEST_PREFIX = "test_";


		/** 
		 * Constructor.
		 */
		private Profile() {
			//This method is intentionally left blank.
		}

	}

	public static final class Logs {

		public static final String ERROR_UNABLE_FIND_DELETIONS = "Error retrieving deletions";
		
		public static final String ERROR_UNABLE_FIND_INSERTIONS = "Unable to retrieve change-set insertions"; 

		public static final String ERROR_UNABLE_FIND_TERMINOLOGIES = "Unable to retrieve terminologies from database"; 

		public static final String CALLED_GET_TERMINOLOGY_BY_ID = "Called GET /terminology by ID";
		
		public static final String ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST = "The requested document does not exists"; 

		public static final String ERROR_RETRIEVING_HOST_INFO = "Error while retrieving host informations"; 




		private Logs() {
			//This method is intentionally left blank. 
		}
	}


	
  
	/**
	 *	Constants.
	 */
	private Constants() {

	}

}
