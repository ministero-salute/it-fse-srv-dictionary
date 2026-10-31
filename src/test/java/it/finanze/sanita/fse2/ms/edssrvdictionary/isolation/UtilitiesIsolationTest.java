/* SPDX-License-Identifier: AGPL-3.0-or-later */
package it.finanze.sanita.fse2.ms.edssrvdictionary.isolation;

import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.csv.TerminologyBuilderDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.crud.pagination.GetDocsPageLinksDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.impl.*;
import org.junit.jupiter.api.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.*;
import java.io.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("ISOLATION")
class UtilitiesIsolationTest {
    @Test void csvDescriptionsFlagsAndWhitelist() {
        var dto = new TerminologyBuilderDTO(); dto.setDisplay("Display");
        assertThat(dto.getDescription()).isEqualTo("Display");
        assertThat(dto.isActive()).isTrue();
        dto.setStatus("active"); assertThat(dto.isDeprecated()).isFalse();
        dto.setNotSelectable(true); assertThat(dto.isActive()).isFalse();
        dto.setStatus("DEPRECATED"); assertThat(dto.isActive()).isFalse();
        dto.setDisplayName("Preferred"); assertThat(dto.getDescription()).isEqualTo("Preferred");
        var whitelist = TerminologyETY.fromCSV("metadata\nCode;Display\n".getBytes(), "system", null, null);
        assertThat(whitelist).singleElement().satisfies(t -> {
            assertThat(t.getCode()).isEqualTo("#WHITELIST#");
            assertThat(t.getDescription()).isEqualTo("This record handles whitelisted CodeSystems");
            assertThat(t.isDeleted()).isFalse();
        });
        var terms = TerminologyETY.fromCSV("metadata\nCode;Display;DisplayName\nC;Fallback;Preferred\n".getBytes(), "s", "v", new Date(10));
        assertThat(terms).singleElement().satisfies(t -> assertThat(t.getDescription()).isEqualTo("Preferred"));
    }
    @Test void chunkPartitioningAndDates() {
        var changes = ChangeSetUtility.toChangeset(ServiceIsolationTest.term());
        assertThat(changes.getDescription().getCode()).isEqualTo("code");
        assertThat(ChangeSetUtility.chunks(List.of(), 2).getCount()).isZero();
        var chunk = ChangeSetUtility.chunks(List.of(changes, changes, changes), 2);
        assertThat(chunk.getCount()).isEqualTo(2);
        assertThat(chunk.getItems()).isEqualTo(3);
        assertThat(chunk.getIds()).extracting(List::size).containsExactly(2, 1);
        assertThat(MiscUtility.convertToOffsetDateTime(null)).isNull();
        assertThat(MiscUtility.convertToOffsetDateTime(new Date(0)).toString()).isEqualTo("1970-01-01T00:00Z");
        assertThat(StringUtility.isNullOrEmpty(null)).isTrue();
        assertThat(StringUtility.isNullOrEmpty("")).isTrue();
        assertThat(StringUtility.isNullOrEmpty(" ")).isFalse();
        var id = new ObjectIdValidator(); id.initialize(null);
        assertThat(id.isValid(null, null)).isFalse();
        assertThat(id.isValid("wrong", null)).isFalse();
        assertThat(id.isValid(ServiceIsolationTest.ID, null)).isTrue();
        var date = new NoFutureDateValidator(); date.initialize(null);
        assertThat(date.isValid(null, null)).isTrue();
        assertThat(date.isValid(new Date(0), null)).isTrue();
        assertThat(date.isValid(new Date(System.currentTimeMillis() + 60000), null)).isFalse();
    }
    @Test void binaryReadFailuresAndResources() throws Exception {
        var file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2});
        assertThat(FileUtility.throwIfEmpty(file)).containsExactly((byte) 1, (byte) 2);
        when(file.getBytes()).thenReturn(new byte[0]);
        assertThatThrownBy(() -> FileUtility.throwIfEmpty(file)).isInstanceOf(DataProcessingException.class);
        IOException failure = new IOException("broken");
        when(file.getBytes()).thenThrow(failure);
        assertThatThrownBy(() -> FileUtility.throwIfEmpty(file)).isInstanceOf(DataProcessingException.class).hasCause(failure);
        assertThat(FileUtility.getFileFromInternalResources("Files/vocabulary/Terminology_10Items.csv")).isNotEmpty();
        assertThat(FileUtility.getFileFromInternalResources("missing-resource")).isNull();
        var thread = Thread.currentThread(); var original = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(new ClassLoader(original) {
                @Override public InputStream getResourceAsStream(String name) {
                    return new InputStream() {
                        @Override public int read() throws IOException { throw failure; }
                        @Override public void close() throws IOException { throw failure; }
                    };
                }
            });
            assertThat(FileUtility.getFileFromInternalResources("broken")).isNull();
        } finally { thread.setContextClassLoader(original); }
    }
    @Test void emptyPageHasNoNavigation() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        try {
            var links = GetDocsPageLinksDTO.fromPage("s", new PageImpl<TerminologyETY>(List.of(), PageRequest.of(1, 1), 0));
            assertThat(links.getNext()).isNull(); assertThat(links.getPrev()).isNull();
        } finally { RequestContextHolder.resetRequestAttributes(); }
    }
}
