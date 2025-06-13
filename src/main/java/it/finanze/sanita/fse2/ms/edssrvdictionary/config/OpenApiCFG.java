/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 *
 * Copyright (C) 2023 Ministero della Salute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ValidationUtility.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Configuration
@SuppressWarnings("all")
public class OpenApiCFG {

	@Autowired
	private CustomSwaggerCFG customOpenapi;

	@Bean
	public OpenApiCustomizer disableAdditionalResponseProperties() {
		return openApi -> {
			if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
				openApi.getComponents().getSchemas().values().forEach(s -> s.setAdditionalProperties(false));
			}
		};
	}

	@Bean
	public OpenApiCustomizer binaryProperties() {
		return openApi -> {
			if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) return;
			openApi.getComponents().getSchemas().values().forEach(schema -> {
				if ("binary".equalsIgnoreCase(schema.getName()) && schema.getProperties() != null) {
					schema.getProperties().values().forEach(property -> {
						Schema<?> propSchema = (Schema<?>) property;
						if ("type".equalsIgnoreCase(propSchema.getName())) {
							propSchema.setMaxLength(DEFAULT_BINARY_MAX_SIZE);
							propSchema.setMinLength(DEFAULT_BINARY_MIN_SIZE);
						} else if ("data".equalsIgnoreCase(propSchema.getName())) {
							propSchema.setMaxItems(DEFAULT_ARRAY_MAX_SIZE);
							propSchema.setMinItems(DEFAULT_ARRAY_MIN_SIZE);
							if (propSchema instanceof ArraySchema arrSchema) {
								arrSchema.getItems().setMaxLength(DEFAULT_ARRAY_MAX_SIZE);
								arrSchema.getItems().setMinLength(DEFAULT_ARRAY_MIN_SIZE);
							}
						}
					});
				}
			});
		};
	}

	@Bean
	public OpenApiCustomizer openApiCustomiser() {
		return openApi -> {
			// Info section
			openApi.getInfo().setTitle(customOpenapi.getTitle());
			openApi.getInfo().setVersion(customOpenapi.getVersion());
			openApi.getInfo().setDescription(customOpenapi.getDescription());
			openApi.getInfo().setTermsOfService(customOpenapi.getTermsOfService());

			Contact contact = new Contact();
			contact.setName(customOpenapi.getContactName());
			contact.setUrl(customOpenapi.getContactUrl());
			openApi.getInfo().setContact(contact);

			openApi.getInfo().addExtension("x-api-id", customOpenapi.getApiId());
			openApi.getInfo().addExtension("x-summary", customOpenapi.getApiSummary());

			// Adding servers
			final List<Server> servers = new ArrayList<>();
			final Server devServer = new Server();
			devServer.setDescription("Servizio terminologie");
			devServer.addExtension("x-sandbox", true);
			openApi.setServers(List.of(devServer));
			servers.add(devServer);
			openApi.setServers(servers);

			openApi.getPaths().values().stream().filter(item -> item.getPost() != null).forEach(item -> {
				final Schema<MediaType> schema = item.getPost().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();
				schema.additionalProperties(false);

			});

			openApi.getPaths().values().stream().filter(item -> item.getPut() != null).forEach(item -> {
				final Schema<MediaType> schema = item.getPut().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();
				schema.additionalProperties(false);
			// Multipart handling for POST and PUT
			openApi.getPaths().values().forEach(pathItem -> {
				if (pathItem.getPost() != null) {
					applyMultipartRules(pathItem.getPost().getRequestBody().getContent());
				}
				if (pathItem.getPut() != null) {
					applyMultipartRules(pathItem.getPut().getRequestBody().getContent());
				}
			});
			});
		};
	}

	private void applyMultipartRules(Content content) {
		if (content != null && content.containsKey(MULTIPART_FORM_DATA_VALUE)) {
			Schema<?> schema = content.get(MULTIPART_FORM_DATA_VALUE).getSchema();
			schema.setAdditionalProperties(false);
			if (schema.getProperties() != null && schema.getProperties().get("content_schematron") != null) {
				schema.getProperties().get("content_schematron").setMaxLength(customOpenapi.getFileMaxLength());
			}
		}
	}

	private void setAdditionalProperties(Schema<?> schema) {
		if (schema != null) {
			schema.setAdditionalProperties(false);
			handleSchema(schema);
		}
	}

	private void handleSchema(Schema<?> schema) {
		getProperties(schema).forEach(this::handleArraySchema);
		handleArraySchema(schema);
	}

	private Collection<Schema> getProperties(Schema<?> schema) {
		return schema.getProperties() != null ? schema.getProperties().values() : new ArrayList<>();
	}

	private void handleArraySchema(Schema<?> schema) {
		if (schema instanceof ArraySchema arraySchema) {
			setAdditionalProperties(arraySchema.getItems());
		}
	}
}
