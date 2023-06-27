package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv.WebScrapingBuilderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save terminology.
 */
@Document(collection = "#{@webScrapingBean}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebScrapingETY {
	
	public static final String FIELD_SYSTEM = "system";
	public static final String FIELD_URL = "url";
	public static final String FIELD_FORCE_DRAFT = "force-draft";
	
	@Id
	private String id;
	
	@Field(name = FIELD_SYSTEM)
	private String system;
	
	@Field(name = FIELD_URL)
	private String url;

	@Field(name = FIELD_FORCE_DRAFT)
	private boolean forceDraft;
	
	public static List<WebScrapingETY> fromCSV(byte[] raw) {
		InputStream targetStream = new ByteArrayInputStream(raw);
		Reader reader = new InputStreamReader(targetStream);
		List<WebScrapingBuilderDTO> content = new CsvToBeanBuilder<WebScrapingBuilderDTO>(reader)
				.withType(WebScrapingBuilderDTO.class)
//				.withSeparator(';')
				.build()
				.parse();

		return content
				.stream()
				.map(line -> new WebScrapingETY(null, line.getSystem(), line.getUrl(), line.isForceDraft()))
				.collect(Collectors.toList());
	}
 
}
