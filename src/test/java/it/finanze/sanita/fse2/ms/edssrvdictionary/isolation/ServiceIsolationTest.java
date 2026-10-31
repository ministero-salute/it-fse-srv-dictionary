/* SPDX-License-Identifier: AGPL-3.0-or-later */
package it.finanze.sanita.fse2.ms.edssrvdictionary.isolation;

import it.finanze.sanita.fse2.ms.edssrvdictionary.service.impl.TerminologySRV;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.ITerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;
import org.bson.types.ObjectId;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@Tag("ISOLATION")
class ServiceIsolationTest {
    ITerminologyRepo repo;
    TerminologySRV service;
    static final String ID = "507f1f77bcf86cd799439011";
    static TerminologyETY term() {
        return new TerminologyETY(ID, "system", "code", "v1", "description", new Date(1), new Date(2), new Date(3), false);
    }
    static MockMultipartFile csv(String name) {
        return new MockMultipartFile("file", name, "text/csv", "metadata\nCode;Display\nC1;First\nC2;Second\n".getBytes());
    }
    @BeforeEach void setup() {
        repo = mock(ITerminologyRepo.class);
        service = new TerminologySRV();
        ReflectionTestUtils.setField(service, "repository", repo);
    }
    @Test void changesAndSnapshots() throws Exception {
        Date date = new Date(10);
        when(repo.getEveryActiveTerminology()).thenReturn(List.of(term()));
        when(repo.getInsertions(date)).thenReturn(List.of(term()));
        when(repo.getDeletions(date)).thenReturn(List.of(term()));
        when(repo.insertSnapshot(any())).thenAnswer(call -> {
            SnapshotETY snapshot = call.getArgument(0); snapshot.setId(ID); return snapshot;
        });
        assertThat(service.getInsertions(null)).singleElement().satisfies(c -> assertThat(c.getId()).isEqualTo(ID));
        assertThat(service.getDeletions(null)).isEmpty();
        var chunks = service.createChunks(date);
        assertThat(chunks.getSnapshotID()).isEqualTo(ID);
        assertThat(chunks.getInsertions().getChunksItems()).isEqualTo(1);
        assertThat(chunks.getDeletions().getChunksItems()).isEqualTo(1);
        var initial = service.createChunks(null);
        assertThat(initial.getDeletions().getChunksItems()).isZero();
        when(repo.getInsertions(date)).thenReturn(List.of());
        assertThat(service.createChunks(date).getInsertions().getChunksItems()).isZero();
        when(repo.getDeletions(date)).thenReturn(List.of());
        assertThat(service.createChunks(date).getSnapshotID()).isNull();
        when(repo.getActiveDocumentCount()).thenReturn(12L);
        assertThat(service.getCollectionSize()).isEqualTo(12);
    }
    @Test void chunksValidateBoundsAndIntegrity() throws Exception {
        assertThatThrownBy(() -> service.getChunks(ID)).isInstanceOf(DocumentNotFoundException.class);
        var snapshot = SnapshotETY.empty();
        var chunk = new ChunksETY(List.of(List.of(new ObjectId(ID))), 1, 1, 1);
        snapshot.setInsertions(chunk); snapshot.setDeletions(chunk);
        when(repo.getSnapshot(ID)).thenReturn(snapshot);
        when(repo.findByIds(anyList())).thenReturn(List.of(term()));
        assertThat(service.getTermsByChunkIns(ID, 0)).singleElement().satisfies(t -> assertThat(t.getCode()).isEqualTo("code"));
        assertThat(service.getTermsByChunkDel(ID, 0)).containsExactly(new ObjectId(ID));
        assertThatThrownBy(() -> service.getTermsByChunkIns(ID, -1)).isInstanceOf(OutOfRangeException.class);
        assertThatThrownBy(() -> service.getTermsByChunkDel(ID, 1)).isInstanceOf(OutOfRangeException.class);
        when(repo.findByIds(anyList())).thenReturn(List.of());
        assertThatThrownBy(() -> service.getTermsByChunkIns(ID, 0)).isInstanceOf(DataIntegrityException.class);
    }
    @Test void lookupDeleteAndPagination() throws Exception {
        assertThatThrownBy(() -> service.getTerminologyById(ID)).isInstanceOf(DocumentNotFoundException.class);
        when(repo.findById(ID)).thenReturn(term());
        assertThat(service.getTerminologyById(ID).getCode()).isEqualTo("code");
        assertThatThrownBy(() -> service.deleteTerminologiesBySystem("system")).isInstanceOf(DocumentNotFoundException.class);
        assertThatThrownBy(() -> service.getTerminologies(0, 1, "system")).isInstanceOf(DocumentNotFoundException.class);
        when(repo.existsBySystem("system")).thenReturn(true);
        when(repo.deleteBySystem("system")).thenReturn(List.of(term()));
        assertThat(service.deleteTerminologiesBySystem("system")).isEqualTo(1);
        assertThatThrownBy(() -> service.getTerminologies(0, 0, "system")).isInstanceOf(OutOfRangeException.class);
        assertThatThrownBy(() -> service.getTerminologies(-1, 1, "system")).isInstanceOf(OutOfRangeException.class);
        when(repo.getBySystem(eq("system"), any())).thenReturn(new PageImpl<>(List.of(term()), PageRequest.of(0, 1), 1));
        assertThat(service.getTerminologies(0, 1, "system").getValue()).hasSize(1);
        assertThatThrownBy(() -> service.getTerminologies(1, 1, "system")).isInstanceOf(OutOfRangeException.class);
    }
    @Test void csvUploadAndUpdate() throws Exception {
        when(repo.insertAll(anyList())).thenAnswer(c -> c.getArgument(0));
        when(repo.updateBySystem(anyString(), any(), any(), anyList())).thenAnswer(c -> c.getArgument(3));
        assertThat(service.uploadTerminologyCsv(csv("system.csv"), "v1", new Date(1))).isEqualTo(2);
        var captor = org.mockito.ArgumentCaptor.forClass(List.class);
        verify(repo).insertAll(captor.capture());
        var inserted = (TerminologyETY) captor.getValue().getFirst();
        assertThat(inserted.getSystem()).isEqualTo("system");
        assertThat(inserted.getCode()).isEqualTo("C1");
        assertThat(inserted.getDescription()).isEqualTo("First");
        assertThatThrownBy(() -> service.updateTerminologyCsv(csv("system.csv"), null, null)).isInstanceOf(DocumentNotFoundException.class);
        when(repo.existsBySystem("system")).thenReturn(true);
        assertThat(service.updateTerminologyCsv(csv("system.csv"), "v2", new Date(1))).isEqualTo(2);
        when(repo.existsBySystemVersionAndRelease(anyString(), any(), any())).thenReturn(true);
        assertThatThrownBy(() -> service.uploadTerminologyCsv(csv("system.csv"), null, null)).isInstanceOf(DocumentAlreadyPresentException.class);
        for (var file : Arrays.asList(null, new MockMultipartFile("file", new byte[0]))) {
            assertThatThrownBy(() -> service.uploadTerminologyCsv(file, null, null)).isInstanceOf(InvalidContentException.class);
            assertThatThrownBy(() -> service.updateTerminologyCsv(file, null, null)).isInstanceOf(InvalidContentException.class);
        }
        for (String name : Arrays.asList(null, "")) {
            var file = mock(org.springframework.web.multipart.MultipartFile.class);
            when(file.getOriginalFilename()).thenReturn(name);
            assertThatThrownBy(() -> service.uploadTerminologyCsv(file, null, null)).isInstanceOf(DataProcessingException.class);
            assertThatThrownBy(() -> service.updateTerminologyCsv(file, null, null)).isInstanceOf(DataProcessingException.class);
        }
    }
}
