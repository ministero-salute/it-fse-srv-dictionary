/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import com.opencsv.bean.CsvToBeanBuilder;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Reader;
import java.util.List;

public class AbstractTest {

	@Autowired
    public MongoTemplate mongoTemplate;

	public static final String INVALID_SNAPSHOT_ID ="invalid_id";

	protected void dropVocabularyCollection() {
    	mongoTemplate.dropCollection(TerminologyETY.class);
    }

	protected List<TerminologyBuilderDTO> buildDTOFromCsv(Reader reader){
		List<TerminologyBuilderDTO> output = null; 
		output = new CsvToBeanBuilder(reader).withType(TerminologyBuilderDTO.class).withSeparator(',').build().parse();
		return output;
	}

}
