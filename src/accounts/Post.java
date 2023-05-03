package accounts;
import java.io.Serializable;
import java.util.ArrayList;
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;

	private User user;
	private String caption;
	private String date;
	private String mediaSrc;
	private ArrayList<User> userLikes;
	private int likes;
	public Post() {
		super();
		
	}
	
	public boolean isLiked(User user) {
		if (userLikes == null || userLikes.contains(user) == false) {
			userLikes=new ArrayList<User>();
			return false;
		} else {
			return true;
		}
	}
	
	public void addPostLike(User user) {
		 this.likes++;
		 userLikes.add(user);
	}
	
	public void removeLike(User user) {
		this.likes--;
		userLikes.remove(user);
	}
	
	public int getPostLikes() {
		return this.likes;
	}
	
	public User getUserOfPost() {
		return user;
	}

	public void setUserOfPost(User user) {
		this.user = user;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMediaSrc() {
		return mediaSrc;
	}

	public void setMediaSrc(String mediaSrc) {
		this.mediaSrc = mediaSrc;
	}
	
	@Override
	public String toString() {
		return "Post [user=" + user.getEmail() + ", caption=" + caption + ", date=" + date + ", mediaSrc=" + mediaSrc + "]";
	}


}
