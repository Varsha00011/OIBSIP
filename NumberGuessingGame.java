import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {

        int round = 1;
        int totalScore = 0;
        boolean playAgain = true;

        System.out.println("====================================");
        System.out.println("       NUMBER GUESSING GAME");
        System.out.println("====================================");

        while (playAgain) {

            System.out.println("\nSelect Difficulty Level:");
            System.out.println("1. Easy   (1-50, 10 attempts)");
            System.out.println("2. Medium (1-100, 7 attempts)");
            System.out.println("3. Hard   (1-200, 5 attempts)");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            int maxNumber;
            int maxAttempts;

            // Difficulty selection
            if (choice == 1) {
                maxNumber = 50;
                maxAttempts = 10;
                System.out.println("\nDifficulty: EASY");
            } 
            else if (choice == 2) {
                maxNumber = 100;
                maxAttempts = 7;
                System.out.println("\nDifficulty: MEDIUM");
            } 
            else if (choice == 3) {
                maxNumber = 200;
                maxAttempts = 5;
                System.out.println("\nDifficulty: HARD");
            } 
            else {
                System.out.println("Invalid choice! Medium difficulty selected.");
                maxNumber = 100;
                maxAttempts = 7;
            }

            // Generate random number
            int secretNumber = random.nextInt(maxNumber) + 1;

            int attempts = 0;
            boolean correct = false;

            System.out.println("\n------------------------------------");
            System.out.println("Round " + round);
            System.out.println("Guess a number between 1 and " + maxNumber);
            System.out.println("You have " + maxAttempts + " attempts.");
            System.out.println("------------------------------------");

            // Guessing loop
            while (attempts < maxAttempts) {

                System.out.print("\nEnter your guess: ");
                int guess = sc.nextInt();

                attempts++;

                System.out.println("Attempt: " + attempts + "/" + maxAttempts);

                if (guess == secretNumber) {

                    System.out.println("Correct! 🎉");
                    System.out.println("You guessed the number in "
                            + attempts + " attempts.");

                    // Score calculation
                    int score = (maxAttempts - attempts + 1) * 10;
                    totalScore += score;

                    System.out.println("Round Score: " + score);
                    correct = true;

                    break;
                } 
                else if (guess > secretNumber) {
                    System.out.println("Too High!");
                } 
                else {
                    System.out.println("Too Low!");
                }

                // Show remaining attempts
                int remaining = maxAttempts - attempts;

                if (remaining > 0) {
                    System.out.println("Attempts remaining: " + remaining);
                }
            }

            // If player couldn't guess
            if (!correct) {
                System.out.println("\nYou Lost! 😢");
                System.out.println("The correct number was: " + secretNumber);
            }

            // Round summary
            System.out.println("\n========== ROUND SUMMARY ==========");
            System.out.println("Round " + round
                    + " — guessed in " + attempts + " attempts");
            System.out.println("Total Score: " + totalScore);
            System.out.println("===================================");

            round++;

            // Play again
            System.out.print("\nDo you want to Play Again? (yes/no): ");
            String answer = sc.next();

            if (!answer.equalsIgnoreCase("yes")) {
                playAgain = false;
            }
        }

        // Final result
        System.out.println("\n====================================");
        System.out.println("           GAME OVER");
        System.out.println("====================================");
        System.out.println("Total Rounds Played: " + (round - 1));
        System.out.println("Final Score: " + totalScore);
        System.out.println("Thank you for playing!");
        System.out.println("====================================");

        sc.close();
    }
}