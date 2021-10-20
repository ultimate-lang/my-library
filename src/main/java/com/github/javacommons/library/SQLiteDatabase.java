package com.github.javacommons.library;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.sql2o.Sql2o;

public class SQLiteDatabase {

    //String url;
    private SQLiteDS ds;

    public SQLiteDatabase(String path) {
        //this.url = "jdbc:sqlite:" + path;
        this.ds = new SQLiteDS(path);
    }
    
    public String getUrl() {
        return this.ds.getUrl();
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
    
    public Sql2o newSql2o() {
        return new Sql2o(this.getDataSource());
    }
    
    public SQLiteJodd newJodd() {
        return new SQLiteJodd(this);
    }
    
    public SQLiteOrmLite newOrmLite() throws SQLException {
        return new SQLiteOrmLite(this);
    }

    public void executeUpdate(String sql) throws SQLException {
        try (Connection conn = this.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        }
    }

    public void executeUpdate(List<String> sqlList) throws SQLException {
        try (Connection conn = this.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                for (String sql : sqlList) {
                    stmt.executeUpdate(sql);
                }
            }
        }
    }

    public void executeUpdateFromResource(String path) throws SQLException {
        this.executeUpdate(Resources.asString(path));
    }

}
