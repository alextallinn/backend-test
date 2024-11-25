package org.askend.service;

import org.askend.controller.ResponseIdentifierError;
import org.askend.controller.request.FilterValue;
import org.askend.controller.request.FilterValuesRequest;
import org.askend.controller.responce.FilterColumnsResponse;
import org.askend.controller.responce.FilterTypeResponse;
import org.askend.controller.responce.FilterValuesResponse;
import org.askend.dto.FilterDto;
import org.askend.dto.FilterValueDto;
import org.askend.mapper.FilterCriteriaMapper;
import org.askend.repository.FilterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilterService {
    private static final Logger log = LoggerFactory.getLogger(FilterService.class);
    public static final String ELEMENT_NOT_EXITS = "ELEMENT_NOT_EXITS";
    private final FilterRepository filterRepository;
    private final FilterCriteriaMapper filterCriteriaMapper;


    @Autowired
    public FilterService(FilterRepository filterRepository, FilterCriteriaMapper filterCriteriaMapper) {
        this.filterRepository = filterRepository;
        this.filterCriteriaMapper = filterCriteriaMapper;
    }

    public List<FilterColumnsResponse> getFilterColumns() {
        return filterRepository.getFilterColumList().stream()
                .map(filterCriteriaMapper::toResponse)
                .toList();
    }

    public Map<String, List<FilterTypeResponse>> getFilterTypes() {
        return filterRepository.getFilterTypeMap().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().stream()
                        .map(filterCriteriaMapper::toResponse)
                        .toList()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public FilterValuesResponse getFilterValues(Integer filterId) throws ResponseIdentifierError {
        FilterDto filterDto = filterRepository.getFilter(filterId);
        if (filterDto == null) {
            throw new ResponseIdentifierError(ELEMENT_NOT_EXITS);
        }
        var retVal = filterCriteriaMapper.toResponse(filterDto);
        List<FilterValueDto> filterValues = filterRepository.getFilterValueListByFilterId(filterId);
        retVal.setFilterValueList(filterValues.stream()
                .map(filterCriteriaMapper::toResponse)
                .toList());
        return retVal;
    }

    public FilterValuesResponse saveFilterValues(FilterValuesRequest filterValuesRequest) {
        FilterDto filterDto = filterCriteriaMapper.toDto(filterValuesRequest);
        filterDto = filterRepository.saveFilter(filterDto);
        Integer filterId = filterDto.getId();
        List<FilterValueDto> filterValueDtoList = filterValuesRequest.getFilterValueList().stream()
                .map(filterCriteriaMapper::toDto)
                .map(dto -> dto.setFilterId(filterId))
                .toList();
        List<FilterValue> filterValueList = filterRepository.saveFilterValueList(filterId, filterValueDtoList).stream()
                .map(filterCriteriaMapper::toResponse)
                .toList();
        FilterValuesResponse response = filterCriteriaMapper.toResponse(filterDto);
        response.setFilterValueList(filterValueList);
        return response;
    }

    public boolean deleteFilterElement(Integer elementId) {
        boolean result = filterRepository.deleteFilterElement(elementId);
        if (!result) {
            log.warn("Error deleting filter element: {}", elementId);
        }
        return result;
    }

    public int deleteFilter(Integer id) {
        int result = filterRepository.deleteFilter(id);
        if (result == 0) {
            log.info("Error deleting filter with elements: {}", id);
        }
        return result;
    }
}
