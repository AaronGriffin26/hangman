import java.util.ArrayList;
import java.util.Scanner;

public class Game {
	/**
	 * Difficulty level of the game, used to pick a word pool in the glossary.
	 */
	private int difficulty;
	/**
	 * The correct word the player must reveal.
	 */
	private String word;
	/**
	 * Array of flags associated with each letter of the word.
	 */
	private boolean[] revealed;
	/**
	 * Letters that the player has entered.
	 */
	private ArrayList<Character> guessed = new ArrayList<>();
	/**
	 * Letters that the player has entered and are not in the word.
	 */
	private StringBuilder missedLetters = new StringBuilder();

	public Game(int difficulty) {
		this.difficulty = difficulty;
	}

	public void run() {
		pickWord();
		while(isPlaying() && !wordRevealed()) {
			displayState();
			processGuess(nextGuess());
		}
		if(wordRevealed())
			displayVictory();
		else
			displayGameOver();
	}

	private void pickWord() {
		word = Glossary.pick(difficulty);
		revealed = new boolean[word.length()];
	}

	private boolean isPlaying() {
		return (missedLetters.length() < 7);
	}

	private boolean wordRevealed() {
		for(boolean b : revealed) {
			if(!b)
				return false;
		}
		return true;
	}

	private void displayState() {
		System.out.println(Hangman.STATES[missedLetters.length()]);
		if(difficulty > 0)
			System.out.println("Level " + (difficulty + 1));
		System.out.println("Missed letters: " + missedLetters);
		System.out.println(getWordDisplay());
	}

	private String getWordDisplay() {
		var revealedLetters = new StringBuilder();
		for(int i = 0; i < word.length(); i++) {
			if(i > 0)
				revealedLetters.append(" ");
			if(revealed[i])
				revealedLetters.append(word.charAt(i));
			else
				revealedLetters.append("_");
		}
		return revealedLetters.toString();
	}

	private char nextGuess() {
		var scanner = new Scanner(System.in);
		while(true) {
			System.out.println("Guess a letter.");
			String choice = scanner.nextLine();
			if(choice.matches("^([a-z]|[A-Z])$")) {
				char letter = choice.toLowerCase().charAt(0);
				if(!guessed.contains(letter))
					return letter;
				else
					System.out.println("You have already guessed that letter. Choose again.");
			}
		}
	}

	private void processGuess(char guess) {
		guessed.add(guess);
		boolean miss = true;
		for(int i = 0; i < revealed.length; i++) {
			if(word.charAt(i) == guess) {
				revealed[i] = true;
				miss = false;
			}
		}
		if(miss)
			missedLetters.append(guess);
	}

	private void displayGameOver() {
		System.out.println(Hangman.STATES[missedLetters.length()]);
		System.out.println("You have made too many mistakes and died. The word was \"" + word + "\".");
	}

	private void displayVictory() {
		System.out.println("Yes! The secret word is \"" + word + "\"! You have won!");
	}

	public boolean won() {
		return (missedLetters.length() < 7);
	}
}
