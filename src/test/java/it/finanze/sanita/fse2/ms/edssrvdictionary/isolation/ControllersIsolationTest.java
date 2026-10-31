/* SPDX-License-Identifier: AGPL-3.0-or-later */
package it.finanze.sanita.fse2.ms.edssrvdictionary.isolation;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.handler.ExceptionCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.TerminologyDocumentDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.data.snapshot.ChunksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import java.util.*;
import org.bson.types.ObjectId;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("ISOLATION")
class ControllersIsolationTest {
    ITerminologySRV service;
    MockMvc mvc;
    TerminologyCTL terms;
    ExceptionCTL advice;
    @BeforeEach void setup() {
        service = mock(ITerminologySRV.class);
        terms = new TerminologyCTL(); var changes = new ChangeSetCTL(); advice = new ExceptionCTL();
        ReflectionTestUtils.setField(terms, "service", service);
        ReflectionTestUtils.setField(changes, "terminologySRV", service);
        Tracer tracer = OpenTelemetry.noop().getTracer("isolation");
        for (Object bean : List.of(terms, changes, advice)) ReflectionTestUtils.setField(bean, "tracer", tracer);
        mvc = MockMvcBuilders.standaloneSetup(terms, changes, new LivenessCheckCTL()).setControllerAdvice(advice).build();
    }
    @Test void successfulHttpContracts() throws Exception {
        String id = ServiceIsolationTest.ID;
        var term = ServiceIsolationTest.term(); var dto = TerminologyDocumentDTO.fromEntity(term);
        when(service.getTerminologyById(id)).thenReturn(dto);
        mvc.perform(get("/v1/terminology/id/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.document.code").value("code"));
        for (int page = 0; page < 3; page++) {
            when(service.getTerminologies(page, 1, "system")).thenReturn(new AbstractMap.SimpleImmutableEntry<>(new PageImpl<>(List.of(term), PageRequest.of(page, 1), 3), List.of(dto)));
            var response = mvc.perform(get("/v1/terminology/system").param("page", "" + page).param("limit", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.numberOfItems").value(1)).andExpect(jsonPath("$.items[0].description").value("description"));
            if (page < 2) response.andExpect(jsonPath("$.links.next").value("http://localhost/v1/terminology/system?page=" + (page + 1) + "&limit=1"));
            if (page > 0) response.andExpect(jsonPath("$.links.prev").value("http://localhost/v1/terminology/system?page=" + (page - 1) + "&limit=1"));
        }
        when(service.uploadTerminologyCsv(any(), any(), any())).thenReturn(2);
        when(service.updateTerminologyCsv(any(), any(), any())).thenReturn(3);
        mvc.perform(multipart("/v1/terminology").file(ServiceIsolationTest.csv("system.csv"))).andExpect(status().isCreated()).andExpect(jsonPath("$.insertedItems").value(2));
        mvc.perform(multipart(org.springframework.http.HttpMethod.PUT, "/v1/terminology").file(ServiceIsolationTest.csv("system.csv"))).andExpect(status().isCreated()).andExpect(jsonPath("$.updatedItems").value(3));
        when(service.deleteTerminologiesBySystem("system")).thenReturn(4);
        mvc.perform(delete("/v1/terminology/system")).andExpect(status().isOk()).andExpect(jsonPath("$.deletedItems").value(4));
        when(service.createChunks(null)).thenReturn(new ChunksDTO(id, new ChunksDTO.Chunk(1, 100, 2), new ChunksDTO.Chunk(1, 100, 3)));
        when(service.getCollectionSize()).thenReturn(20L);
        mvc.perform(get("/v1/changeset/terminology/chunks")).andExpect(status().isOk()).andExpect(jsonPath("$.totalNumberOfElements").value(5)).andExpect(jsonPath("$.collectionSize").value(20));
        when(service.getTermsByChunkIns(id, 0)).thenReturn(List.of(dto));
        when(service.getTermsByChunkDel(id, 0)).thenReturn(List.of(new ObjectId(id)));
        mvc.perform(get("/v1/changeset/terminology/chunks/ins/" + id + "/0")).andExpect(status().isOk()).andExpect(jsonPath("$.documents[0].code").value("code"));
        mvc.perform(get("/v1/changeset/terminology/chunks/del/" + id + "/0")).andExpect(status().isOk()).andExpect(jsonPath("$.documents[0]").value(id));
        mvc.perform(get("/status")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }
    @Test void domainErrorsHaveProblemDetails() throws Exception {
        Exception[] errors = { new DocumentNotFoundException("missing"), new DocumentAlreadyPresentException("duplicate"),
            new InvalidContentException("invalid", "file"), new OutOfRangeException("range", "page"),
            new DataIntegrityException("integrity"), new OperationException("database", new com.mongodb.MongoException("offline")), new DataProcessingException("bytes"), new IllegalStateException("unknown") };
        int[] statuses = {404, 409, 400, 400, 500, 500, 422, 500};
        for (int i = 0; i < errors.length; i++) {
            Exception error = errors[i];
            doAnswer(c -> { throw error; }).when(service).deleteTerminologiesBySystem("system");
            mvc.perform(delete("/v1/terminology/system")).andExpect(status().is(statuses[i]))
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.status").value(statuses[i])).andExpect(jsonPath("$.detail").value(error.getMessage()));
        }
        ReflectionTestUtils.setField(advice, "tracer", mock(Tracer.class));
        mvc.perform(delete("/v1/terminology/system")).andExpect(status().isInternalServerError());
    }
    static class InvalidBean { @NotBlank String system = ""; }
    static class WritableHeadersAdvice extends ExceptionCTL {
        org.springframework.http.ResponseEntity<Object> missingParameter() {
            return handleMissingServletRequestParameter(new org.springframework.web.bind.MissingServletRequestParameterException("page", "int"), new org.springframework.http.HttpHeaders(), org.springframework.http.HttpStatus.BAD_REQUEST, null);
        }
        org.springframework.http.ResponseEntity<Object> missingPart() {
            return handleMissingServletRequestPart(new org.springframework.web.multipart.support.MissingServletRequestPartException("file"), new org.springframework.http.HttpHeaders(), org.springframework.http.HttpStatus.BAD_REQUEST, null);
        }
    }
    @Test void missingFieldProblemDetailsWithWritableHeaders() {
        var writable = new WritableHeadersAdvice();
        ReflectionTestUtils.setField(writable, "tracer", OpenTelemetry.noop().getTracer("isolation"));
        var parameter = writable.missingParameter(); var part = writable.missingPart();
        org.assertj.core.api.Assertions.assertThat(parameter.getStatusCode().value()).isEqualTo(400);
        org.assertj.core.api.Assertions.assertThat(((it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO) parameter.getBody()).getDetail()).isEqualTo("Il parametro richiesto 'page' non è presente");
        org.assertj.core.api.Assertions.assertThat(((it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.error.base.ErrorResponseDTO) part.getBody()).getDetail()).isEqualTo("Il campo richiesto 'file' non è presente");
    }
    @Test void requestValidationErrors() throws Exception {
        // Spring 6 supplies read-only headers: the existing advice mutates them and
        // MVC falls back to an empty 400. Keep this regression visible, without changing production.
        mvc.perform(get("/v1/terminology/system").param("limit", "1")).andExpect(status().isBadRequest()).andExpect(content().string(""));
        mvc.perform(get("/v1/terminology/system").param("page", "oops").param("limit", "1")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value("Impossibile convertire page nel tipo int"));
        mvc.perform(multipart("/v1/terminology")).andExpect(status().isBadRequest()).andExpect(content().string(""));
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var violations = factory.getValidator().validate(new InvalidBean());
            doThrow(new ConstraintViolationException(violations)).when(service).deleteTerminologiesBySystem("system");
            mvc.perform(delete("/v1/terminology/system")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.instance").value(org.hamcrest.Matchers.containsString("system")));
        }
        ReflectionTestUtils.setField(terms, "tracer", mock(Tracer.class));
        when(service.getTerminologyById(ServiceIsolationTest.ID)).thenReturn(TerminologyDocumentDTO.fromEntity(ServiceIsolationTest.term()));
        mvc.perform(get("/v1/terminology/id/" + ServiceIsolationTest.ID)).andExpect(status().isOk());
    }
}
