package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class TerminologyBuilderDTO {

	@CsvBindByPosition(position = 0)
	private String system;
	
	@CsvBindByPosition(position = 1)
	private String code;

	@CsvBindByPosition(position = 11)
	private String description;
}
