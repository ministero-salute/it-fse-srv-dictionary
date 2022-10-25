package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.TerminologyETY;

public class EntityDTOTest extends AbstractTest {
    private final String TEST_TERMINOLOGY_ID = "test_terminology_ID"; 
    private final String TEST_TERMINOLOGY_SYSTEM = "test_terminology_system"; 
    private final String TEST_TERMINOLOGY_CODE = "test_terminology_code"; 
    private final String TEST_TERMINOLOGY_VERSION = "test_terminology_version"; 
    private final String TEST_TERMINOLOGY_DESCRIPTION = "test_terminology_description";  

    @Test
    @DisplayName("Test terminology entity creation")
    void createTerminologyEtyTest() throws Exception{
        TerminologyETY ety = new TerminologyETY();

        ety.setId(TEST_TERMINOLOGY_ID);
        ety.setSystem(TEST_TERMINOLOGY_SYSTEM);
        ety.setCode(TEST_TERMINOLOGY_CODE);
        ety.setVersion(TEST_TERMINOLOGY_VERSION);
        ety.setDescription(TEST_TERMINOLOGY_DESCRIPTION);

        assertEquals(String.class, ety.getId().getClass());
        assertEquals(String.class, ety.getDescription().getClass());
        assertEquals(String.class, ety.getCode().getClass());
        assertEquals(String.class, ety.getVersion().getClass());
        assertEquals(String.class, ety.getSystem().getClass());

        assertEquals(TEST_TERMINOLOGY_ID, ety.getId());
        assertEquals(TEST_TERMINOLOGY_DESCRIPTION, ety.getDescription());
        assertEquals(TEST_TERMINOLOGY_CODE, ety.getCode());
        assertEquals(TEST_TERMINOLOGY_VERSION, ety.getVersion());
        assertEquals(TEST_TERMINOLOGY_SYSTEM, ety.getSystem());
    }
}


