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
		
		String fileName = "";
		File file = multi.getFile("userProfile");	//profileUpdate���� name���� userProfile�� ������ ������. 
		if(file!= null) {			//userProfile�� �����Ѵٸ�
			String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);	//�� �����ϴ� ������ Ȯ����
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {				//�̰͵��� �ϳ��� �´ٸ�
				String prev = new UserDAO().getUser(userID).getUserProfile();		//������ �ִ� ������ ������ �̸�
				File prevFile = new File(savePath + "/" + prev);					//������ �ִ� ������ ������ ����
				if(prevFile.exists()) {												//�����Ѵٸ�
					prevFile.delete();												//�����ְ�
				}
				fileName = file.getName();											//�����̸��� ���� ���ε��� ���Ϸκ��� ��������
			}else {																	//Ȯ���ڰ� �ٸ��ٸ� ����ó��
				if(file.exists()) {													//�ϴ� ������ ���� ����
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
		new UserDAO().profile(userID, fileName);								//���� ���ε� ���ְ�
		session.setAttribute("messageType", "Success message");					//���� �޽��� �����. 
		session.setAttribute("messageContent", "success updating image");
		response.sendRedirect("index.jsp");
		
	}

}
