package reply;

public class ReplyDTO {
	int replyID;
	String userID;
	String replyContent;
	String replyDate;
	int boardID;
	public int getBoardID() {
		return boardID;
	}
	public void setBoardID(int boardID) {
		this.boardID = boardID;
	}
	public int getReplyID() {
		return replyID;
	}
	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(String replyDate) {
		this.replyDate = replyDate;
	}
	
	
	public ReplyDTO(int replyID, String userID, String replyContent, String replyDate, int boardID) {
		
		this.replyID = replyID;
		this.userID = userID;
		this.replyContent = replyContent;
		this.replyDate = replyDate;
		this.boardID = boardID;
	}
	public ReplyDTO() {
		
	}
	
	
}
