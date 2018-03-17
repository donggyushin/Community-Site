package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class userLoginServlet
 */
@WebServlet("/userLoginServlet")
public class userLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userLoginServlet() {
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
		System.out.println("서블릿 통괴");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");
		if(userID == null || userID.equals("") || userPassword == null || userPassword.equals("")) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Fill the whole form");
			response.sendRedirect("login.jsp");
			return;
		}
		int result = new UserDAO().login(userID, userPassword);
		//1 성공 2 비밀번호틀림 0아이디 없음 -1 디비오류
		if(result == 1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "Success Message");
			request.getSession().setAttribute("messageContent", "Success login");
			response.sendRedirect("index.jsp");
			return;
		}else if(result == 2) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Wrong password input");
			response.sendRedirect("login.jsp");
			return;
		}else if(result == 0) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Not existing ID");
			response.sendRedirect("login.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Sorry! Database error has occured");
			response.sendRedirect("login.jsp");
			return;
		}
		
	}

}
