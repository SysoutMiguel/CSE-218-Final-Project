package accounts;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class Posts implements Serializable {
	private static final long serialVersionUID = 1L;

	private LinkedList<Post> allPosts;
	private static Posts posts ;
	public Posts() {
		allPosts = new LinkedList<Post>();
	}
	
	public void addPost(Post post) {
		allPosts.addFirst(post);
	}
	
	public void deletePost(Post post) {
		if (allPosts.contains(post)) {
			allPosts.remove(post);
		}
	}
	//	Get all posts from every user
	public LinkedList<Post> getAllPosts() {
		return allPosts;
	}
	
	public void setAllPosts(LinkedList<Post> newAllPosts) {
		allPosts = newAllPosts;
	}

	public LinkedList<Post> getUserPosts(User user) {
		Iterator<Post> it = allPosts.iterator();
		LinkedList<Post> userPosts = new LinkedList<Post>();
		Post currentPost;
		while (it.hasNext()) {
			currentPost = it.next();
			if (currentPost.getUserOfPost() == user) {
				userPosts.add(currentPost);
			}
		}
		return userPosts;
	}
	
	public LinkedList<Post> getUserSubscribedPosts(User user){
		LinkedList<Post> userPosts = new LinkedList<Post>();
		if (user.getSubscribed() == null) {
			return userPosts;
		}
		Iterator<Post> it = allPosts.iterator();
		Post currentPost;
		while(it.hasNext()) {
			currentPost = it.next();
			if (user.getSubscribed().contains(currentPost.getUserOfPost())) {
				userPosts.add(currentPost);
			}
		}
		return userPosts;
	}
	
	public void display() {
		Iterator<Post>it = allPosts.iterator();
		Post post = new Post();
		int count = 0;
		while (it.hasNext()) {
			count++;
			post = it.next();
			System.out.println(count+". "+post.toString() + ", ");
			
		}
	}
	public static Posts getInstance() {
		if (posts == null) {
			posts = new Posts();
			System.out.println("New Posts instance Created");
		}
		return posts;
	}

}
