package accounts;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

public class Users implements Serializable {
	private static final long serialVersionUID = 1L;
	private TreeMap<String, User> allUsers;
	private static Users users ;
	
	public Users() {
		allUsers = new TreeMap<String, User>();
		allUsers.put("admin", new User("admin", "admin"));
	}

	public TreeMap<String, User> getAllUsers() {
		return allUsers;
	}
	
	public void setAllUsers(TreeMap<String, User> allUsers2) {
		allUsers = allUsers2;
	}
	
	public void addAccount(User user) {
		allUsers.put(user.getEmail(), user);
		return;
	}
	
	public User findAcct(String email) {
		return allUsers.get(email);
	}
	
	public void display() {
		Set<String> keys = allUsers.keySet();
		for (String key : keys) {
			System.out.println("Key : " + key + "\t\t" + "Value : " + allUsers.get(key));
		}
	}
	
	public void setUsersImgToNull() {
		Set<String> keys = allUsers.keySet();
		for (String key : keys) {
			User user = allUsers.get(key);
			user.setImageNull();
		}
	}
	
	public static Users getInstance() {
		if (users == null) {
			users = new Users();
			System.out.println("New User instance Created");
		}
		return users;
	}
}
