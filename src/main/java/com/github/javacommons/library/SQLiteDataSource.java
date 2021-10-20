package com.github.javacommons.library;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.sql2o.Sql2o;

public class SQLiteDataSource extends org.sqlite.SQLiteDataSource {

    public SQLiteDataSource(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        this.setUrl(url);
    }

    public void executeUpdate(String sql) throws SQLException {
        try(Connection conn = this.getConnection()) {
            try(Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        }
    }
    
    public void executeUpdate(List<String> sqlList) throws SQLException {
        try(Connection conn = this.getConnection()) {
            try(Statement stmt = conn.createStatement()) {
                for(String sql: sqlList) {
                    stmt.executeUpdate(sql);
                }
            }
        }
    }
    
    public void executeUpdateFromResource(String path) throws SQLException {
        this.executeUpdate(Resources.asString(path));
    }
    
    public List<String> tableList() {
        Sql2o db = new Sql2o(this);
        try (org.sql2o.Connection conn = db.open()) {
            final List<String> result = conn.createQuery("SELECT name FROM sqlite_master WHERE type='table' and name<>'sqlite_sequence'")
                    .executeScalarList(String.class);
            return result;
        }
    }
    
    public boolean tableExists(String tableName) {
        List<String> tableList = this.tableList();
        return tableList.contains(tableName);
    }

}
