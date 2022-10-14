package it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.StringUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.IOException;
import java.util.*;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.repository.IChangeSetRepo.*;

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

	public static final String FILE_EXT_DOTTED = ".xml";

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

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate; 
	
	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public static List<TerminologyETY> fromXML(byte[] raw, String system, String version) throws DataProcessingException {
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
					if(!StringUtility.isNullOrEmpty(n1.get(FIELD_CODE).asText())) {
						String code = n1.get(FIELD_CODE)==null ? "" : n1.get(FIELD_CODE).asText(); 
						if(!StringUtility.isNullOrEmpty(code)) {
							String description = n1.get("")==null ? "" : n1.get("").asText();
							out.add(new TerminologyETY(null, system, code, version,description, current, current, false));
						}
					}
				}
			}
		} catch (IOException e) {
			throw new DataProcessingException(Logs.ERR_ETY_PARSE_XML, e);
		}
		return out;
	}
	
}
