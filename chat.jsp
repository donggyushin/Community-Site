<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ page import="java.net.URLEncoder"%>
    <%@ page import="java.net.URLDecoder" %>
    <%@ page import="user.UserDAO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" type="text/css" href="./css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="./css/custom.css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" src="./js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="./js/popper.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<title>PARK</title>
<%
		String userID = null;		//유저가 로그인 했다면 아이디 값을 받아온다. 
		if(session.getAttribute("userID")!= null){
			userID = (String) session.getAttribute("userID");
	}
  String toID = null;
  if(request.getParameter("toID")!=null){
	  toID = (String)request.getParameter("toID");
  } 
  //상대방없을때 되돌려 보내기
  if(toID == null){
	  session.setAttribute("messageType", "Error Message");
	  session.setAttribute("messageContent", "There is no person who can chat with you");
	  response.sendRedirect("index.jsp");
	  return;
  }	
		//로그인 안했으면 로그인 창으로 돌려보내기 
		if(userID == null){
			session.setAttribute("messageType", "Error Message");
			session.setAttribute("messageContent", "Please sign in");
			response.sendRedirect("login.jsp");
			return;
		}	else{
			userID = URLDecoder.decode(userID, "utf-8");
		}
		
		int messageNumber = 0;
		
		String fromProfile = new UserDAO().getProfile(userID);
		String toProfile = new UserDAO().getProfile(toID);
		
		System.out.println(fromProfile);
		
	%>
<script type="text/javascript">
  function autoClosingAlert(selector, delay){
      var alert = $(selector).alert();
      alert.show();
      window.setTimeout(function(){
        alert.hide();
      }, delay);
  }

  function submitFunction(){
      var fromID = '<%=userID%>';
      var toID =  '<%=toID%>';
      var chatContent = $("#chatContent").val();
      $.ajax({
        type : "post",
        url : "./chatSubmitServlet",
        data : {
          fromID : encodeURIComponent(fromID),
          toID : encodeURIComponent(toID),
          chatContent : encodeURIComponent(chatContent)
        },
        success : function(result){
            if(result == 0){
                $("#checkModalTitle").html('Error Message');
                $("#checkModalContent").html('there is some problem in your ID or counter ID or chat content');
                $("#checkModal").modal("show");
            }
            else if(result == 1){
                autoClosingAlert("#successAlert", 2000);
            }else {
                autoClosingAlert("#warningAlert", 2000);
            }
        }
      });
            $("#chatContent").val('');
  }

  var lastID = 0;

  function chatListFunction(type){

    var fromID = '<%= userID%>';
    var toID = '<%= toID%>';
    $.ajax({
      type:"post",
      url:"./chatListServlet",
      data:{
        fromID : encodeURIComponent(fromID),
        toID : encodeURIComponent(toID),
        chatType : type
      },
      success : function(data){
          if(data == ""){
              return;
          }

          var parsed = JSON.parse(data);
          var result = parsed.result;
          for(var i = 0 ; i < result.length; i ++){

            /*
              fromID가 채팅을 치는 사람. toID는 받는 사람. 그러면 fromID가 userID값과 같다면? 내가 친 채팅. 
              그 반대의 경우는 상대방이 친 채팅. 
            */
            
            //from, to, content, time 순으로 추출
              addChat(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value);
          }
          lastID = Number(parsed.last);
      }
    });
  }
  function addChat(fromID, toID, chatContent, chatTime){
    

if(fromID == '<%= userID%>'){    //보낸 사람이 나일때
$("#chatList").append(
          '<li class="mar-btm">'+
          '<div class="media-right">'+                           //상대방이 보냈을때와 내가 보냈을때를 구분
          '<img src="<%= fromProfile%>" class="img-circle img-sm" alt="Profile Picture">'+
          '</div>'+
          '<div class="media-body pad-hor speech-right">'+
          '<div class="speech">'+
          '<p>'+ chatContent +
          '</p>'+
          '<p class="speech-time">'+
          '<i class="fa fa-clock-o fa-fw"></i>'+ chatTime +
          '</p>'+
          '</div>' +
          '</div>' +
          '</li>'
        );
}else{		
	$("#chatList").append(
	          '<li class="mar-btm">'+
	          '<div class="media-left">'+                           //상대방이 보냈을때와 내가 보냈을때를 구분
	          '<img src="<%= toProfile%>" class="img-circle img-sm" alt="Profile Picture">'+
	          '</div>'+
	          '<div class="media-body pad-hor">'+
	          '<div class="speech">'+
	          '<a href="#" class="media-heading">'+ fromID +
	          '</a>'+
	          '<p>'+ chatContent +
	          '</p>'+
	          '<p class="speech-time">'+
	          '<i class="fa fa-clock-o fa-fw"></i>'+ chatTime +
	          '</p>'+
	          '</div>' +
	          '</div>' +
	          '</li>'
	        );           
}
$("#scrollBar").scrollTop($("#scrollBar")[0].scrollHeight);
      
  }

                  
                  
     function getInfiniteChatList(){
      setInterval(function(){
        chatListFunction(lastID);
      }, 2000);
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
        <a class="nav-link" href="chatBox.jsp">Mail Box<span id="unread" style="color: red" class="label label-info"></span></a>
      </li>
    </ul>
  </div>
</nav>
<div class="container">
    <div class="col-md-12 col-lg-6">
        <div class="panel">
        	<!--Heading-->
    		<div class="panel-heading">
    			<div class="panel-control">
    				<div class="btn-group">
    					<button class="btn btn-default" type="button" data-toggle="collapse" data-target="#demo-chat-body"><i class="fa fa-chevron-down"></i></button>
    					<button type="button" class="btn btn-default" data-toggle="dropdown"><i class="fa fa-gear"></i></button>
    					<ul class="dropdown-menu dropdown-menu-right">
    						<li><a href="#">Available</a></li>
    						<li><a href="#">Busy</a></li>
    						<li><a href="#">Away</a></li>
    						<li class="divider"></li>
    						<li><a id="demo-connect-chat" href="#" class="disabled-link" data-target="#demo-chat-body">Connect</a></li>
    						<li><a id="demo-disconnect-chat" href="#" data-target="#demo-chat-body">Disconect</a></li>
    					</ul>
    				</div>
    			</div>
    			<h3 class="panel-title">Chat</h3>
    		</div>
    		<!--Widget body-->
    		<div id="demo-chat-body">
    			<div class="nano has-scrollbar" style="height:380px;">
    				<div class="nano-content pad-all" style="overflow-y: auto;" id="scrollBar" tabindex="0" style="right: -17px;">
    					<ul class="list-unstyled media-block"  id="chatList">

    					</ul>
    				</div>
    			<div class="nano-pane"><div class="nano-slider" style="height: 141px; transform: translate(0px, 0px);"></div></div></div>
    
    			<!--Widget footer-->
    			<div class="panel-footer">
              <div class="col-xs-12">
                <input type="text" id="chatContent" name="chatContent" placeholder="Enter your text" class="form-control chat-input">
              </div>
                <div>
                <button class="btn btn-primary btn-block" type="button" onclick="submitFunction()">Send</button>
              </div>             
    			</div>
    		</div>
    	</div>
    </div>
    <div class="alert alert-success" id="successAlert" style="display: none;">
	  <strong>Success sending message</strong>
	</div>
	<div class="alert alert-warning" id="warningAlert" style="display: none;">
	  <strong>Sorry! Database error has occured</strong>
	</div>
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


<script type="text/javascript">
  //시작하자마자 바로 시작하는 함수
  $(document).ready(function(){
    chatListFunction("");
    getInfiniteChatList();
    infiniteGetUnread();
  });
</script>
      <footer class="mastfoot mt-auto" style="text-align: center; margin-top: 30px; color: white; background-color: black; height: 90px;">
        <div class="inner">
          <p>Cover template for <a href="https://getbootstrap.com/">Bootstrap</a>, by <a href="https://www.instagram.com/donggyu_00/">@Donggyu</a>.</p>
        </div>
      </footer>




</body>
</html>