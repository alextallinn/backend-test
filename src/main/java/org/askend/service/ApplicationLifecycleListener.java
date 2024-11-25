package org.askend.service;

import org.apache.commons.io.IOUtils;
import org.askend.utils.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class ApplicationLifecycleListener {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLifecycleListener.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ApplicationLifecycleListener(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @EventListener
    public void contextRefreshed(ContextRefreshedEvent event) {
        String ddlSql = getResourceFileAsString("initialize-db-DDL.sql");
        Arrays.stream(ddlSql.split(";\n")).forEach(subSql -> {
            if (subSql.trim().length() <= 1)
                return;
            namedParameterJdbcTemplate.execute(subSql, Collections.emptyMap(), new PreparedStatementCallback<Object>() {
                @Override
                public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    int count = ps.executeUpdate();
                    log.info("Database schema applied, count: {}, warnings: {}, SQL start: {}...",
                            count, SqlQuery.getAllWarnings(ps.getWarnings()),
                            subSql.substring(0, Math.min(30, subSql.length())).replaceAll("\n", " "));
                    return count;
                }
            });
        });
        String sql = getResourceFileAsString("initialize-db.sql");
        Arrays.stream(sql.split(";\n")).forEach(subSql -> {
            if (subSql.trim().length() <= 1)
                return;
            int updated = new SqlQuery(namedParameterJdbcTemplate, subSql)
                    .update();
            log.info("Success sql updated: {}, SQL start: {}...",
                    updated, subSql.substring(0, Math.min(30, subSql.length())).replaceAll("\n", " "));
        });
    }

    protected String getResourceFileAsString(String resourcePath) {
        try {
            URL resource =
                    this.getClass().getResource(resourcePath.startsWith("/") ? resourcePath : ("/" + resourcePath));
            File file = new File(resource.toURI());
            // remove UTF8 BOM, ZERO WIDTH NO-BREAK SPACE
            String returnString = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);
            return returnString.startsWith("\uFEFF") ? returnString.replaceFirst("\uFEFF", "") : returnString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
