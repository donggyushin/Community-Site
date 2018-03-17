package user;

public class UserDTO {
	
    String userID;
    String userPassword;
    String userName;
    int userAge;
    String userGender;
    String userEmail;
    String userEmailHash;
    String userProfile;
    boolean userCheck;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserEmailHash() {
		return userEmailHash;
	}
	public void setUserEmailHash(String userEmailHash) {
		this.userEmailHash = userEmailHash;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public boolean isUserCheck() {
		return userCheck;
	}
	public void setUserCheck(boolean userCheck) {
		this.userCheck = userCheck;
	}
	public UserDTO(String userID, String userPassword, String userName, int userAge, String userGender,
			String userEmail, String userEmailHash, String userProfile, boolean userCheck) {
		this.userID = userID;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userEmail = userEmail;
		this.userEmailHash = userEmailHash;
		this.userProfile = userProfile;
		this.userCheck = userCheck;
	}
	
    public UserDTO() {
    	
    }
    
    
	
}
