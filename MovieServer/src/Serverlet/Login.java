package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.*;

import com.mysql.jdbc.Statement;

import Entitypackage.UserInfo;
import Entitypackage.VerifyUtils;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Login() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		System.out.println(stb.toString());
		JSONObject recjs = new JSONObject(stb.toString());
		String p_email = recjs.getString("email");
		String p_pword = recjs.getString("password");
		String p_recaptcha = null;
		if(recjs.has("recaptcha"))
			p_recaptcha = recjs.getString("recaptcha");
		String fname = null;
		String lname = null;
		ResultSet res = null;
		//verify the recaptra
//		if(p_recaptcha != null)
//			if(!VerifyUtils.verify(p_recaptcha)) {
//				System.out.println("wrong recaptcha");
//				json.put("type", "error");
//				map.put("errorMessage", "recaptcha wrong");
//				json.put("message", map);
//				response.getWriter().append(json.toString());
//				//response.getWriter().append(VerifyUtils.error.toString());
//				return;
//			}
		try {
			Connection conn = ins.getPool().getConnection();
			Statement sta1 = (Statement)conn.createStatement();
			res = sta1.executeQuery("select firstName, lastName, email, password,id from customers where email = \""+p_email+"\";");
			while(res.next()) {
				System.out.println(res.getString(4));
				if(!p_pword.equals(res.getString(4))) {
					System.out.println("wrong pass");
					json.put("type", "error");
					map.put("errorMessage", "wrong password");
					json.put("message", map);
					response.getWriter().append(json.toString());
					return;
				}
				json.put("type", "success");
				map.put("email", p_email);
				map.put("password", p_pword);
				fname = new String(res.getString(1));
				lname = new String(res.getString(2));
				map.put("firstName", fname);
				map.put("lastName", lname);
				json.put("message", map);
				response.getWriter().append(json.toString());
				request.getSession().setAttribute("user", new UserInfo(p_email,res.getString(5)));
				ins.getPool().returnConnection(conn);
				return;
			}
			//did not find a result
			System.out.println("no user");
			json.put("type", "error");
			map.put("errorMessage", "user does not exist");
			json.put("message", map);
			response.getWriter().append(json.toString());				
			return;
			
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
