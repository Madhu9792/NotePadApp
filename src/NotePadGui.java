//package ApplicationWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class NotePadGui {

    private JFrame frame;
    private JTextArea textArea;
    private File currentFile = null;
    private boolean darkMode = true;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                NotePadGui window = new NotePadGui();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public NotePadGui() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Simple Notepad");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Text Area
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        enableDarkMode(); // default dark mode
        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // ---------------- File Menu ----------------
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(createMenuItem("New Tab", e -> {
            textArea.setText("");
            currentFile = null;
            frame.setTitle("Simple Notepad - New Tab");
        }));
        fileMenu.add(createMenuItem("Open", e -> openFile()));
        fileMenu.add(createMenuItem("Save", e -> saveFile()));
        fileMenu.add(createMenuItem("Save As", e -> saveFileAs()));
        fileMenu.add(createMenuItem("Exit", e -> System.exit(0)));

        // ---------------- Edit Menu ----------------
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        editMenu.add(createMenuItem("Paste", e -> textArea.paste()));
        editMenu.add(createMenuItem("Cut", e -> textArea.cut()));
        editMenu.add(createMenuItem("Copy", e -> textArea.copy()));
        editMenu.add(createMenuItem("Select All", e -> textArea.selectAll()));
        editMenu.add(createMenuItem("Clear All", e -> textArea.setText("")));

        // ---------------- View Menu ----------------
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        // Zoom Submenu (UI only)
        JMenu zoomMenu = new JMenu("Zoom");
        viewMenu.add(zoomMenu);
        zoomMenu.add(new JMenuItem("Zoom In"));
        zoomMenu.add(new JMenuItem("Zoom Out"));
        zoomMenu.add(new JMenuItem("Restore Default Zoom"));

        // Word Wrap (UI only)
        JCheckBoxMenuItem wordWrapItem = new JCheckBoxMenuItem("Word Wrap");
        viewMenu.add(wordWrapItem);

        // Status Bar (UI only)
        JCheckBoxMenuItem statusBarItem = new JCheckBoxMenuItem("Status Bar");
        viewMenu.add(statusBarItem);

        // Dark Mode Toggle
        JCheckBoxMenuItem darkModeToggle = new JCheckBoxMenuItem("Dark Mode");
        darkModeToggle.setSelected(true);
        darkModeToggle.addActionListener(e -> {
            darkMode = darkModeToggle.isSelected();
            if (darkMode) enableDarkMode();
            else enableLightMode();
        });
        viewMenu.add(darkModeToggle);
    }

    // ---------------- Helper: Create Menu Item ----------------
    private JMenuItem createMenuItem(String name, ActionListener action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }

    // ---------------- Open File ----------------
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(currentFile));
                textArea.read(reader, null);
                reader.close();
                frame.setTitle("Simple Notepad - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error: Cannot open file");
            }
        }
    }

    // ---------------- Save File ----------------
    private void saveFile() {
        if (currentFile == null) saveFileAs();
        else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                textArea.write(writer);
                writer.close();
                frame.setTitle("Simple Notepad - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error: Cannot save file");
            }
        }
    }

    // ---------------- Save File As ----------------
    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = fileChooser.getSelectedFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                textArea.write(writer);
                writer.close();
                frame.setTitle("Simple Notepad - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error: Cannot save file");
            }
        }
    }

    // ---------------- Dark / Light Mode ----------------
    private void enableDarkMode() {
        frame.getContentPane().setBackground(Color.BLACK);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
    }

    private void enableLightMode() {
        frame.getContentPane().setBackground(Color.WHITE);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setCaretColor(Color.BLACK);
    }
}
