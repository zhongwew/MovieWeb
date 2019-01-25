package Serverlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import Entitypackage.EmployeeInfo;
import Entitypackage.globalInstance;

/**
 * Servlet implementation class Elogin
 */
@WebServlet("/Employee/Login")
public class Elogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public Elogin() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
		JSONObject js = new JSONObject();
		JSONObject mjs = new JSONObject();
		BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		StringBuffer stb = new StringBuffer();
		String line = new String();
		while((line = buf.readLine()) != null) {
			stb.append(line);
		}
		System.out.println(stb.toString()+"111");
		JSONObject recjs = new JSONObject(stb.toString());
		String query = new String();
		String p_email = recjs.getString("email");
		String p_pass = recjs.getString("password");
		query = "select email, password, fullname from employee where email = \""+p_email+"\"";
		try {
			Connection conn = (Connection) ins.getPool().getConnection();
			Statement sta = (Statement)conn.createStatement();
			ResultSet res = sta.executeQuery(query);
			res.next();//skip the headline
			if(p_email.equals(res.getString(1))) {
				if(p_pass.equals(res.getString(2))) {
					js.put("type", "success");
					js.put("message", mjs);
					response.getWriter().append(js.toString());
					request.getSession().setAttribute("employee", new EmployeeInfo(res.getString(1),res.getString(3)));
					EmployeeInfo emp = (EmployeeInfo) request.getSession().getAttribute("employee");
					System.out.println(emp.getName());
					return;
				}
				else {
					js.put("type", "error");
					mjs.put("errorMessage", "wrong password");
					js.put("message",mjs);
					response.getWriter().append(js.toString());
					return;
				}
			}
			else {
				js.put("type", "error");
				mjs.put("errorMessage", "user not exist");
				js.put("message",mjs);
				response.getWriter().append(js.toString());
				return;
			}
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
