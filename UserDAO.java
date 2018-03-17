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
						return 1;		//�α��� ����
					}else {
						return 2;		//���̵�� ������ ��й�ȣ�� ��ġ���� �ʴ°��.
					}
				}
				return 0;	//���̵� ���� ���.
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) {pstmt.close();}}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) {rs.close();}}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) {conn.close();}}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//�����ͺ��̽� ���� 
		}
		
		
		public int registerCheck(String userID) {	//�ߺ�üũ �ϴ� �Լ�.
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
							return 0;		//�̹� ���̵� ����. 
						}
						return 1;		//ȸ������ ����
					}catch(Exception e) {
						e.printStackTrace();
					}finally {
						try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
						try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
						try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
					}
					
					return -1;	//�����ͺ��̽� ����
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
				return pstmt.executeUpdate();		//�����ߴٸ� 1�� ��ȯ�Ұ���.
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//�����ͺ��̽� ����
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
					return 1;	//ģ�� ����
				}
				return 0;	// ģ�� ����
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try{if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try{if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
				try{if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			
			return -1;	//DB����
			
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
				return pstmt.executeUpdate();	//�����ϸ� 1��ȯ
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {if(pstmt != null) pstmt.close();}catch(Exception e) {e.printStackTrace();}
				try {if(conn != null) conn.close();}catch(Exception e) {e.printStackTrace();}
				try {if(rs != null) rs.close();}catch(Exception e) {e.printStackTrace();}
			}
			return -1;	//DB ����
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
			return -1;	//db����
		}
		
		
		
		
		//���� ������ �ּҸ� �������� �Լ� 
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
