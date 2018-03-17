package util;

import javax.servlet.http.HttpServletRequest;

public class ClientIP {

	//해당 유저의 아이피를 가지고 오는 함수
	public static String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-FORWARDED-FOR"); 
	    if (ip == null || ip.length() == 0) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0) {
	        ip = request.getRemoteAddr() ;
	    }
	    return ip;
	}
	
}
