/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.repository;

import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ProfileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Collections.*;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.config.Constants.Profile.TEST_PREFIX;

@Configuration
public class CollectionNaming {

    @Autowired
    private ProfileUtility profiles;

    @Bean("chunksIndexBean")
    public String getChunksIndexCollection() {
        return profiles.isTestProfile() ? TEST_PREFIX + CHUNKS_INDEX : CHUNKS_INDEX;
    }

    @Bean("chunkBean")
    public String getChunkCollection() {
        return profiles.isTestProfile() ? TEST_PREFIX + CHUNKS : CHUNKS;
    }
    
    @Bean("webScrapingBean")
    public String getWebScrapingBeanCollection() {
        if (profiles.isTestProfile()) {
            return TEST_PREFIX + WEB_SCRAPING;
        }
        return WEB_SCRAPING;
    }
    
    
}
