import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Course {
    private String courseCode, title, description, schedule;
    private int capacity;
    private List<String> registeredStudentIDs;
    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.registeredStudentIDs = new ArrayList<>();
    }
    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public String getSchedule() { return schedule; }
    public int getRegisteredCount() { return registeredStudentIDs.size(); }
    public int getAvailableSlots() { return capacity - registeredStudentIDs.size(); }
    public boolean registerStudent(String studentID) {
        if(getAvailableSlots() > 0 && !registeredStudentIDs.contains(studentID)) {
            registeredStudentIDs.add(studentID);
            return true;
        }
        return false;
    }
    public boolean removeStudent(String studentID) {
        return registeredStudentIDs.remove(studentID);
    }
}

class Student {
    private String studentID, name;
    private List<Course> registeredCourses;
    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }
    public String getStudentID() { return studentID; }
    public String getName() { return name; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }
    public boolean registerCourse(Course course) {
        if (!registeredCourses.contains(course)) {
            registeredCourses.add(course);
            return true;
        }
        return false;
    }
    public boolean dropCourse(Course course) {
        return registeredCourses.remove(course);
    }
}

public class CourseRegistrationSystem extends JFrame {
    private static List<Course> courseList = new ArrayList<>();
    private static Map<String, Student> studentMap = new HashMap<>();
    
    private JTable courseTable;
    private DefaultTableModel courseTableModel;
    
    private JPanel studentPanel;
    private CardLayout studentCardLayout;
    private JPanel loginPanel;
    private JPanel registrationPanel;
    
    private JTextField studentIDField;
    private JTextField studentNameField;
    private JButton loginButton;
    
    private JComboBox<String> courseComboBox;
    private JButton registerCourseButton;
    private JTable studentCourseTable;
    private DefaultTableModel studentCourseTableModel;
    private JButton dropCourseButton;
    
    private Student currentStudent;
    
    public CourseRegistrationSystem() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) { }
        setTitle("Course Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        initializeCourses();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Course Catalog", createCourseCatalogPanel());
        tabbedPane.add("Student Registration", createStudentPanel());
        add(tabbedPane);
    }
    
    private void initializeCourses() {
        courseList.add(new Course("CS101", "Introduction to Computer Science", "Fundamentals of computer science concepts and programming basics.", 30, "Mon & Wed 10:00-11:30"));
        courseList.add(new Course("MAT101", "Calculus I", "Differential and integral calculus with real-world applications.", 40, "Tue & Thu 09:00-10:30"));
        courseList.add(new Course("PHY101", "Physics I", "Mechanics, motion, energy, and introductory thermodynamics.", 35, "Mon, Wed & Fri 08:00-09:00"));
        courseList.add(new Course("ENG101", "English Literature", "Exploration of classical and modern literature works.", 25, "Tue & Thu 11:00-12:30"));
        courseList.add(new Course("HIS101", "World History", "A comprehensive overview of world history events and cultures.", 30, "Fri 13:00-16:00"));
    }
    
    private JPanel createCourseCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        String[] columns = {"Course Code", "Title", "Capacity", "Schedule", "Available Slots"};
        courseTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        courseTable = new JTable(courseTableModel);
        courseTable.setFillsViewportHeight(true);
        courseTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int row = courseTable.getSelectedRow();
                    if(row != -1) {
                        String code = (String) courseTableModel.getValueAt(row, 0);
                        Course course = findCourseByCode(code);
                        if(course != null) {
                            String details = "Course Code: " + course.getCourseCode() +
                                             "\nTitle: " + course.getTitle() +
                                             "\nDescription: " + course.getDescription() +
                                             "\nCapacity: " + course.getCapacity() +
                                             "\nRegistered: " + course.getRegisteredCount() +
                                             "\nAvailable Slots: " + course.getAvailableSlots() +
                                             "\nSchedule: " + course.getSchedule();
                            JOptionPane.showMessageDialog(panel, details, "Course Details", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
        refreshCourseTable();
        JScrollPane scrollPane = new JScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshCourseTable());
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }
    
    private Course findCourseByCode(String code) {
        for(Course course : courseList) {
            if(course.getCourseCode().equals(code)) return course;
        }
        return null;
    }
    
    private JPanel createStudentPanel() {
        studentPanel = new JPanel();
        studentCardLayout = new CardLayout();
        studentPanel.setLayout(studentCardLayout);
        loginPanel = createLoginPanel();
        registrationPanel = createRegistrationPanel();
        studentPanel.add(loginPanel, "login");
        studentPanel.add(registrationPanel, "registration");
        studentCardLayout.show(studentPanel, "login");
        return studentPanel;
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        studentIDField = new JTextField(15);
        panel.add(studentIDField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        studentNameField = new JTextField(15);
        panel.add(studentNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginButton = new JButton("Login / Register");
        panel.add(loginButton, gbc);
        loginButton.addActionListener(e -> handleLogin());
        return panel;
    }
    
    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        courseComboBox = new JComboBox<>();
        topPanel.add(courseComboBox);
        registerCourseButton = new JButton("Register Course");
        topPanel.add(registerCourseButton);
        registerCourseButton.addActionListener(e -> handleCourseRegistration());
        panel.add(topPanel, BorderLayout.NORTH);
        String[] columns = {"Course Code", "Title", "Schedule"};
        studentCourseTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentCourseTable = new JTable(studentCourseTableModel);
        studentCourseTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(studentCourseTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("My Courses"));
        panel.add(tableScroll, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dropCourseButton = new JButton("Drop Selected Course");
        bottomPanel.add(dropCourseButton);
        dropCourseButton.addActionListener(e -> handleCourseDrop());
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    private void refreshCourseTable() {
        courseTableModel.setRowCount(0);
        for(Course course : courseList) {
            Object[] row = {course.getCourseCode(), course.getTitle(), course.getCapacity(), course.getSchedule(), course.getAvailableSlots()};
            courseTableModel.addRow(row);
        }
    }
    
    private void updateCourseComboBox() {
        courseComboBox.removeAllItems();
        for(Course course : courseList) {
            if(course.getAvailableSlots() > 0 && !currentStudent.getRegisteredCourses().contains(course))
                courseComboBox.addItem(course.getCourseCode() + " - " + course.getTitle());
        }
    }
    
    private void refreshStudentCourseTable() {
        studentCourseTableModel.setRowCount(0);
        if(currentStudent != null) {
            for(Course course : currentStudent.getRegisteredCourses()) {
                Object[] row = {course.getCourseCode(), course.getTitle(), course.getSchedule()};
                studentCourseTableModel.addRow(row);
            }
        }
    }
    
    private void handleLogin() {
        String id = studentIDField.getText().trim();
        String name = studentNameField.getText().trim();
        if(id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both Student ID and Name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(studentMap.containsKey(id)) {
            currentStudent = studentMap.get(id);
            JOptionPane.showMessageDialog(this, "Welcome back, " + currentStudent.getName() + "!");
        } else {
            currentStudent = new Student(id, name);
            studentMap.put(id, currentStudent);
            JOptionPane.showMessageDialog(this, "Registration successful! Welcome, " + name + "!");
        }
        updateCourseComboBox();
        refreshStudentCourseTable();
        studentCardLayout.show(studentPanel, "registration");
    }
    
    private void handleCourseRegistration() {
        if(currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Login first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(courseComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No courses available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String selected = (String) courseComboBox.getSelectedItem();
        String code = selected.split(" - ")[0];
        Course selectedCourse = findCourseByCode(code);
        if(selectedCourse != null && selectedCourse.registerStudent(currentStudent.getStudentID())) {
            currentStudent.registerCourse(selectedCourse);
            JOptionPane.showMessageDialog(this, "Course registered!");
            refreshStudentCourseTable();
            updateCourseComboBox();
            refreshCourseTable();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Course full or already registered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCourseDrop() {
        if(currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Login first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int selectedRow = studentCourseTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to drop.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String code = (String) studentCourseTableModel.getValueAt(selectedRow, 0);
        Course courseToDrop = findCourseByCode(code);
        if(courseToDrop != null && courseToDrop.removeStudent(currentStudent.getStudentID())) {
            currentStudent.dropCourse(courseToDrop);
            JOptionPane.showMessageDialog(this, "Course dropped.");
            refreshStudentCourseTable();
            updateCourseComboBox();
            refreshCourseTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to drop course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CourseRegistrationSystem crs = new CourseRegistrationSystem();
            crs.setVisible(true);
        });
    }
}
