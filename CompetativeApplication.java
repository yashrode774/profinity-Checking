package com.example.Competative;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CompetativeApplication {

	static Map<String, String[]> words = new HashMap<>();

	static int largestWordLength = 0;

	static {
		loadConfigs();
	}

	public static void loadConfigs() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/Users/yrode01/testing/Java/Competative/src/wordList.csv"));
			String line = "";
			int counter = 0;
			while((line = reader.readLine()) != null) {
				counter++;
				String[] content = null;
				try {
					content = line.split(",");
					if(content.length == 0) {
						continue;
					}
					String word = content[0];
					String[] ignore_in_combination_with_words = new String[]{};
					if(content.length > 1) {
						ignore_in_combination_with_words = content[1].split("_");
					}

					if(word.length() > largestWordLength) {
						largestWordLength = word.length();
					}
					words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

				} catch(Exception e) {
					e.printStackTrace();
				}

			}
			System.out.println("Loaded " + counter + " words to filter out");
		} catch (IOException e) {
			e.printStackTrace();
		}

    }


	/**
	 * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
	 * @param input
	 * @return
	 */
	public static ArrayList<String> badWordsFound(String input) {
		System.out.println(words.size());
		if(input == null) {
			return new ArrayList<>();
		}

		// don't forget to remove leetspeak, probably want to move this to its own function and use regex if you want to use this
		input = input.replaceAll("1","i");
		input = input.replaceAll("!","i");
		input = input.replaceAll("3","e");
		input = input.replaceAll("4","a");
		input = input.replaceAll("@","a");
		input = input.replaceAll("5","s");
		input = input.replaceAll("7","t");
		input = input.replaceAll("0","o");
		input = input.replaceAll("9","g");


		ArrayList<String> badWords = new ArrayList<>();
		input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

		// iterate over each letter in the word
		for(int start = 0; start < input.length(); start++) {
			// from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
			for(int offset = 1; offset < (input.length()+1 - start) && offset < largestWordLength; offset++)  {
				String wordToCheck = input.substring(start, start + offset);
				if(words.containsKey(wordToCheck)) {
					// for example, if you want to say the word bass, that should be possible.
					String[] ignoreCheck = words.get(wordToCheck);
					boolean ignore = false;
					for(int s = 0; s < ignoreCheck.length; s++ ) {
						if(input.contains(ignoreCheck[s])) {
							ignore = true;
							break;
						}
					}
					if(!ignore) {
						badWords.add(wordToCheck);
					}
				}
			}
		}


		for(String s: badWords) {
			System.out.println(s + " qualified as a bad word in the sentece.");
		}
		System.out.println(" ");
		return badWords;

	}



	public static void main(String[] args) {

		ArrayList<String> badWords = badWordsFound("Yash Rode aftrekken");
		if(badWords.size() > 0) {
			System.out.println("This message was blocked because a bad word was found. If you believe this word should not be blocked, please message support.");
		}
		else
			System.out.println("This is a good sentence.");
	}

}
