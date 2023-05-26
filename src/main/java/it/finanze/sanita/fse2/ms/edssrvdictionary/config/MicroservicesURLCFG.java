/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class MicroservicesURLCFG {
 
	/**
	 * Ms eds-query host.
	 */
	@Value("${ms.url.eds-query}")
	private String queryHost;
	
}
