<%@ page contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="util.SHA256" %>
    <%@ page import="user.UserDTO" %>
    <%@ page import="user.UserDAO" %>
    <%@ page import="java.io.PrintWriter" %>
<%
	request.setCharacterEncoding("utf-8");
	String userID = null;
	String code = null;
	if(session.getAttribute("userID")!= null){
		userID = (String) session.getAttribute("userID");
	}
	if(userID == null){
		session.setAttribute("messageType", "Error Message");
		session.setAttribute("messageContent", "Please login");
		response.sendRedirect("login.jsp");
		return;
	}
	if(request.getParameter("code")!= null){
		code = (String) request.getParameter("code");
	}
	
	String userEmailHash = new UserDAO().getUser(userID).getUserEmailHash();
	if(!userEmailHash.equals(code)){
		session.setAttribute("messageType", "Error message");
		session.setAttribute("messageContent", "Sorry, the emails are differents each other between now email and email you input when you make new account");
		response.sendRedirect("index.jsp");
	}
	
	int result = new UserDAO().userEmailCheck(userID);
	if(result == -1){
		session.setAttribute("messageType", "Error message");
		session.setAttribute("messageContent", "Sorry! Database Error has occured!");
		response.sendRedirect("index.jsp");
		return;
	}else{
		session.setAttribute("messageType", "Success message");
		session.setAttribute("messageContent", "You are checked successfully!");
		response.sendRedirect("index.jsp");
		return;
	}
%>