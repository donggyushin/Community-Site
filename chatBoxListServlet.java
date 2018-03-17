package chat;

import java.io.IOException;

import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.UserDAO;
/**
 * Servlet implementation class chatBoxListServlet
 */
@WebServlet("/chatBoxListServlet")
public class chatBoxListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public chatBoxListServlet() {
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
		String userID = request.getParameter("userID");
		if(userID == null || userID.equals("")) {
			request.getSession().setAttribute("messageType", "Error Message");
			request.getSession().setAttribute("messageContent", "The user client doesn't have appropriate authorization");
			response.sendRedirect("logoutAction.jsp");
			return;
		}
		userID = URLDecoder.decode(userID, "utf-8");
		
		
		response.getWriter().write(getChatBoxList(userID));
		
		
	}
	
	public String getChatBoxList(String userID) {
		ArrayList<ChatDTO> boxList = new ChatDAO().getBox(userID);
		if(boxList.size() == 0 ) {
			return "";
		}
		StringBuffer result = new StringBuffer("");
		result.append("{ \"result\" : [");
		for(int i = boxList.size()-1 ; i >= 0; i --) {
			String unread = "";
			if(userID.equals(boxList.get(i).getToID())) {
				unread = new ChatDAO().getUnreadNumberByPersonal(userID, boxList.get(i).getFromID());
			}
			if(unread.equals("0")) {
				unread = "";
			}
			result.append("[ {\"value\" : \""+boxList.get(i).getFromID()+"\" },");
			result.append(" {\"value\" : \""+boxList.get(i).getToID()+"\" },");
			result.append(" {\"value\" : \""+boxList.get(i).getChatContent()+"\" },");
			result.append(" {\"value\" : \""+boxList.get(i).getChatTime()+"\" },");
			result.append("{ \"value\" : \""+unread+"\" },");
			if(userID.equals(boxList.get(i).getFromID())) {
				result.append("{ \"value\" : \""+new UserDAO().getProfile(boxList.get(i).getToID())+"\" }]");
			}else {
				result.append("{ \"value\" : \""+new UserDAO().getProfile(boxList.get(i).getFromID())+"\" }]");
			}
			
			if(i != 0) result.append(",");
		}
		result.append("], \"last\" : \""+boxList.get(boxList.size()-1).getChatID()+"\" }");
		System.out.println(result.toString());
		return result.toString();
	}
	

}
