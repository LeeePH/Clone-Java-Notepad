import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

public class Window extends JFrame implements ActionListener {
    public Backend function;
    public JTextArea textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    // JMENU MAIN BAR
    JMenu menuFile, menuEdit, menuFormat, menuView, menuHelp;
    // JMENU FONT BAR
    JMenu formatFontSize, formatFontFamily;
    // JMENU ITEMS
    JMenuItem fileNew, fileNewWindow, fileOpen, fileSave, fileSaveAs, fileExit,
    editUndo, editCut, editCopy, editPaste, editDelete, editSelectAll,
    formatWordWrap,
    viewZoom, viewStatusBar,
    helpAboutNotepad;

    // FONT SIZE & FAMILY SUB ATTRIB
    JMenuItem fontArial, fontDialog, fontTimesNewRoman, fontComic, font12, font20, font29, font35, font40;

    // NOTEPAD THEMES
    JMenu viewTheme;
    JMenuItem darkTheme, lightTheme, blueTheme;

    boolean wordWrapOn = false;

    UndoManager undoManager;

    public Window() {
        // INITIALIZE WINDOW
        super("Notepad | Lee");
        function = new Backend(this);

        setSize(StylingStructure.notepadSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                function.exitConfirmation();
            }
        });

        undoManager = new UndoManager();

        displayGUI();

    }

    public void displayGUI() {
        // DECLARE TEXT AREA
        textArea = new JTextArea();
        textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
        textArea.addKeyListener(function);
        textArea.setComponentPopupMenu(createPopupMenu());
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    showPopupMenu(e.getX(), e.getY());
                }
            }
        });

        function.selectedFont = "Arial";
        function.displayFont(20);
        function.themeChanger("Dark");

        // DECLARE SCROLL PANE FOR TEXT AREA
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // ADD TEXTAREA SCROLL PANE
        add(scrollPane);

        displayMenuBar();
    }

    public JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        // ADD POPUP MENU WHEN RIGHT CLICKED
        JMenuItem undoItem = new JMenuItem("Undo");

        JMenuItem redoItem = new JMenuItem("Redo");

        JMenuItem cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut");

        JMenuItem copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy");

        JMenuItem pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste");

        popupMenu.add(undoItem);
        popupMenu.add(redoItem);
        popupMenu.addSeparator();
        popupMenu.add(cutItem);
        popupMenu.add(copyItem);
        popupMenu.add(pasteItem);

        return popupMenu;
    }

    private void showPopupMenu(int x, int y) {
        JPopupMenu popupMenu = createPopupMenu();
        popupMenu.show(textArea, x, y);
    }

    public void displayMenuBar() {
        // DECLARE JMENU BAR
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // DECLARE MENU FILE
        menuFile = new JMenu();
        menuFile.setText("File");
        menuBar.add(menuFile);

        // DECLARE MENU EDIT
        menuEdit = new JMenu();
        menuEdit.setText("Edit");
        menuBar.add(menuEdit);

        // DECLARE MENU FORMAT
        menuFormat = new JMenu();
        menuFormat.setText("Format");
        menuBar.add(menuFormat);

        // DECLARE MENU VIEW
        menuView = new JMenu();
        menuView.setText("View");
        menuBar.add(menuView);

        // DECLARE MENU HELP
        menuHelp = new JMenu();
        menuHelp.setText("Help");
        menuBar.add(menuHelp);


        // DECLARE MENU ITEMS
        // FILE MENU
        // NEW ITEM
        fileNew = new JMenuItem();
        fileNew.setText("New (Ctrl + N)");
        fileNew.addActionListener(this);
        fileNew.setActionCommand("New");
        menuFile.add(fileNew);

        // NEW WINDOW ITEM
        fileNewWindow = new JMenuItem();
        fileNewWindow.setText("New Window (Ctrl + Shift + N)");
        fileNewWindow.addActionListener(this);
        fileNewWindow.setActionCommand("New Window");
        menuFile.add(fileNewWindow);

        // OPEN WINDOW ITEM
        fileOpen = new JMenuItem();
        fileOpen.setText("Open.. (Ctrl + O)");
        fileOpen.addActionListener(this);
        fileOpen.setActionCommand("Open");
        menuFile.add(fileOpen);

        // SAVE WINDOW ITEM
        fileSave = new JMenuItem();
        fileSave.setText("Save (Ctrl + S)");
        fileSave.addActionListener(this);
        fileSave.setActionCommand("Save");
        menuFile.add(fileSave);

        // SAVE AS WINDOW ITEM
        fileSaveAs = new JMenuItem();
        fileSaveAs.setText("Save as (Ctrl + Shift + S)");
        fileSaveAs.addActionListener(this);
        fileSaveAs.setActionCommand("Save as");
        menuFile.add(fileSaveAs);

        // EXIT WINDOW ITEM
        fileExit = new JMenuItem();
        fileExit.setText("Exit");
        fileExit.addActionListener(this);
        fileExit.setActionCommand("Exit");
        fileExit.setPreferredSize(new Dimension(150, fileExit.getPreferredSize().height));
        menuFile.add(fileExit);


        // EDIT MENU
        // UNDO ITEM
        editUndo = new JMenuItem();
        editUndo.setText("Undo     (Ctrl + Z)");
        editUndo.addActionListener(this);
        editUndo.setActionCommand("Undo");
        editUndo.setPreferredSize(new Dimension(150, editUndo.getPreferredSize().height));
        menuEdit.add(editUndo);

        // CUT ITEM
        editCut = new JMenuItem(new DefaultEditorKit.CutAction());
        editCut.setText("Cut     (Ctrl + X)");
        menuEdit.add(editCut);

        // COPY ITEM
        editCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
        editCopy.setText("Copy     (Ctrl + C)");
        menuEdit.add(editCopy);

        // PASTE ITEM
        editPaste = new JMenuItem(new DefaultEditorKit.PasteAction());
        editPaste.setText("Paste     (Ctrl + V)");
        menuEdit.add(editPaste);

        // DELETE ITEM
        editDelete = new JMenuItem();
        editDelete.setText("Delete");
        menuEdit.add(editDelete);

        // SELECT ALL ITEM
        editSelectAll = new JMenuItem();
        editSelectAll.setText("Select All     (Ctrl + A)");
        menuEdit.add(editSelectAll);


        // FORMAT MENU
        // WORD WRAP ITEM
        formatWordWrap = new JMenuItem();
        formatWordWrap.setText("Word Wrap: OFF");
        formatWordWrap.addActionListener(this);
        formatWordWrap.setActionCommand("Word Wrap");
        menuFormat.add(formatWordWrap);

        // FONT SIZE
        formatFontSize = new JMenu();
        formatFontSize.setText("Font Size:");
        menuFormat.add(formatFontSize);

        // FONT FAMILY
        formatFontFamily = new JMenu();
        formatFontFamily.setText("Font Family:");
        menuFormat.add(formatFontFamily);

        fontAttributes();

        // VIEW MENU
        // ZOOM ITEM
        viewZoom = new JMenuItem();
        viewZoom.setText("Zoom..");
        menuView.add(viewZoom);

        // STATUS BAR ITEM
        viewStatusBar = new JMenuItem();
        viewStatusBar.setText("Status Bar");
        menuView.add(viewStatusBar);

        // NOTEPAD THEME ITEM
        viewTheme = new JMenu();
        viewTheme.setText("Themes:");
        viewTheme.setPreferredSize(new Dimension(150, viewTheme.getPreferredSize().height));
        menuView.add(viewTheme);

        themeAttributes();


        // HELP MENU
        // ABOUT NOTEPAD
        helpAboutNotepad = new JMenuItem();
        helpAboutNotepad.setText("About Notepad | Lee");
        helpAboutNotepad.addActionListener(this);
        helpAboutNotepad.setActionCommand("About");
        menuHelp.add(helpAboutNotepad);
    }

    public void fontAttributes() {
        // FONT SIZES
        // FONT 12
        font12 = new JMenuItem();
        font12.setText("12");
        font12.addActionListener(this);
        font12.setActionCommand("Font 12");
        formatFontSize.add(font12);

        // FONT 20
        font20 = new JMenuItem();
        font20.setText("20");
        font20.addActionListener(this);
        font20.setActionCommand("Font 20");
        formatFontSize.add(font20);

        // FONT 29
        font29 = new JMenuItem();
        font29.setText("29");
        font29.addActionListener(this);
        font29.setActionCommand("Font 29");
        formatFontSize.add(font29);

        // FONT 35
        font35 = new JMenuItem();
        font35.setText("35");
        font35.addActionListener(this);
        font35.setActionCommand("Font 35");
        formatFontSize.add(font35);

        // FONT 40
        font40 = new JMenuItem();
        font40.setText("40");
        font40.addActionListener(this);
        font40.setActionCommand("Font 40");
        formatFontSize.add(font40);

        // FONT FAMILIES
        // FONT ARIAL
        fontArial = new JMenuItem();
        fontArial.setText("Arial");
        fontArial.addActionListener(this);
        fontArial.setActionCommand("Arial");
        formatFontFamily.add(fontArial);

        // FONT DIALOG
        fontDialog = new JMenuItem();
        fontDialog.setText("Dialog");
        fontDialog.addActionListener(this);
        fontDialog.setActionCommand("Dialog");
        formatFontFamily.add(fontDialog);

        // FONT COMIC
        fontComic = new JMenuItem();
        fontComic.setText("Comic Sans Strip");
        fontComic.addActionListener(this);
        fontComic.setActionCommand("Comic");
        formatFontFamily.add(fontComic);

        // FONT TIMES NEW ROMAN
        fontTimesNewRoman = new JMenuItem();
        fontTimesNewRoman.setText("Times New Roman");
        fontTimesNewRoman.addActionListener(this);
        fontTimesNewRoman.setActionCommand("Times New Roman");
        formatFontFamily.add(fontTimesNewRoman);
    }

    public void themeAttributes() {
        // DARK THEME
        darkTheme = new JMenuItem();
        darkTheme.setText("Dark Theme");
        darkTheme.addActionListener(this);
        darkTheme.setActionCommand("Dark");
        viewTheme.add(darkTheme);

        // LIGHT THEME
        lightTheme = new JMenuItem();
        lightTheme.setText("Light Theme");
        lightTheme.addActionListener(this);
        lightTheme.setActionCommand("Light");
        viewTheme.add(lightTheme);

        // BLUE THEME
        blueTheme = new JMenuItem();
        blueTheme.setText("Blue Theme");
        blueTheme.addActionListener(this);
        blueTheme.setActionCommand("Blue");
        viewTheme.add(blueTheme);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch(command) {
            // FILE METHOD
            case "New":
                function.newFileMethod();
                break;
            case "New Window":
                function.newWindowMethod();
                break;
            case "Open":
                function.openMethod();
                break;
            case "Save":
                function.saveFileMethod();
                break;
            case "Save as":
                function.saveAsFileMethod();
                break;
            case "Exit":
                function.exitConfirmation();
                break;

            // EDIT METHOD
            case "Undo":
                function.undo();
                break;

            // FORMAT METHOD
            case "Word Wrap":
                function.wordWrapMethod();
                break;

            // FONT SIZES
            case "Font 12":
                function.displayFont(12);
                break;
            case "Font 20":
                function.displayFont(20);
                break;
            case "Font 29":
                function.displayFont(29);
                break;
            case "Font 35":
                function.displayFont(35);
                break;
            case "Font 40":
                function.displayFont(40);
                break;

            // FONT FAMILIES
            case "Arial":
                function.setFont(command);
                break;
            case "Dialog":
                function.setFont(command);
                break;
            case "Comic":
                function.setFont(command);
                break;
            case "Times New Roman":
                function.setFont(command);
                break;

            // VIEW METHOD
            // NOTEPAD THEME
            case "Light":
                function.themeChanger(command);
                break;
            case "Dark":
                function.themeChanger(command);
                break;
            case "Blue":
                function.themeChanger(command);
                break;


            // ABOUT NOTEPAD
            case "About":
                function.displayAboutNootepad();
                break;
        }
    }
}
