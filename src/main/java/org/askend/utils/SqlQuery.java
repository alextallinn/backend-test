package org.askend.utils;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SqlQuery {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private String sql;
    private Map<String, Object> paramMap = new HashMap<>();

    public SqlQuery(NamedParameterJdbcTemplate namedParameterJdbcTemplate, @Language("SQL") String sql) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sql = sql;
    }

    public <T> T selectSingleOrNull(RowMapper<T> rowMapper) {
        List<T> results = this.namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
        if (results.isEmpty()) {
            return null;
        }
        if (results.size() == 1) {
            return results.get(0);
        }
        throw new IncorrectResultSizeDataAccessException(1, results.size());
    }

    public <T> T selectSingleOrNull(Class<T> clazz) {
        return selectSingleOrNull(resolveRowMapper(clazz));
    }

    public <T> List<T> select(Class<T> clazz) {
        return this.namedParameterJdbcTemplate.query(sql, paramMap, resolveRowMapper(clazz));
    }

    public int update() {
        return this.namedParameterJdbcTemplate.update(sql, this.paramMap);
    }

    public Integer insertReturningId() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(paramMap), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public SqlQuery setParameter(String param, Object value) {
        paramMap.put(param, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> RowMapper<T> resolveRowMapper(Class<T> clazz) {
        if (clazz.isEnum()) {
            return (rs, rowNum) -> (T) Enum.valueOf((Class<Enum>) clazz, rs.getString(1));
        }
        if (clazz.equals(Boolean.class)) {
            return (rs, rowNum) -> (T) rs.getObject(1, Boolean.class);
        }
        if (clazz.equals(Integer.class)) {
            return (rs, rowNum) -> (T) rs.getObject(1, Integer.class);
        }
        if (clazz.equals(BigDecimal.class)) {
            return (rs, rowNum) -> (T) rs.getObject(1, BigDecimal.class);
        }
        if (clazz.equals(Long.class)) {
            return (rs, rowNum) -> (T) rs.getObject(1, Long.class);
        }
        if (clazz.equals(String.class)) {
            return (rs, rowNum) -> (T) rs.getString(1);
        }
        if (clazz.equals(Map.class)) {
            return (RowMapper<T>) new ColumnMapRowMapper();
        }
        return new BeanPropertyRowMapper<>(clazz, true);
    }

    @NotNull public static String getAllWarnings(SQLWarning warning) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        while (warning != null) {
            stringBuilder.append(warning.getMessage()).append("\n");
            warning = warning.getNextWarning();
        }
        return stringBuilder.toString();
    }
}
