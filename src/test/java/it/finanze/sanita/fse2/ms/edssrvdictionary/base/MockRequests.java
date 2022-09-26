package it.finanze.sanita.fse2.ms.edssrvdictionary.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class MockRequests {

    private MockRequests() {}

    public static MockHttpServletRequestBuilder findTerminologyByIdMockRequest(final String id) {
        return get("http://127.0.0.1:9088/v1/terminology/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
    
}
