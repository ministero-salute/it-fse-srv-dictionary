/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_DELETED;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_INSERTION_DATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_LAST_UPDATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_RELEASE_DATE;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv.TerminologyBuilderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save terminology.
 */
@Document(collection = "#{@terminologyBean}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminologyETY {

	public static final String FIELD_ID = "_id";
	public static final String FIELD_SYSTEM = "system";
	public static final String FIELD_CODE = "code";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_DESCRIPTION = "description";

	public static final String FILE_XML_EXT_DOTTED = ".xml";
	public static final String FILE_CSV_EXT_DOTTED = ".csv";

	@Id
	private String id;
	
	@Field(name = FIELD_SYSTEM)
	private String system;
	
	@Field(name = FIELD_CODE)
	private String code;

	@Field(name = FIELD_VERSION)
	private String version;
	
	@Field(name = FIELD_DESCRIPTION)
	private String description;

	@Field(name = FIELD_RELEASE_DATE)
	private Date releaseDate; 

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate; 
	
	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public static List<TerminologyETY> fromCSV(byte[] raw, String system, String version, Date releaseDate) {
		Date current = new Date();
				
		InputStream targetStream = new ByteArrayInputStream(raw);
		Reader reader = new InputStreamReader(targetStream); 
		List<TerminologyBuilderDTO> content = new CsvToBeanBuilder<TerminologyBuilderDTO>(reader)
				.withType(TerminologyBuilderDTO.class)
				.withSeparator(';')
				.withSkipLines(1)
				.build()
				.parse();

		if (content.isEmpty()) content.add(getWhiteList());
		
		return content
				.stream()
//				.filter(TerminologyBuilderDTO::isActive)
				.map(line -> new TerminologyETY(null, system, line.getCode(), version, line.getDescription(), releaseDate, current, current, false))
				.collect(Collectors.toList());
	}
	
	 
	private static TerminologyBuilderDTO getWhiteList() {
		TerminologyBuilderDTO terminology = new TerminologyBuilderDTO();
		terminology.setCode("#WHITELIST#");
		terminology.setDisplay("This record handles whitelisted CodeSystems");
		return terminology;
	}
	
}
