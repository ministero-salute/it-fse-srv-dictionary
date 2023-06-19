package it.finanze.sanita.fse2.ms.edssrvdictionary.enums;

import lombok.Getter;

@Getter
public enum FormatEnum {
	CUSTOM_CSV(".csv"),
	CUSTOM_JSON(".json"),
	FHIR_R4_XML(".xml"),
	FHIR_R4_JSON(".json"),
	CLA_ML(".ml");
	
	private String fileExtension;
	
	FormatEnum(String inFileExtension){
		fileExtension = inFileExtension;
	}
}
