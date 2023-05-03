package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PostUtilities {
	
	// Useful when implementing files instead of just captions
	public static String[] convertFileText2Array(String str) throws FileNotFoundException {
		File file = new File(str);
		try (Scanner sc = new Scanner(file)) {
			String fileInText = sc.nextLine();
			String[] arr = fileInText.split(" ");
			return arr;
		}
	}
	
	public static String[] convertText2Array(String str) {
		String[] arr = str.split(" ");
		return arr;
	}
	
	public static String convertText2String(String str) throws FileNotFoundException{
		File file = new File(str);
		Scanner sc = new Scanner(file);
		String lyrics = sc.nextLine();
		sc.close();
		return lyrics;
	}

}
