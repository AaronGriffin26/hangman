import java.util.Scanner;

public class Main {
	private static final String INTRO = "H A N G M A N";
	private static final String PLAY_AGAIN_PROMPT = "Do you want to play again? (yes or no)";
	private static final String DIFFICULTY_GOING_UP = "Let's make it harder...";

	public static void main(String[] args) {
		showMessage(INTRO);
		boolean playing = true;
		int difficulty = 0;
		while(playing) {
			var game = new Game(difficulty);
			game.run();
			if(game.won())
				difficulty++;
			showMessage(PLAY_AGAIN_PROMPT);
			playing = playAgainChoice();
			if(playing && game.won())
				showMessage(DIFFICULTY_GOING_UP);
		}
	}

	/**
	 * Displays text for the player.
	 *
	 * @param message Message template to display
	 */
	private static void showMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Checks if the player wants to play again.
	 *
	 * @return True if player is playing again, false otherwise.
	 */
	private static boolean playAgainChoice() {
		var scanner = new Scanner(System.in);
		while(true) {
			String choice = scanner.nextLine().toLowerCase();
			if(choice.equals("y") || choice.equals("yes"))
				return true;
			if(choice.equals("n") || choice.equals("no"))
				return false;
			System.out.println("Please enter 'yes' or 'no' if you want to play again.\n");
		}
	}
}
