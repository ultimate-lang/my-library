import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jodd.db.DbOom;
import jodd.db.DbQuery;
import jodd.db.DbSession;

public class SQLiteQuery extends DbQuery {

    public SQLiteQuery(DbOom dbOom, Connection conn, String sqlString) {
        super(dbOom, conn, sqlString);
    }

    public SQLiteQuery(DbOom dbOom, DbSession session, String sqlString) {
        super(dbOom, session, sqlString);
    }
    
    public SQLiteQuery(DbOom dbOom, String sqlString) {
        super(dbOom, sqlString);
    }
    
    public List<Map<String, Object>> executeAsList() throws SQLException {
        ResultSet rs = this.execute();
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
    
    public Object executeScalar() throws SQLException {
        ResultSet rs = this.execute();
        Object result = null;
        if (rs.next()) {
            result = rs.getObject(1);
        }
        return result;
    }

}
