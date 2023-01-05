package it.finanze.sanita.fse2.ms.edssrvdictionary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Constants.Profile.TEST)
@AutoConfigureMockMvc
class ErrorHandlingTest {
    
    @Autowired
    MockMvc mvc;

    @MockBean
    ITerminologySRV terminologySRV;

    @Test
    void constraintViolationExceptionTest() throws Exception {
        
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Constraint violation");

        Path path = mock(Path.class);
        when(path.toString()).thenReturn("path");

        // Populate path with node
        Path.Node node = mock(Path.Node.class);
        when(node.getName()).thenReturn("node");
        when(path.iterator()).thenReturn(new HashSet<Path.Node>().iterator());
        when(violation.getPropertyPath()).thenReturn(path);

        violations.add(violation);
        
        when(terminologySRV.createChunks(null))
            .thenThrow(new ConstraintViolationException("Constraint violation", violations));

        mvc.perform(get("/v1/changeset/terminology/chunks")).andExpect(status().isBadRequest());
    }

    @Test
    void typeMismatchExceptionTest() throws Exception {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);

        when(exception.getName()).thenReturn("name");
        MethodParameter methodParam = mock(MethodParameter.class);
        when(exception.getParameter()).thenReturn(methodParam);

        when(exception.getParameter().getParameter()).thenAnswer(
            invocation -> {
                return mock(Parameter.class);
            }
        );

        when(terminologySRV.createChunks(null))
            .thenThrow(new MethodArgumentTypeMismatchException(null, null, null, null, null));

        mvc.perform(get("/v1/changeset/terminology/chunks")).andExpect(status().isBadRequest());
    }

    @Test
    void genericErrorTest() throws Exception {
        when(terminologySRV.createChunks(null))
            .thenThrow(new RuntimeException("Business exception"));

        mvc.perform(get("/v1/changeset/terminology/chunks")).andExpect(status().isInternalServerError());
    }

    @Test
    void operationException() throws Exception {
        when(terminologySRV.createChunks(null))
            .thenThrow(new OperationException("Operation exception", new MongoException("Mongo exception")));

        mvc.perform(get("/v1/changeset/terminology/chunks")).andExpect(status().isInternalServerError());
    }
}
