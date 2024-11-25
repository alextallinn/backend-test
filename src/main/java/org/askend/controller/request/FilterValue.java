package org.askend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FilterValue {
    private Integer id;
    @NotNull(message = "REQUIRED")
    @Positive(message = "MUST_BE_POSITIVE")
    private Integer columnId;
    @NotNull(message = "REQUIRED")
    @Positive(message = "MUST_BE_POSITIVE")
    private Integer typeId;
    @NotBlank(message = "REQUIRED")
    private String valueText;

    public Integer getId() {
        return id;
    }

    public FilterValue setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public FilterValue setColumnId(Integer columnId) {
        this.columnId = columnId;
        return this;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public FilterValue setTypeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getValueText() {
        return valueText;
    }

    public FilterValue setValueText(String valueText) {
        this.valueText = valueText;
        return this;
    }
}
