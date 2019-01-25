package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.mysql.jdbc.Statement;

import Entitypackage.EmployeeInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Movie
 */
@WebServlet("/Movie")
public class Movie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Movie() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String p_id = request.getParameter("id");
		//select
		String query = null;
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		JSONObject tempjs = null;
		JSONArray tempjarr = null;
		JSONObject tempjs2 = null;
		try {
			query = new String("select id, title, year, director from movies where id = \""+p_id+"\";");
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta1 = (Statement)conn.createStatement();
			Statement sta2 = (Statement)conn.createStatement();
			ResultSet res1 = sta1.executeQuery(query);
			System.out.println(query);
			ResultSet res2 = null;
			while(res1.next()) {
				tempjs = new JSONObject();
				tempjs.put("id", res1.getString(1));
				tempjs.put("title", res1.getString(2));
				tempjs.put("year", res1.getInt(3));
				tempjs.put("director", res1.getString(4));
				tempjarr = new JSONArray();
				Vector<String> genres = null;
				res2 = sta2.executeQuery("select id, name from stars join stars_in_movies on stars.id = stars_in_movies.starId where movieId = \""+res1.getString(1)+"\";");
				while(res2.next()) {
					tempjs2 = new JSONObject();
					tempjs2.put("id", res2.getString(1));
					tempjs2.put("name",res2.getString(2));
					tempjarr.put(tempjs2);
				}
				tempjs.put("stars", tempjarr);
				tempjarr = new JSONArray();
				res2 = sta2.executeQuery("select name from genres join genres_in_movies on genres.id = genres_in_movies.genreId where movieId = \""+res1.getString(1)+"\";");
				genres = new Vector<String>();
				while(res2.next()) {
					genres.add(res2.getString(1));
				}
				tempjs.put("genres", genres);
				res2 = sta2.executeQuery("select rating from ratings where movieId = \""+res1.getString(1)+"\";");
				while(res2.next()) {
					tempjs.put("rating", res2.getFloat(1));
				}
			}
			if(tempjs == null) {//no results
				mjs.put("errorMessage", "no result");
				js.put("message", mjs);
				js.put("type", "error");
				response.getWriter().append(js.toString());
				return;
			}
			mjs.put("movie", tempjs);
			js.put("message", mjs);
			js.put("type", "success");
			response.getWriter().append(js.toString());
			ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			mjs.put("errorMessage", "wrong query");
			js.put("message", mjs);
			js.put("type", "error");
			response.getWriter().append(js.toString());
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		EmployeeInfo emp = (EmployeeInfo) request.getSession().getAttribute("employee");
		if(emp == null) {
			js.put("type", "error");
			mjs.put("errorMessage", "you have to login first");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			return;
		}
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		JSONObject recjs = new JSONObject(stb.toString());
		String p_id = new String();
		String p_title = recjs.getString("title");
		int p_year = Integer.parseInt(recjs.getString("year"));
		String p_director = recjs.getString("director");
		String p_star = recjs.getString("star");
		String p_genre = recjs.getString("genre");
		String new_s_id = new String();
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta = (Statement)conn.createStatement();
			ResultSet res0 = sta.executeQuery("select max(id) from stars");//create new id for star
			while(res0.next()) {
				new_s_id = "nm"+Integer.toString(Integer.parseInt(res0.getString(1).substring(2))+1);
			}
			res0 = sta.executeQuery("select max(id) from movies");//create new id for star
			while(res0.next()) {
				p_id = "tt"+Integer.toString(Integer.parseInt(res0.getString(1).substring(2))+1);
			}
			String query = "call add_movie(\""+p_id+"\",\""+p_title+"\",\""+p_director+"\",\""+p_year+"\",\""+p_star+"\",\""+p_genre+"\",\""+new_s_id+"\")";
			ResultSet res = sta.executeQuery(query);
			while(res.next()) {
				mjs.put("updateType", res.getString(1));
			}
			js.put("type", "success");
			js.put("message",mjs);
			response.getWriter().append(js.toString());
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			js.put("type", "error");
			mjs.put("errorMessage", e.toString());
			js.put("message", mjs);
			response.getWriter().append(js.toString());
		}
	}

}
