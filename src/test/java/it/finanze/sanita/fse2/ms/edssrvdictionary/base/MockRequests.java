/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;

public final class MockRequests {

    private MockRequests() {}

    public static MockHttpServletRequestBuilder findTerminologyByIdMockRequest(final String id) {
        return get("http://127.0.0.1:9088/v1/terminology/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 

    public static MockHttpServletRequestBuilder getTerminologyByChunkInsMockRequest( String snapshotid, int idx){
        return get("http://127.0.0.1:9088/v1/terminology/chunks/ins/" + snapshotid +"/" + idx).contentType(MediaType.APPLICATION_JSON_VALUE);

    }

    public static MockHttpServletRequestBuilder getTerminologyByChunkDelMockRequest( String snapshotid, int idx){
        return get("http://127.0.0.1:9088/v1/terminology/chunks/del/" + snapshotid +"/"+ idx).contentType(MediaType.APPLICATION_JSON_VALUE);

    }
    
    public static MockHttpServletRequestBuilder uploadTerminologiesCsv(MultipartFile file, String version, Date releaseDate){
        return post("http://127.0.0.1:9088/v1/terminology").contentType(MediaType.APPLICATION_JSON_VALUE);

    }
}
