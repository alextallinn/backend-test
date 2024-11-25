package org.askend.dto;

public final class FilterValueDto {
    private Integer id;
    private Integer filterId;
    private String valueText;
    private Integer columnId;
    private Integer typeId;

    public Integer getId() {
        return id;
    }

    public FilterValueDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getFilterId() {
        return filterId;
    }

    public FilterValueDto setFilterId(Integer filterId) {
        this.filterId = filterId;
        return this;
    }

    public String getValueText() {
        return valueText;
    }

    public FilterValueDto setValueText(String valueText) {
        this.valueText = valueText;
        return this;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public FilterValueDto setColumnId(Integer columnId) {
        this.columnId = columnId;
        return this;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public FilterValueDto setTypeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        FilterValueDto that = (FilterValueDto) o;

        if (!id.equals(that.id))
            return false;
        if (!filterId.equals(that.filterId))
            return false;
        if (!valueText.equals(that.valueText))
            return false;
        if (!columnId.equals(that.columnId))
            return false;
        return typeId.equals(that.typeId);
    }

    @Override public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + filterId.hashCode();
        result = 31 * result + valueText.hashCode();
        result = 31 * result + columnId.hashCode();
        result = 31 * result + typeId.hashCode();
        return result;
    }
}
