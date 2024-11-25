package org.askend.repository;

import org.askend.dto.FilterColumnDto;
import org.askend.dto.FilterDto;
import org.askend.dto.FilterTypeDto;
import org.askend.dto.FilterValueDto;
import org.askend.utils.JsonUtil;
import org.askend.utils.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FilterRepository {
    private static final Logger log = LoggerFactory.getLogger(FilterRepository.class);
    public static final String FILTER_ID = "filterId";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FilterRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<FilterColumnDto> getFilterColumList() {
        return new SqlQuery(namedParameterJdbcTemplate, ""
                + "SELECT id, column_name AS name, column_type AS columnType FROM filter_column")
                .select(FilterColumnDto.class).stream()
                .toList();
    }

    public Map<String, List<FilterTypeDto>> getFilterTypeMap() {
        return new SqlQuery(namedParameterJdbcTemplate, ""
                + "SELECT id, type_name AS typeName, column_type AS columnType FROM filter_type")
                .select(FilterTypeDto.class).stream()
                .collect(Collectors.groupingBy(FilterTypeDto::getColumnType, Collectors.toList()));
    }

    public FilterDto getFilter(Integer id) {
        return new SqlQuery(namedParameterJdbcTemplate, ""
                + "SELECT id, name FROM filter WHERE id = :id")
                .setParameter("id", id)
                .selectSingleOrNull(FilterDto.class);
    }

    public List<FilterValueDto> getFilterValueListByFilterId(Integer filterId) {
        return new SqlQuery(namedParameterJdbcTemplate, ""
                + "SELECT id, value_text AS valueText, filter_column_id AS columnId, filter_type_id AS typeId, filter_id as filterId "
                + "FROM filter_element where filter_id = :filterId")
                .setParameter(FILTER_ID, filterId)
                .select(FilterValueDto.class);
    }

    public FilterValueDto getFilterValue(Integer id) {
        return new SqlQuery(namedParameterJdbcTemplate, ""
                + "SELECT id, value_text AS valueText, filter_column_id AS columnId, filter_type_id AS typeId, filter_id as filterId "
                + "FROM filter_element where id = :id")
                .setParameter("id", id)
                .selectSingleOrNull(FilterValueDto.class);
    }

    public FilterDto saveFilter(FilterDto filterDto) {
        if (filterDto.getId() == null) {
            Integer newId = new SqlQuery(namedParameterJdbcTemplate, ""
                    + "INSERT INTO filter (name) VALUES (:name)")
                    .setParameter("name", filterDto.getName())
                    .insertReturningId();
            return getFilter(newId);
        } else {
            return new SqlQuery(namedParameterJdbcTemplate, ""
                    + "UPDATE filter SET name = :name WHERE id = :id")
                    .setParameter("id", filterDto.getId())
                    .setParameter("name", filterDto.getName())
                    .update() > 0 ? filterDto : null;
        }
    }

    public List<FilterValueDto> saveFilterValueList(Integer filterId, List<FilterValueDto> filterValueDtoList) {
        Map<Integer, FilterValueDto> oldFilterValues = getFilterValueListByFilterId(filterId).stream()
                .collect(Collectors.toMap(FilterValueDto::getId, val -> val));
        if (CollectionUtils.isEmpty(filterValueDtoList) && CollectionUtils.isEmpty(oldFilterValues)) {
            return Collections.emptyList();
        } else if (CollectionUtils.isEmpty(filterValueDtoList) && !CollectionUtils.isEmpty(oldFilterValues)) {
            return new SqlQuery(namedParameterJdbcTemplate, "DELETE FROM filter_element WHERE filter_id = :filterId")
                    .setParameter(FILTER_ID, filterId)
                    .update() > 0 ? Collections.emptyList() : null;
        } else {
            // Insert / Update
            Map<Integer, FilterValueDto> res = filterValueDtoList.stream()
                    .map(dto -> {
                        if (dto.getId() == null
                                || (oldFilterValues.containsKey(dto.getId()) && !oldFilterValues.get(dto.getId()).equals(dto))) {
                            FilterValueDto newDto = this.saveFilterValue(dto);

                            log.warn("newDto: {}", JsonUtil.objectToJson(newDto));
                            return newDto;
                        }
                        return dto;
                    })
                    .collect(Collectors.toMap(FilterValueDto::getId, value -> value));

            oldFilterValues.values().stream()
                    .filter(dto -> !res.keySet().contains(dto.getId()))
                    .forEach(dto -> new SqlQuery(namedParameterJdbcTemplate, ""
                            + "DELETE FROM filter_element WHERE id = :id")
                            .setParameter("id", dto.getId())
                            .update());
            return res.values().stream().toList();
        }
    }

    public FilterValueDto saveFilterValue(FilterValueDto dto) {
        if (dto.getId() == null) {
            Integer newId = new SqlQuery(namedParameterJdbcTemplate, ""
                    + "INSERT INTO filter_element (filter_id, filter_column_id, filter_type_id, value_text) "
                    + "VALUES (:filterId, :columnId, :typeId, :valueText) ")
                    .setParameter(FILTER_ID, dto.getFilterId())
                    .setParameter("columnId", dto.getColumnId())
                    .setParameter("typeId", dto.getTypeId())
                    .setParameter("valueText", dto.getValueText())
                    .insertReturningId();
            return getFilterValue(newId);
        }
        return new SqlQuery(namedParameterJdbcTemplate, """
                UPDATE filter_element SET filter_column_id = :columnId, filter_type_id = :typeId, value_text = :valueText 
                WHERE id = :id
                """)
                .setParameter("id", dto.getId())
                .setParameter(FILTER_ID, dto.getFilterId())
                .setParameter("columnId", dto.getColumnId())
                .setParameter("typeId", dto.getTypeId())
                .setParameter("valueText", dto.getValueText())
                .update() > 0 ? dto : null;
    }

    public boolean deleteFilterElement(Integer elementId) {
        // ToDo: convert in to logical delete, if needed
        return new SqlQuery(namedParameterJdbcTemplate, "DELETE FROM filter_element WHERE id = :id")
                .setParameter("id", elementId)
                .update() > 0;
    }

    public int deleteFilter(Integer id) {
        // ToDo: convert in to logical delete, if needed
        int updated = new SqlQuery(namedParameterJdbcTemplate, "DELETE FROM filter_element WHERE filter_id = :filterId")
                .setParameter(FILTER_ID, id)
                .update();
        updated += new SqlQuery(namedParameterJdbcTemplate, "DELETE FROM filter WHERE id = :id")
                .setParameter("id", id)
                .update();
        return updated;
    }
}
