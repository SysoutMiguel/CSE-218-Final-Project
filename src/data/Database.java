package data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import accounts.Comments;
import accounts.Posts;
import accounts.Users;

public class Database implements Serializable {
	private static final long serialVersionUID = 1L;
	public Users allUsers;
	public Posts allPosts;
	public Comments allComments;
	private static Database data = null;
	public static HashSet<String> dictionary = null;

	public Database() {
		allUsers = new Users();
		allPosts = new Posts();
		allComments = new Comments();
	}

	public Users getAllUsers() {
		return allUsers;
	}

	public Posts getAllPosts() {
		return allPosts;
	}

	public Comments getAllComments() {
		return allComments;
	}

	public void setAllComments(Comments comments) {
		this.allComments = comments;
	}

	public void setAllPosts(Posts posts) {
		this.allPosts = posts;
	}

	public void setAllUsers(Users users) {
		this.allUsers = users;
	}

	public static void saveDictionary() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(new File("./src/data/dictionaryData.dat"))))) {
			oos.writeObject(dictionary);
			System.out.println("Save Dictonary");
		} catch (IOException e) {
			System.out.println("Error Save");
		}
	}

	@SuppressWarnings("unchecked")
	public static HashSet<String> loadDictionary() {
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(new File("./src/data/dictionaryData.dat"))))) {
			dictionary = (HashSet<String>) ois.readObject();
			System.out.println("Load Dictionary");
		} catch (IOException | ClassNotFoundException e) {
			getInstanceDictionary();

		}
		return dictionary;
	}

	private static HashSet<String> getInstanceDictionary() {
		if (dictionary == null) {
			dictionary = new HashSet<String>();
			dictionary = util.DictionaryMethods.loadWords(dictionary);
			saveDictionary();
		}
		return dictionary;
	}

	public static Database getInstance() {
		if (data == null) {
			data = new Database();
			System.out.println("New database instance Created");
		}
		return data;
	}

	public static void load() {
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(new File("./src/data/data.dat"))))) {
			data = (Database) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			getInstance();
			System.out.println("error load");
		}
	}

	public static void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(new File("./src/data/data.dat"))))) {
			oos.writeObject(data);
		} catch (IOException e) {
			System.out.println("Error Save");
			e.printStackTrace();
		}
	}

}
