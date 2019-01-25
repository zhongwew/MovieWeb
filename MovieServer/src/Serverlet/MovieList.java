package Serverlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Entitypackage.globalInstance;

/**
 * Servlet implementation class MovieList
 */
@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public MovieList() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins  = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //Get request : /MovieList?genre=...&
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ResultSet res = null;
		ResultSet dres = null;
		String temp_id = new String();
		Statement sta = null;
		Statement sta2 = null;
		JSONObject json = new JSONObject();
		JSONObject mjs = new JSONObject();
		JSONArray jarr = new JSONArray();
		Map<String,String> map = new HashMap<String,String>();
		
		response.setContentType("text/html");
		String genre = request.getParameter("genre");
		String name = request.getParameter("title");
		String page = request.getParameter("page");
		String order = request.getParameter("sortType");
		String pagecount = request.getParameter("count");
		System.out.println(genre);
		System.out.println(name);
		System.out.println(page);
		System.out.println(order);
		
		if(page == null) {
			JSONObject ejson = new JSONObject();
			ejson.put("type", "error");
			map.put("errorMessage", "wrong query");
			ejson.put("message", map);
			response.getWriter().append(ejson.toString());
			return;
		}
		int pageNum = Integer.parseInt(page);
		
		
		StringBuffer que = new StringBuffer();
		StringBuffer countq = new StringBuffer("select count(*) ");
		StringBuffer resq = new StringBuffer("select * ");
		
		if(genre != null && genre != "") {//has genre parameter
			que.append("from movies, genres, genres_in_movies where genres.name=\""+ genre +"\" and genres.id = genres_in_movies.genreId and movies.id = genres_in_movies.movieId ");
		}
		else {//no genre parameter
			que.append("from movies ");
		}
		if (name != null&&name!="") {//has name
			if(genre != null && genre != "")
				que.append("title like '"+name+"%' ");
			else
				que.append("where title like '"+name+"%' ");
		}
		if(order != null && order != "") {
			if(order.equals("TITLE_DESC"))
				que.append("order by title desc");
			if(order.equals("TITLE_ASC"))
				que.append("order by title asc");
			if(order.equals("YEAR_DESC"))
				que.append("order by year desc");
			if(order.equals("YEAR_ASC"))
				que.append("order by year asc");
		}
		countq.append(que.toString());
		countq.append(";");
		que.append(" limit "+Integer.toString((pageNum-1)*20+1)+","+pagecount+";");
		
		resq.append(que.toString());
		
		String query = resq.toString();
		String countquery = countq.toString();
//		if((genre == null || genre == "") && (name != null&&name!="")) {
//			query = "select * from movies where title like '"+name+"%' limit "+Integer.toString((pageNum-1)*20+1)+",20;";
//		}
//		else if((genre != null&&genre!="") && (name == null || name == "")) {
//			query = "select * from movies, genres, genres_in_movies where genres.name=\""+ genre +"\"and genres.id = genres_in_movies.genreId and movies.id = genres_in_movies.movieId limit "+Integer.toString((pageNum-1)*20+1)+",20;";
//		}
//		else if((genre != null && genre != "") && (name != null && name != "")) {
//			query = "select * from movies, genres, genres_in_movies where movies.title like '"+name+"%' and genres.name=\""+ genre +"\"and genres.id = genres_in_movies.genreId and movies.id = genres_in_movies.movieId limit "+Integer.toString((pageNum-1)*20+1)+",20;";
//		}else {
//			query = "select * from movies limit "+Integer.toString((pageNum-1)*20+1)+",20;";
//		}
		System.out.println(query);
		System.out.println(countquery);
		
		try {
			Connection conn = (Connection)ins.getPool().getConnection();
			sta = (Statement) conn.createStatement();
			sta2 = (Statement) conn.createStatement();
			res = sta.executeQuery(query);
			dres = sta2.executeQuery(countquery);//query the count
			while(dres.next()) {
				int count = dres.getInt(1);
				System.out.println(count);
				mjs.put("length",count);
			}
			while(res.next()) {
				JSONObject js = new JSONObject();
				JSONArray star_name = new JSONArray();
				JSONObject star = null;
				Vector<String> genres_name = new Vector<String>();
				js.put("id", res.getString(1));
				js.put("title", res.getString(2));
				js.put("year", res.getString(3));
				js.put("director", res.getString(4));
				temp_id = res.getString(1);
				dres = sta2.executeQuery("select id, name from stars join stars_in_movies on stars.id = stars_in_movies.starId where movieId = \""+temp_id+"\";");
				while(dres.next()) {
					star = new JSONObject();
					star.put("id",dres.getString(1));
					star.put("name",dres.getString(2));
					star_name.put(star);
				}
				js.put("stars", star_name);
				dres = sta2.executeQuery("select genres.name from genres join genres_in_movies on genres.id = genres_in_movies.genreid where movieId = \""+temp_id+"\";");
				while(dres.next()) {
					genres_name.add(dres.getString(1));
				}
				js.put("genres", genres_name);
				dres = sta2.executeQuery("select rating from ratings where movieId = \""+temp_id+"\";");
				while(dres.next()) {
					js.put("rating", dres.getFloat(1));
				}
				jarr.put(js);
				//response.getWriter().append(js.toString());
			}
			if(jarr.isNull(0)) {
				json.put("type", "error");
				map.put("errorMessage", "no result return");
				json.put("message", map);
				response.getWriter().append(json.toString());
				return;
			}
			json.put("type", "success");
			mjs.put("movies", jarr);
			json.put("message", mjs);
			response.getWriter().append(json.toString());
			res.close();
			ins.getPool().returnConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
