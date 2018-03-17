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
<title>PARK</title>
<%
		String userID = null;		//유저가 로그인 했다면 아이디 값을 받아온다. 
		if(session.getAttribute("userID")!= null){
			userID = (String) session.getAttribute("userID");
	}
		
		int messageNumber = 0;
		
		
		if(userID == null || userID.equals("")){
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "You must login to enter this page");
			response.sendRedirect("login.jsp");
			return;
		}else{
			userID = URLDecoder.decode(userID, "utf-8");
		}
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

    function getChatBoxList(){
      var userID = '<%= userID%>';
      $.ajax({
        type : "post",
        url : "./chatBoxListServlet",
        data : {
          userID : encodeURIComponent(userID)
        },
        success : function(data){
            if(data == "") {return;}
            var parsed = JSON.parse(data);
            var result = parsed.result;
            $("#boxList").html('');
            for(var i = 0 ; i < result.length; i ++){
                //처음넘어오는 fromID,toID값을 모두 상대방 아이디로 바꿔주는 작업
                if(result[i][0].value == userID){   //보낸 사람이 나일때, 
                    result[i][0].value = result[i][1].value;  //상대방 아이디로 바꿔줌
                }else{                        //받는 사람이 나일때
                    result[i][1].value = result[i][0].value;  //상대방 아이디로 바꿔줌 
                }
                addBox(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value, result[i][4].value, result[i][5].value);
            }
        }
      });
    }
    function addBox(lastID, toID, chatContent, chatTime, unread, profile){
        $("#boxList").append(
            '<tr onclick="location.href=\'chat.jsp?toID='+ encodeURIComponent(toID) +
            '\'">'+
            '<th scope="row"><img src='+profile+' class="img-circle img-sm" alt="Profile Picture">&nbsp;'+ lastID + 
            ' : </th>'+
            '<td style="text-align: left;">'+ chatContent +
            '<span style="color: red;">&nbsp;'+ unread +
            '</span>'+ 
            '</td>'+
            '<td></td>'+
            '<td>'+ chatTime + 
            '</td>'+
            '</tr>'
            
          );
    }           
    function getInfiniteChatBox(){
      setInterval(function(){
        getChatBoxList();
      }, 4000);
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
      <table class="table">
        <thead class="thead-dark">
          <tr>
            <th scope="col"></th>
            <th scope="col">Chat Box</th>
            <th scope="col"></th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody id="boxList" style="overflow-y: auto;">
          
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
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
        </div>
      </footer>


<script type="text/javascript">
  $(document).ready(function(){
    infiniteGetUnread();
    getChatBoxList();
    getInfiniteChatBox();
  });
</script>


</body>
</html>