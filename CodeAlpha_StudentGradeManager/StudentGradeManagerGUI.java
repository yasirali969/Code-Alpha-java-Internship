import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentGradeManagerGUI extends JFrame {
    // GUI Components
    private JTextField nameField;
    private JTextField gradeField;
    private JButton addButton;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    
    private JLabel averageLabel;
    private JLabel highestLabel;
    private JLabel lowestLabel;

    // Lists to store data dynamically
    private ArrayList<String> studentNames;
    private ArrayList<Double> studentGrades;

    // Custom UI Colors
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245); // Light Gray-Blue
    private final Color CARD_COLOR = new Color(255, 255, 255);       // Pure White
    private final Color BUTTON_COLOR = new Color(16, 185, 129);      // Vibrant Emerald Green
    private final Color BUTTON_TEXT_COLOR = Color.WHITE;
    
    // Stats Colors
    private final Color BLUE_TEXT = new Color(29, 78, 216);
    private final Color GREEN_TEXT = new Color(21, 128, 61);
    private final Color RED_TEXT = new Color(185, 28, 28);

    public StudentGradeManagerGUI() {
        // Initialize data lists
        studentNames = new ArrayList<>();
        studentGrades = new ArrayList<>();

        // Setup Frame Window
        setTitle("🎯 Student Grade Manager");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(15, 15));

        // 1. Input Panel (Top)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), " Add Student Record "),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        
        JLabel nameLabel = new JLabel("👤 Student Name:");
        nameLabel.setFont(labelFont);
        inputPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(nameField);

        JLabel gradeLabel = new JLabel("📊 Grade / Marks:");
        gradeLabel.setFont(labelFont);
        inputPanel.add(gradeLabel);
        
        gradeField = new JTextField();
        gradeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        inputPanel.add(gradeField);

        inputPanel.add(new JLabel("")); // Empty placeholder
        
        // Styled Action Button
        addButton = new JButton("Add Student Record");
        addButton.setBackground(BUTTON_COLOR);
        addButton.setForeground(BUTTON_TEXT_COLOR);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        inputPanel.add(addButton);

        // 2. Report Table Panel (Center)
        String[] columnNames = {"Student Name", "Grade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTable.setRowHeight(24);
        reportTable.setGridColor(new Color(230, 230, 230));
        
        // Customizing Table Header
        JTableHeader header = reportTable.getTableHeader();
        header.setBackground(new Color(55, 65, 81)); // Dark Slate Header
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.getViewport().setBackground(CARD_COLOR);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), " Student Report "),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 3. Statistics Panel (Bottom)
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        statsPanel.setBackground(CARD_COLOR);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), " Performance Summary "),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        averageLabel = new JLabel(" Average Score: N/A");
        highestLabel = new JLabel(" Highest Score: N/A");
        lowestLabel = new JLabel(" Lowest Score: N/A");

        // Apply specific color matching to matching metrics
        Font statsFont = new Font("Segoe UI", Font.BOLD, 14);
        averageLabel.setFont(statsFont);
        averageLabel.setForeground(BLUE_TEXT);
        
        highestLabel.setFont(statsFont);
        highestLabel.setForeground(GREEN_TEXT);
        
        lowestLabel.setFont(statsFont);
        lowestLabel.setForeground(RED_TEXT);

        statsPanel.add(averageLabel);
        statsPanel.add(highestLabel);
        statsPanel.add(lowestLabel);

        // Add padding around the main frame layouts
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        mainContainer.add(inputPanel, BorderLayout.NORTH);
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        mainContainer.add(statsPanel, BorderLayout.SOUTH);
        
        add(mainContainer);

        // Action Listener for the Add Button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentData();
            }
        });
    }

    private void addStudentData() {
        String name = nameField.getText().trim();
        String gradeStr = gradeField.getText().trim();

        if (name.isEmpty() || gradeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Name and Grade fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double grade = Double.parseDouble(gradeStr);
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Grade should typically be between 0 and 100.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

            studentNames.add(name);
            studentGrades.add(grade);

            tableModel.addRow(new Object[]{name, String.format("%.2f", grade)});

            nameField.setText("");
            gradeField.setText("");
            nameField.requestFocus();

            calculateStatistics();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for the Grade.", "Invalid Grade", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateStatistics() {
        if (studentGrades.isEmpty()) return;

        double sum = 0;
        double max = studentGrades.get(0);
        double min = studentGrades.get(0);

        for (double grade : studentGrades) {
            sum += grade;
            if (grade > max) max = grade;
            if (grade < min) min = grade;
        }

        double average = sum / studentGrades.size();

        averageLabel.setText(String.format(" Average Score: %.2f", average));
        highestLabel.setText(String.format(" Highest Score: %.2f", max));
        lowestLabel.setText(String.format(" Lowest Score: %.2f", min));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentGradeManagerGUI().setVisible(true);
            }
        });
    }
}