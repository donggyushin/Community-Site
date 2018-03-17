package board;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Servlet implementation class boardWriteServlet
 */
@WebServlet("/boardWriteServlet")
public class boardWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public boardWriteServlet() {
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024;
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");		//��μ���. \ �̰� 2���� /�� �ٲ��ֱ�. 
		try {																			//���⼭ ������ �߻��� ���� �����ϱ� try/catch��
			multi = new MultipartRequest(request, savePath, fileMaxSize, "utf-8", new DefaultFileRenamePolicy());
		}catch(Exception e) {															//�ߺ��Ǵ� �̸��� �˾Ƽ� ������ ��������ִ� Ŭ����
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "The size of file couldn't be over 10MB");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		
		HttpSession session = request.getSession();				//servlet���� session�� ���������� �̷��� �ؾ���. 
		String userID = (String) session.getAttribute("userID");
		
		String boardTitle = multi.getParameter("boardTitle");
		String boardContent = multi.getParameter("boardContent");
		if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Fill the whole form");
			response.sendRedirect("boardWrite.jsp");
			return;
		}
		
		String fileName = "";
		String fileRealName = "";
		File file = multi.getFile("boardFile");
		if(file != null) {
			fileName = multi.getOriginalFileName("boardFile");
			fileRealName = file.getName();
		}
		int result = new BoardDAO().register(userID, boardTitle, boardContent, fileName, fileRealName);
		if(result == -1) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "Sorry! Database Error has occured!!");
			response.sendRedirect("boardWrite.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Success Message");
			request.getSession().setAttribute("messageContent", "Success uploading new board!");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
	}

}
