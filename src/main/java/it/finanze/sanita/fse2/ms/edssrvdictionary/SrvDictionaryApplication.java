/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import it.finanze.sanita.fse2.ms.edssrvdictionary.client.handler.RestTemplateResponseErrorHandler;

@SpringBootApplication
public class SrvDictionaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrvDictionaryApplication.class, args);
	}


	/**
	 * Definizione rest template.
	 * 
	 * @return	rest template
	 */
	@Bean 
	@Qualifier("restTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
		return restTemplate;
	} 
}
