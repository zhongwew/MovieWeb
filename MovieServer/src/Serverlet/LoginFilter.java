package Serverlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest re = (HttpServletRequest) request; 
		HttpServletResponse res = (HttpServletResponse) response;
		String reurl = re.getRequestURI().toLowerCase();
		if(reurl.endsWith("index.html") || reurl.endsWith("Login")) {//request to login page doesn't need redirect
			System.out.println(reurl);
			chain.doFilter(request, response);
			return;
		}
		if(re.getSession().getAttribute("user") == null) {//user has not login yet
			res.sendRedirect("index.html");
			return;
		}else {
			chain.doFilter(request, response);
			return;
		}

		// pass the request along the filter chain
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
