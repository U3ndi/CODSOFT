import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class AdvancedNumberGuessGame {

    // Constants for the game
    private static final int MIN_DIFFICULTY = 1;
    private static final int MAX_DIFFICULTY = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int totalScore = 0;
        int roundsWon = 0;
        boolean continuePlaying = true;

        // Main loop for the game
        while (continuePlaying) {
            // Display game mode choices
            System.out.println("\nChoose a difficulty level (1 - Easy, 2 - Medium, 3 - Hard): ");
            int difficulty = getValidDifficulty(scanner);
            int maxAttempts = getMaxAttempts(difficulty);
            int range = getNumberRange(difficulty);

            // Start the round with the selected difficulty
            int roundScore = playRound(scanner, random, maxAttempts, range);
            if (roundScore > 0) {
                roundsWon++;
                totalScore += roundScore;
            }

            // Ask if the user wants to play another round
            System.out.print("\nWould you like to play another round? (Y/N): ");
            String response = scanner.next();
            continuePlaying = response.equalsIgnoreCase("Y");
        }

        // Display final results
        System.out.println("\n--- Game Over ---");
        System.out.println("Rounds won: " + roundsWon);
        System.out.println("Total score: " + totalScore);

        scanner.close();
    }

    /**
     * Get a valid difficulty level from the user.
     *
     * @param scanner Scanner object to read user input.
     * @return A valid difficulty level (1, 2, or 3).
     */
    private static int getValidDifficulty(Scanner scanner) {
        int difficulty;
        while (true) {
            try {
                difficulty = scanner.nextInt();
                if (difficulty >= MIN_DIFFICULTY && difficulty <= MAX_DIFFICULTY) {
                    break;
                } else {
                    System.out.println("Invalid difficulty. Please choose 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next(); // Clear the invalid input
            }
        }
        return difficulty;
    }

    /**
     * Get the maximum number of attempts based on the selected difficulty.
     *
     * @param difficulty Difficulty level chosen by the user.
     * @return Maximum number of attempts allowed.
     */
    private static int getMaxAttempts(int difficulty) {
        switch (difficulty) {
            case 1:
                return 10; // Easy
            case 2:
                return 7; // Medium
            case 3:
                return 5; // Hard
            default:
                return 7; // Default (Medium)
        }
    }

    /**
     * Get the range of the number to guess based on the selected difficulty.
     *
     * @param difficulty Difficulty level chosen by the user.
     * @return The maximum number in the range for the guessing game.
     */
    private static int getNumberRange(int difficulty) {
        switch (difficulty) {
            case 1:
                return 50; // Easy
            case 2:
                return 100; // Medium
            case 3:
                return 200; // Hard
            default:
                return 100; // Default (Medium)
        }
    }

    /**
     * Executes a single round of the guessing game.
     *
     * @param scanner    Scanner object to read user input.
     * @param random     Random object for generating the secret number.
     * @param maxAttempts The maximum number of attempts allowed.
     * @param range      The range for the secret number to be guessed.
     * @return Points for the round (0 if failed).
     */
    private static int playRound(Scanner scanner, Random random, int maxAttempts, int range) {
        int secretNumber = random.nextInt(range) + 1;
        int attempts = 0;
        boolean guessedCorrectly = false;

        System.out.printf("\nGuess the number between 1 and %d. You have %d attempts.\n", range, maxAttempts);

        while (attempts < maxAttempts && !guessedCorrectly) {
            System.out.print("Enter your guess: ");
            int guess = getValidGuess(scanner);

            attempts++;

            if (guess == secretNumber) {
                System.out.println("Congratulations! You guessed the number in " + attempts + " attempt(s).");
                guessedCorrectly = true;
                int roundScore = calculateScore(maxAttempts, attempts);
                System.out.println("You earned " + roundScore + " point(s) this round.");
                return roundScore;
            } else if (guess < secretNumber) {
                System.out.println("The number is higher than your guess. Try again!");
            } else {
                System.out.println("The number is lower than your guess. Try again!");
            }
        }

        System.out.println("Sorry, you've used all your attempts. The correct number was: " + secretNumber);
        return 0;
    }

    /**
     * Get a valid guess from the user.
     *
     * @param scanner Scanner object to read user input.
     * @return A valid integer guess.
     */
    private static int getValidGuess(Scanner scanner) {
        int guess = -1;
        while (true) {
            try {
                guess = scanner.nextInt();
                break; // If valid input is provided, break out of the loop
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.next(); // Clear the invalid input
            }
        }
        return guess;
    }

    /**
     * Calculate the score based on the number of attempts used.
     *
     * @param maxAttempts The maximum number of attempts allowed.
     * @param attempts    The number of attempts the player used.
     * @return The score for the round.
     */
    private static int calculateScore(int maxAttempts, int attempts) {
        return Math.max(1, maxAttempts - attempts + 1);
    }
}
