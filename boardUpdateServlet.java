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
		// 웹서버 컨테이너 경로
	    String root = request.getSession().getServletContext().getRealPath("/");
	 // 파일 저장 경로(ex : /home/tour/web/ROOT/upload)
	    String savePath = root + "upload";
	 // 업로드 파일명
	    String uploadFile = "";
	    // 실제 저장할 파일명
	    String newFileName = "";


		int sizeLimit = 1024*1024*10;
		
		MultipartRequest multi = null;
		// 아래와 같이 MultipartRequest를 생성만 해주면 파일이 업로드 된다.(파일 자체의 업로드 완료)
		try {
			multi = new MultipartRequest(request, savePath, sizeLimit, "utf-8", new DefaultFileRenamePolicy());
		}catch(Exception e) {
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "The file's size can not be over 10MB");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
		// MultipartRequest로 전송받은 데이터를 불러온다.
		// enctype을 "multipart/form-data"로 선언하고 submit한 데이터들은 request객체가 아닌 MultipartRequest객체로 불러와야 한다.
		
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
			fileName = multi.getOriginalFileName("boardFile");		//업로드한 파일의 원본 이름
			fileRealName = file.getName();							//보여지는 이름
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
