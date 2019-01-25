package Serverlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Entitypackage.UserInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class User
 */
@WebServlet("/User")
public class User extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public User() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stu
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		if(user == null) {
			js.put("type", "error");
			mjs.put("errorMessage", "not login yet");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			return;
		}
		String email = user.getName();
		String firstName = new String();
		String lastName = new String();
		String address = new String();

		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta = (Statement)conn.createStatement();
			ResultSet res = sta.executeQuery("select firstName, lastname, address from customers where email = \""+email+"\";");
			while(res.next()) {
				firstName = res.getString(1);
				lastName = res.getString(2);
				address = res.getString(3);
			}
			ins.getPool().returnConnection(conn);
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(firstName == "") {
			js.put("type", "error");
			mjs.put("errorMessage", "no such user");
			js.put("message", mjs);
			response.getWriter().append(js.toString());
			return;
		}
		js.put("type", "success");
		mjs.put("firstName", firstName);
		mjs.put("lastName", lastName);
		mjs.put("address", address);
		js.put("message", mjs);
		response.getWriter().append(js.toString());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
