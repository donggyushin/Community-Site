package board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

/**
 * Servlet implementation class boardUpdateServlet
 */
@WebServlet("/boardUpdateServlet")
public class boardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public boardUpdateServlet() {
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
		//doGet(request, response);
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; utf-8");
		// ������ �����̳� ���
	    String root = request.getSession().getServletContext().getRealPath("/");
	 // ���� ���� ���(ex : /home/tour/web/ROOT/upload)
	    String savePath = root + "upload";
	 // ���ε� ���ϸ�
	    String uploadFile = "";
	    // ���� ������ ���ϸ�
	    String newFileName = "";


		int sizeLimit = 1024*1024*10;
		
		MultipartRequest multi = null;
		// �Ʒ��� ���� MultipartRequest�� ������ ���ָ� ������ ���ε� �ȴ�.(���� ��ü�� ���ε� �Ϸ�)
		try {
			multi = new MultipartRequest(request, savePath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
		}catch(Exception e) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "The file's size can not be over 10MB");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
		// MultipartRequest�� ���۹��� �����͸� �ҷ��´�.
		// enctype�� "multipart/form-data"�� �����ϰ� submit�� �����͵��� request��ü�� �ƴ� MultipartRequest��ü�� �ҷ��;� �Ѵ�.
		
		String boardID = request.getParameter("boardID");
		String boardTitle = multi.getParameter("boardTitle");
		String boardContent = multi.getParameter("boardContent");
		
		if(boardID == null || boardID.equals("") || boardTitle.equals("") || boardTitle == null || 
				boardContent == null || boardContent.equals("")) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "Fill the whole form");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
		String fileName = "";
		String fileRealName = "";
		File file = multi.getFile("boardFile");
		if(file != null) {
			fileName = multi.getOriginalFileName("boardFile");		//���ε��� ������ ���� �̸�
			fileRealName = file.getName();							//�������� �̸�
		}
		
		int result = new BoardDAO().update(boardID, boardTitle, boardContent, fileName, fileRealName);
		if(result == -1) {
			request.getSession().setAttribute("messagetype", "Error message");
			request.getSession().setAttribute("messageContent", "Sorry! Database Error has occured!");
			response.sendRedirect("boardView.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "Success message");
			request.getSession().setAttribute("messageContent", "Success updating board!");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
	}

}
