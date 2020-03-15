package gui;

import log.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private Locale locale = Locale.getDefault();
    private ResourceBundle bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
//       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int inset = 50;
//        setBounds(inset, inset,
//            screenSize.width  - inset*2,
//            screenSize.height - inset*2);
        setLocationRelativeTo(null);
        setContentPane(desktopPane);
        addWindow(createLogWindow());
        addWindow(createGameWindow());
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    protected LogWindow createLogWindow()
    {
        var logWindowTitle = bundle.getString("logWindowTitle");
        var startLogMessage = bundle.getString("startLogMessage");
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), logWindowTitle);
        logWindow.setLocation(10,10);
        Logger.debug(startLogMessage);
        return logWindow;
    }

    protected GameWindow createGameWindow()
    {
        var gameWindowTitle = bundle.getString("gameWindowTitle");
        GameWindow gameWindow = new GameWindow(gameWindowTitle);
        gameWindow.setSize(400, 400);
//        gameWindow.setAlignmentX(GameWindow.CENTER_ALIGNMENT);
        return gameWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu visualModeMenu = CreateVisualModeMenu();
        JMenu testMenu = CreateTestMenu();
        JMenu languageMenu = CreateLanguageMenu();
        menuBar.add(visualModeMenu);
        menuBar.add(testMenu);
        menuBar.add(languageMenu);
        return menuBar;
    }

    private JMenu CreateLanguageMenu()
    {
        var languageMenuTitle = bundle.getString("languageMenuTitle");
        var languageMenuDescription = bundle.getString("testMenuDescription");
        var russianMenuItemTitle = bundle.getString("russianMenuItemTitle");
        var englishMenuItemTitle = bundle.getString("englishMenuItemTitle");
        JMenu languageMenu = new JMenu(languageMenuTitle);
        languageMenu.setMnemonic(KeyEvent.VK_T);
        languageMenu.getAccessibleContext()
                .setAccessibleDescription(languageMenuDescription);
        JMenuItem russianItem = new JMenuItem(russianMenuItemTitle, KeyEvent.VK_S);
        russianItem.addActionListener((event) -> {
            locale = Locale.getDefault();
        });
        languageMenu.add(russianItem);
        JMenuItem englishItem = new JMenuItem(englishMenuItemTitle, KeyEvent.VK_S);
        englishItem.addActionListener((event) -> {
            locale = Locale.ENGLISH;
        });
        languageMenu.add(englishItem);
        return languageMenu;
    }

    private JMenu CreateTestMenu()
    {
        var testMenuTitle = bundle.getString("testMenuTitle");
        var testMenuDescription = bundle.getString("testMenuDescription");
        var logMessageItemText = bundle.getString("logMessageItemText");
        var newLogMessage = bundle.getString("newLogMessage");
        JMenu testMenu = new JMenu(testMenuTitle);
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext()
                .setAccessibleDescription(testMenuDescription);
        JMenuItem addLogMessageItem = new JMenuItem(logMessageItemText, KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug(newLogMessage);
        });
        testMenu.add(addLogMessageItem);
        return testMenu;
    }

    private void UpdateNames()
    {
        //обновить названия в меню и окнах соответственно текущей локали
    }

    private JMenu CreateVisualModeMenu()
    {
        var visualModeMenuTitle = bundle.getString("visualModeMenuTitle");
        var visualModeMenuDescription = bundle.getString("visualModeMenuDescription");
        var systemModeText = bundle.getString("systemModeText");
        var crossplatformModeText = bundle.getString("crossplatformModeText");
        JMenu visualModeMenu = new JMenu(visualModeMenuTitle);
        visualModeMenu.setMnemonic(KeyEvent.VK_V);
        visualModeMenu
                .getAccessibleContext()
                .setAccessibleDescription(visualModeMenuDescription);
        JMenuItem systemMode = new JMenuItem(systemModeText, KeyEvent.VK_S);
        systemMode.addActionListener((event) -> {
            setVisualMode(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        JMenuItem crossplatformMode = new JMenuItem(crossplatformModeText, KeyEvent.VK_S);
        crossplatformMode.addActionListener((event) -> {
            setVisualMode(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        visualModeMenu.add(crossplatformMode);
        return visualModeMenu;
    }
    
    private void setVisualMode(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }
}
