package board;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class boardDeleteServlet
 */
@WebServlet("/boardDeleteServlet")
public class boardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public boardDeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);		//url을 통해서 boardID값을 넘겨줄때 doGet함수를 사용하기 때문에. 이렇게 해줘야함. 주의! 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String boardID = null;
		
		if(request.getParameter("boardID")!= null) {
			boardID = (String)request.getParameter("boardID");
		}
		if(boardID == null || boardID.equals("")) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Invalid bulletin board");
			response.sendRedirect("boardView.jsp");
			return;
		}
		BoardDTO board = new BoardDAO().getBoard(boardID);
		HttpSession session = request.getSession();
		String userID = null;
		
		if(session.getAttribute("userID")!= null) {
			userID = (String) session.getAttribute("userID");
		}
		
		if(!userID.equals(board.getUserID())) {
			request.getSession().setAttribute("messagetype", "Error message");
			request.getSession().setAttribute("messageContent", "you don't have authorization to access this page");
			response.sendRedirect("index.jsp");
			return;
		}
		
		BoardDAO boardDAO = new BoardDAO();
		String savePath = request.getRealPath("upload").replaceAll("\\\\", "/");
		String prev = boardDAO.getRealFile(boardID);
		
		int result = boardDAO.delete(boardID);
		if(result == -1) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "fail to delete the file");
			response.sendRedirect("index.jsp");
			return;
		}else {
			File prevFile = new File(savePath + "/" + prev);
			if(prevFile.exists()) {
				prevFile.delete();
			}
			request.getSession().setAttribute("messageType", "Success message");
			request.getSession().setAttribute("messageContent", "Success deleting the bulletin board");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
		
	}

}
