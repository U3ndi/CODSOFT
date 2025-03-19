import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class NumberGameGUI extends JFrame implements ActionListener {
    private JTextArea displayArea;
    private JTextField inputField;
    private JButton submitButton;
    private Random random;
    private int targetNumber;
    private int attempts;
    private int maxAttempts;
    private int round;
    private int totalScore;
    private int difficultyMultiplier;
    private String difficulty;
    private ArrayList<String> guessHistory;
    private boolean instructionsShown;

    public NumberGameGUI(int maxAttempts, int difficultyMultiplier, String difficulty) {
        this.maxAttempts = maxAttempts;
        this.difficultyMultiplier = difficultyMultiplier;
        this.difficulty = difficulty;
        round = 0;
        totalScore = 0;
        instructionsShown = false;
        random = new Random();
        guessHistory = new ArrayList<>();
        setTitle("NumberGame");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        inputField = new JTextField(10);
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputField.addActionListener(this);
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.addActionListener(this);
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(173, 216, 230));
        inputPanel.add(new JLabel("Your Guess:"));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        add(mainPanel);
        showInstructions();
        startNewRound();
        setVisible(true);
    }

    private void showInstructions() {
        String instructions = "Welcome to NumberGame!\n\n" +
                              "Rules:\n" +
                              "1. A random number between 1 and 100 is generated.\n" +
                              "2. Enter your guess in the field provided.\n" +
                              "3. Receive feedback: too high, too low, or correct.\n" +
                              "4. Limited attempts per round.\n" +
                              "5. Extra hints depend on difficulty level.\n" +
                              "   - Easy: Hints provided every time.\n" +
                              "   - Medium: Hints after 3 attempts.\n" +
                              "   - Hard: No extra hints.\n" +
                              "6. Score is calculated using remaining attempts and a difficulty multiplier.\n" +
                              "7. Final score is displayed in a table.\n\n" +
                              "Difficulty: " + difficulty + "\n" +
                              "Number range: 1 to 100.\n\n" +
                              "Good luck!";
        displayArea.setText(instructions + "\n\n");
        instructionsShown = true;
    }

    private void startNewRound() {
        round++;
        attempts = 0;
        targetNumber = random.nextInt(100) + 1;
        guessHistory.clear();
        if(instructionsShown) {
            displayArea.setText("");
            instructionsShown = false;
        }
        displayArea.append("Round " + round + " begins. You have " + maxAttempts + " attempts.\n");
    }

    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText().trim();
        if(input.isEmpty()) return;
        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch(Exception ex) {
            displayArea.append("Invalid input. Please enter a number.\n");
            inputField.setText("");
            return;
        }
        attempts++;
        String feedback = "Guess: " + guess;
        if(guess < targetNumber) {
            feedback += " - Too low.";
            if(difficulty.equalsIgnoreCase("Easy") || (difficulty.equalsIgnoreCase("Medium") && attempts >= 3)) {
                feedback += " Hint: The number is " + (targetNumber % 2 == 0 ? "even." : "odd.");
            }
        } else if(guess > targetNumber) {
            feedback += " - Too high.";
            if(difficulty.equalsIgnoreCase("Easy") || (difficulty.equalsIgnoreCase("Medium") && attempts >= 3)) {
                feedback += " Hint: The number is " + (targetNumber % 2 == 0 ? "even." : "odd.");
            }
        } else {
            feedback += " - Correct!";
        }
        guessHistory.add(feedback);
        updateDisplay();
        if(guess == targetNumber) {
            int roundScore = (maxAttempts - attempts + 1) * difficultyMultiplier * 10;
            totalScore += roundScore;
            displayArea.append("Round Score: " + roundScore + "\n");
            int option = JOptionPane.showConfirmDialog(this, "Round " + round + " finished.\nYour round score: " + roundScore + "\nTotal Score: " + totalScore + "\nPlay another round?", "Round Over", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                startNewRound();
            } else {
                endGame();
            }
        } else if(attempts >= maxAttempts) {
            displayArea.append("Out of attempts. The number was " + targetNumber + ".\n");
            int option = JOptionPane.showConfirmDialog(this, "Round " + round + " finished.\nThe number was " + targetNumber + ".\nTotal Score: " + totalScore + "\nPlay another round?", "Round Over", JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION) {
                startNewRound();
            } else {
                endGame();
            }
        }
        inputField.setText("");
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for(String s : guessHistory) {
            sb.append(s).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void endGame() {
        JFrame tableFrame = new JFrame("Final Score");
        tableFrame.setSize(300, 200);
        tableFrame.setLocationRelativeTo(this);
        String[] columnNames = {"Rounds", "Score"};
        Object[][] data = {{round, totalScore}};
        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        String instructions = "Instructions:\n" +
                              "1. A random number between 1 and 100 is generated.\n" +
                              "2. Enter your guess and get feedback.\n" +
                              "3. Limited attempts per round.\n" +
                              "4. Hints depend on difficulty level.\n" +
                              "5. Score is calculated based on attempts and difficulty.\n" +
                              "6. Final score is shown in a table.\n" +
                              "Select a difficulty level to start.";
        JOptionPane.showMessageDialog(null, instructions, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
        Object[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(null, "Select Difficulty Level", "Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        int attemptsLimit;
        int multiplier;
        String level;
        if(choice == 0) {
            attemptsLimit = 10;
            multiplier = 1;
            level = "Easy";
        } else if(choice == 1) {
            attemptsLimit = 7;
            multiplier = 2;
            level = "Medium";
        } else {
            attemptsLimit = 5;
            multiplier = 3;
            level = "Hard";
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NumberGameGUI(attemptsLimit, multiplier, level);
            }
        });
    }
}
