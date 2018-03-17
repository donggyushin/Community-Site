<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.net.URLEncoder"%>
    <%@ page import="java.net.URLDecoder"%>
    <%@ page import="board.BoardDTO" %>
    <%@ page import="board.BoardDAO" %>
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
		
		String boardID = null;
		if(request.getParameter("boardID")!= null){
			boardID = (String) request.getParameter("boardID");
		}
		if(boardID == null || boardID.equals("")){
			session.setAttribute("messageType", "Error message");
			session.setAttribute("messageContent", "Invalid board update page");
			response.sendRedirect("index.jsp");
			return;
		}
		
		BoardDTO board = new BoardDAO().getBoard(boardID);
		
		
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


<div class="container">
  <form method="post" action="boardUpdateServlet" enctype="multipart/form-data">
  <table class="table">
  <thead class="thead-dark">
    <tr>
      <th scope="col"></th>
      <th scope="col">Board Updating Form</th>
      <th scope="col"></th>
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">Title: </th>
      <td><input type="text" name="boardUpdateTitle" class="form-control" value="<%=board.getBoardTitle() %>" maxlength="50"></td>
    </tr>
    <tr>
      <th scope="row">Content: </th>
      <td><textarea name="boardContent" class="form-control" value="<%=board.getBoardContent() %>" rows="10" maxlength="2000" ></textarea></td>
    </tr>
    <tr>
      <th scope="row">File Upload</th>
      <td><input type="file" name="boardFile"></td>
      <td></td>
      <td></td>
    </tr>
  </tbody>
</table> 
<button class="btn btn-secondary" style="float: right;" type="Submit">Submit</button> 
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



      <footer class="mastfoot mt-auto" style="text-align: center; margin-top: 800px; color: white; background-color: black; height: 90px;">
        <div class="inner" style="margin-top : 350px;">
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
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