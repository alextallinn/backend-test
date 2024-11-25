package org.askend.controller;

import org.askend.controller.request.FilterValuesRequest;
import org.askend.controller.responce.FilterColumnsResponse;
import org.askend.controller.responce.FilterTypeResponse;
import org.askend.controller.responce.FilterValuesResponse;
import org.askend.controller.spec.TestControllerSpec;
import org.askend.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE})
public class TestController implements TestControllerSpec {
    private final FilterService filterService;

    @Autowired
    public TestController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping("test")
    public String test() {
        return "Hello, World!";
    }


    /**
     * Get existing filter with list of values
     *
     * @param id - identification of the filter
     * @return filter object
     * @throws ResponseIdentifierError
     */
    @Override @GetMapping("{id}")
    public FilterValuesResponse getFilterValues(@PathVariable Integer id) throws ResponseIdentifierError {
        return filterService.getFilterValues(id);
    }

    @Override @GetMapping("columns")
    public List<FilterColumnsResponse> getFilterColumns() {
        return filterService.getFilterColumns();
    }

    @Override @GetMapping("types")
    public Map<String, List<FilterTypeResponse>> getFilterTypes() {
        return filterService.getFilterTypes();
    }

    @Override @PutMapping("saveFilter")
    public FilterValuesResponse saveFilter(@RequestBody FilterValuesRequest filterValuesRequest) {
        return filterService.saveFilterValues(filterValuesRequest);
    }

    @Override @DeleteMapping("delete-filter?id={id}")
    public Integer deleteFilter(@PathVariable Integer id) {
        return filterService.deleteFilter(id);
    }

    @Override @DeleteMapping("delete-filter-element?id={elementId}")
    public Boolean deleteFilterElement(@PathVariable Integer elementId) {
        return filterService.deleteFilterElement(elementId);
    }
}
