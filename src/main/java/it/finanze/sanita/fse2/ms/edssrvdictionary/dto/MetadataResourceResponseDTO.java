package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetadataResourceResponseDTO {

	private List<MetadataResourceDTO> metadataResource; 
}
