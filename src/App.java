import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class App extends JFrame {
    private JTextField startWordField;
    private JTextField endWordField;
    private JComboBox<String> algorithmSelector;
    private JTextArea resultArea;
    private JLabel nodesVisitedLabel;
    private JLabel executionTimeLabel;
    private Dictionary dictionary;

    public App() {
        super("Word Ladder Solver");
        initializeDictionary();
        initializeUI();
    }

    private void initializeDictionary() {
        try {
            dictionary = new Dictionary("../dict/dictionary.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error membaca dictionary : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        add(createInputPanel(), BorderLayout.NORTH);
        add(createResultPanel(), BorderLayout.CENTER);
        add(createStatsPanel(), BorderLayout.SOUTH);

        setSize(350, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        panel.add(createLabeledField("Start Word", startWordField = new JTextField(20)));
        panel.add(createLabeledField("End Word", endWordField = new JTextField(20)));
        panel.add(createLabeledField("Pilih Algoritma", algorithmSelector = new JComboBox<>(new String[] {
            "UCS - Uniform Cost Search",
            "Greedy BFS - Greedy Best First Search",
            "A* - A Star Search"
        })));
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton solveButton = new JButton("Solusi");
        JButton clearButton = new JButton("Reset");
        solveButton.addActionListener(this::solveWordLadder);
        clearButton.addActionListener(this::clearFields);
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
    
        panel.add(buttonPanel);
    
        return panel;
    }
    
    private JPanel createLabeledField(String labelText, Component field) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);
    
        if (field instanceof JTextField) {  
            ((JTextField) field).setMargin(new Insets(2, 3, 2, 10));
        }
        
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Path"));
        
        resultArea = new JTextArea(10, 25);
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(5, 3, 5, 10));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        return panel;
    }    

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 20, 5, 20);
    
        nodesVisitedLabel = new JLabel("Node dikunjungi : ");
        gbc.insets = new Insets(5, 8, 0, 20);
        panel.add(nodesVisitedLabel, gbc);
    
        executionTimeLabel = new JLabel("Waktu Eksekusi : ");
        gbc.insets = new Insets(10, 8, 10, 20); 
        panel.add(executionTimeLabel, gbc);
    
        return panel;
    }
    

    private void solveWordLadder(ActionEvent event) {
        String start = startWordField.getText();
        String end = endWordField.getText();
        int choice = algorithmSelector.getSelectedIndex() + 1;

        WordLadderSolver solver = null;
        switch (choice) {
            case 1:
                solver = new UCSSolver(dictionary);
                break;
            case 2:
                solver = new GreedyBFSSolver(dictionary);
                break;
            case 3:
                solver = new AStarSolver(dictionary);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Pemilihan algoritma tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        try {
            long startTime = System.nanoTime();
            List<String> path = solver.solve(start, end);
            long endTime = System.nanoTime();
            if (path.isEmpty()) {
                resultArea.setText("Tidak ada path yang ditemukan.");
            } else {
                resultArea.setText(String.join("\n", path));
                nodesVisitedLabel.setText("Node dikunjungi : " + solver.getNodesVisited());
                executionTimeLabel.setText("Waktu Eksekusi : " + (endTime - startTime) / 1_000_000.0 + " ms");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Solusi Word Ladder Error : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields(ActionEvent event) {
        startWordField.setText("");
        endWordField.setText("");
        resultArea.setText("");
        nodesVisitedLabel.setText("Node dikunjungi : ");
        executionTimeLabel.setText("Waktu Eksekusi : ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
