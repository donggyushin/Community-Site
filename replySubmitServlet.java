package reply;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class replySubmitServlet
 */
@WebServlet("/replySubmitServlet")
public class replySubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public replySubmitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession();
		String userID = (String)session.getAttribute("userID");
		String replyContent = request.getParameter("replyContent");
		String boardID = request.getParameter("boardID");
		if(userID == null || userID.equals("") || replyContent == null || replyContent.equals("") ||
				boardID == null || boardID.equals("")) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Fill the whole form");
			response.sendRedirect("index.jsp");
			return;
		}
		
		int result = new ReplyDAO().submit(userID, replyContent, boardID);
		if(result == -1) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Sorry! Database Error has occured!");
			response.sendRedirect("index.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Success Message");
			request.getSession().setAttribute("messageContent", "Success writing new reply");
			response.sendRedirect("boardShow.jsp?boardID="+boardID);
			return;
		}
		
	}

}
