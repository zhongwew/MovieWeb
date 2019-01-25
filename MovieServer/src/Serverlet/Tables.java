package Serverlet;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Entitypackage.EmployeeInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Tables
 */
@WebServlet("/Tables")
public class Tables extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Tables() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		JSONArray jarr = new JSONArray();
		EmployeeInfo emp = (EmployeeInfo) request.getSession().getAttribute("employee");
		if(emp == null) {
			js.put("type", "error");
			mjs.put("errorMessage", "you have to login first");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			return;
		}
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta1 = (Statement)conn.createStatement();
			Statement sta2 = (Statement)conn.createStatement();
			ResultSet res1 = sta1.executeQuery("show tables");
			ResultSet res2 = null;
			while(res1.next()) {
				JSONObject tempjs = new JSONObject();
				JSONArray attrjs = new JSONArray();
				tempjs.put("name", res1.getString(1));
				System.out.println(res1.getString(1));
				res2 = sta2.executeQuery("select * from "+res1.getString(1)+" limit 1,10");
				ResultSetMetaData meta = res2.getMetaData();
				System.out.println(meta.getColumnCount());
				for(int i = 0;i<meta.getColumnCount();i++) {
					JSONObject columnjs = new JSONObject();
					columnjs.put("name", meta.getColumnName(i+1));
					columnjs.put("type", meta.getColumnTypeName(i+1));
					attrjs.put(columnjs);
				}
				tempjs.put("attributes", attrjs);
				jarr.put(tempjs);
			}
			if(jarr.isNull(0)) {
				js.put("type", "error");
				mjs.put("errorMessage", "no message");
				js.put("message", mjs);
				response.getWriter().append(js.toString());
				return;
			}
			js.put("type", "success");
			mjs.put("tables", jarr);
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
