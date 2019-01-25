package DBpackage;
import java.lang.*;
import java.sql.*;
import java.util.Vector;

public class DBpool {//This is a self implemented database pool
	
	//data members
	private String jbdcDriver = "com.mysql.jdbc.Driver";
	private String dbUrl = "jdbc:mysql://localhost:3306/moviedb";
	private String dbUser = "testuser";
	private String dbPassword = "wang";
	private String testTable = "";
	private int initialConnectionNum = 10;
	private int maxConnectionNum = 50;
	private int incrementalConnectionNum = 5;
	private Vector<PooledConnection> connections = null;
	
//	public static void main(String[] arg) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, InterruptedException {
//		DBpool db = new DBpool("com.mysql.jdbc.Driver","testuser","wang","jdbc:mysql://localhost:3306/moviedb");
//		ResultSet res = db.getConnection().createStatement().executeQuery("select * from genres;");
//		while(res.next()) {
//			System.out.println(res.getString(2));
//		}
//	}
	
	
	//initialize the DBpool
	//connect the JDBC using driver, url, name and password
	public DBpool() {};
	public DBpool(String driver, String name, String pass, String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.jbdcDriver = driver;
		this.dbUser = name;
		this.dbPassword = pass;
		this.dbUrl = url;
		this.createPool();
	}
	
	public synchronized void createPool() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if(this.connections != null) {
			return;
		}
		Driver driver = (Driver)(Class.forName(this.jbdcDriver).newInstance());
		DriverManager.registerDriver(driver);
		this.connections = new Vector<PooledConnection>();
		this.createConnection(initialConnectionNum);
	}
	
	public void createConnection(int num) throws SQLException {
		for(int i = 0; i<num; i++) {
			if(this.connections.size() > this.maxConnectionNum) {
				return;
			}
			this.connections.addElement(new PooledConnection(newConnection()));
		}
	}
	
	public Connection newConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
		//for the first time we connect to the database
		//we might need to change the max size of pool based on the metadata
		if(this.connections.size() == 0) {
			DatabaseMetaData metadata = conn.getMetaData();
			int dbMaxconnectNum = metadata.getMaxConnections();
			if(dbMaxconnectNum > 0 && dbMaxconnectNum < this.maxConnectionNum) {
				this.maxConnectionNum = dbMaxconnectNum;
			}
		}
		return conn;
	}
	
	public synchronized Connection getConnection() throws SQLException, InterruptedException {
		Connection conn = null;
		if(this.connections.size() == 0) {
			return conn;
		}
		conn = this.getFreeConnection();
		while(conn == null) {
			this.wait(30);
			conn = this.getFreeConnection();
		}
		return conn;
	}
	
	private Connection getFreeConnection() throws SQLException {
		Connection conn = null;
		conn = this.findFreeConnection();
		if(conn == null) {
			this.createConnection(incrementalConnectionNum);
			this.findFreeConnection();
		}
		return conn;
	}
	
	private Connection findFreeConnection() throws SQLException {
		Connection conn = null;
		PooledConnection pol = null;
		for(int i = 0; i< connections.size();i++) {
			pol = this.connections.get(i);
			if(!pol.isBusy()) {
				conn = pol.getConnection();
				pol.setBusy(true);
				if(!this.testConnection(conn)) {
					conn = this.newConnection();
					pol.setCon(conn);
				}
				break;
			}
		}
		return conn;
	}
	
	private boolean testConnection(Connection conn) {
		boolean usable = true;
		return usable;
	}
	
	public void returnConnection(Connection conn) {
		if(this.connections == null) {
			return;
		}
		PooledConnection pol = null;
		for(int i = 0; i< this.connections.size(); i++) {
			pol = this.connections.get(i);
			if(pol.getConnection() == conn) {
				pol.setBusy(false);
			}
		}
	}
	
	private void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}
	
	public void closeConnectionPool() throws InterruptedException, SQLException {
		if(this.connections.size() == 0) {
			return;
		}
		PooledConnection pool = null;
		for(int i = 0;i<connections.size();i++) {
			pool = connections.get(i);
			if(pool.isBusy()) {
				this.wait(5000);
			}
			this.closeConnection(pool.getConnection());
			this.connections.remove(i);
		}
		this.connections = null;
	}
	
	public synchronized void refreshConnectionPool() throws InterruptedException, SQLException {
		if(this.connections == null) {
			return;
		}
		PooledConnection pol = null;
		for(int i = 0; i<this.connections.size();i++) {
			pol = this.connections.get(i);
			if(pol.isBusy()) {
				this.wait(5000);
			}
			this.closeConnection(pol.getConnection());
			pol.setCon(this.newConnection());
			pol.setBusy(false);
		}
	}
	
	private void wait(int mSceond) throws InterruptedException {
		Thread.sleep(mSceond);
	}
	
}

class PooledConnection{
	private Connection conn = null;
	private boolean Busy = false;
	
	public PooledConnection(Connection co) {
		conn = co;
	}
	public void setCon(Connection con) {
		this.conn = con;
	}
	public Connection getConnection() {
		return this.conn;
	}
	public boolean isBusy() {
		return Busy;
	}
	public void setBusy(boolean busy) {
		this.Busy = busy;
	}
}