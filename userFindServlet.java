package user;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class userFindServlet
 */
@WebServlet("/userFindServlet")
public class userFindServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userFindServlet() {
        super();
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset = utf-8");
		
		String findName = request.getParameter("findName");
		if(findName == null || findName.equals("")) {
			response.getWriter().write("");
			return;
		}
		
		findName = URLDecoder.decode(findName, "utf-8");
		
		int result = new UserDAO().findFriend(findName);
		//1친구 있음 0친구 없음 -1DB오류
		if(result == 0) {
			response.getWriter().write("0");
			return;
		}else if(result == -1) {
			response.getWriter().write("-1");
			return;
		}else {				//친구가 있을 경우 
			response.getWriter().write(profileJason(findName));
		}
		
	}
	
	public String profileJason(String findName) {
		StringBuffer userProfile = new StringBuffer("");
		userProfile.append("{ \"userProfile\" : \""+new UserDAO().getProfile(findName)+"\" }");
		return userProfile.toString();
	}

}
