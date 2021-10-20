package com.github.javacommons.library;

import java.sql.Connection;
import jodd.db.DbOom;
import jodd.db.DbSession;
import jodd.db.ThreadDbSessionHolder;
import jodd.db.connection.DataSourceConnectionProvider;

public class SQLiteJodd {

    private SQLiteDatabase db;
    private DataSourceConnectionProvider cp;
    private DbOom jodd;

    protected SQLiteJodd(SQLiteDatabase db) {
        this.db = db;
        this.cp = new DataSourceConnectionProvider(this.db.getDataSource());
        this.jodd = DbOom.create()
                .withConnectionProvider(cp)
                .get();
        jodd.connect();
        DbSession session = new DbSession(cp);
        ThreadDbSessionHolder.set(session);
    }

    public DbOom getDB() {
        return this.jodd;
    }

    public DbSession getSession() {
        return new DbSession(cp);
    }

    public SQLiteQuery createQuery(Connection conn, String sqlString) {
        return new SQLiteQuery(this.jodd, conn, sqlString);
    }

    public SQLiteQuery createQuery(DbSession session, String sqlString) {
        return new SQLiteQuery(this.jodd, session, sqlString);
    }

    public SQLiteQuery createQuery(String sqlString) {
        return new SQLiteQuery(this.jodd, sqlString);
    }

}
