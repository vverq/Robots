package gui;

import log.Logger;
import models.RobotController;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
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
    private DialogWindow exitWindow;
    private DialogWindow warningWindow;
    private DialogWindow modeChooserWindow;
    private StatesKeeper keeper;
    private StateWindow stateCoordinatesWindow;
    private StateWindow stateDistanceWindow;

    MainApplicationFrame()
    {
        keeper = new StatesKeeper(new File("src/main/resources/framesProperties.json"));
        setLocationRelativeTo(null);
        setContentPane(desktopPane);
        setMinimumSize(new Dimension(750,600));

        modeChooserWindow = createModeChooserWindow();
        menuBar = generateMenuBar();
        logWindow = createLogWindow();
        gameWindow = createGameWindow();
        exitWindow = createExitWindow();
        warningWindow = createRestoreWarningWindow();
        stateCoordinatesWindow = createStateCoordinatesWindow();
        stateDistanceWindow = createStateDistanceWindow();

        setJMenuBar(menuBar);
        addWindow(logWindow);
        addWindow(gameWindow);
        addWindow(stateCoordinatesWindow);
        addWindow(stateDistanceWindow);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (exitWindow.createDialogAndGetAnswer()) {
                    try
                    {
                        keeper.save();
                    }
                    catch (IOException ex)
                    {
                        System.out.println(ex.toString());
                    }
                    System.exit(0);
                }
            }
        });
        try
        {
            if (keeper.canLoad()) {
                if (warningWindow.createDialogAndGetAnswer()) {
                    keeper.load();
                }
            }
        }
        catch(IOException | ParseException e)
        {
            Logger.error(e.toString());
        }
    }

    private DialogWindow createRestoreWarningWindow()
    {
        var warningDialogTitle = bundle.getString("warningDialogTitle");
        var warningDialog =  bundle.getString("warningDialog");
        String[] warningWindowOptions = {
                bundle.getString("warningDialogItemFirst"),
                bundle.getString("warningDialogItemSecond")
        };
        return new DialogWindow(warningDialogTitle, warningDialog, warningWindowOptions);
    }

    private DialogWindow createModeChooserWindow()
    {
        var modeChooseTitle = bundle.getString("modeChooseDialogTitle");
        var modeChooseDialog =  bundle.getString("modeChooseDialog");
        String[] warningWindowOptions = {
                bundle.getString("modeChooseDialogItemFirst"),
                bundle.getString("modeChooseDialogItemSecond")
        };
        return new DialogWindow(modeChooseTitle, modeChooseDialog, warningWindowOptions);
    }

    private DialogWindow createExitWindow()
    {
        var exitWindowTitle = bundle.getString("exitDialogTitle");
        var exitWindowDialog =  bundle.getString("exitDialog");
        String[] exitWindowOptions = {
                bundle.getString("exitDialogItemFirst"),
                bundle.getString("exitDialogItemSecond")
        };
        return new DialogWindow(exitWindowTitle, exitWindowDialog, exitWindowOptions);
    }

    private LogWindow createLogWindow()
    {
        var logWindowTitle = bundle.getString("logWindowTitle");
        var startLogMessage = bundle.getString("startLogMessage");
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), logWindowTitle, keeper);
        logWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(getInternalFrameListener(logWindow));
        logWindow.setLocation(10,10);
        Logger.debug(startLogMessage);
        return logWindow;
    }

    private GameWindow createGameWindow()
    {
        var gameWindowTitle = bundle.getString("gameWindowTitle");
        try
        {
            var automode = false;
            if (modeChooserWindow.createDialogAndGetAnswer())
            {
                automode = true;
            }
            GameWindow gameWindow = new GameWindow(gameWindowTitle, automode , keeper);
            logWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            gameWindow.addInternalFrameListener(getInternalFrameListener(gameWindow));
            gameWindow.setLocation(300, 50);
            gameWindow.setSize(330, 350);
            return gameWindow;
        }
        catch (IOException e)
        {
            Logger.error(e.toString());
        }
        return gameWindow;
    }

    private StateWindow createStateCoordinatesWindow()
    {
        var stateCoordinatesWindowTitle = bundle.getString("coordinatesWindowTitle");
        StateWindow stateCoordinatesWindow = new StateCoordinatesWindow(
                stateCoordinatesWindowTitle, gameWindow.getRobotController());
        stateCoordinatesWindow.setLocation(800, 50);
        stateCoordinatesWindow.setSize(200, 100);
        return stateCoordinatesWindow;
    }

    private StateWindow createStateDistanceWindow()
    {
        var stateDistanceWindowTitle = bundle.getString("distanceWindowTitle");
        StateWindow stateDistanceWindow = new StateDistanceWindow(
                stateDistanceWindowTitle, gameWindow.getRobotController(), gameWindow.getTargetGenerator());
        stateDistanceWindow.setLocation(800, 250);
        stateDistanceWindow.setSize(200, 100);
        return stateDistanceWindow;
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
        addLogMessageItem.addActionListener((event) -> Logger.debug(newLogMessage));
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
            updateNames(Locale.getDefault());
        });
        languageMenu.add(russianItem);

        JMenuItem englishItem = new JMenuItem(englishMenuItemTitle, KeyEvent.VK_S);
        englishItem.addActionListener((event) -> {
            updateNames(Locale.ENGLISH);
        });
        languageMenu.add(englishItem);

        return languageMenu;
    }

    private void updateNames(Locale locale)
    {
        updateMenu(locale);
        gameWindow.updateNames(locale);
        logWindow.updateNames(locale);
        warningWindow.updateDialog(locale,
                "warningDialogTitle",
                "warningDialog",
                new String[]{
                        "warningDialogItemFirst",
                        "warningDialogItemSecond"
                });
        exitWindow.updateDialog(locale,
                "exitDialogTitle",
                "exitDialog",
                new String[]{
                        "exitDialogItemFirst",
                        "exitDialogItemSecond"
                });
    }

    private JMenuItem createExitMenuItem()
    {
        var exitMenuTitle = bundle.getString("exitMenuTitle");
        JMenuItem exit = new JMenuItem(exitMenuTitle);
        exit.setMnemonic(KeyEvent.VK_Q);
        exit.setMaximumSize(new Dimension(exit.getPreferredSize()));
        exit.addActionListener((event) -> {
            if (exitWindow.createDialogAndGetAnswer()) {
                this.dispose();
                System.exit(0);
            }
        });
        return exit;
    }

    private void updateMenu(Locale locale)
    {
        bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
        setLocale(locale);

        updateVisualModeMenu(menuBar);
        updateTestMenu(menuBar);
        updateLanguageMenu(menuBar);
        
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

    private InternalFrameListener getInternalFrameListener(JInternalFrame frame)
    {
        return new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent internalFrameEvent)
            {
                if (exitWindow.createDialogAndGetAnswer()) {
                    frame.dispose();
                }
            }
        };
    }
}
