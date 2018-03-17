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
    String userID = null;   //유저가 로그인 했다면 아이디 값을 받아온다. 
    if(session.getAttribute("userID")!= null){
      userID = (String) session.getAttribute("userID");
  }
    
    
    
    //회원가입하는 부분이므로 이미 로그인이 되어있으면 안됌.
    if(userID != null){
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "이미 로그인이 되어있습니다. ");
      response.sendRedirect("index.jsp");
    }



  %>

  <script type="text/javascript">
    function idCheckFunction(){
		  var userID = $("#userID").val();
      $.ajax({
          type:"post",
          url:"./userRegisterCheckServlet",
          data:{
              userID : encodeURIComponent(userID)
          },
          success : function(result){
        	//0 이미 아이디 존재, 1 회원가입 가능 -1 데이터베이스 오류
              if(result == 0){
            	  $("#checkModalTitle").html('Error Message');
                $("#checkModalContent").html('Already existing ID');
              }else if(result == 1){
                $("#checkModalTitle").html('Success Message');
                $("#checkModalContent").html('You can register with this ID!');
              }else{
                $("#checkModalTitle").html('Error Message');
                $("#checkModalContent").html('Sorry, we\'ve got crushed on DB Error');
              }
          }

      });
      $("#checkModal").modal("show");

    }

function passwordCheckFunction(){
  var userPassword1 = $("#userPassword1").val();
  var userPassword2 = $("#userPassword2").val();

  if(userPassword1 != userPassword2){
      $("#passwordCheckMessage").html('The Passwords are different each other');
  }else if(userPassword1 == userPassword2){
      $("#passwordCheckMessage").html('');
  }

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
        
      </li>											<!-- 안 읽은 메시지가 있으면 unread에 그만큼 숫자를 넣어준다 -->
      <li class="nav-item">
        <a class="nav-link" href="#">Mail Box<span id="unread" class="label label-info"></span></a>
      </li>
    </ul>
  </div>
</nav>




<div class="container">
  <form method="post" action="./userRegisterServlet">
    <table class="table" style="margin-top: 30px;">
  <thead class="thead-light">
    <tr>
      <th scope="col"></th>
      <th scope="col" style="text-align: right; width: 450px;">회원 가입 양식</th>
      <th scope="col"></th>
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th scope="row"><h5>ID</h5></th>
      <td><input type="text" id="userID" name="userID" class="form-control" maxlength="20" placeholder="Input ID..."></td>
      <td><button class="btn btn-primary" type="button" style="float: right;" onclick="idCheckFunction()">Check</button></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row">Password</th>
      <td><input onkeyup="passwordCheckFunction()" name="userPassword1" type="password" id="userPassword1" class="form-control" maxlength="20" placeholder="Input Password..."></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row">Check PWD</th>
      <td><input onkeyup="passwordCheckFunction()" type="password" name="userPassword2" id="userPassword2" class="form-control" maxlength="20" placeholder="Input Password to check"></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row">Name</th>
      <td><input type="text" id="userName" class="form-control" maxlength="20" name="userName" placeholder="Input Name..."></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row">Age</th>
      <td><input type="number" id="userAge" class="form-control" maxlength="20" name="userAge" placeholder="Input Age..."></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row" colspan="2">Gender</th>
        <td>
         <div class="form-check">
          <input class="form-check-input" type="radio" name="userGender" id="userGender" value="남자" checked>Male
        </div>
        <div class="form-check">
          <input class="form-check-input" type="radio" name="userGender" id="userGender" value="여자">Female
        </div>
        </td> 
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row">E-mail</th>
      <td><input type="email" class="form-control" name="userEmail" id="userEmail" maxlength="20" placeholder="abc@abc..."></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <th scope="row"></th>
      <td style="color: red;" id="passwordCheckMessage"></td>
      <td></td>
      <td><button class="btn btn-primary" type="Submit">Submit!</button></td>
    </tr>
  </tbody>
</table>
  </form>
</div>




      <footer class="mastfoot mt-auto" style="text-align: center; margin-top: 30px; color: white; background-color: black; height: 90px;">
        <div class="inner">
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
        </div>
      </footer>



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








</body>
</html>