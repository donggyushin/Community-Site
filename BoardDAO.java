package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DatabaseUtil;

public class BoardDAO {
	
	
	public ArrayList<BoardDTO> getList(){
		String SQL = "select * from board order by boardGroup desc, boardSequence ASC";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<BoardDTO> boardList = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				BoardDTO board = new BoardDTO(rs.getString("userID"), 
						rs.getInt("boardID"), 
						rs.getString("boardTitle"), 
						rs.getString("boardContent"), 
						rs.getString("boardDate"), 
						rs.getInt("boardHit"), 
						rs.getString("boardFile"), 
						rs.getString("boardRealFile"), 
						rs.getInt("boardGroup"), 
						rs.getInt("boardSequence"), 
						rs.getInt("boardLevel"));
				boardList.add(board);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return boardList;
	}
	
	
	public int register(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
		String SQL = "insert into board select ? , ifnull((select max(boardID) from board) + 1, 1), ?,?,now(), 0, ?, ?, "
				+ "ifnull((select max(boardGroup) from board)+ 1, 0), 0, 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent);
			pstmt.setString(4, boardFile);
			pstmt.setString(5, boardRealFile);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				if(rs != null) rs.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;	//DB오류
	}
	
	
	public BoardDTO getBoard(String boardID) {
		String SQL = "select * from board where boardID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		BoardDTO board = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				board = new BoardDTO(rs.getString("userID"), 
						rs.getInt("boardID"), 
						rs.getString("boardTitle").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"), 
						rs.getString("boardContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"), 
						rs.getString("boardDate"), 
						rs.getInt("boardHit"), 
						rs.getString("boardFile"), 
						rs.getString("boardRealFile"), 
						rs.getInt("boardGroup"), 
						rs.getInt("boardSequence"), 
						rs.getInt("boardLevel"));
				return board;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(rs != null) rs.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return board;
		
	}
	
	
	public int hit(String boardID) {
		String SQL = "update board set boardHit = boardHit + 1 where boardID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//DB 오류
	}
	
	
	public String getFile(String boardID) {
		String SQL = "select boardFile from board where boardID = ?";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			return "";
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	public String getRealFile(String boardID) {
		String SQL = "select boardRealFile from board where boardID = ?";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			return "";
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}
		return null;
	}
	
	
	public int update(String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
		String SQL = "update board set boardTitle = ?, boardContent = ?, boardFile = ?, boardRealFile = ? where boardID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setString(3, boardFile);
			pstmt.setString(4, boardRealFile);
			pstmt.setInt(5, Integer.parseInt(boardID));
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//DB 오류
	}
	
	
	public int delete(String boardID) {
		String SQL = "delete from board where boardID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!= null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!= null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!= null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	//DB오류
	}
	
	public int getNext() {
		String SQL = "select max(boardID) from board";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;	
	}
	
	public ArrayList<BoardDTO> getListByPageNumber(int pageNumber){
		String SQL = "select * from board where boardID < ? order by boardID desc limit 5";
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<BoardDTO> boardList = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - 5 * (pageNumber - 1));
			rs = pstmt.executeQuery();
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				BoardDTO board = new BoardDTO(rs.getString("userID"), 
						rs.getInt("boardID"), 
						rs.getString("boardTitle"), 
						rs.getString("boardContent"), 
						rs.getString("boardDate"), 
						rs.getInt("boardHit"), 
						rs.getString("boardFile"), 
						rs.getString("boardRealFile"), 
						rs.getInt("boardGroup"), 
						rs.getInt("boardSequence"), 
						rs.getInt("boardLevel"));
				boardList.add(board);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt!=null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn!=null) conn.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs!=null) rs.close();}catch(Exception e) {e.printStackTrace();}
		}
		return boardList;
	}
	
	
	public int checkNextPage(int pageNumber) {
		String SQL = "select * from board where boardID < ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - 5*(pageNumber - 1));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 1;	//다음 페이지가 존재할 경우
			}
			return 0;	//다음 페이지가 존재하지 않을 경우.
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return -1;
	}
	
	
}
