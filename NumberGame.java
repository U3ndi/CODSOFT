import java.util.Random;
import java.util.Scanner;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        final int MIN = 1;
        final int MAX = 100;
        final int MAX_ATTEMPTS = 7;  // Maximum attempts per round

        int totalScore = 0;
        int roundsWon = 0;
        boolean playAgain = true;

        while (playAgain) {
            // Generate a random number between MIN and MAX
            int numberToGuess = random.nextInt(MAX - MIN + 1) + MIN;
            int attempts = 0;
            boolean guessedCorrectly = false;

            System.out.println("\n=== New Round ===");
            System.out.println("Try to guess the number between " + MIN + " and " + MAX + ". You have " + MAX_ATTEMPTS + " attempts.");

            // Loop for user attempts
            while (attempts < MAX_ATTEMPTS && !guessedCorrectly) {
                System.out.print("Enter your guess: ");

                // Check if the input is a number
                while (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid number.");
                    scanner.next(); // clear invalid input
                    System.out.print("Enter your guess: ");
                }
                int guess = scanner.nextInt();

                // Check if the number is within the allowed range
                if (guess < MIN || guess > MAX) {
                    System.out.println("The number must be between " + MIN + " and " + MAX + ". Try again!");
                    continue;  // Allow user to enter a valid number again
                }

                attempts++;

                if (guess == numberToGuess) {
                    System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    roundsWon++;

                    // Calculate points based on remaining attempts
                    int roundScore = MAX_ATTEMPTS - attempts + 1;
                    totalScore += roundScore;
                    System.out.println("You won " + roundScore + " points in this round.");
                } else if (guess < numberToGuess) {
                    System.out.println("The number is higher. Try again!");
                } else {
                    System.out.println("The number is lower. Try again!");
                }

                if (!guessedCorrectly && attempts == MAX_ATTEMPTS) {
                    System.out.println("You have reached the maximum number of attempts! The correct number was: " + numberToGuess);
                }
            }

            // Ask the user if they want to play another round
            System.out.print("\nDo you want to play another round? (Y/N): ");
            String response = scanner.next();
            playAgain = response.equalsIgnoreCase("Y");
        }

        // Display final results
        System.out.println("\n--- Game Over ---");
        System.out.println("Rounds won: " + roundsWon);
        System.out.println("Total points: " + totalScore);

        scanner.close();
    }
}
