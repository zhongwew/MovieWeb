package Entitypackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import DBpackage.DBpool;

public class globalInstance {
	private static DBpool pool = null;
	public globalInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		if(pool != null) {
			pool.createConnection(1);
			return;
		}
		pool = new DBpool("com.mysql.jdbc.Driver","testuser","wang","jdbc:mysql://localhost:3306/moviedb");
	}
	public DBpool getPool() {
		return this.pool;
	}

}
