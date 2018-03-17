package chat;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class chatSubmitServlet
 */
@WebServlet("/chatSubmitServlet")
public class chatSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public chatSubmitServlet() {
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
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		
		//만약 빠진게 하나라도 있다면, 0반환. modal창 띄워주기
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("") || 
				chatContent == null || chatContent.equals("")) {
			response.getWriter().write("0");
			return;
		}
		
		fromID = URLDecoder.decode(fromID, "utf-8");
		toID = URLDecoder.decode(toID, "utf-8");
		chatContent = URLDecoder.decode(chatContent, "utf-8");
		int result = new ChatDAO().submit(fromID, toID, chatContent);
		
		//-1데이터베이스 오류, 1 성공
		if(result == -1) {
			response.getWriter().write("-1");
		}else {
			response.getWriter().write("1");
		}
	}

}
