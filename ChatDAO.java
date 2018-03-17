package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DatabaseUtil;

public class ChatDAO {
	
	
	public ArrayList<ChatDTO> getChatListByNumber(String fromID, String toID, String number){
		String SQL = "select * from chat where (fromID=? and toID=?) or (fromID=? and toID=?) and "
				+ "chatID > (select max(chatID)-? from chat where (fromID=? and toID=?) or (fromID=? and toID=?))";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<ChatDTO> chatList = null;
		try {
			chatList = new ArrayList<ChatDTO>();
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(number));
			pstmt.setString(6, fromID);
			pstmt.setString(7, toID);
			pstmt.setString(8, toID);
			pstmt.setString(9, fromID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ChatDTO chat = new ChatDTO(rs.getInt("chatID"), rs.getString("fromID"), 
						rs.getString("toID"), rs.getString("chatContent"), rs.getString("chatTime"), rs.getInt("chatRead"));
				chatList.add(chat);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!=null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!=null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return chatList;
	}
	
	
	//입력받은 아이디 값보다 큰 채팅들 모두 출력 
	public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID){
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<ChatDTO> chatList = null;
		String SQL = "select * from chat where ((fromID = ? and toID = ?) or (fromID = ? and toID = ?)) and chatID > ? "
				+ "order by chatTime";
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID));
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				ChatDTO chat = new ChatDTO(rs.getInt("chatID"), rs.getString("fromID"), rs.getString("toID"), rs.getString("chatContent"), 
						rs.getString("chatTime"), rs.getInt("chatRead"));
				chat.setChatContent(rs.getString("chatContent").replaceAll("\n", "<br>").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(" ", "&nbsp;"));
				chatList.add(chat);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!= null) {pstmt.close();}}catch(Exception e) {e.printStackTrace();}
			try {if(rs!= null) {rs.close();}}catch(Exception e) {e.printStackTrace();}
			try {if(conn!= null) {conn.close();}}catch(Exception e) {e.printStackTrace();}
		}
		
		
		return chatList;
		
	}
	
	
	
	public int chatRead(String fromID, String toID) {
		String SQL = "update chat set chatRead = 1 where toID = ? and fromID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//DB오류
	}
	
	
	
	
	public int submit(String fromID, String toID, String chatContent) {
		String SQL = "insert into chat values(null, ?,?,?,now(), 0)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!= null) {pstmt.close();}}catch(Exception e) {e.printStackTrace();}
			try {if(rs!= null) {rs.close();}}catch(Exception e) {e.printStackTrace();}
			try {if(conn!= null) {conn.close();}}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//데이터베이스 오류
	}
	
	
	//안읽은 채팅갯수 가져오기 
	public int getUnreadNumber(String userID) {
		String SQL = "select count(*) from chat where toID = ? and chatRead = 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!= null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!= null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!= null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return -1;	//DB오류
		
	}
	
	
	//개인별로 안읽은 채팅갯수 가져오기
	public String getUnreadNumberByPersonal(String userID, String counterID) {
		String SQL = "select count(*) from chat where fromID = ? and toID = ? and chatRead = 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, counterID);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			return "0";
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!=null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!=null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return null;	//DB오류
	}
	
	
	
	//메시지함에서 불러올 ArrayList		이부분이 가장 어려움
	public ArrayList<ChatDTO> getBox(String userID){
		String SQL = "select * from chat where chatID in (select max(chatID) from chat where fromID = ? or toID = ? group by fromID,toID)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<ChatDTO> chatList = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				ChatDTO chatDTO = new ChatDTO(rs.getInt("chatID"), rs.getString("fromID"), rs.getString("toID"), 
						rs.getString("chatContent"), rs.getString("chatTime"), rs.getInt("chatRead"));
				chatList.add(chatDTO);
			}
			for(int i = 0 ; i < chatList.size(); i ++) {
				ChatDTO x = chatList.get(i);
				for(int j = 0 ; j < chatList.size(); j ++) {
					ChatDTO y = chatList.get(j);
					if(x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
						if(x.getChatID() > y.getChatID()) {
							chatList.remove(y);
							j--;
						}else {
							chatList.remove(x);
							i--;
							break;
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return chatList;
	}
	
	
	
}
