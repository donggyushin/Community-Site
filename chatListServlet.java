package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class chatListServlet
 */
@WebServlet("/chatListServlet")
public class chatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public chatListServlet() {
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
		String chatType = request.getParameter("chatType");
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("") || chatType == null) {
			response.getWriter().write("");
			return;
		}
		
		fromID = URLDecoder.decode(fromID, "utf-8");
		toID = URLDecoder.decode(toID, "utf-8");
		
		if(chatType.equals("")) {
			response.getWriter().write(getChatListFirst(fromID, toID));
			return;
		}else {
			response.getWriter().write(getChatListByID(fromID, toID, chatType));
			return;
		}
		
	}
	
	public String getChatListFirst(String fromID, String toID) {
		ArrayList<ChatDTO> chatList = new ChatDAO().getChatListByNumber(fromID, toID, "30");
		StringBuffer result = new StringBuffer("");
		result.append("{ \"result\" : [");
		if(chatList.size() == 0 ) return "";
		for(int i = 0 ; i < chatList.size(); i ++) {
			result.append("[{ \"value\" : \""+chatList.get(i).getFromID()+"\" },");
			result.append("{ \"value\" : \""+chatList.get(i).getToID()+"\" },");
			result.append("{ \"value\" : \""+chatList.get(i).getChatContent()+"\" },");
			result.append("{ \"value\" : \""+chatList.get(i).getChatTime()+"\" }]");
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("], \"last\" : \""+chatList.get(chatList.size()-1).getChatID()+"\"}");
		new ChatDAO().chatRead(fromID, toID);
		return result.toString();
	}
	
	public String getChatListByID(String fromID, String toID, String chatID) {
		ArrayList<ChatDTO> chatList = new ChatDAO().getChatListByID(fromID, toID, chatID);
		StringBuffer result = new StringBuffer("");
		if(chatList.size() == 0 ) return "";
		result.append("{ \"result\" : [");
		for(int i = 0 ; i < chatList.size(); i ++) {
			result.append("[ { \"value\" : \""+chatList.get(i).getFromID()+"\" },");
			result.append(" { \"value\" : \""+chatList.get(i).getToID()+"\" },");
			result.append(" { \"value\" : \""+chatList.get(i).getChatContent()+"\" },");
			result.append(" { \"value\" : \""+chatList.get(i).getChatTime()+"\" }]");
			if(i != chatList.size()-1) result.append(",");
		}
		result.append("], \"last\" : \""+chatList.get(chatList.size()-1).getChatID()+"\"}");
		new ChatDAO().chatRead(fromID, toID);
		return result.toString();
	}
	

}
