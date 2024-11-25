package org.askend.mapper;

import org.askend.controller.request.FilterValue;
import org.askend.controller.request.FilterValuesRequest;
import org.askend.controller.responce.FilterColumnsResponse;
import org.askend.controller.responce.FilterTypeResponse;
import org.askend.controller.responce.FilterValuesResponse;
import org.askend.dto.FilterColumnDto;
import org.askend.dto.FilterDto;
import org.askend.dto.FilterTypeDto;
import org.askend.dto.FilterValueDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilterCriteriaMapper {
    FilterColumnsResponse toResponse(FilterColumnDto filterCriteria);

    FilterTypeResponse toResponse(FilterTypeDto filterTypeDto);

    FilterValue toResponse(FilterValueDto filterValues);

    FilterValuesResponse toResponse(FilterDto filterDto);

    FilterDto toDto(FilterValuesRequest filterValuesRequest);

    FilterValueDto toDto(FilterValue filterValue);
}
