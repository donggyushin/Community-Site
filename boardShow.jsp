<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.net.URLEncoder"%>
    <%@ page import="java.net.URLDecoder"%>
    <%@ page import="board.BoardDTO" %>
    <%@ page import="board.BoardDAO" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="reply.ReplyDTO" %>
    <%@ page import="reply.ReplyDAO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="./css/custom.css">
<script type="text/javascript" src="./js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="./js/popper.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<title>PARK</title>
<%
		String userID = null;		//유저가 로그인 했다면 아이디 값을 받아온다. 
		if(session.getAttribute("userID")!= null){
			userID = (String) session.getAttribute("userID");
	}
		
		if(userID != null){
			userID = URLDecoder.decode(userID, "utf-8");		
		}														//readChat 부분 함수 다시 고치기. 그게 문제일수도 있음. 
																//그거 고쳐도 안돼면 getUnreadFunction()여기서 ajax 고치기 이 둘중 하나가 거의 100퍼 문제임
		String messageNumber = "";
		
		
		if(userID == null){
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "You must login to enter this page");
			response.sendRedirect("login.jsp");
			return;
		}
		
		String boardID = null;
		if(request.getParameter("boardID")!= null){
			boardID = (String) request.getParameter("boardID");
		}
		if(boardID == null || boardID.equals("")){
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "Sorry, invalid board");
			response.sendRedirect("boardView.jsp");
			return;
		}
		
		ArrayList<ReplyDTO> replyList = new ReplyDAO().getReplyList(boardID);
		
		
	%>
	<script type="text/javascript">

		function getUnreadFunction(){
			
			var userID = '<%= userID%>';
			
			$.ajax({
				type:"post",
				url : "./chatGetUnreadServlet",
				data : {
					userID : encodeURIComponent(userID)
				},
				success : function(result){
					if(result == "") return;
					if(result == "0"){
						$("#unread").html('');
					}else{
					$("#unread").html('&nbsp;'+result);
					}
				}
			});
		}	
		
		function infiniteGetUnread(){
			setInterval(function(){
				getUnreadFunction();
			}, 3000);
		}
		
	</script>
  <style type="text/css">
    a{
      text-decoration : none;
      color: black;
    }
  </style>
</head>
<body>
	

	<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #e3f2fd;">
  <a class="navbar-brand" href="index.jsp">PARK</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
        <a class="nav-link active" href="index.jsp">Main <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="boardView.jsp">Bulletin Board</a>
      </li>
      <li class="nav-item">
      	<a href="find.jsp" class="nav-link">Search friend</a>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Manage Account
        </a>
		<%
			if(userID == null){	//로그인이 안되어있다면 로그인과 회원가입 클릭가능하고, 로그아웃 클릭x
		%>
		<div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="login.jsp">Login</a>
          <a class="dropdown-item" href="join.jsp">New Account</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item disabled" href="#">Logout</a>
        </div>
		<%	
		}else{ 		//그렇지 않다면 반대
		%>
		<div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item disabled" href="#">Login</a>
          <a class="dropdown-item disabled" href="#">New Account</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="logoutAction.jsp">Logout</a>
          <a href="update.jsp" class="dropdown-item">Update</a>
          <a href="profileUpdate.jsp" class="dropdown-item">Profile</a>
        </div>
		<%
		}
		%>
        
      </li>
      <li class="nav-item">
      
        <a class="nav-link" href="chatBox.jsp">Mail Box<span id="unread" style="color: red;" class="label label-info"></span></a>
      </li>
    </ul>
  </div>
</nav>
<%
	BoardDTO board = new BoardDAO().getBoard(boardID);
	new BoardDAO().hit(boardID);
%>

<div class="container">
  <table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col"></th>
      <th scope="col">Board</th>
      <th scope="col"></th>
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row" style="width: 110px;">Title : </th>
      <td><%=board.getBoardTitle() %></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row" style="width: 110px;">Writer : </th>
      <td><%=board.getUserID() %></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td style="width: 110px;">Date : </td>
      <td><%=board.getBoardDate().substring(0, 10) %></td>
    </tr>
    <tr>
      <td style="width: 110px;">Hit : </td>
      <td><%=board.getBoardHit() %></td>
    </tr>
    <tr>
      <td>File : </td>
      <td><%=board.getBoardRealFile() %>
      <%
      	if(!board.getBoardRealFile().equals("")){
      	%>
      	<a href="fileDownload.jsp?boardID=<%=board.getBoardID() %>" class="btn btn-primary">Donwload file</a>	
      	<%
      	}
      %>
      </td>
    </tr>
    <tr>
      <td style="height: 300px;"><%=board.getBoardContent() %></td>
    </tr>
    <tr>
      <td></td>
      <td style="float: right;">
        <a href="boardView.jsp" class="btn btn-primary">List</a>
        <%
        if(userID.equals(board.getUserID())){
        %>
      <a href="boardUpdate.jsp?boardID=<%=board.getBoardID() %>" class="btn btn-primary">Update</a>
      <a href="boardDeleteServlet?boardID=<%=board.getBoardID() %>" class="btn btn-primary" onclick="return confirm('are you sure to delete this board?')">Delete</a>
        <%
      }
      %>
      </td>
      
    </tr>
    
  </tbody>
</table>
</div>

<div class="container">
  <form method="post" action="replySubmitServlet?boardID=<%=boardID%>">
<table class="table">
  <thead class="thead-light">
    <tr>
      <th style="width: 480px;"><input type="text" name="replyContent" id="replyContent" class="form-control" placeholder="Input reply...." maxlength="200"></th>
      <th style="width: 110px;"><button class="btn btn-primary">Submit</button></th>
    </tr>
  </thead>
</table>  
  </form>
  <table class="table">
  <tbody>
    <tr>
    <%
    	for(int i = 0 ; i < replyList.size(); i ++){
    		%>
    		<th scope="row" style="width: 110px;"><%=replyList.get(i).getUserID() %></th>
    		<td><%=replyList.get(i).getReplyContent() %></td>
    		<td style="width: 180px;"><%=replyList.get(i).getReplyDate().substring(0, 10) %></td>
      		<td><a href="replyDeleteServlet?replyID=<%=replyList.get(i).getReplyID() %>&&boardID=<%=boardID %>" class="btn btn-primary">delete</a></td>
    		<%
    	}
    %>
      
      
      
    </tr>
  </tbody>
</table>
</div>


<%
//MessageType과 MessageContent 를 받아오는 부분
    String messageType = null;
    String messageContent = null;
    if(session.getAttribute("messageType")!= null){
      messageType = (String)session.getAttribute("messageType");
    }
    if(session.getAttribute("messageContent")!= null){
      messageContent = (String) session.getAttribute("messageContent");
    }

    if(messageContent != null){
%>
<div class="modal" id="messageModal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title"><%=messageType%></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p><%= messageContent%></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
  $("#messageModal").modal("show");
</script>
<%
session.removeAttribute("messageType");
session.removeAttribute("messageContent");
  }

%>



      <footer class="mastfoot mt-auto" style="text-align: center; margin-top: 800px; color: white; background-color: black; height: 90px;">
        <div class="inner" style="margin-top : 350px;">
          <p>Cover template for <a href="https://getbootstrap.com/" style="color: white;">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/" style="color: white;">@Donggyu</a>.</p>
        </div>
      </footer>


<script type="text/javascript">
	$(document).ready(function(){
		getUnreadFunction();
		infiniteGetUnread();
	});
</script>


</body>
</html>