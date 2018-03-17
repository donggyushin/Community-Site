package reply;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class replyDeleteServlet
 */
@WebServlet("/replyDeleteServlet")
public class replyDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public replyDeleteServlet() {
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
		String replyID = null;
		String boardID = request.getParameter("boardID");
		HttpSession session = request.getSession();
		String userID = null;
		if(session.getAttribute("userID")!= null) {
			userID = (String)session.getAttribute("userID");
		}
		
		if(request.getParameter("replyID")!= null) {
			replyID = (String)request.getParameter("replyID");
		}
		if(replyID == null || replyID.equals("")) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Invalid reply");
			response.sendRedirect("index.jsp");
			return;
		}
		
		ReplyDTO reply = new ReplyDAO().getReply(replyID);
		if(!userID.equals(reply.getUserID())) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "You don't have authorization");
			response.sendRedirect("index.jsp");
			return;
		}
		
		int result = new ReplyDAO().deleteReply(replyID);
		if(result == -1) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Sorry! Database Error has occured!");
			response.sendRedirect("index.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Success message");
			request.getSession().setAttribute("messageContent", "Success deleting reply");
			response.sendRedirect("boardShow.jsp?boardID="+boardID);
			return;
		}
	}

}
