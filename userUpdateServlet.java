package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class userUpdateServlet
 */
@WebServlet("/userUpdateServlet")
public class userUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userUpdateServlet() {
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
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession();
		String userID = (String)session.getAttribute("userID");
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userEmail = request.getParameter("userEmail");
		String userAge = request.getParameter("userAge");
		String userName = request.getParameter("userName");
		String profile = request.getParameter("userProfile");
		String userGender = request.getParameter("userGender");
		
		
		
		//안들어온 정보가 있다면 되돌려보내기 
		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("") || 
				userPassword2 == null || userPassword2.equals("") || userEmail == null || userEmail.equals("") ||
				userAge == null || userAge.equals("") || userName == null || userName.equals("") || 
				userGender == null || userGender.equals("")) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Fill the whole form");
			response.sendRedirect("update.jsp");
			return;
		}
		//입력한 비밀번호가 서로 다를시에 돌려보내기 
		if(!userPassword1.equals(userPassword2)) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "The passwords are different each other, Please check again");
			response.sendRedirect("update.jsp");
			return;
		}
		
		System.out.println(userPassword1 + userAge + userName + userGender + userEmail + userID);
		int result = new UserDAO().userUpdate(userPassword1, userAge, userName, userGender, userEmail, userID);
		
		//DB오류일때
		if(result == -1) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Sorry! Database Error has occured!");
			response.sendRedirect("update.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Success Message");
			request.getSession().setAttribute("messageContent", "Success changing user information");
			response.sendRedirect("index.jsp");
			return;
		}
	}

}
