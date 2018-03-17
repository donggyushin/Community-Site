<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.net.URLEncoder"%>
    <%@ page import="java.net.URLDecoder" %>
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
<style type="text/css">
  a {
    color: black;
    text-decoration: none;
  }
</style>
<title>PARK</title>
<%
		String userID = null;		//유저가 로그인 했다면 아이디 값을 받아온다. 
		if(session.getAttribute("userID")!= null){
			userID = (String) session.getAttribute("userID");
			
			
		//로그인 안한 사람은 못들어오게 하기 
		if(userID == null){
			session.setAttribute("messageType", "Error message");
			session.setAttribute("messageContent", "You must login to enter this page");
			response.sendRedirect("login.jsp");
			return;
  		}else{
  			userID = URLDecoder.decode(userID, "utf-8");
  		}
	}
		
		int messageNumber = 0;
	
	%>

<script type="text/javascript">
  function searchFunction(){
    var findName = $("#findName").val();
    $.ajax({
      type : "post",
      url : "./userFindServlet",
      data : {
        findName : encodeURIComponent(findName)
      },
      success : function(result){
          if(result == "") return;
          //1친구 있음 0친구 없음 -1DB오류
          if(result == "0"){
        	  $("#checkModalTitle").html('Error Message');
            $("#checkModalContent").html('There is no user you are finding');
            $("#checkModal").modal("show");
          }else if(result == "-1"){
            $("#checkModalTitle").html("Error Message");
            $("#checkModalContent").html("Sorry! Database Error has occured!");
            $("#checkModal").modal("show");
          }else{
            var parsed = JSON.parse(result);
            var profile = parsed.userProfile;
            addFriend(findName, profile);
          }
      }
    });
  }

function addFriend(findName, profile){
      $("#friendList").html('');

      $("#friendList").append(
          '<th scope="row"><h4>Friend : </h4></th>'+
          '<td><a href="chat.jsp?toID='+ encodeURIComponent(findName) + 
          '">' +
          '<img src='+profile+' class="img-circle img-sm" alt="Profile Picture">'+
          '<h6>'+ findName + '</h6>'+ 
          '</a></td>'+
          '<td></td>' +
          '<td></td>' 
        );
}

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



<!-- 친구 찾는 테이블 창 -->
<table class="table" style="margin-top: 30px;">
  <thead class="thead-dark">
    <tr>
      <th scope="col"></th>
      <th scope="col">Search Friend at here!</th>
      <th scope="col"></th>
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row">Friend Name</th>
      <td><input type="text" name="findName" id="findName" class="form-control" placeholder="userName...."></td>
      <td></td>
      <td><button type="button" class="btn btn-primary" onclick="searchFunction()">Search</button></td>
    </tr>
  </tbody>
</table>

<table class="table">
  <tbody>
    <tr id="friendList">

    </tr>
  </tbody>
</table>



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



<div class="modal" id="checkModal" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="checkModalTitle"></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <p id="checkModalContent"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>




      <footer class="mastfoot mt-auto" style="text-align: center; margin-top: 800px; color: white; background-color: black; height: 90px;">
        <div class="inner" style="margin-top : 350px;">
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
        </div>
      </footer>

<script type="text/javascript">
  $(document).ready(function(){
    infiniteGetUnread();
  });
</script>



</body>
</html>