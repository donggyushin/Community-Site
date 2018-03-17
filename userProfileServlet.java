package user;

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
 * Servlet implementation class userProfileServlet
 */
@WebServlet("/userProfileServlet")
public class userProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public userProfileServlet() {
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
		response.setContentType("text/html; charset= utf-8");
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024;
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");		//경로설정. \ 이거 2개는 /로 바꿔주기. 
		try {																			//여기서 오류가 발생할 수도 있으니까 try/catch문
			multi = new MultipartRequest(request, savePath, fileMaxSize, "utf-8", new DefaultFileRenamePolicy());
		}catch(Exception e) {															//중복되는 이름을 알아서 적당히 변경시켜주는 클래스
			request.getSession().setAttribute("messageType", "Error message");
			request.getSession().setAttribute("messageContent", "The size of file couldn't be over 10MB");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		
		HttpSession session = request.getSession();				//servlet에서 session값 가져오려면 이렇게 해야함. 
		String userID = (String) session.getAttribute("userID");
		
		String fileName = "";
		File file = multi.getFile("userProfile");	//profileUpdate에서 name값이 userProfile인 파일을 가져옴. 
		if(file!= null) {			//userProfile이 존재한다면
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);	//그 존재하는 파일의 확장자
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {				//이것들중 하나가 맞다면
				String prev = new UserDAO().getUser(userID).getUserProfile();		//이전에 있던 유저의 프로필 이름
				File prevFile = new File(savePath + "/" + prev);					//이전에 있던 유저의 프로필 파일
				if(prevFile.exists()) {												//존재한다면
					prevFile.delete();												//지워주고
				}
				fileName = file.getName();											//파일이름을 새로 업로드한 파일로부터 가져오고
			}else {																	//확장자가 다르다면 예외처리
				if(file.exists()) {													//일단 가져온 파일 삭제
					file.delete();
				}
				session.setAttribute("messageType", "Error Message");
				session.setAttribute("messageContent", "Only image files are available to upload");
				response.sendRedirect("profileUpdate.jsp");
				return;
			}
		}else {
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "Failed to get the file from profileUpdate page");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		new UserDAO().profile(userID, fileName);								//파일 업로드 해주고
		session.setAttribute("messageType", "Success message");					//성공 메시지 띄워줌. 
		session.setAttribute("messageContent", "success updating image");
		response.sendRedirect("index.jsp");
		
	}

}
