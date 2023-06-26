/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_BINARY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.DEFAULT_BINARY_MIN_SIZE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;


@Configuration
@SuppressWarnings("all")
public class OpenApiCFG {

	@Autowired
	private CustomSwaggerCFG customOpenapi;

	public OpenApiCFG() {
		// Empty constructor.
	}

	@Bean
	public OpenApiCustomiser disableAdditionalResponseProperties() {
		return openApi -> openApi.getComponents().
				getSchemas().
				values().
				forEach( s -> s.setAdditionalProperties(false));
	}

	@Bean
	public OpenApiCustomiser binaryProperties() {
		return openApi -> openApi
				.getComponents()
				.getSchemas()
				.values()
				.forEach(item -> {
					if(item.getName().equalsIgnoreCase("binary")) {
						item.getProperties().values().forEach(property -> {
							if(((Schema) property).getName().equalsIgnoreCase("type")){
								((Schema<Object>) property).setMaxLength(DEFAULT_BINARY_MAX_SIZE);
								((Schema<Object>) property).setMinLength(DEFAULT_BINARY_MIN_SIZE);
							} else if(((Schema) property).getName().equalsIgnoreCase("data")){
								((Schema<Object>) property).setMaxItems(DEFAULT_ARRAY_MAX_SIZE);
								((Schema<Object>) property).setMinItems(DEFAULT_ARRAY_MIN_SIZE);
								((ArraySchema) property).getItems().setMaxLength(DEFAULT_ARRAY_MAX_SIZE);
								((ArraySchema) property).getItems().setMinLength(DEFAULT_ARRAY_MIN_SIZE);
								System.out.println("Binary data setted");
							}
						});
					}
				});
	}


	@Bean
	public OpenApiCustomiser openApiCustomiser() {

		return openApi -> {

			// Populating info section.
			openApi.getInfo().setTitle(customOpenapi.getTitle());
			openApi.getInfo().setVersion(customOpenapi.getVersion());
			openApi.getInfo().setDescription(customOpenapi.getDescription());
			openApi.getInfo().setTermsOfService(customOpenapi.getTermsOfService());

			// Adding contact to info section
			final Contact contact = new Contact();
			contact.setName(customOpenapi.getContactName());
			contact.setUrl(customOpenapi.getContactUrl());
			openApi.getInfo().setContact(contact);

			// Adding extensions
			openApi.getInfo().addExtension("x-api-id", customOpenapi.getApiId());
			openApi.getInfo().addExtension("x-summary", customOpenapi.getApiSummary());

			// Adding servers
			final List<Server> servers = new ArrayList<>();
			final Server devServer = new Server();
			devServer.setDescription("Dictionary Development URL");
			devServer.setUrl("http://localhost:" + customOpenapi.getPort());
			devServer.addExtension("x-sandbox", true);

			servers.add(devServer);
			openApi.setServers(servers);

			openApi.getPaths().values().stream().filter(item -> item.getPost() != null).forEach(item -> {

				final RequestBody rb = item.getPost().getRequestBody();
				if(rb!=null) {
					MediaType mediaType = rb.getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE);
					if(mediaType != null) {
						final Schema<MediaType> schema = mediaType.getSchema();
						schema.additionalProperties(false);
					}
				}
			});

			openApi.getPaths().values().stream().filter(item -> item.getPut() != null).forEach(item -> {
				final Schema<MediaType> schema = item.getPut().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();
				schema.additionalProperties(false);
			});
			
			//START 
			SecurityScheme jwtScheme = new SecurityScheme()
			        .name(Constants.Headers.JWT_BUSINESS_HEADER)
			        .type(SecurityScheme.Type.APIKEY)
			        .in(SecurityScheme.In.HEADER);
			openApi.getComponents().addSecuritySchemes(Constants.Headers.JWT_BUSINESS_HEADER, jwtScheme);
			
			SecurityScheme authScheme = new SecurityScheme()
			        .type(SecurityScheme.Type.HTTP)
			        .bearerFormat("JWT")
			        .scheme("bearer")
			        .description("JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token} [RFC8725](https://tools.ietf.org/html/RFC8725).\"");
			        
			openApi.getComponents().addSecuritySchemes("bearerAuth", authScheme);

			
			List<SecurityRequirement> securityReqGOV = Arrays.asList(
					new SecurityRequirement().addList("bearerAuth"),
					new SecurityRequirement().addList(Constants.Headers.JWT_BUSINESS_HEADER));
			
			// Add security requirements to the two endpoints that require jwt
//			openApi.getPaths().get("/v1/certificate/revoke/multi").getPost()
//			.security(securityReqGOV);
		};
	}

	private void disableAdditionalPropertiesToMultipart(Content content) {
		if (content.containsKey(MULTIPART_FORM_DATA_VALUE)) {
			content.get(MULTIPART_FORM_DATA_VALUE).getSchema().setAdditionalProperties(false);
		}
	}

	private void setAdditionalProperties(Schema<?> schema) {
		if (schema == null) return;
		schema.setAdditionalProperties(false);
		handleSchema(schema);
	}

	private void handleSchema(Schema<?> schema) {
		getProperties(schema).forEach(this::handleArraySchema);
		handleArraySchema(schema);
	}

	private Collection<Schema> getProperties(Schema<?> schema) {
		if (schema.getProperties() == null) return new ArrayList<>();
		return schema.getProperties().values();
	}

	private void handleArraySchema(Schema<?> schema) {
		ArraySchema arraySchema = getSchema(schema, ArraySchema.class);
		if (arraySchema == null) return;
		setAdditionalProperties(arraySchema.getItems());
	}

	private <T> T getSchema(Schema<?> schema, Class<T> clazz) {
		try { return clazz.cast(schema); }
		catch(ClassCastException e) { return null; }
	}

	@Bean
	public MappingJackson2HttpMessageConverter octetStreamJsonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM));
		return converter;
	}
}