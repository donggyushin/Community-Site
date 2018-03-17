
    <%@ page import="java.net.URLEncoder"%>
    <%@ page import="java.net.URLDecoder"%>
    <%@ page contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Address"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Session"%>
<%@page import="javax.mail.Authenticator"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="user.UserDAO"%>
<%@page import="util.SHA256"%>
<%@page import="util.Gmail"%>
<!DOCTYPE html>
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
			session.setAttribute("messageType", "Error message");
			session.setAttribute("messageContent", "You are not logined now!");
			response.sendRedirect("login.jsp");
			return;
		}
		
		//이메일 체크가 되어있는지 확인 
		boolean emailChecked = new UserDAO().getUser(userID).isUserCheck();
		if(emailChecked){
			//이메일이 이미 체크되어 있다면
			session.setAttribute("messageType", "Alert");
			session.setAttribute("messageContent", "You are already logined!");
			response.sendRedirect("index.jsp");
			return;
		}
		
		// 사용자에게 보낼 메시지를 기입합니다.

				String host = "http://localhost:8080/UserChat2/";
				String from = "donggyu9410@gmail.com";
				String to = new UserDAO().getUser(userID).getUserEmail();
				String subject = "this is a mail to check your email for the user community site, Donggyus's Park";
				String content = "Do your email check by enter follwing link" +

					"<a href='" + host + "emailCheckAction.jsp?code=" + new SHA256().getSHA256(to) + "'>Go!</a>";

		
		
				// SMTP에 접속하기 위한 정보를 기입합니다.
				Properties p = new Properties();
				p.put("mail.smtp.user", from);
				p.put("mail.smtp.host", "smtp.googlemail.com");
				p.put("mail.smtp.port", "465");
				p.put("mail.smtp.starttls.enable", "true");
				p.put("mail.smtp.auth", "true");
				p.put("mail.smtp.debug", "true");
				p.put("mail.smtp.socketFactory.port", "465");
				p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				p.put("mail.smtp.socketFactory.fallback", "false");

				 

				try{

				    Authenticator auth = new Gmail();
				    Session ses = Session.getInstance(p, auth);
				    ses.setDebug(true);
				    MimeMessage msg = new MimeMessage(ses); 
				    msg.setSubject(subject);
				    Address fromAddr = new InternetAddress(from);
				    msg.setFrom(fromAddr);
				    Address toAddr = new InternetAddress(to);
				    msg.addRecipient(Message.RecipientType.TO, toAddr);
				    msg.setContent(content, "text/html;charset=UTF-8");
				    Transport.send(msg);

				} catch(Exception e){
				    e.printStackTrace();
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('오류가 발생했습니다..');");
					script.println("history.back();");
					script.println("</script>");
					script.close();

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

<section class="container text-center" style="margin-top: 50px;">
   <div class="alert alert-success" role="alert" style="margin-top: 50px;">
   		Email was send. Please check the mail at your email you input when you make your account.
   </div>
</section>




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