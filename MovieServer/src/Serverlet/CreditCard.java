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
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import Entitypackage.globalInstance;

/**
 * Servlet implementation class CreditCard
 */
@WebServlet("/CreditCard")
public class CreditCard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static globalInstance ins = null;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public CreditCard() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        super();
        ins = new globalInstance();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stu
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
		String p_ccId = recjs.getString("ccId");
		String p_first = recjs.getString("firstName");
		String p_last = recjs.getString("lastName");
		JSONObject js = null;
		JSONObject mjs = null;
		
		try {
			Connection conn = (Connection)ins.getPool().getConnection();
			PreparedStatement sta1 = (PreparedStatement)conn.prepareStatement("select * from creditcards where firstName = ? and lastName = ? and id = ?;");
			sta1.setString(1, p_first);
			sta1.setString(2, p_last);
			sta1.setString(3, p_ccId);
			ResultSet res = sta1.executeQuery();
			while(res.next()) {
				js = new JSONObject();
				js.put("type", "success");
				response.getWriter().append(js.toString());
			}
			if(js == null)//didn't find result
			{
				js = new JSONObject();
				mjs = new JSONObject();
				js.put("type", "error");
				mjs.put("errorMessage", "wrong information");
				js.put("message",mjs);
				response.getWriter().append(js.toString());
				ins.getPool().returnConnection(conn);
				return;
			}
			ins.getPool().returnConnection(conn);

		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
