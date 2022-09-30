package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSet;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.NoFutureDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;

/**
 * ChangeSet retriever controller
 *
 * @author Riccardo Bonesi
 */
@Tag(name = API_CHANGESET_TAG)
@Validated
public interface IChangeSetCTL {
	@GetChangeSet
	@GetMapping(API_CHANGESET_STATUS)
    ChangeSetResDTO changeSet(
		@RequestParam(value=API_QP_LAST_UPDATE, required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;
}
