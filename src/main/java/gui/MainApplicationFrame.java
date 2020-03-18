package gui;

import log.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;


public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private Locale locale = Locale.getDefault();
    private ResourceBundle bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private JMenuBar menuBar;

    MainApplicationFrame()
    {
        setLocationRelativeTo(null);
        setContentPane(desktopPane);

        logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = createGameWindow();
        addWindow(gameWindow);

        menuBar = generateMenuBar();
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getExitDialog(0);
            }
        });
    }
    
    private LogWindow createLogWindow()
    {
        var logWindowTitle = bundle.getString("logWindowTitle");
        var startLogMessage = bundle.getString("startLogMessage");
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), logWindowTitle);
        logWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(getInternalFrameListener(2));
        logWindow.setLocation(10,10);
        Logger.debug(startLogMessage);
        return logWindow;
    }

    private GameWindow createGameWindow()
    {
        var gameWindowTitle = bundle.getString("gameWindowTitle");
        GameWindow gameWindow = new GameWindow(gameWindowTitle);
        gameWindow.addInternalFrameListener(getInternalFrameListener(1));
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    private void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu visualModeMenu = createVisualModeMenu();
        JMenu testMenu = createTestMenu();
        JMenu languageMenu = createLanguageMenu();
        JMenuItem exitMenuItem = createExitMenuItem();

        menuBar.add(visualModeMenu);
        menuBar.add(testMenu);
        menuBar.add(languageMenu);
        menuBar.add(exitMenuItem);
        menuBar.setLocale(locale);

        return menuBar;
    }

    private JMenu createVisualModeMenu()
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
        visualModeMenu.add(systemMode);

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

    private JMenu createTestMenu()
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

    private JMenu createLanguageMenu()
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
            updateNames();
        });
        languageMenu.add(russianItem);

        JMenuItem englishItem = new JMenuItem(englishMenuItemTitle, KeyEvent.VK_S);
        englishItem.addActionListener((event) -> {
            locale = Locale.ENGLISH;
            updateNames();
        });
        languageMenu.add(englishItem);

        return languageMenu;
    }

    private JMenuItem createExitMenuItem()
    {
        var exitMenuTitle = bundle.getString("exitMenuTitle");
        JMenuItem exit = new JMenuItem(exitMenuTitle);
        exit.setMnemonic(KeyEvent.VK_Q);
        exit.setMaximumSize(new Dimension(exit.getPreferredSize()));
        exit.addActionListener((event) -> getExitDialog(0));
        return exit;
    }

    private void getExitDialog(int id)
    {
        String[] options = {
                bundle.getString("exitDialogItemFirst"),
                bundle.getString("exitDialogItemSecond")
        };
        int n = JOptionPane
                .showOptionDialog(new JFrame(), bundle.getString("exitDialog"),
                        bundle.getString("exitDialogTitle"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
        if (n == 0)
        {
            new JFrame().setVisible(false);
            switch (id)
            {
                case(0):
                    System.exit(0);
                case(1):
                    gameWindow.removeNotify();
                case(2):
                    logWindow.dispose();
            }
        }
    }

    private void updateNames()
    {
        bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
        setLocale(locale);

        updateVisualModeMenu(menuBar);
        updateTestMenu(menuBar);
        updateLanguageMenu(menuBar);

        gameWindow.setTitle(bundle.getString("gameWindowTitle"));
        logWindow.setTitle(bundle.getString("logWindowTitle"));

        var exitItem = (JMenuItem)menuBar.getComponent(3);
        exitItem.setText(bundle.getString("exitMenuTitle"));

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateVisualModeMenu(JMenuBar menuBar)
    {
        menuBar.getMenu(0).setText(bundle.getString("visualModeMenuTitle"));
        menuBar.getMenu(0).getItem(0).setText(bundle.getString("systemModeText"));
        menuBar.getMenu(0).getItem(1).setText(bundle.getString("crossplatformModeText"));
    }

    private void updateTestMenu(JMenuBar menuBar)
    {
        menuBar.getMenu(1).setText(bundle.getString("testMenuTitle"));
        menuBar.getMenu(1).getItem(0).setText(bundle.getString("logMessageItemText"));
    }

    private void updateLanguageMenu(JMenuBar menuBar)
    {
        menuBar.getMenu(2).setText(bundle.getString("languageMenuTitle"));
        menuBar.getMenu(2).getItem(0).setText(bundle.getString("russianMenuItemTitle"));
        menuBar.getMenu(2).getItem(1).setText(bundle.getString("englishMenuItemTitle"));
    }

    private InternalFrameListener getInternalFrameListener(int id)
    {
        return new InternalFrameListener() {
            @Override
            public void internalFrameOpened(InternalFrameEvent internalFrameEvent) { }

            @Override
            public void internalFrameClosing(InternalFrameEvent internalFrameEvent)
            {
                getExitDialog(id);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent internalFrameEvent) {}

            @Override
            public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {}

            @Override
            public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {}

            @Override
            public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {}

            @Override
            public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {}
        };
    }
}
