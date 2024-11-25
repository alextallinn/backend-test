package com.askend.integrationtest;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.askend.spring_boot.TestApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag("integration-test")
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTestSupport {
    protected static final Logger log = LoggerFactory.getLogger(IntegrationTestSupport.class);
    @LocalServerPort int localPort;
    @Autowired private DataSource dataSource;
    @Autowired protected NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUpBeforeIntegrationsTestsStart() {
        RestAssured.basePath = "/";
        RestAssured.defaultParser = Parser.JSON;

        Path resourceDirectory = Paths.get("src", "test", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        System.setProperty("application.root", absolutePath);
        System.out.println("Configured 'application.root' with value: " + System.getProperty("application.root"));
    }

    @DynamicPropertySource
    static void injectProperties(DynamicPropertyRegistry r) {
        r.add("spring.datasource.username", () -> "su");
        r.add("spring.datasource.password", () -> "password");
    }

    @BeforeEach
    void setUpBeforeEachTest() {
        RestAssured.port = localPort;
    }

    protected static RequestSpecification withDefaultRequestSpec() {
        return RestAssured
                .given()
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE);
    }
}
