package Serverlet;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

import DBpackage.DBconn;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class NormalSearch
 */
@WebServlet("/NormalSearch")
public class NormalSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public NormalSearch() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        //ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//write the time report
		FileWriter fw = new FileWriter("/home/ubuntu/tomcat/webapps/MovieServer/timeReport.txt",true);
		DBconn conSource = null;
		long startT = System.nanoTime();//record the start time of query
		long dbTime = 0;
		long start_j = 0;
		long time_j = 0;
		
		//initialize of JSON format
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		JSONArray jarr = new JSONArray();
		
		response.setContentType("text/html");
		//get parameters
		if(request.getParameter("keyword")==null) return;
		String keyword = request.getParameter("keyword");
		String[] key = keyword.split(" ");
		StringBuffer p_key = new StringBuffer();
		for(int i = 0;i<key.length;i++) {
			p_key.append("+"+key[i]+"* ");
		}
		String count = request.getParameter("count");
		String query_movie = "select * from movies where match(title) against(? in boolean mode) or edth(title,?,?) limit ?;";
		String query_star = "select id, name from stars join stars_in_movies on stars.id = stars_in_movies.starId where movieId = ?;";
		String query_genre = "select genres.name from genres join genres_in_movies on genres.id = genres_in_movies.genreid where movieId = ?;";
		String query_rating = "select rating from ratings where movieId = ?;";
		try {			
			start_j = System.nanoTime();
			conSource = new DBconn();
			//create connection
			java.sql.Connection conn = conSource.getConnection();
			//create statement to query movie list
			PreparedStatement sta = (PreparedStatement)conn.prepareStatement(query_movie);
			sta.setString(1, p_key.toString());
			sta.setString(2, keyword);
			sta.setInt(3, keyword.length()/3);
			sta.setInt(4, Integer.parseInt(count));
			//Statement sta = (Statement)conn.createStatement();
			//create statement to query stars
			PreparedStatement sta2 = (PreparedStatement) conn.prepareStatement(query_star);
//			Statement sta2 = (Statement)conn.createStatement();
			//create statement to query genres
			PreparedStatement sta3 = (PreparedStatement)conn.prepareStatement(query_genre);
//			Statement sta3 = (Statement)conn.createStatement();
			//create statement to query rating
			PreparedStatement sta4 = (PreparedStatement)conn.prepareStatement(query_rating);
//			Statement sta4 = (Statement)conn.createStatement();
			//create result set
			long startT_db = System.nanoTime();
			//ResultSet res = sta.executeQuery("select * from movies where match(title) against('"+p_key.toString()+"' in boolean mode) or edth(title,'"+keyword+"',"+keyword.length()/3+") limit "+count+";");
			ResultSet res = sta.executeQuery();
			ResultSet dres;//query genres and stars
			//get result from resultSet
			while(res.next()) {
				JSONObject tempjs = new JSONObject();
				JSONArray star_name = new JSONArray();
				JSONObject star = null;
				Vector<String> genres_name = new Vector<String>();
				tempjs.put("id", res.getString(1));
				tempjs.put("title", res.getString(2));
				tempjs.put("year", res.getString(3));
				tempjs.put("director", res.getString(4));
				String temp_id = res.getString(1);
				sta2.setString(1, temp_id);
//				dres = sta2.executeQuery("select id, name from stars join stars_in_movies on stars.id = stars_in_movies.starId where movieId = '"+temp_id+"';");
				dres = sta2.executeQuery();
				while(dres.next()) {
					star = new JSONObject();
					star.put("id",dres.getString(1));
					star.put("name",dres.getString(2));
					star_name.put(star);
				}
				tempjs.put("stars", star_name);
				sta3.setString(1, temp_id);
				//dres = sta3.executeQuery("select genres.name from genres join genres_in_movies on genres.id = genres_in_movies.genreid where movieId = '"+temp_id+"';");
				dres = sta3.executeQuery();
				while(dres.next()) {
					genres_name.add(dres.getString(1));
				}
				tempjs.put("genres", genres_name);
				sta4.setString(1, temp_id);
				//dres = sta4.executeQuery("select rating from ratings where movieId = '"+temp_id+"';");
				dres = sta4.executeQuery();
				long endTime_db = System.nanoTime();
				dbTime = endTime_db - startT_db; //record the query time
				
				while(dres.next()) {
					tempjs.put("rating", dres.getFloat(1));
				}
				jarr.put(tempjs);
				//response.getWriter().append(js.toString());
			}
			if(jarr.isNull(0)) {
				js.put("type", "error");
				mjs.put("errorMessage", "no result return");
				js.put("message", mjs);
				response.getWriter().append(js.toString());
				return;
			}
			js.put("type", "success");
			mjs.put("movies", jarr);
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			res.close();
			
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			js.put("type", "error");
			mjs.put("errorMessage", e.toString());
			js.put("message", mjs);
			response.getWriter().append(js.toString());
		}finally {
			try {
				conSource.closeConnection();
				time_j = System.nanoTime()-start_j;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endT = System.nanoTime();
		//output format: servlet time + query time + jdbc time
		fw.write(endT-startT+"\t"+dbTime+"\t"+time_j+"\n");
		fw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
