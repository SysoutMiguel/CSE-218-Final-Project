package accounts;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.image.Image;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userName;
	private String email;
	private String password;
	private AppUserRole appUserRole;
	private String phoneNum;
	private String age;
	private String state;
	private String firstName;
	private String lastName;
	private String pfp;
	private ArrayList<User> subscribers;
	private ArrayList<User> subscribed;
	private transient Image img;

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
		if (email.equals("admin") && password.equals("admin")) {
			appUserRole = AppUserRole.ADMIN;
		} else {
			appUserRole = AppUserRole.USER;
		}
		
	}

	public User() {
	}

	public String getPfp() {
		return pfp;
	}

	public void setPfp(String pfp) {
		this.pfp = pfp;
		updateImage();
	}
	
	private void updateImage() {
		img = new Image(pfp);
	}
	
	public Image getImage() {
		if (img == null) {
			if (pfp == null) {
				String defaultPic = System.getProperty("user.dir");
				defaultPic = defaultPic + "\\src\\resource\\Default_User_PFP.png";
				pfp = defaultPic;
			}
			img = new Image(pfp);
		}
		return img;
	}
	
	public void setImageNull() {
		this.img = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AppUserRole getAppUserRole() {
		return appUserRole;
	}

	public void setPhoneNumber(String phone) {
		this.phoneNum = phone;
	}

	public String getPhoneNumber() {
		return phoneNum;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAge() {
		return age;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setfName(String firstName) {
		this.firstName = firstName;
	}

	public String getFname() {
		return firstName;
	}

	public void setlName(String lastName) {
		this.lastName = lastName;
	}

	public String getlName() {
		return lastName;
	}

	public ArrayList<User> getSubscribers() { // Users following me
		return this.subscribers;
	}

	public ArrayList<User> getSubscribed() { // Users I am following
		return this.subscribed;
	}

	public void addSubscriber(User user) {	// Add user followers
		if (this.subscribers == null) {
			this.subscribers = new ArrayList<User>();
			this.subscribers.add(user);
		} else {
			this.subscribers.add(user);
		}
	}

	public void removeSubscriber(User user) {
		if (this.subscribers.contains(user)) {
			this.subscribers.remove(user);
		}
	}

	public void subscribeTo(User user) {	// User to follow
		if (this.subscribed == null) {
			subscribed = new ArrayList<User>();
			this.subscribed.add(user);

		} else {
			this.subscribed.add(user);
		}
	}

	public void unsubscribeTo(User user) {
		if (this.subscribed.contains(user)) {
			subscribed.remove(user);
		}
	}

	public boolean follows(User user) {
		if (this.subscribed == null) {
			this.subscribed = new ArrayList<User>();
			return false;
		}
		if (this.subscribed.contains(user)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", appUserRole=" + appUserRole + ", phoneNum=" + phoneNum + ", age=" + age + ", state=" + state
				+ ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
