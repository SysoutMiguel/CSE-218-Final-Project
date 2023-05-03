package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

import data.Database;

public class DictionaryMethods implements Serializable {
	private static final long serialVersionUID = 1L;

	public static double getTypos(String essay) {
		int typos = 0;
		String[] words = essay.trim().split("[^a-zA-Z']+");
		for (int i = 0; i < words.length; i++) {
			if (words[i].contains("[^A-Z]")) {
				if (!Database.dictionary.contains(words[i])) {
					if (!Database.dictionary.contains(words[i].toLowerCase())) {
						typos++;
					}
				}
			} else {
				if (!Database.dictionary.contains(words[i].toLowerCase()))
					typos++;
			}
		}
		return typos;
	}

	public static HashSet<String> loadWords(HashSet<String> dictionary) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./src/data/dictionary.txt"));
			String line = reader.readLine();
			while (line != null) {
				dictionary.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Error importing text");
		}

		return dictionary;
	}
}
