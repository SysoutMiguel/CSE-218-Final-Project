package accounts;

import java.io.Serializable;

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String caption;
	private String date;
	private Post post;
	
	public Comment() {}
	
	
	public void setPostOfComment(Post post) {
		this.post = post;
	}
	
	public void setUserOfComment(User user) {
		this.user = user;
	}
	
	public void setCaptionOfComment(String comment) {
		this.caption = comment;
	}
	
	public String getCaptionOfComment() {
		return this.caption;
	}
	
	public User getUserOfComment() {
		return this.user;
	}
	
	public Post getPostOfComment() {
		return this.post;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
