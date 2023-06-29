/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSetChunks;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSetResource;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.operation.changeset.GetChangeSetSnapshot;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ResourceDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistorySnapshotDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OutOfRangeException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.NoFutureDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Logs.ERR_VAL_FUTURE_DATE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.RoutesUtility.*;

/**
 * ChangeSet retriever controller
 */
@Validated
@RequestMapping(API_CHANGESET_MAPPER)
public interface IChangeSetCTL {

	@GetChangeSetChunks
	@GetMapping(API_CHANGESET_CHUNKS)
	@Tag(name = API_CHANGESET_TAG)
	ChangeSetDTO changeSet(
		@RequestParam(value=API_QP_LAST_UPDATE, required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = ERR_VAL_FUTURE_DATE)
		Date lastUpdate
	) throws OperationException;

	@GetChangeSetResource
	@GetMapping(API_CHANGESET_RESOURCE)
	@Tag(name = API_CHANGESET_TAG)
	ResourceDTO resource(
		@PathVariable(API_RESOURCE_ID)
		@NotBlank
		String id,
		@PathVariable(API_RESOURCE_VERSION)
		@NotBlank
		String version,
		@RequestParam(value = API_QP_REF, required = false)
		String ref,
		@RequestParam(value = API_QP_CHUNK, defaultValue = "0")
		@PositiveOrZero
		int chunk
	) throws DocumentNotFoundException, OutOfRangeException;

	@GetChangeSetSnapshot
	@GetMapping(API_CHANGESET_SNAPSHOT)
	@Tag(name = API_CHANGESET_TAG)
	HistorySnapshotDTO snapshot();

}
