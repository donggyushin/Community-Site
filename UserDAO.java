package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DatabaseUtil;

public class UserDAO {
	
		
		
		public int login(String userID, String userPassword) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			String SQL = "select userPassword from user where userID = ?";
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					if(rs.getString("userPassword").equals(userPassword)){
						return 1;		//로그인 성공
					}else {
						return 2;		//아이디는 있지만 비밀번호가 일치하지 않는경우.
					}
				}
				return 0;	//아이디가 없는 경우.
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) {pstmt.close();}}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) {rs.close();}}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//데이터베이스 오류 
		}
		
		
		public int registerCheck(String userID) {	//중복체크 하는 함수.
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			String SQL = "select * from user where userID = ?";
					
					try {
						conn = DatabaseUtil.getConnection();
						pstmt = conn.prepareStatement(SQL);
						pstmt.setString(1, userID);
						rs = pstmt.executeQuery();
						if(rs.next() || userID.equals("")) {
							return 0;		//이미 아이디가 있음. 
						}
						return 1;		//회원가입 가능
					}catch(Exception e) {
						e.printStackTrace();
					}finally {
						try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
						try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
						try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
					}
					
					return -1;	//데이터베이스 오류
		}
		
		
		public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail,String userEmailHash, String userProfile) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			String SQL = "insert into user values(?,?,?,?,?,?,?,?, false)";
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, userPassword);
				pstmt.setString(3, userName);
				pstmt.setInt(4, Integer.parseInt(userAge));
				pstmt.setString(5, userGender);
				pstmt.setString(6, userEmail);
				pstmt.setString(7, userEmailHash);
				pstmt.setString(8, userProfile);
				return pstmt.executeUpdate();		//성공했다면 1을 반환할것임.
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//데이터베이스 오류
		}
		
		public int findFriend(String userID) {
			String SQL = "select * from user where userID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return 1;	//친구 있음
				}
				return 0;	// 친구 없음
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try{if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try{if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
				try{if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//DB오류
			
		}	 
		
		
		public UserDTO getUser(String userID) {
			String SQL = "select * from user where userID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			UserDTO user = null;
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					user = new UserDTO(rs.getString("userID"), 
							rs.getString("userPassword"), 
							rs.getString("userName"), 
							rs.getInt("userAge"), 
							rs.getString("userGender"), 
							rs.getString("userEmail"), 
							rs.getString("userEmailHash"), 
							rs.getString("userProfile"), 
							rs.getBoolean("userCheck"));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			return user;
		}
		
		
		public int userUpdate(String userPassword, String userAge, String userName, String userGender, String userEmail,String userID) {
			String SQL = "update user set userPassword = ?, userAge = ?, userName = ?, userGender = ?,userEmail = ? "
					+ " where userID = ? ";
			PreparedStatement pstmt = null;
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userPassword);
				pstmt.setInt(2, Integer.parseInt(userAge));
				pstmt.setString(3, userName);
				pstmt.setString(4, userGender);
				pstmt.setString(5, userEmail);
				pstmt.setString(6, userID);
				return pstmt.executeUpdate();	//성공하면 1반환
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			}
			return -1;	//DB 오류
		}
		
		
		public int profile(String userID,String userProfile) {
			String SQL = "update user set userProfile = ? where userID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userProfile);
				pstmt.setString(2, userID);
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
			return -1;	//db오류
		}
		
		
		
		
		//유저 프로필 주소를 가져오는 함수 
		public String getProfile(String userID) {
			String SQL = "select userProfile from user where userID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			Connection conn = null;
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					if(rs.getString(1).equals("")) {
						
						return "http://localhost:8080/UserChat2/image/github-mark.png";
					}
					return "http://localhost:8080/UserChat2/upload/" + rs.getString(1);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt!= null) pstmt.close();
					if(conn != null) conn.close();
					if(rs != null ) rs.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return "http://localhost:8080/UserChat2/image/github-mark.png";
		}
		
		
		public int userEmailCheck(String userID) {
			String SQL = "update user set userCheck = true where userID = ?";
			PreparedStatement pstmt = null;
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = DatabaseUtil.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
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
		
		
}
