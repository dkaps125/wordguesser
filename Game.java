package wordguesser;

import java.io.*;
import java.util.*;

public class Game {

	ArrayList<String> words = new ArrayList<String>();
	String username;
	String top;

	/**
	 * Reads a dictionary from a file, storing the separate strings in an array
	 * list
	 */
	public void createWords() {
		Scanner in;
		try {
			in = new Scanner(getClass().getResourceAsStream("Dict2.txt"));
			while (in.hasNext()) {
				String s = in.nextLine();
				if (!s.contains("\'"))
					this.words.add(s);
			}

			// This fragment allows for the editing of the word list based on
			// restrictions
			// defined by the programmer.
			// BufferedWriter writer = new BufferedWriter(
			// new FileWriter(new File("H:\\Dicts\\Dict2.txt"), true));
			// for (String str : this.words){
			// if (str.length() > 2) { //restriction goes here
			// writer.write(str);
			// writer.newLine();
			// }
			// }
			// System.out.println(this.words.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Randomly shuffles the characters in a word
	 * 
	 * @param word
	 *            the word to be shuffled
	 * @return a list containing the characters of the word in a random order
	 */
	public static ArrayList<Character> shuffle(String word) {
		ArrayList<Character> res = new ArrayList<Character>();
		ArrayList<Integer> done = new ArrayList<Integer>();
		Random s = new Random();
		int roof = word.length();
		while (done.size() < roof) {
			int t = s.nextInt(roof);
			if (!done.contains(t)) {
				res.add(word.charAt(t));
				done.add(t);
			}
		}
		return res;
	}

	/**
	 * Separates a word into characters and places it in an ArrayList
	 * 
	 * @param arg0
	 *            the word to be separated into a list
	 * @return a list containing the characters of a word
	 */
	public static ArrayList<Character> toCharList(String arg0) {
		ArrayList<Character> list = new ArrayList<Character>();
		for (char c : arg0.toCharArray()) {
			list.add(c);
		}
		return list;
	}

	/**
	 * Generates all of the valid permutations of a given word
	 * 
	 * @param str
	 *            the original word
	 * @return a list of valid words
	 */
	public ArrayList<String> genPermutations(String str) {
		ArrayList<String> res = new ArrayList<String>();

		for (String s : this.words) {
			boolean works = true;
			ArrayList<Character> temp = toCharList(str);
			for (int i = 0; i < s.length(); i++) {
				if (!temp.contains(s.charAt(i))) {
					works = false;
					break;
				} else {
					temp.remove((Character) s.charAt(i));
				}
			}
			if (works)
				res.add(s);
		}
		return res;
	}

	/**
	 * Randomly selects a word with a given length from the program's dictionary
	 * 
	 * @param lim
	 *            the length of the resultant word
	 * @return a random word with the given length
	 */
	public String chooseWord(int lim) {
		String top = "";
		Random gen = new Random();
		for (int i = 0; i < this.words.size(); i++) {
			String s = this.words.get(gen.nextInt(this.words.size()));
			if (s.length() == lim) {
				top = s;
				break;
			}
		}
		return top;
	}

	/**
	 * Determines whether a string exists among the permutations of a given word
	 * 
	 * @param orig
	 *            the original word
	 * @param str
	 *            the user-entered string
	 * @param mixed
	 *            a list of the shuffled characters of the original word
	 * @param permutations
	 *            a list of the possible valid permutations of the original word
	 * @return true if the word exists in the given parameters; false otherwise
	 */
	public boolean checksOut(String str, ArrayList<Character> mixed,
			ArrayList<String> permutations) {
		if (this.words.contains(str) && mixed.containsAll(toCharList(str))
				&& permutations.contains(str))
			return true;
		return false;
	}

	public static Hashtable<String, String> parseScore(String str) {
		Hashtable<String, String> parsed = new Hashtable<String, String>(3);
		Scanner s = new Scanner(str);
		s.useDelimiter(" : ");
		int i = 0;
		while (s.hasNext()) {
			// String temp = s.next();
			parsed.put("word" + i, s.next());
			parsed.put("score" + i, s.next());
			i++;
		}
		return parsed;
	}

	public static void main(String[] args) {
		Game g = new Game();
		Hashtable results = new Hashtable();
		g.createWords();

		Scanner in = new Scanner(System.in);
		System.out.print("Enter username: ");
		g.username = in.next();
		int decision = in.nextInt();
		System.out.print("Enter word length limit: ");
		int lim = Integer.parseInt(in.next());
		g.top = g.chooseWord(lim);

		ArrayList<String> perm = g.genPermutations(g.top);

		ArrayList<Character> shuffled = shuffle(g.top); // obtains a list with
														// the shuffled letters
		System.out.println(g.top);
		System.out.println(perm);
		System.out.println(shuffled + "\n Enter word: ");
		String entry = in.next();
		int goal = perm.size();

		ArrayList<String> found = new ArrayList<String>();

		while (!entry.equalsIgnoreCase("q") && found.size() != goal) {
			if (g.checksOut(entry, shuffled, perm)) { // assures that the user
														// entry is valid and
														// correct
				found.add(perm.remove(perm.indexOf(entry)));
				System.out.println("Yes");
			} else
				System.out.println("No");
			System.out.println("Found: " + found + ".\n" + found.size()
					+ " out of " + goal);
			System.out.println(shuffled + "\n Enter word: ");
			entry = in.next();
		}

		System.out.println("Found: " + found);
		System.out.println("Did not find: " + perm);
		results.put("message", " : " + g.top + " : " + found.size());
		results.put("channel", g.username); // change the channel per test

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);

	}
}
