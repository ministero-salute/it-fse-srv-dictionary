package it.finanze.sanita.fse2.ms.edssrvdictionary;

import java.io.Reader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.opencsv.bean.CsvToBeanBuilder;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

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
