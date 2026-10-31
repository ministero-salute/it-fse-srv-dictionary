/* SPDX-License-Identifier: AGPL-3.0-or-later */
package it.finanze.sanita.fse2.ms.edssrvdictionary.isolation;

import it.finanze.sanita.fse2.ms.edssrvdictionary.config.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.config.mongo.*;
import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.CollectionNaming;
import it.finanze.sanita.fse2.ms.edssrvdictionary.utility.ProfileUtility;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;

@Tag("ISOLATION")
@SpringJUnitConfig(ConfigurationIsolationTest.IsolationConfiguration.class)
@ActiveProfiles("ISOLATION")
@TestPropertySource(properties = {"docs.info.api-id=dictionary", "docs.info.summary=Summary", "docs.info.title=Dictionary",
    "info.app.version=1.0", "docs.info.description=Description", "docs.info.termsOfService=https://example.org/terms",
    "docs.info.contact.name=Support", "docs.info.contact.url=https://example.org", "server.port=8080", "validation.file-max-size=1234"})
class ConfigurationIsolationTest {
    @org.springframework.context.annotation.Profile("ISOLATION")
    @Configuration(proxyBeanMethods = false)
    @Import({CustomSwaggerCFG.class, OpenApiCFG.class, OpenTelemetryConfig.class, CollectionNaming.class, ProfileUtility.class})
    static class IsolationConfiguration {}
    @Autowired OpenApiCFG config;
    @Autowired CustomSwaggerCFG swagger;
    @Autowired CollectionNaming naming;
    @Test void mongoFactoryAndConverterUseConfiguredSchemaWithoutConnecting() {
        var props = new MongoPropertiesCFG();
        props.setUri("mongodb://localhost:27017"); props.setSchemaName("isolated_dictionary");
        var client = org.mockito.Mockito.mock(com.mongodb.client.MongoClient.class);
        var database = org.mockito.Mockito.mock(com.mongodb.client.MongoDatabase.class);
        org.mockito.Mockito.when(client.getDatabase("isolated_dictionary")).thenReturn(database);
        org.mockito.Mockito.when(database.getCodecRegistry()).thenReturn(com.mongodb.MongoClientSettings.getDefaultCodecRegistry());
        try (var clients = org.mockito.Mockito.mockStatic(com.mongodb.client.MongoClients.class);
             var context = new org.springframework.context.support.GenericApplicationContext()) {
            clients.when(() -> com.mongodb.client.MongoClients.create(org.mockito.ArgumentMatchers.any(com.mongodb.MongoClientSettings.class))).thenReturn(client);
            var cfg = new MongoDatabaseCFG();
            ReflectionTestUtils.setField(cfg, "props", props);
            ReflectionTestUtils.setField(cfg, "appContext", context);
            assertThat(cfg.createFactory(props).getMongoDatabase()).isSameAs(database);
            var template = cfg.createTemplate();
            assertThat(template.getDb()).isSameAs(database);
            var document = new org.bson.Document();
            template.getConverter().write(ServiceIsolationTest.term(), document);
            assertThat(document).containsEntry("system", "system").doesNotContainKey("_class");
        }
    }
    @Test void isolatedConfigurationAndCollectionNaming() {
        assertThat(swagger.getPort()).isEqualTo(8080);
        assertThat(new OpenTelemetryConfig().tracer()).isNotNull();
        assertThat(naming.getTerminologyCollection()).isEqualTo("terminology_eds");
        assertThat(naming.getSnapshotCollection()).isEqualTo("snapshot_eds");
        var utility = new ProfileUtility();
        assertThat(utility.isTestProfile()).isFalse();
        var env = new MockEnvironment(); ReflectionTestUtils.setField(utility, "environment", env);
        assertThat(utility.isTestProfile()).isFalse();
        env.setActiveProfiles("TEST"); assertThat(utility.isTestProfile()).isTrue();
        var testNaming = new CollectionNaming(); ReflectionTestUtils.setField(testNaming, "profileUtility", utility);
        assertThat(testNaming.getTerminologyCollection()).isEqualTo("test_terminology_eds");
        assertThat(testNaming.getSnapshotCollection()).isEqualTo("test_snapshot_eds");
    }
    @Test void openApiMetadataMultipartAndBinaryRestrictions() {
        var api = new OpenAPI().info(new Info()).paths(new Paths());
        config.disableAdditionalResponseProperties().customise(api);
        config.binaryProperties().customise(api);
        api.components(new Components());
        config.disableAdditionalResponseProperties().customise(api);
        config.binaryProperties().customise(api);
        var type = new StringSchema().name("type");
        var data = new ArraySchema().items(new StringSchema()).name("data");
        var binary = new ObjectSchema().name("binary").addProperty("type", type).addProperty("data", data).addProperty("other", new StringSchema());
        api.getComponents().addSchemas("Binary", binary).addSchemas("Other", new ObjectSchema()).addSchemas("EmptyBinary", new Schema<>().name("binary"));
        config.disableAdditionalResponseProperties().customise(api);
        config.binaryProperties().customise(api);
        assertThat(binary.getAdditionalProperties()).isEqualTo(false);
        assertThat(type.getMaxLength()).isEqualTo(10000);
        assertThat(data.getMaxItems()).isEqualTo(10000);
        assertThat(data.getItems().getMaxLength()).isEqualTo(10000);
        var upload = new ObjectSchema().addProperty("content_schematron", new StringSchema());
        var body = new RequestBody().content(new Content().addMediaType("multipart/form-data", new MediaType().schema(upload)));
        api.getPaths().addPathItem("/upload", new PathItem().post(new Operation().requestBody(body)).put(new Operation().requestBody(body)));
        api.getPaths().addPathItem("/read", new PathItem().get(new Operation()));
        config.openApiCustomiser().customise(api);
        assertThat(api.getInfo().getTitle()).isEqualTo("Dictionary");
        assertThat(api.getInfo().getContact().getName()).isEqualTo("Support");
        assertThat(api.getInfo().getExtensions()).containsEntry("x-api-id", "dictionary");
        assertThat(api.getServers()).singleElement().satisfies(s -> assertThat(s.getExtensions()).containsEntry("x-sandbox", true));
        assertThat(upload.getAdditionalProperties()).isEqualTo(false);
        assertThat(((Schema<?>) upload.getProperties().get("content_schematron")).getMaxLength()).isEqualTo(1234);
        // A plain data property also gets array bounds without requiring ArraySchema.
        binary.addProperty("data", new Schema<>().name("data"));
        config.binaryProperties().customise(api);
        assertThat(((Schema<?>) binary.getProperties().get("data")).getMinItems()).isZero();
    }
}
