package reply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DatabaseUtil;

public class ReplyDAO {
	
	public int submit(String userID, String replyContent, String boardID) {
		String SQL = "insert into reply values(null, ?,?,now(), ?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, replyContent);
			pstmt.setInt(3, Integer.parseInt(boardID));
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//DB¿À·ù
	}
	
	public ArrayList<ReplyDTO> getReplyList(String boardID){
		String SQL = "select * from reply where boardID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		ArrayList<ReplyDTO> replyList = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			replyList = new ArrayList<ReplyDTO>();
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReplyDTO reply = new ReplyDTO(rs.getInt("replyID"), 
						rs.getString("userID"), 
						rs.getString("replyContent"), 
						rs.getString("replyDate"), 
						rs.getInt("boardID"));
				replyList.add(reply);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!=null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!=null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return replyList;
	}
	
	public int deleteReply(String replyID) {
		String SQL = "delete from reply where replyID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(replyID));
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return -1;
	}
	
	public ReplyDTO getReply(String replyID) {
		String SQL = "select * from reply where replyID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ReplyDTO reply = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(replyID));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				reply = new ReplyDTO(rs.getInt("replyID"), 
						rs.getString("userID"), 
						rs.getString("replyContent"), 
						rs.getString("replyDate"), 
						rs.getInt("boardID"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}
		return reply;
	}
	
}
