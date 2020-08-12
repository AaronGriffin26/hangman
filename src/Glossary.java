import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Glossary {
	/**
	 * The number of graded words to skip per difficulty level.
	 */
	private static final int DIFFICULTY_STEP = 17;
	/**
	 * The number of graded words to randomly pick from in a difficulty level.
	 */
	private static final int DIFFICULTY_RANGE = 62;
	/**
	 * The relative frequency of each letter in the alphabet as found in the Concise Oxford Dictionary (11th edition, 2004).
	 * Each letter in the word adds 1/LETTER_USAGE to the grade.
	 */
	private static final float[] LETTER_USAGE = {
			43.31f, 10.56f, 23.13f, 17.25f, // ABCD
			56.88f, 9.24f, 12.59f, 15.31f, // EFGH
			38.45f, 1f, 5.61f, 27.98f, // IJKL
			15.36f, 33.92f, 36.51f, 16.14f, // MNOP
			1f, 38.64f, 29.23f, 35.43f, // QRST
			18.51f, 5.13f, 6.57f, 1.48f, // UVWX
			9.06f, 1.39f // YZ
	};
	/**
	 * The amount of grade to add for each letter not included in a word.
	 */
	private static final float MISSING_LETTER_GRADE = 0.8f;

	private static String[] dictionary;

	public static String pick(int difficulty) {
		if(dictionary == null)
			generateDifficultyDictionary();
		var rng = new Random();
		int startIndex = Math.min(difficulty * DIFFICULTY_STEP, dictionary.length - DIFFICULTY_RANGE);
		return dictionary[startIndex + rng.nextInt(DIFFICULTY_RANGE)];
	}

	private static void generateDifficultyDictionary() {
		var words = new LinkedList<String>();
		var grades = new LinkedList<Float>();
		for(String w : getWords()) {
			float grade = gradeWord(w);
			int i = 0;
			while(true) {
				if(i == words.size() || grade < grades.get(i)) {
					words.add(i, w);
					grades.add(i, grade);
					break;
				}
				i++;
			}
		}
		dictionary = new String[words.size()];
		for(int i = 0; i < words.size(); i++) {
			dictionary[i] = words.get(i);
			 System.out.println(words.get(i) + " = " + grades.get(i));
		}
	}

	private static List<String> getWords() {
		Path path = Paths.get(System.getProperty("user.dir"), "src");
		try {
			return Files.readAllLines(Paths.get(path.toString(), "Glossary.txt"));
		}
		catch(Exception ignored) {
			var list = new ArrayList<String>();
			for(int i = 0; i < 99; i++) {
				list.add("errorabcdfghijklmnpqstuvwxyz");
			}
			return list;
		}
	}

	private static float gradeWord(String word) {
		float grade = 0f;
		var characters = new ArrayList<Character>();
		for(char c : word.toCharArray()) {
			if(!characters.contains(c)) {
				characters.add(c);
				grade += 1f / LETTER_USAGE[c - 'a'];
			}
		}
		grade += (26f - characters.size()) * MISSING_LETTER_GRADE;
		return grade;
	}
}
