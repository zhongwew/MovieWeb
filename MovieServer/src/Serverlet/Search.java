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

import Entitypackage.globalInstance;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Search() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		JSONObject recjs = new JSONObject(stb.toString());
		String p_title = recjs.getString("title");
		String p_direc = recjs.getString("director");
		String p_genre = recjs.getString("genres");
		String p_star = recjs.getString("star");
		JSONArray p_year = recjs.getJSONArray("year");
		if(p_year == null) {
			p_year = new JSONArray();
			p_year.put(0);
			p_year.put(10000);
		}
		else {
			System.out.println(p_year.get(0));
			System.out.println(p_year.get(1));
		}
		
		//select
		String query = null;
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		JSONArray jarr = new JSONArray();
		JSONObject tempjs = null;
		JSONArray tempjarr = null;
		JSONObject tempjs2 = null;
		try {
			query =
					new String("select movies.id, title, year, director from movies, (select distinct movieId from stars_in_movies join stars on stars.id = stars_in_movies.starId where stars.name like \"%"+p_star+"%\") a, (select distinct movieId from genres_in_movies join genres on genres.id = genres_in_movies.genreId where genres.name like \""+p_genre+"%\") b where year > "+Integer.toString(p_year.getInt(0))+" and year<"+Integer.toString(p_year.getInt(1))+" and director like \""+p_direc+"%\" and a.movieId = movies.id and movies.id = b.movieId and title like \""+p_title+"%\" limit 1,20;");
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
				jarr.put(tempjs);
			}
			if(jarr.isNull(0)) {//no results
				mjs.put("errorMessage", "no result");
				js.put("message", mjs);
				js.put("type", "error");
				response.getWriter().append(js.toString());
				return;
			}
			mjs.put("results", jarr);
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

}
