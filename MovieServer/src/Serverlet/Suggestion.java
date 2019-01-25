package Serverlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import Entitypackage.globalInstance;

/**
 * Servlet implementation class Suggestion
 */
@WebServlet("/Suggestion")
public class Suggestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Suggestion() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
		JSONObject moviejs = new JSONObject();
		JSONObject starjs = new JSONObject();
		JSONArray sug_arr = new JSONArray();
		
		response.setContentType("text/html");
		if(request.getParameter("keyword")==null) return;
		String keyword = request.getParameter("keyword");
		String[] key = keyword.split(" ");
		StringBuffer p_key = new StringBuffer();
		for(int i = 0;i<key.length;i++) {
			p_key.append("+"+key[i]+"* ");
		}
		String query = "select * from movies where match(title) against(? in boolean mode) or edth(title,?,?) limit 5;";
		System.out.println(query);
		String query2 = "select * from stars where match(name) against(? in boolean mode) or edth(name,?,?) limit 5;";
		System.out.println(query2);
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			PreparedStatement sta_m = (PreparedStatement)conn.prepareStatement(query);
			sta_m.setString(1, p_key.toString());
			sta_m.setString(2, keyword);
			sta_m.setInt(3,keyword.length()/3);
			PreparedStatement sta_s = (PreparedStatement)conn.prepareStatement(query2);
			sta_s.setString(1, p_key.toString());
			sta_s.setString(2, keyword);
			sta_s.setInt(3,keyword.length()/3);
			
			//excute movie query
			ResultSet res = sta_m.executeQuery();
			moviejs.put("title", "Movies");
			JSONArray children_js = new JSONArray();
			while(res.next()) {
				JSONObject tempjs = new JSONObject();
				tempjs.put("id", res.getString(1));
				tempjs.put("title", res.getString(2));
				children_js.put(tempjs);
			}
			moviejs.put("children", children_js);
			sug_arr.put(moviejs);
			
			//excute star query
			res = sta_s.executeQuery();
			starjs.put("title", "Stars");
			JSONArray schil_js = new JSONArray();
			while(res.next()) {
				JSONObject tempjs = new JSONObject();
				tempjs.put("id", res.getString(1));
				tempjs.put("title", res.getString(2));
				schil_js.put(tempjs);
			}
			starjs.put("children", schil_js);
			sug_arr.put(starjs);
			if(schil_js.isNull(0) && children_js.isNull(0)) {
				js.put("type","error");
				mjs.put("errorMessage", "no result");
				js.put("message", mjs);
				response.getWriter().append(js.toString());
				return;
			}
			ins.getPool().returnConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		js.put("type", "success");
		mjs.put("suggestions",sug_arr);
		js.put("message", mjs);
		response.getWriter().append(js.toString());
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
