import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class QuizApp extends JFrame {

    
    static class Question {
        String subject;
        String questionText;
        String[] options;
        int correctAnswer; 
        int timeLimit; 

        public Question(String subject, String questionText, String[] options, int correctAnswer, int timeLimit) {
            this.subject = subject;
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
            this.timeLimit = timeLimit;
        }
    }

    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int score;
    private int remainingTime;
    private Timer timer;
    private int selectedGrade; 

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel startPanel;
    private JPanel quizPanel;
    private JPanel resultPanel;
    
    private JComboBox<Integer> gradeComboBox;
    
    private JLabel subjectLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JLabel timerLabel;
    private JButton nextButton;

    public QuizApp() {
        setTitle("Knowledge Quiz for High School Students");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createStartPanel();
        createQuizPanel();
        
        add(mainPanel);
    }
    
    private void createStartPanel() {
        startPanel = new JPanel(new BorderLayout());
        startPanel.setBackground(new Color(240, 248, 255)); 
        
        JLabel titleLabel = new JLabel("Welcome to the Knowledge Quiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 25, 112)); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        startPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JLabel gradeLabel = new JLabel("Select Your Grade Level (10, 11, or 12):");
        gradeLabel.setFont(new Font("Verdana", Font.PLAIN, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(gradeLabel, gbc);
        
        Integer[] grades = {10, 11, 12};
        gradeComboBox = new JComboBox<>(grades);
        gradeComboBox.setFont(new Font("Verdana", Font.PLAIN, 22));
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(gradeComboBox, gbc);
        
        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Verdana", Font.BOLD, 26));
        startButton.setBackground(new Color(60, 179, 113)); 
        startButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(startButton, gbc);
        
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedGrade = (Integer) gradeComboBox.getSelectedItem();
                loadQuestions();
                currentQuestionIndex = 0;
                score = 0;
                showQuestion();
                cardLayout.show(mainPanel, "quizPanel");
            }
        });
        
        startPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(startPanel, "startPanel");
    }
    
    
    private void createQuizPanel() {
        quizPanel = new JPanel(new BorderLayout());
        quizPanel.setBackground(new Color(255, 250, 240)); 
       
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(72, 61, 139)); 
        
        subjectLabel = new JLabel("Subject: ", SwingConstants.LEFT);
        subjectLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        subjectLabel.setForeground(Color.WHITE);
        subjectLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(subjectLabel, BorderLayout.WEST);
        
        timerLabel = new JLabel("Time: ", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 26));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(timerLabel, BorderLayout.EAST);
        
        quizPanel.add(topPanel, BorderLayout.NORTH);
        

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 250, 240));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        optionsGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4]; 
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Verdana", Font.PLAIN, 22));
            optionButtons[i].setBackground(new Color(255, 250, 240));
            optionButtons[i].setForeground(new Color(47, 79, 79)); 
            optionsGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
            centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        quizPanel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 250, 240));
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Verdana", Font.BOLD, 24));
        nextButton.setBackground(new Color(65, 105, 225)); 
        nextButton.setForeground(Color.WHITE);
        bottomPanel.add(nextButton);
        quizPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                nextQuestion();
            }
        });
        
        mainPanel.add(quizPanel, "quizPanel");
    }
    
    private void loadQuestions() {
        if (selectedGrade == 10) {
            questions = loadGrade10Questions();
        } else if (selectedGrade == 11) {
            questions = loadGrade11Questions();
        } else if (selectedGrade == 12) {
            questions = loadGrade12Questions();
        }
    }
    
    private ArrayList<Question> loadGrade10Questions() {
        ArrayList<Question> list = new ArrayList<>();
        int timeLimit = 60;
        
        list.add(new Question("Literature", "Grade 10 Literature Q1: Who wrote 'Romeo and Juliet'?", 
                new String[]{"William Shakespeare", "Charles Dickens", "Mark Twain", "Jane Austen"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q2: Which of these is a famous epic poem?", 
                new String[]{"The Iliad", "The Odyssey", "Paradise Lost", "Beowulf"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q3: What is a simile?", 
                new String[]{"A comparison using 'like' or 'as'", "An exaggerated statement", "A narrative poem", "A type of drama"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q4: Which term refers to a recurring theme in literature?", 
                new String[]{"Motif", "Allegory", "Irony", "Symbolism"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q5: What is the main purpose of literature?", 
                new String[]{"To entertain and educate", "To calculate numbers", "To build structures", "To develop software"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q6: Identify the literary device in: 'The wind whispered through the trees.'", 
                new String[]{"Personification", "Metaphor", "Simile", "Alliteration"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 10 Literature Q7: What does the term 'protagonist' mean?", 
                new String[]{"Main character", "Villain", "Supporting character", "Narrator"}, 0, timeLimit));
        
        list.add(new Question("Biology", "Grade 10 Biology Q1: What is the basic unit of life?", 
                new String[]{"Cell", "Atom", "Molecule", "Organ"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 10 Biology Q2: Which organelle is known as the powerhouse of the cell?", 
                new String[]{"Mitochondria", "Nucleus", "Ribosome", "Chloroplast"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 10 Biology Q3: What process do plants use to convert sunlight into energy?", 
                new String[]{"Photosynthesis", "Respiration", "Transpiration", "Fermentation"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 10 Biology Q4: Which blood cell helps in clotting?", 
                new String[]{"Platelet", "Red blood cell", "White blood cell", "Plasma"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 10 Biology Q5: What is the human genetic material?", 
                new String[]{"DNA", "RNA", "Protein", "Carbohydrate"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 10 Biology Q6: Which system controls the body’s functions?", 
                new String[]{"Nervous system", "Circulatory system", "Respiratory system", "Digestive system"}, 0, timeLimit));
        
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q1: What is the chemical symbol for water?", 
                new String[]{"H2O", "CO2", "O2", "NaCl"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q2: What is the pH value of a neutral solution?", 
                new String[]{"7", "0", "14", "5"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q3: Which gas is most abundant in Earth’s atmosphere?", 
                new String[]{"Nitrogen", "Oxygen", "Carbon Dioxide", "Hydrogen"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q4: What is an element?", 
                new String[]{"A pure substance", "A compound", "A mixture", "A solution"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q5: What process describes a solid turning directly into gas?", 
                new String[]{"Sublimation", "Evaporation", "Condensation", "Melting"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 10 Chemistry Q6: What is the periodic table used for?", 
                new String[]{"Organizing elements", "Calculating numbers", "Mapping countries", "Storing data"}, 0, timeLimit));
        
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q1: What is the value of 7 + 5?", 
                new String[]{"12", "11", "13", "10"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q2: Solve: 9 x 3 =", 
                new String[]{"27", "26", "28", "30"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q3: What is the square root of 64?", 
                new String[]{"8", "6", "7", "9"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q4: What is 15 - 6?", 
                new String[]{"9", "8", "10", "7"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q5: What is 12 / 4?", 
                new String[]{"3", "4", "2", "6"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 10 Mathematics Q6: If x + 5 = 12, what is x?", 
                new String[]{"7", "6", "8", "9"}, 0, timeLimit));
        
        return list;
    }
    
    private ArrayList<Question> loadGrade11Questions() {
        ArrayList<Question> list = new ArrayList<>();
        int timeLimit = 45;
        
        list.add(new Question("Literature", "Grade 11 Literature Q1: Who is the author of 'Pride and Prejudice'?", 
                new String[]{"Jane Austen", "Emily Brontë", "Charles Dickens", "Virginia Woolf"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q2: Which literary period is known for its focus on emotion?", 
                new String[]{"Romanticism", "Modernism", "Realism", "Postmodernism"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q3: What is a common theme in tragedy?", 
                new String[]{"Fate", "Comedy", "Adventure", "Mystery"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q4: Define 'irony'.", 
                new String[]{"A contrast between expectations and reality", "A type of rhyme", "A literary character", "A narrative style"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q5: Which work is considered a classic of American literature?", 
                new String[]{"The Great Gatsby", "To Kill a Mockingbird", "Moby Dick", "1984"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q6: What is the meaning of 'allegory'?", 
                new String[]{"A story with a hidden meaning", "A factual report", "A biography", "A scientific article"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 11 Literature Q7: What does the term 'genre' refer to in literature?", 
                new String[]{"Category of literature", "Author's name", "Plot twist", "Character type"}, 0, timeLimit));
        
        list.add(new Question("Biology", "Grade 11 Biology Q1: What is the function of ribosomes?", 
                new String[]{"Protein synthesis", "Energy production", "DNA replication", "Cell structure"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 11 Biology Q2: Which process describes cell division in somatic cells?", 
                new String[]{"Mitosis", "Meiosis", "Binary Fission", "Fusion"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 11 Biology Q3: What pigment is essential for photosynthesis?", 
                new String[]{"Chlorophyll", "Hemoglobin", "Melanin", "Carotene"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 11 Biology Q4: What type of molecule is an enzyme?", 
                new String[]{"Protein", "Carbohydrate", "Lipid", "Nucleic acid"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 11 Biology Q5: What structure contains the cell’s genetic material?", 
                new String[]{"Nucleus", "Mitochondria", "Cytoplasm", "Cell wall"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 11 Biology Q6: What is homeostasis?", 
                new String[]{"Maintenance of internal balance", "Cell division", "Energy production", "Genetic mutation"}, 0, timeLimit));
        
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q1: What is the atomic number?", 
                new String[]{"Number of protons", "Number of neutrons", "Atomic mass", "Valence electrons"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q2: Which bond is formed by the sharing of electrons?", 
                new String[]{"Covalent bond", "Ionic bond", "Metallic bond", "Hydrogen bond"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q3: What is the chemical formula for table salt?", 
                new String[]{"NaCl", "KCl", "CaCO3", "H2O"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q4: Which state of matter has a definite volume but no fixed shape?", 
                new String[]{"Liquid", "Solid", "Gas", "Plasma"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q5: What is an exothermic reaction?", 
                new String[]{"Releases heat", "Absorbs heat", "No heat change", "Reaches equilibrium"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 11 Chemistry Q6: What is the pH of a basic solution?", 
                new String[]{"Greater than 7", "Equal to 7", "Less than 7", "Always 14"}, 0, timeLimit));
        
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q1: Solve for x: 2x + 5 = 15.", 
                new String[]{"5", "4", "6", "7"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q2: What is the area of a circle with radius 3? (Use π ≈ 3.14)", 
                new String[]{"28.26", "18.84", "9.42", "31.4"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q3: What is the derivative of sin(x)?", 
                new String[]{"cos(x)", "-cos(x)", "sin(x)", "-sin(x)"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q4: Solve: 3² + 4² =", 
                new String[]{"25", "20", "24", "30"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q5: What is 15% of 200?", 
                new String[]{"30", "20", "25", "35"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 11 Mathematics Q6: Simplify: 12/4 + 3", 
                new String[]{"6", "5", "7", "8"}, 0, timeLimit));
        
        return list;
    }
    
    private ArrayList<Question> loadGrade12Questions() {
        ArrayList<Question> list = new ArrayList<>();
        int timeLimit = 30;
       
        list.add(new Question("Literature", "Grade 12 Literature Q1: Which modern author wrote 'The Road'?", 
                new String[]{"Cormac McCarthy", "Ernest Hemingway", "F. Scott Fitzgerald", "George Orwell"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q2: What is a common element of dystopian literature?", 
                new String[]{"Oppressive societal control", "Romantic love", "Adventure", "Comedy"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q3: Define 'stream of consciousness'.", 
                new String[]{"A narrative mode", "A plot device", "A dialogue style", "A character archetype"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q4: Which work is a prime example of postmodern literature?", 
                new String[]{"Slaughterhouse-Five", "Moby Dick", "Great Expectations", "The Odyssey"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q5: What does 'alliteration' mean?", 
                new String[]{"Repetition of initial consonant sounds", "Repetition of vowel sounds", "Rhyme at the end of lines", "Exaggerated statements"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q6: Who is a prominent poet of the Beat Generation?", 
                new String[]{"Allen Ginsberg", "Robert Frost", "Walt Whitman", "Sylvia Plath"}, 0, timeLimit));
        list.add(new Question("Literature", "Grade 12 Literature Q7: What is the role of a narrator?", 
                new String[]{"To tell the story", "To illustrate", "To argue", "To define"}, 0, timeLimit));
        
        list.add(new Question("Biology", "Grade 12 Biology Q1: What is the function of the Golgi apparatus?", 
                new String[]{"Modifying and packaging proteins", "Protein synthesis", "Energy production", "Genetic storage"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 12 Biology Q2: What is the process by which organisms maintain a stable internal environment?", 
                new String[]{"Homeostasis", "Metabolism", "Adaptation", "Evolution"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 12 Biology Q3: Which process results in genetic variation?", 
                new String[]{"Meiosis", "Mitosis", "Binary fission", "Budding"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 12 Biology Q4: What type of macromolecule are enzymes primarily made of?", 
                new String[]{"Proteins", "Lipids", "Carbohydrates", "Nucleic acids"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 12 Biology Q5: What is the role of chloroplasts?", 
                new String[]{"Photosynthesis", "Respiration", "Digestion", "Replication"}, 0, timeLimit));
        list.add(new Question("Biology", "Grade 12 Biology Q6: Which system defends the body against infection?", 
                new String[]{"Immune system", "Circulatory system", "Nervous system", "Endocrine system"}, 0, timeLimit));
        
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q1: What is Avogadro's number?", 
                new String[]{"6.022 x 10^23", "3.14", "9.81", "1.602 x 10^-19"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q2: Which acid is found in car batteries?", 
                new String[]{"Sulfuric acid", "Hydrochloric acid", "Acetic acid", "Citric acid"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q3: What is an isotope?", 
                new String[]{"Atoms of the same element with different masses", "Ionic compound", "Mixture", "Polymer"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q4: What is the chemical formula for methane?", 
                new String[]{"CH4", "CO2", "C2H6", "C3H8"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q5: Which type of reaction absorbs energy?", 
                new String[]{"Endothermic", "Exothermic", "Catalytic", "Combustion"}, 0, timeLimit));
        list.add(new Question("Chemistry", "Grade 12 Chemistry Q6: What does the law of conservation of mass state?", 
                new String[]{"Mass is neither created nor destroyed", "Mass increases", "Mass decreases", "Mass is irrelevant"}, 0, timeLimit));
        
    
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q1: Solve for x: 3x - 7 = 11.", 
                new String[]{"6", "5", "7", "8"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q2: What is the derivative of e^x?", 
                new String[]{"e^x", "x", "1", "0"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q3: Calculate the integral of 2x dx.", 
                new String[]{"x^2 + C", "2x + C", "x + C", "2 + C"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q4: What is the value of π (approx.)?", 
                new String[]{"3.14", "2.17", "3.00", "3.41"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q5: Simplify: (2x)^3.", 
                new String[]{"8x^3", "6x^3", "2x^3", "x^3"}, 0, timeLimit));
        list.add(new Question("Mathematics", "Grade 12 Mathematics Q6: What is the solution to the quadratic equation x² - 5x + 6 = 0?", 
                new String[]{"x = 2 or 3", "x = 1 or 6", "x = -2 or -3", "x = 0"}, 0, timeLimit));
        
        return list;
    }
   
    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            subjectLabel.setText("Subject: " + q.subject);
            questionLabel.setText("<html><body style='width:800px;'>" + q.questionText + "</body></html>");
            for (int i = 0; i < optionButtons.length; i++) {
                optionButtons[i].setText(q.options[i]);
                optionButtons[i].setSelected(false);
            }
            optionsGroup.clearSelection();
            
            remainingTime = q.timeLimit;
            timerLabel.setText("Time: " + remainingTime + " sec");
            
            if (timer != null) {
                timer.stop();
            }
            timer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    remainingTime--;
                    timerLabel.setText("Time: " + remainingTime + " sec");
                    if (remainingTime <= 0) {
                        timer.stop();
                        nextQuestion();
                    }
                }
            });
            timer.start();
        } else {
            showResult();
        }
    }
    
    private void checkAnswer() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            int selectedIndex = -1;
            for (int i = 0; i < optionButtons.length; i++) {
                if (optionButtons[i].isSelected()) {
                    selectedIndex = i;
                    break;
                }
            }
            if (selectedIndex == q.correctAnswer) {
                score++;
            }
        }
    }
    
    private void nextQuestion() {
        if (timer != null) {
            timer.stop();
        }
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion();
        } else {
            showResult();
        }
    }
    
    private void showResult() {
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(240, 248, 255));
        JLabel resultLabel = new JLabel("Your Score: " + score + " / " + questions.size(), SwingConstants.CENTER);
        resultLabel.setFont(new Font("Verdana", Font.BOLD, 36));
        resultLabel.setForeground(new Color(25, 25, 112));
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        
        JButton restartButton = new JButton("Restart Quiz");
        restartButton.setFont(new Font("Verdana", Font.BOLD, 26));
        restartButton.setBackground(new Color(65, 105, 225));
        restartButton.setForeground(Color.WHITE);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "startPanel");
            }
        });
        resultPanel.add(restartButton, BorderLayout.SOUTH);
        
        mainPanel.add(resultPanel, "resultPanel");
        cardLayout.show(mainPanel, "resultPanel");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new QuizApp().setVisible(true);
            }
        });
    }
}
