package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class userRegisterServlet
 */
@WebServlet("/userRegisterServlet")
public class userRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userRegisterServlet() {
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
		String userID = null;
		String userPassword1 = null;
		String userPassword2 = null;
		String userName = null;
		String userAge = null;
		String userGender = null;
		String userEmail = null;
		String userProfile = null;
		userID = request.getParameter("userID");
		userPassword1 = request.getParameter("userPassword1");
		userPassword2 = request.getParameter("userPassword2");
		userName = request.getParameter("userName");
		userAge = request.getParameter("userAge");
		userGender = request.getParameter("userGender");
		userEmail = request.getParameter("userEmail");
		
		
		//�Է��� �ȵ� ������ �������. 
		if(userID == null || userID.equals("") ||userPassword1 == null || userPassword1.equals("") ||userPassword2 == null || userPassword2.equals("") ||
				userName == null || userName.equals("") ||userAge == null || userAge.equals("") ||
						userGender == null || userGender.equals("") || userEmail == null || userEmail.equals("")) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "There remains empty box. Please input the whole form");
			response.sendRedirect("join.jsp");
			return;
		}
		
		//��й�ȣ�� ���� �ٸ����.
		if(!userPassword1.equals(userPassword2)) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "The passwords are different each other.");
			response.sendRedirect("join.jsp");
			return;
		}
		
		String userEmailHash = util.SHA256.getSHA256(userEmail);
		
		
		int result = new UserDAO().register(userID, userPassword1, userName, userAge, userGender, userEmail, userEmailHash, "");
		
		//�������� ��� 
		if(result == 1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "Success Message");
			request.getSession().setAttribute("messageContent", "Success making new account, Please login to use Chatting web page!");
			response.sendRedirect("login.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Fail making new account");
			response.sendRedirect("join.jsp");
			return;
		}
		
		
	}

}
