package org.askend.dto;

public class FilterColumnDto {
    private String id;
    private String name;
    private String columnType;

    public String getId() {
        return id;
    }

    public FilterColumnDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterColumnDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getColumnType() {
        return columnType;
    }

    public FilterColumnDto setColumnType(String columnType) {
        this.columnType = columnType;
        return this;
    }

}
