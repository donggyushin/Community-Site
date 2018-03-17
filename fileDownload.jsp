<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.*" %>
<%@ page import="board.BoardDAO" %>

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
	request.setCharacterEncoding("utf-8");
	String boardID = request.getParameter("boardID");
	if(boardID == null || boardID.equals("")){
		session.setAttribute("messageType", "Error message");
		session.setAttribute("messageContent", "Invalid File!");
		response.sendRedirect("index.jsp");
		return;
	}
	// 파일 업로드된 경로
    String root = request.getSession().getServletContext().getRealPath("/");
    String savePath = root + "upload";
	String fileName = "";
	String fileRealName = "";
	BoardDAO boardDAO = new BoardDAO();
	//서버에 실제 저장된 파일명
	fileName = boardDAO.getFile(boardID);
	//실제 내보낼 파일 명
	fileRealName = boardDAO.getRealFile(boardID);
	if(fileName.equals("") || fileName == null || fileRealName.equals("") || fileRealName == null){
		session.setAttribute("messageType", "Error message");
		session.setAttribute("messageContent", "The file doesn't exist!");
		response.sendRedirect("index.jsp");
		return;
	}
	InputStream in = null;
    OutputStream os = null;
    File file = null;
    boolean skip = false;
    String client = "";
 
 
    try{
         
 
        // 파일을 읽어 스트림에 담기
        try{
            file = new File(savePath, fileName);		//fileRealName의 이름을 가진 파일을 해당 경로에서 가져온다. 
            in = new FileInputStream(file);					//inputStream에 해당 파일을 넣어준다. 
        }catch(FileNotFoundException fe){					//파일을 발견하지 못했을시에 skip에 true 값을 준다. 
            skip = true;
        }
        
        
        
        client = request.getHeader("User-Agent");
        
        // 파일 다운로드 헤더 지정		(어떤 브라우저로 접속을 하든지간에 잘 작동할 수 있도록. 이부분은 잘 모르겠음)
        response.reset() ;
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Description", "JSP Generated Data");	//JSP 로 생성한 데이터임을 알림
	
        if(!skip){			//파일을 발견 했을 시. 
        	 
            
            // IE		인터넷 익스플로어로 접솔 할경우
            if(client.indexOf("MSIE") != -1){								//fileName에 실제로 내보낼 파일명을 한글로 바꿔서 보내준다. 
                response.setHeader ("Content-Disposition", "attachment; filename="+new String(fileRealName.getBytes("KSC5601"),"ISO8859_1"));
 
            }else{		//다른 브라우저로 접속할 경우. 
                // 한글 파일명 처리
                fileRealName = new String(fileRealName.getBytes("utf-8"),"iso-8859-1");
 																		//fileName에 실제로 내보낼 파일 명 입력	
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileRealName + "\"");
                response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
            } 
             //서버가 클라이언트로 전송할 데이터의 길이 전송 
            response.setHeader ("Content-Length", ""+file.length() );
 
 
       
            os = response.getOutputStream();
            //버퍼를 만들어서 전송
            byte b[] = new byte[(int)file.length()];
            int leng = 0;
             
            while( (leng = in.read(b)) > 0 ){
                os.write(b,0,leng);
            }
 
        }else{		//파일을 발견하지 못했을 시
            response.setContentType("text/html;charset=UTF-8");
            out.println("<script language='javascript'>alert('fail to find the file');history.back();</script>");
 
        }
         
        in.close();
        os.close();
 
    }catch(Exception e){
      e.printStackTrace();
    }


		
 
	

%>
</head>
<body>
	
</body>
</html>