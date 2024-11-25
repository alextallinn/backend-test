package org.askend.dto;

public class FilterTypeDto {
    private Long id;
    private String columnType;
    private String typeName;

    public Long getId() {
        return id;
    }

    public FilterTypeDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getColumnType() {
        return columnType;
    }

    public FilterTypeDto setColumnType(String columnType) {
        this.columnType = columnType;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public FilterTypeDto setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }
}
