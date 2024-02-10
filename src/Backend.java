import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Backend implements KeyListener {
    public Window window;

    String fileName, filePath;

    // FONTS
    Font arial, dialog, comic, timesNewRoman;
    String selectedFont;

    public Backend(Window window) {
        this.window = window;
    }

    // FILE METHODS
    public void exitConfirmation() {
        String text = window.textArea.getText();

        if(text.isEmpty()) {
            System.exit(0);
        } else {
            int result = JOptionPane.showConfirmDialog(null, "Do you want to save before exiting the program?", "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if(result == JOptionPane.YES_OPTION) {
                saveAsFileMethod();
            } else {
                System.exit(0);
            }
        }
    }

    public void newFileMethod() {
        String text = window.textArea.getText();

        if(!text.isEmpty()) {
            int confirmation = JOptionPane.showConfirmDialog(window, "Do you want to save before initializing New?", "New Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if(confirmation == JOptionPane.YES_OPTION) {
                saveAsFileMethod();
            } else {
                window.textArea.setText("");
                window.setTitle("New");
            }
        } else {
            window.textArea.setText("");
            window.setTitle("New");
        }

    }

    public void newWindowMethod() {
        String text = window.textArea.getText();

        if(!text.isEmpty()) {
            int confirmation = JOptionPane.showConfirmDialog(window,"Do you want to save your file and open a new window?",
                    "New Window Confirmation", JOptionPane.YES_NO_OPTION);

            if(confirmation == JOptionPane.YES_OPTION) {
                //
            } else {
                new Window().setVisible(true);
            }
        } else {
            new Window().setVisible(true);
        }
    }

    public void openMethod() {
        FileDialog fileDialog = new FileDialog(window, "Open", FileDialog.LOAD);
        fileDialog.setVisible(true);

        fileName = fileDialog.getFile();
        filePath = fileDialog.getDirectory();

        if(fileName != null && filePath != null) {
            window.setTitle(fileName);

            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath + fileName));
                window.textArea.setText("");

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    window.textArea.append(line + "\n");
                }

                bufferedReader.close();
            } catch(Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(window, "Error opening file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void saveFileMethod() {
        if(fileName == null) {
            saveAsFileMethod();
        } else {
            try {
                FileWriter fileWriter = new FileWriter(filePath + fileName);
                fileWriter.write(window.textArea.getText());
                window.setTitle(fileName);

                fileWriter.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsFileMethod() {
        FileDialog fileDialog = new FileDialog(window, "Save As", FileDialog.SAVE);
        fileDialog.setVisible(true);

        if(fileDialog.getFile() != null) {
            fileName = fileDialog.getFile();
            filePath = fileDialog.getDirectory();
            window.setTitle(fileName);
        }

        try {
            FileWriter fileWriter = new FileWriter(filePath + fileName);
            fileWriter.write(window.textArea.getText());

            fileWriter.close();
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(window, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EDIT METHODS
    // UNDO
    public void undo() {
        window.undoManager.undo();
    }

    // FORMAT METHODS
    public void wordWrapMethod() {
        if(!window.wordWrapOn) {
            window.wordWrapOn = true;
            window.textArea.setLineWrap(true);
            window.textArea.setWrapStyleWord(true);
            window.formatWordWrap.setText("Word Wrap: ON");
        } else {
            window.wordWrapOn = false;
            window.textArea.setLineWrap(false);
            window.textArea.setWrapStyleWord(false);
            window.formatWordWrap.setText("Word Wrap: OFF");
        }
    }

    public void displayFont(int fontSize) {
        arial = new Font("Arial", Font.PLAIN, fontSize);
        dialog = new Font("Dialog", Font.PLAIN, fontSize);
        comic = new Font("Comic Sans MS", Font.PLAIN, fontSize);
        timesNewRoman = new Font("Times New Roman", Font.PLAIN, fontSize);

        setFont(selectedFont);
    }

    public void setFont(String font) {
        selectedFont = font;

        switch(selectedFont) {
            case "Arial":
                window.textArea.setFont(arial);
                break;
            case "Dialog":
                window.textArea.setFont(dialog);
                break;
            case "Comic":
                window.textArea.setFont(comic);
                break;
            case "Times New Roman":
                window.textArea.setFont(timesNewRoman);
                break;
        }
    }

    // VIEW METHODS
    // NOTEPAD THEME
    public void themeChanger(String color) {
        switch(color) {
            case "Light":
                window.getContentPane().setBackground(StylingStructure.lightBackground);
                window.textArea.setBackground(StylingStructure.lightBackground);
                window.textArea.setForeground(StylingStructure.lightText);
                break;
            case "Dark":
                window.getContentPane().setBackground(StylingStructure.darkBackground);
                window.textArea.setBackground(StylingStructure.darkBackground);
                window.textArea.setForeground(StylingStructure.darkText);
                window.textArea.setCaretColor(Color.WHITE);
                break;
            case "Blue":
                window.getContentPane().setBackground(StylingStructure.blueBackground);
                window.textArea.setBackground(StylingStructure.blueBackground);
                window.textArea.setForeground(StylingStructure.blueText);
                window.textArea.setCaretColor(Color.BLACK);
                break;
        }
    }

    public void displayAboutNootepad() {
        JDialog aboutDialog = new JDialog();

        aboutDialog.setTitle("About Notepad | Lee");
        aboutDialog.setSize(StylingStructure.aboutDialog);
        aboutDialog.getContentPane().setBackground(StylingStructure.darkBackground);
        aboutDialog.setBackground(StylingStructure.darkBackground);
        aboutDialog.setLocationRelativeTo(window);

        JLabel text = new JLabel();
        text.setText("<html>A clone notepad made in vanilla Java - Lee</html>");
        text.setForeground(StylingStructure.darkText);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setFont(StylingStructure.defaultFont);
        aboutDialog.add(text);

        aboutDialog.setVisible(true);

    }


    // KEYBOARD CONFIGS

    @Override
    public void keyTyped(KeyEvent e) {
        // NOT NEEDED
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // NEW WINDOW SHORTCUT
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N) {
            newFileMethod();
        }

        // NEW WINDOW SHORTCUT
        if(e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_N) {
            newWindowMethod();
        }

        // OPEN WINDOW SHORTCUT
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
            openMethod();
        }

        // SAVE WINDOW SHORTCUT
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            saveFileMethod();
        }

        // SAVE AS WINDOW SHORTCUT
        if(e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_S) {
            saveAsFileMethod();
        }

        // UNDO SHORTCUT
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            undo();
        }

        //
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // NOT NEEDED
    }
}
