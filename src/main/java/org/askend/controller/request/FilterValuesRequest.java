package org.askend.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class FilterValuesRequest {
    @Positive(message = "MUST_BE_POSITIVE")
    private Integer id;
    @NotNull(message = "USED_UNITS_REQUIRED")
    private String name;
    private List<FilterValue> filterValueList;

    public Integer getId() {
        return id;
    }

    public FilterValuesRequest setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterValuesRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<FilterValue> getFilterValueList() {
        return filterValueList;
    }

    public FilterValuesRequest setFilterValueList(List<FilterValue> filterValueList) {
        this.filterValueList = filterValueList;
        return this;
    }
}
