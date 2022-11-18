/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_DELETED;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_INSERTION_DATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_LAST_UPDATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.FIELD_RELEASE_DATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.MiscUtility.isNullOrEmpty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
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
	
	public static List<TerminologyETY> fromXML(byte[] raw, String system, String version, Date releaseDate) throws DataProcessingException {
		// Working var
		List<TerminologyETY> out = new ArrayList<>();
		Date current = new Date();
		// Create xml mapper
		XmlMapper mapper = new XmlMapper();
		// Read hierarchy three
		JsonNode node;
		try {
			node = mapper.readTree(raw);
			// Retrieve field iterator
			Iterator<Map.Entry<String, JsonNode>> iter = node.fields();
			// Iterate on each item
			while(iter.hasNext()) {
				Map.Entry<String, JsonNode> n = iter.next();
				Iterator<JsonNode> it = n.getValue().elements();
				while(it.hasNext()) {
					JsonNode n1 = it.next();
					if(!isNullOrEmpty(n1.get(FIELD_CODE).asText())) {
						String code = n1.get(FIELD_CODE)==null ? "" : n1.get(FIELD_CODE).asText(); 
						if(!isNullOrEmpty(code)) {
							String description = n1.get("")==null ? "" : n1.get("").asText();
							out.add(new TerminologyETY(null, system, code, version,description, releaseDate, current, current, false));
						}
					}
				}
			}
		} catch (IOException e) {
			throw new DataProcessingException(Logs.ERR_ETY_PARSE_XML, e);
		}
		return out;
	}
	
	private static TerminologyBuilderDTO getWhiteList() {
		TerminologyBuilderDTO terminology = new TerminologyBuilderDTO();
		terminology.setCode("#WHITELIST#");
		terminology.setDisplay("This record handles whitelisted CodeSystems");
		return terminology;
	}
	
}
