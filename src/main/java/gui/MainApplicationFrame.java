package gui;

import log.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

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
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow()
    {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        gameWindow.setAlignmentX(GameWindow.CENTER_ALIGNMENT);
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
        menuBar.add(visualModeMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private JMenu CreateTestMenu()
    {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext()
                .setAccessibleDescription("Тестовые команды");
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
        return testMenu;
    }

    private JMenu CreateVisualModeMenu()
    {
        JMenu visualModeMenu = new JMenu("Режим отображения");
        visualModeMenu.setMnemonic(KeyEvent.VK_V);
        visualModeMenu
                .getAccessibleContext()
                .setAccessibleDescription("Управление режимом отображения приложения");
        JMenuItem systemMode = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemMode.addActionListener((event) -> {
            setVisualMode(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        JMenuItem crossplatformMode = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
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
