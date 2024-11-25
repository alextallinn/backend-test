package org.askend.controller.responce;

import org.askend.controller.request.FilterValue;

import java.util.List;

public final class FilterValuesResponse {
    private Integer id;
    private String name;
    private List<FilterValue> filterValueList;

    public Integer getId() {
        return id;
    }

    public FilterValuesResponse setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterValuesResponse setName(String name) {
        this.name = name;
        return this;
    }

    public List<FilterValue> getFilterValueList() {
        return filterValueList;
    }

    public FilterValuesResponse setFilterValueList(List<FilterValue> filterValueList) {
        this.filterValueList = filterValueList;
        return this;
    }
}
