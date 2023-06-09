package it.finanze.sanita.fse2.ms.edssrvdictionary.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.finanze.sanita.fse2.ms.edssrvdictionary.enums.TypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDTO {
	
	@NotEmpty(message = "Il campo name deve essere valorizzato")
    @NotNull(message = "Il campo name non può essere null")
	private String name;
	
	@NotEmpty(message = "Il campo version deve essere valorizzato")
    @NotNull(message = "Il campo version non può essere null")
	private String version;
	
	@NotEmpty(message = "Il campo url deve essere valorizzato")
    @NotNull(message = "Il campo url non può essere null")
	private String url;
	
	@NotEmpty(message = "Il campo oid deve essere valorizzato")
    @NotNull(message = "Il campo oid non può essere null")
	private String oid;
	
    @NotNull(message = "Il campo type non può essere null")
	private TypeEnum type;
}
