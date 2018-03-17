<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.net.URLEncoder"%>
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
		
		
		//로그인 했으면 못들어옴. 
		if(userID != null){
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "You can't enter this page in login");
			response.sendRedirect("index.jsp");
			return;
		}
		
	%>

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
        <a class="nav-link" href="chat.jsp">Chat</a>
      </li>
      <li class="nav-item">
        <a href="find.jsp" class="nav-link">Search friend</a>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          회원관리
        </a>
		<%
			if(userID == null){	//로그인이 안되어있다면 로그인과 회원가입 클릭가능하고, 로그아웃 클릭x
		%>
		<div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="login.jsp">로그인</a>
          <a class="dropdown-item" href="join.jsp">회원가입</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item disabled" href="#">로그아웃</a>
        </div>
		<%	
		}else{ 		//그렇지 않다면 반대
		%>
		<div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item disabled" href="#">로그인</a>
          <a class="dropdown-item disabled" href="#">회원가입</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="logoutAction.jsp">로그아웃</a>
        </div>
		<%
		}
		%>
        
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Mail Box<span id="unread" class="label label-info"></span></a>
      </li>
    </ul>
  </div>
</nav>

<div class="container">
    <form method="post" action="./userLoginServlet">
        <table class="table">
          <thead class="thead-dark">
            <tr>
              <th scope="col"></th>
              <th scope="col"><h2>Login<h2></th>
              <th scope="col"></th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">userName</th>
              <td><input type="text" name="userID" id="userID" class="form-control" maxlength="20" placeholder="Input ID..."></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <th scope="row">Password</th>
              <td><input type="Password" name="userPassword" id="userPassword" maxlength="20" class="form-control" placeholder="Input Password..."></td>
              <td></td>
              <td></td>
            </tr>
          </tbody>
        </table>
        <button type="submit" style="margin: auto; float: right" class="btn btn-primary mt-3">Login</button>    
  </form>
  
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



      <footer class="mastfoot mt-auto" style="text-align: center;  color: white; background-color: black; height: 90px;">
        <div class="inner" style="margin-top : 250px">
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
        </div>
      </footer>





</body>
</html>