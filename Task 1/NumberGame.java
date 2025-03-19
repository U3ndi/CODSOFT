import java.util.*;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int rounds = 0;
        int totalScore = 0;

        System.out.println("Welcome to NumberGame!");

        while (true) {
            int numberToGuess = random.nextInt(100) + 1;
            int attempts = 0;
            int maxAttempts = 10;
            boolean guessedCorrectly = false;

            System.out.println("Round " + (rounds + 1) + " begins! Guess a number between 1 and 100.");

            while (attempts < maxAttempts) {
                System.out.print("Enter your guess: ");
                int userGuess = scanner.nextInt();
                attempts++;

                if (userGuess == numberToGuess) {
                    System.out.println("Correct! You guessed the number in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    totalScore += (maxAttempts - attempts + 1) * 10;
                    break;
                } else if (userGuess < numberToGuess) {
                    System.out.println("Too low! Try again.");
                } else {
                    System.out.println("Too high! Try again.");
                }
            }

            if (!guessedCorrectly) {
                System.out.println("You've run out of attempts. The number was: " + numberToGuess);
            }

            rounds++;
            System.out.print("Do you want to play another round? (yes/no): ");
            String playAgain = scanner.next().toLowerCase();

            if (!playAgain.equals("yes")) break;
        }

        System.out.println("Game Over! Here's your score:");
        System.out.println("-------------------------------");
        System.out.printf("| %-10s | %-10s |\n", "Rounds", "Score");
        System.out.println("-------------------------------");
        System.out.printf("| %-10d | %-10d |\n", rounds, totalScore);
        System.out.println("-------------------------------");

        scanner.close();
    }
}
