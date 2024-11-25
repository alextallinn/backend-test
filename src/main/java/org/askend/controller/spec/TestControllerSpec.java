package org.askend.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.askend.controller.ResponseIdentifierError;
import org.askend.controller.request.FilterValuesRequest;
import org.askend.controller.responce.FilterColumnsResponse;
import org.askend.controller.responce.FilterTypeResponse;
import org.askend.controller.responce.FilterValuesResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

public interface TestControllerSpec {

    @Operation(tags = "filterValues",
            description = "Get filter values with sub elements",
            summary = "Get filter values with sub elements",
            parameters = {
                    @Parameter(name = "id", in = PATH, required = true, description = "Filter id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filter response with values")
            })
    @GetMapping("{id}") FilterValuesResponse getFilterValues(@PathVariable Integer id) throws ResponseIdentifierError;

    @GetMapping("columns") List<FilterColumnsResponse> getFilterColumns();

    @GetMapping("types") Map<String, List<FilterTypeResponse>> getFilterTypes();

    @PutMapping("saveFilter") FilterValuesResponse saveFilter(@RequestBody FilterValuesRequest filterValuesRequest);

    @DeleteMapping("delete-filter?id={id}") Integer deleteFilter(@PathVariable Integer id);

    @DeleteMapping("delete-filter-element?id={elementId}") Boolean deleteFilterElement(@PathVariable Integer elementId);
}
