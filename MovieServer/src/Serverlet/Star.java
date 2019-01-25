package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import com.mysql.jdbc.Statement;

import Entitypackage.EmployeeInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Star
 */
@WebServlet("/Star")
public class Star extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Star() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();//include message and type
		JSONObject mjson = new JSONObject(); //include errorMessage, starMessage and moviesMessage
		JSONObject starjs = new JSONObject(); 
		JSONArray jarr = new JSONArray();
		JSONObject moviejs = new JSONObject();
		String starID = request.getParameter("starID"); 
		ResultSet res = null; //used to store star info
		ResultSet res2 = null; //used to query movie info
		if(starID == null) {
			json.put("type", "error");
			mjson.put("errorMessage", "no parameter");
			json.put("message", mjson);
			response.getWriter().append(json.toString());
			return;
		}
		
		try {
			Connection conn = ins.getPool().getConnection();
			String que1 = new String("select stars.id,name,birthYear,movieId from stars join stars_in_movies on stars.id = stars_in_movies.starID where starID = \""+starID+"\";");
			Statement st1 = (Statement) conn.createStatement();
			Statement st2 = (Statement) conn.createStatement();
			res = st1.executeQuery(que1);
			res.next();
			starjs.put("id", res.getString(1));
			starjs.put("name", res.getString(2));
			starjs.put("birthYear", res.getInt(3));
			do {
				JSONObject tempjs = new JSONObject();
				JSONArray star_name = new JSONArray();  //used to store stars' names 
				Vector<String> genre_name = new Vector<String>(); // used to store genre names
				res2 = st2.executeQuery("select id, year,title,director from movies where id = \"" + res.getString(4)+"\";"); //to query the movie basic info
				res2.next(); //pass the headline row
				tempjs.put("id", res2.getString(1));
				tempjs.put("year", res2.getInt(2));
				tempjs.put("title", res2.getString(3));
				tempjs.put("director", res2.getString(4));
				res2 = st2.executeQuery("select stars.name,stars.id from stars join stars_in_movies on stars.id = stars_in_movies.starId where stars_in_movies.movieId = \""+res.getString(4)+"\";");
				while(res2.next()) {
					JSONObject stjs = new JSONObject();
					stjs.put("id", res2.getString(2));
					stjs.put("name", res2.getString(1));
					star_name.put(stjs);
				}
				tempjs.put("stars", star_name);
				res2 = st2.executeQuery("select genres.name from genres join genres_in_movies on genres.id = genres_in_movies.genreId where movieId = \""+res.getString(4)+"\";");
				while(res2.next()) {
					genre_name.add(res2.getString(1));
				}
				tempjs.put("genres", genre_name);
				res2 = st2.executeQuery("select rating from ratings where movieId = \""+res.getString(4)+"\";");
				while(res2.next()) {
					tempjs.put("rating", res2.getFloat(1));
				}
				jarr.put(tempjs);
			}while(res.next());
			ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("type", "error");
			mjson.put("errorMessage", "invalid query");
			json.put("message", mjson);
			response.getWriter().append(json.toString());
			return;
		}
		json.put("type", "success");
		moviejs.put("movies", jarr);
		moviejs.put("star", starjs);
		json.put("message", moviejs);
		response.getWriter().append(json.toString());
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
		String query = new String();
		String p_id = new String();
		String p_name = recjs.getString("name");
		try {
			Connection conn = ins.getPool().getConnection();
			Statement sta1 = (Statement)conn.createStatement();
			ResultSet res0 = sta1.executeQuery("select max(id) from stars");//create new id for star
			while(res0.next()) {
				System.out.println(res0.getString(1).substring(2));
				p_id = "nm"+Integer.toString(Integer.parseInt(res0.getString(1).substring(2))+1);
			}
			if(recjs.has("birthYear")) {
				int p_birthYear = Integer.parseInt(recjs.getString("birthYear"));
				query = "insert into stars values(\""+p_id+"\",\""+p_name+"\","+p_birthYear+")";
			}
			else {
				query = "insert into stars values(\""+p_id+"\",\""+p_name+"\",NULL)";
			}
			int res = sta1.executeUpdate(query);
			if(res<0) {
				js.put("type", "error");
				mjs.put("errorMessage", "update fail");
				js.put("message", mjs);
				response.getWriter().append(js.toString());
				return;
			}
			js.put("type", "success");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			js.put("type", "error");
			mjs.put("errorMessage", "update fail");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
		}
		
	}

}
