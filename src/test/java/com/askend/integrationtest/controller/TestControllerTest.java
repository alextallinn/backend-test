package com.askend.integrationtest.controller;

import com.askend.integrationtest.IntegrationTestSupport;
import io.restassured.response.ValidatableResponse;
import org.askend.controller.responce.FilterValuesResponse;
import org.askend.service.FilterService;
import org.askend.utils.SqlQuery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.BDDAssertions.then;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestControllerTest extends IntegrationTestSupport {

    Integer filterId;

    @BeforeAll
    void setUp() {
        this.filterId = new SqlQuery(jdbcTemplate, """
                INSERT INTO filter (name) 
                VALUES ('My Filter 1')
                """).insertReturningId();
        // ToDo: replace ID with sub select
        new SqlQuery(jdbcTemplate, """
                INSERT INTO filter_element (filter_id, filter_column_id, filter_type_id, value_text) 
                VALUES (:filterId, 1, 1, '4'), 
                       (:filterId, 2, 2, 'Moeow'), 
                       (:filterId, 3, 3, '2021-10-25'); 
                       """)
                .setParameter("filterId", filterId)
                .update();
    }

    @AfterAll
    void tearDown() {
        new SqlQuery(jdbcTemplate, "DELETE FROM filter_element WHERE filter_id = :filterId")
                .setParameter("filterId", filterId)
                .update();
        new SqlQuery(jdbcTemplate, "DELETE FROM filter WHERE id = :filterId")
                .setParameter("filterId", filterId)
                .update();
    }

    /** {@link org.askend.controller.TestController#getFilterValues(Integer)} */
    @Test
    void getFilterValues_success() {
        FilterValuesResponse filterValuesResponse = withDefaultRequestSpec()
                .get("/api/{0}", this.filterId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().body()
                .as(FilterValuesResponse.class);

        then(filterValuesResponse).isNotNull();
        then(filterValuesResponse.getName()).isEqualTo("My Filter 1");
        then(filterValuesResponse.getFilterValueList()).hasSize(3);
        then(filterValuesResponse.getFilterValueList().get(0).getValueText()).isEqualTo("4");
    }

    @Test
    void getFilterValues_error() {
        ValidatableResponse response = withDefaultRequestSpec()
                //.pathParam("id", 1)
                .get("/api/{0}", -99)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
        then(response.extract().asString())
                .isEqualTo(FilterService.ELEMENT_NOT_EXITS);
    }
}
