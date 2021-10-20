package com.github.javacommons.my_library;

import com.github.javacommons.library.SQLiteDatabase;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class Sql2oMain {

    public static void main(String[] args) throws SQLException {

        System.out.println("Connecting to database...");
        final SQLiteDatabase db = new SQLiteDatabase("accout.db3");
        //final DataSource ds = db.getDataSource();

        //ds.executeUpdate("create table if not exists test(id INTEGER PRIMARY KEY AUTOINCREMENT, val varchar(20));delete from test;");
        db.executeUpdateFromResource("init.sql");

        //Sql2o sql2o = new Sql2o(ds);
        Sql2o sql2o = db.newSql2o();

        try (Connection conn = sql2o.open()) {

            try (Connection tran = sql2o.beginTransaction()) {
                String insertSql = "insert into test(val) values (:val)";
                Long key = tran
                        .createQuery(insertSql, true)
                        .addParameter("val", "val-1")
                        .executeUpdate()
                        .getKey(Long.class);
                System.err.println(key);
                key = tran
                        .createQuery(insertSql, true)
                        .addParameter("val", "val-2")
                        .executeUpdate()
                        .getKey(Long.class);
                System.err.println(key);
                tran.commit();
            }

            final List<Map<String, Object>> records = conn.createQuery("select * from test").executeAndFetchTable().asList();
            System.out.println(records);
            System.out.println(records.get(0));

            final Long count = conn.createQuery("select count(*) from test").executeScalar(Long.class);
            System.err.println("" + count + "件");

            final List<String> scalarList = conn.createQuery("select val from test").executeScalarList(String.class);
            System.err.println(scalarList);
        }
    }
    
}
