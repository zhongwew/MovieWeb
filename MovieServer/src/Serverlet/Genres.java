package Serverlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

import Entitypackage.globalInstance;

/**
 * Servlet implementation class Genres
 */
@WebServlet("/Genres")
public class Genres extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;

    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Genres() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins  = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		JSONObject json = new JSONObject();
		JSONObject mjson = new JSONObject();
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			ResultSet res = conn.createStatement().executeQuery("select * from genres;");
			JSONArray jarr = new JSONArray();
			while(res.next()) {
				JSONObject js = new JSONObject();
				js.put("id", res.getInt(1));
				js.put("genre",res.getString(2));
				jarr.put(js);
			}
			if(jarr.isNull(0)) {
				json.put("type", "error");
				map.put("errorMessage", "no results return");
				json.put("message", map);
				response.getWriter().append(json.toString());
				return;
			}
			json.put("type", "success");
			mjson.put("genres", jarr);
			json.put("message", mjson);
			response.getWriter().append(json.toString());
			ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
