package com.github.javacommons.my_library;

import com.github.javacommons.library.SQLiteDatabase;
import com.github.javacommons.library.SQLiteJodd;
import com.github.javacommons.library.SQLiteQuery;
import java.util.List;
import java.util.Map;
import jodd.db.DbSession;
import jodd.db.DbTransactionMode;

public class JoddMain {

    public static void main(String[] args) throws Exception {
        SQLiteDatabase db = new SQLiteDatabase("accout.db3");
        final SQLiteJodd jodd = db.newJodd();
        try (DbSession session = jodd.getSession()) {
            session.beginTransaction(DbTransactionMode.READ_WRITE_TX);
            SQLiteQuery sq = jodd.createQuery(session, "select * from accounts");
            final List<Map<String, Object>> list = sq.executeAsList();
            System.out.println(list.size());
            System.out.println(list);
            SQLiteQuery sq2 = jodd.createQuery(session, "select count(*) from accounts");
            System.out.println("sq2=" + sq2.executeScalar());
            SQLiteQuery sq3 = jodd.createQuery(session, "select count(*) from accounts");
            System.out.println("sq3=" + sq3.executeCount());
            SQLiteQuery sq4 = jodd.createQuery(session, "insert into accounts(name, passwd) values(:name, :passwd)");
            sq4.setGeneratedColumns();
            sq4.setString("name", "name-1");
            sq4.setString("passwd", "password-1");
            sq4.executeUpdate();
            long key = sq4.getGeneratedKey();
            System.out.println("key=" + key);
            SQLiteQuery sq5 = jodd.createQuery(session, "select * from accounts where id=:id");
            sq5.setLong("id", key);
            System.out.println(sq5.executeAsList());
            session.commitTransaction();
        }
    }

}
