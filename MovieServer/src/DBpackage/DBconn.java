package DBpackage;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

public class DBconn {//encapsulate JDBC connection inside this class
	
	private String jbdcDriver = "com.mysql.jdbc.Driver";
	private String dbUrl = "jdbc:mysql://localhost:3306/moviedb";
	private String dbUser = "testuser";
	private String dbPassword = "wang";
	private java.sql.Connection conn = null;
	public DBconn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, NamingException{
//		Driver driver = (Driver)(Class.forName(this.jbdcDriver).newInstance());
//		DriverManager.registerDriver(driver);
		//normal way of creating connection
		//conn = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		//using Mysql connection pool
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/TestDB");
		conn = ds.getConnection();
	}
	
	public java.sql.Connection getConnection() {
		return conn;
	}
	public void closeConnection() throws SQLException {
		conn.close();
	}

}
