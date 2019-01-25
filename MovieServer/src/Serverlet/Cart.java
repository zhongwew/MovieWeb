package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import Entitypackage.CartInfo;
import Entitypackage.UserInfo;
import Entitypackage.globalInstance;
import org.json.*;

/**
 * Servlet implementation class Cart
 */
@WebServlet("/Cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Cart() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {	
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("null")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		CartInfo cart = (CartInfo) request.getSession().getAttribute("cart");
		JSONObject js = new JSONObject();
		JSONArray jarr = new JSONArray();
		JSONObject mjs = new JSONObject();
		JSONObject tempjs = null;
		if(cart == null) {
			mjs.put("errorMessage", "no items");
			js.put("type", "error");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			return;
		}
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			PreparedStatement sta1 = (PreparedStatement)conn.prepareStatement("select id,title,year,director from movies where id =?;");
			PreparedStatement sta2 = (PreparedStatement)conn.prepareStatement("select name from genres join genres_in_movies on genres.id = genres_in_movies.genreId where movieId = ?;");
			PreparedStatement sta3 = (PreparedStatement) conn.prepareStatement("select rating from ratings where movieId = ?;");
			ResultSet res = null;
			ResultSet res2 = null;
			Vector<String> items = cart.getCartInfo();
			System.out.println(items.size());
			for(int i = 0;i<items.size();i++) {
				tempjs = new JSONObject();
				tempjs.put("id", items.get(i));
				sta1.setString(1, items.get(i)); //set the id parameter
				res = sta1.executeQuery();
				res.next();
				tempjs.put("title", res.getString(2));
				tempjs.put("year", res.getInt(3));
				tempjs.put("director", res.getString(4));
				tempjs.put("price", 5);
				tempjs.put("quantity", 1);
				Vector<String> genres_name = new Vector<String>();
				sta2.setString(1, items.get(i));
				res2 = sta2.executeQuery();
				while(res2.next()) {
					genres_name.add(res2.getString(1));
				}
				tempjs.put("genres", genres_name);
				sta3.setString(1, items.get(i));
				res2 = sta3.executeQuery();
				while(res2.next()) {
					tempjs.put("rating", res2.getFloat(1));
				}
				jarr.put(tempjs);
			}
		ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		js.put("type", "success");
		mjs.put("items", jarr);
		js.put("message", mjs);
		response.getWriter().append(js.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//get request's cookie first
		request.getSession().removeAttribute("cart");
		CartInfo cart = new CartInfo();
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		JSONObject js = new JSONObject(stb.toString());
		System.out.println(js.toString());
		JSONArray jarr = js.getJSONArray("items");
		JSONObject tempjs = null;
		for(int i = 0;i<jarr.length();i++) {
			tempjs = jarr.getJSONObject(i);
			cart.additems(tempjs.getString("id"));
		}
		request.getSession().setAttribute("cart", cart);
		JSONObject anjs = new JSONObject();
		anjs.put("type", "success");
		response.getWriter().append(anjs.toString());
		request.getSession().setAttribute("cart", cart);
	}

}
