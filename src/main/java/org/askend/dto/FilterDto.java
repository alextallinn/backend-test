package org.askend.dto;

public class FilterDto {
    private Integer id;
    private String name;

    public FilterDto() {
    }

    public FilterDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public FilterDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterDto setName(String name) {
        this.name = name;
        return this;
    }
}
