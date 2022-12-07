/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.ChangeSetChunkDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetControllerTest extends AbstractTest {

    private final Date date = new Date();

    @MockBean
    private Tracer tracer;

    @Autowired
    private IChangeSetCTL changeSetCTL;

    @Test
    @DisplayName("Test changeSetChunkDTO")
    void ChangeSetChunkDTOTest() throws Exception{
        ChangeSetChunkDTO changeSet2 = changeSetCTL.changeSetChunks(date);
        assertNotNull(changeSet2);
        assertNotNull(changeSet2.getChunks());
        assertEquals(0, changeSet2.getChunks().getInsertions().getChunksItems());
        assertEquals(0, changeSet2.getChunks().getDeletions().getChunksItems());

    }
}
