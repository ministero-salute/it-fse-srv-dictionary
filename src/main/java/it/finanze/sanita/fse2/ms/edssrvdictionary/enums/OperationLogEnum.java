package it.finanze.sanita.fse2.ms.edssrvdictionary.enums;

public enum OperationLogEnum implements ILogEnum {

	QUERY_TERMINOLOGY_BY_ID("QUERY-TERMINOLOGY-BY-ID", "Query Terminology by ID");

	
	private String code;
	
	public String getCode() {
		return code;
	}

	private String description;

	private OperationLogEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

	public String getDescription() {
		return description;
	}

}

