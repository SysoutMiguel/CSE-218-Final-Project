package accounts;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class Comments implements Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedList<Comment> allComments;
	private static Comments comments ;

	public Comments() {
		allComments = new LinkedList<Comment>();
	}
	
	public void addComment(Comment comment) {
		allComments.add(comment);
	}
	
	public void deleteComment(Comment comment) {
		if (allComments.contains(comment)) {
			allComments.remove(comment);
		}
	}
	
	public LinkedList<Comment> getAllComments() {
		return allComments;
	}
	public LinkedList<Comment> getAllCommentsOfPost(Post post) {
		Iterator<Comment> it = allComments.iterator();
		LinkedList<Comment> postComments = new LinkedList<Comment>();
		Comment currentComment;
		while (it.hasNext()) {
			currentComment = it.next();
			if (currentComment.getPostOfComment() == post) {
				postComments.add(currentComment);
			}
		}
		return postComments;
	}
	
	public void setAllComments(LinkedList<Comment> allComments) {
		this.allComments = allComments;
	}
	
	public static Comments getInstance() {
		if (comments == null) {
			comments = new Comments();
			System.out.println("New Comments instance Created");
		}
		return comments;
	}


}
