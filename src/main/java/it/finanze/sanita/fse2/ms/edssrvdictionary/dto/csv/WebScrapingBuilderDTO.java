package it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class WebScrapingBuilderDTO {
    
    @CsvBindByName(column = "system")
	private String system;

	@CsvBindByName(column = "url")
	private String url;

}
