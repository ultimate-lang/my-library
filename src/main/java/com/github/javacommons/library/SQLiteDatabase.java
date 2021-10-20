package com.github.javacommons.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jodd.db.DbOom;
import jodd.db.DbSession;
import jodd.db.ThreadDbSessionHolder;
import jodd.db.connection.DataSourceConnectionProvider;

public class SQLiteDatabase {

    //String url;
    private SQLiteDataSource ds;
    // OrmLite
    private ConnectionSource ormliteConnectionSource;
    private boolean createTable = true;
    // DbOom
    DataSourceConnectionProvider cp;
    DbOom db;

    public SQLiteDatabase(String path) {
        //this.url = "jdbc:sqlite:" + path;
        this.ds = new SQLiteDataSource(path);
        Logger.setGlobalLogLevel(Level.ERROR);
        try {
            this.ormliteConnectionSource = new JdbcConnectionSource(this.ds.getUrl());
            this.cp = new DataSourceConnectionProvider(ds);
            this.db = DbOom.create()
                    .withConnectionProvider(cp)
                    .get();
            db.connect();
            DbSession session = new DbSession(cp);
            ThreadDbSessionHolder.set(session);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SQLiteDatabase.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public SQLiteDataSource getDataSource() {
        return this.ds;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
    
    public ConnectionSource ormliteGetConnectionSource() {
        return this.ormliteConnectionSource;
    }

    public <T extends Object> void ormliteCreateTable(Class<T> dataClass) throws SQLException {
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

    public void ormliteSetCreateTable(boolean createTable) {
        this.createTable = createTable;
    }

    public static Map<String, Object> ormliteToMap(Object x) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map
                = mapper.convertValue(x, new TypeReference<Map<String, Object>>() {
                });
        return map;
    }

    public static <T extends Object> List<Map<String, Object>> ormliteToMapList(List<T> list) {
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

    public DbOom dboomGetDB() {
        return this.db;
    }
    
    public DbSession dboomGetSession() {
        return new DbSession(cp);
    }
    
    public SQLiteQuery dboomCreateQuery(Connection conn, String sqlString) {
        return new SQLiteQuery(this.db, conn, sqlString);
    }

    public SQLiteQuery dboomCreateQuery(DbSession session, String sqlString) {
        return new SQLiteQuery(this.db, session, sqlString);
    }
    
    public SQLiteQuery dboomCreateQuery(String sqlString) {
        return new SQLiteQuery(this.db, sqlString);
    }
    
}
