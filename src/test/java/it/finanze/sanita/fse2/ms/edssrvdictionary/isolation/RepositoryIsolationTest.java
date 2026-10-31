/* SPDX-License-Identifier: AGPL-3.0-or-later */
package it.finanze.sanita.fse2.ms.edssrvdictionary.isolation;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.impl.TerminologyRepo;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.snapshot.SnapshotETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.*;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.ArgumentCaptor;
import java.util.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@Tag("ISOLATION")
class RepositoryIsolationTest {
    MongoTemplate mongo;
    TerminologyRepo repo;
    @BeforeEach void setup() {
        mongo = mock(MongoTemplate.class); repo = new TerminologyRepo();
        ReflectionTestUtils.setField(repo, "mongo", mongo);
    }
    @Test void queriesAndResults() throws Exception {
        var term = ServiceIsolationTest.term(); var terms = List.of(term); var date = new Date(100);
        when(mongo.find(any(Query.class), eq(TerminologyETY.class))).thenReturn(terms);
        when(mongo.findById(any(), eq(TerminologyETY.class))).thenReturn(term);
        when(mongo.exists(any(Query.class), eq(TerminologyETY.class))).thenReturn(true);
        when(mongo.count(any(Query.class), eq(TerminologyETY.class))).thenReturn(3L);
        doReturn(terms).when(mongo).insertAll(anyCollection());
        assertThat(repo.findById(term.getId())).isSameAs(term);
        assertThat(repo.insertAll(terms)).containsExactly(term);
        assertThat(repo.existsBySystem("system")).isTrue();
        assertQuery(new Document("system", "system").append("deleted", false));
        assertThat(repo.getInsertions(date)).containsExactly(term);
        assertQuery(new Document("insertion_date", new Document("$gt", date)).append("deleted", new Document("$ne", true)));
        assertThat(repo.getDeletions(date)).containsExactly(term);
        assertQuery(new Document("last_update_date", new Document("$gt", date))
            .append("insertion_date", new Document("$lte", date)).append("deleted", true));
        assertThat(repo.getEveryActiveTerminology()).containsExactly(term);
        assertThat(repo.findBySystem("system")).containsExactly(term);
        assertThat(repo.findByIds(List.of(new ObjectId(term.getId())))).containsExactly(term);
        assertThat(repo.getActiveDocumentCount()).isEqualTo(3);
        var page = repo.getBySystem("system", PageRequest.of(1, 1));
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).containsExactly(term);
        for (String version : Arrays.asList(null, "", "v1")) {
            for (Date release : Arrays.asList(null, date)) {
                assertThat(repo.existsBySystemVersionAndRelease("system", version, release)).isTrue();
            }
        }
        var q = ArgumentCaptor.forClass(Query.class);
        verify(mongo, atLeastOnce()).exists(q.capture(), eq(TerminologyETY.class));
        assertThat(q.getValue().getQueryObject().getList("$or", Document.class)).containsExactly(new Document("version", "v1"), new Document("release_date", date));
        var snapshot = SnapshotETY.empty();
        when(mongo.insert(snapshot)).thenReturn(snapshot);
        when(mongo.findById(any(), eq(SnapshotETY.class))).thenReturn(snapshot);
        assertThat(repo.insertSnapshot(snapshot)).isSameAs(snapshot);
        assertThat(repo.getSnapshot(term.getId())).isSameAs(snapshot);
    }
    void assertQuery(Document expected) {
        var q = ArgumentCaptor.forClass(Query.class);
        if (expected.containsKey("system")) verify(mongo).exists(q.capture(), eq(TerminologyETY.class));
        else verify(mongo, atLeastOnce()).find(q.capture(), eq(TerminologyETY.class));
        assertThat(q.getValue().getQueryObject()).isEqualTo(expected);
    }
    @Test void softDeleteAndReplacementIntegrity() throws Exception {
        var terms = List.of(ServiceIsolationTest.term());
        when(mongo.find(any(Query.class), eq(TerminologyETY.class))).thenReturn(terms);
        doReturn(terms).when(mongo).insertAll(anyCollection());
        when(mongo.updateMulti(any(Query.class), any(Update.class), eq(TerminologyETY.class))).thenReturn(UpdateResult.acknowledged(1, 1L, null));
        assertThat(repo.deleteBySystem("system")).containsExactlyElementsOf(terms);
        var update = ArgumentCaptor.forClass(Update.class);
        verify(mongo).updateMulti(any(Query.class), update.capture(), eq(TerminologyETY.class));
        assertThat(update.getValue().getUpdateObject().get("$set", Document.class)).containsEntry("deleted", true).containsKey("last_update_date");
        assertThat(repo.updateBySystem("system", "v1", new Date(1), terms)).containsExactlyElementsOf(terms);
        var query = ArgumentCaptor.forClass(Query.class);
        verify(mongo, times(2)).updateMulti(query.capture(), any(Update.class), eq(TerminologyETY.class));
        assertThat(query.getValue().getQueryObject()).isEqualTo(new Document("system", "system").append("deleted", false)
            .append("version", "v1").append("release_date", new Date(1)));
        assertThat(repo.updateBySystem("system", null, null, terms)).hasSize(1);
        for (UpdateResult result : List.of(UpdateResult.acknowledged(0, 0L, null), UpdateResult.acknowledged(1, 0L, null))) {
            when(mongo.updateMulti(any(Query.class), any(Update.class), eq(TerminologyETY.class))).thenReturn(result);
            assertThatThrownBy(() -> repo.deleteBySystem("system")).isInstanceOf(DataIntegrityException.class);
        }
        assertThatThrownBy(() -> repo.updateBySystem("system", "", null, terms)).isInstanceOf(DocumentNotFoundException.class);
    }
    @Test void translatesMongoFailuresAndRetainsCause() throws Exception {
        MongoException failure = new MongoException("offline");
        when(mongo.findById(any(), any(Class.class))).thenThrow(failure);
        when(mongo.find(any(Query.class), any(Class.class))).thenThrow(failure);
        when(mongo.exists(any(Query.class), any(Class.class))).thenThrow(failure);
        when(mongo.count(any(Query.class), any(Class.class))).thenThrow(failure);
        when(mongo.insertAll(anyCollection())).thenThrow(failure);
        when(mongo.insert(any(SnapshotETY.class))).thenThrow(failure);
        var id = ServiceIsolationTest.ID;
        List<org.assertj.core.api.ThrowableAssert.ThrowingCallable> calls = List.of(
            () -> repo.findById(id), () -> repo.getSnapshot(id), () -> repo.insertSnapshot(SnapshotETY.empty()),
            () -> repo.insertAll(List.of()), () -> repo.existsBySystem("s"), () -> repo.existsBySystemVersionAndRelease("s", "v", new Date()),
            () -> repo.getInsertions(new Date()), () -> repo.getDeletions(new Date()), () -> repo.getEveryActiveTerminology(),
            () -> repo.findByIds(List.of()), () -> repo.findBySystem("s"), () -> repo.getActiveDocumentCount(),
            () -> repo.getBySystem("s", PageRequest.of(0, 1)));
        for (var call : calls) assertThatThrownBy(call).isInstanceOf(OperationException.class).hasCause(failure);
        reset(mongo);
        when(mongo.find(any(Query.class), eq(TerminologyETY.class))).thenReturn(List.of());
        when(mongo.updateMulti(any(Query.class), any(Update.class), eq(TerminologyETY.class))).thenThrow(failure);
        assertThatThrownBy(() -> repo.deleteBySystem("s")).isInstanceOf(OperationException.class).hasCause(failure);
        assertThatThrownBy(() -> repo.updateBySystem("s", "v", new Date(), List.of())).isInstanceOf(OperationException.class).hasCause(failure);
    }
}
