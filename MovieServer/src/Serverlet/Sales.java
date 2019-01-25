package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.ResultSet;
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

import Entitypackage.UserInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Sales
 */
@WebServlet("/Sales")
public class Sales extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Sales() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		if(user == null) {
			return;
		}
		String id = user.getId();
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta = (Statement)conn.createStatement();
			ResultSet res = sta.executeQuery("select title,saleDate from sales join movies on sales.movieId = movies.id where customerId = "+id+";");
			JSONArray jarr = new JSONArray();
			int key = 1; //key
			while(res.next()) {
				JSONObject tempjs = new JSONObject();
				tempjs.put("key", key);
				tempjs.put("title", res.getString(1));
				tempjs.put("saleDate", res.getDate(2).toString());
				jarr.put(tempjs);
				key++;
				}
			if(jarr.isNull(0)) {
				js.put("type", "error");
				mjs.put("errorMessage", "no result");
				js.put("message", mjs);
				return;
			}
			js.put("type", "success");
			mjs.put("movies", jarr);
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
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		String id = user.getId();
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		JSONObject recjs = new JSONObject(stb.toString());
		JSONArray p_movieids = recjs.getJSONArray("movies");
		long p_date = recjs.getLong("saleDate");
		Date date = new Date(p_date);
		JSONObject js = new JSONObject();
		//Insert sales record in tables
		try {
			Connection conn = (Connection)ins.getPool().getConnection();
			Statement sta = (Statement)conn.createStatement();
			for(int i = 0;i<p_movieids.length();i++) {
				int res = sta.executeUpdate("insert into sales(customerId,movieId,saleDate) values ("+id+",\""+p_movieids.getString(i)+"\",\""+date.toString()+"\");");
			}
			ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			js.put("type", "error");
			e.printStackTrace();
			return;
		}
		
		js.put("type", "success");
		response.getWriter().append(js.toString());
		request.getSession().removeAttribute("cart");
	}

}
