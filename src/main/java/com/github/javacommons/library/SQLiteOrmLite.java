package com.github.javacommons.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLiteOrmLite {
    private SQLiteDatabase db;
    private ConnectionSource ormliteConnectionSource;
    private boolean createTable = true;

    protected SQLiteOrmLite(SQLiteDatabase db) throws SQLException {
        this.db = db;
        this.ormliteConnectionSource = new JdbcConnectionSource(this.db.getUrl());
    }
    
    public ConnectionSource getConnectionSource() {
        return this.ormliteConnectionSource;
    }

    public <T extends Object> void createTable(Class<T> dataClass) throws SQLException {
        final List<String> createTableStatements = TableUtils.getCreateTableStatements(this.ormliteConnectionSource.getDatabaseType(), dataClass);
        if (this.createTable) {
            System.err.println("--[Executed]");
        } else {
            System.err.println("--[Not Executed]");
        }
        for (int i = 0; i < createTableStatements.size(); i++) {
            System.err.println(createTableStatements.get(i).replaceAll("^CREATE TABLE", "CREATE TABLE IF NOT EXISTS") + ";");
        }
        if (this.createTable) {
            TableUtils.createTableIfNotExists(this.ormliteConnectionSource, dataClass);
        }
    }

    public void setCreateTable(boolean createTable) {
        this.createTable = createTable;
    }

    public static Map<String, Object> toMap(Object x) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map
                = mapper.convertValue(x, new TypeReference<Map<String, Object>>() {
                });
        return map;
    }

    public static <T extends Object> List<Map<String, Object>> toMapList(List<T> list) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map
                    = mapper.convertValue(list.get(i), new TypeReference<Map<String, Object>>() {
                    });
            result.add(map);
        }
        return result;
    }

}
