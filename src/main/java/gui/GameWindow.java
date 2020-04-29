package gui;

import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

public class GameWindow extends RestorableJInternalFrame
{
    private final GameVisualizer m_visualizer;
    private StatesKeeper m_keeper;

    GameWindow(String title, boolean autoMode, StatesKeeper keeper) throws IOException
    {
        super(title, true, true, false, true);
        m_visualizer = new GameVisualizer(autoMode);
        m_keeper = keeper;
        m_keeper.register(this, "GameWindow");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultLocale(JInternalFrame.getDefaultLocale());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void dispose()
    {
        setVisible(false);
        m_keeper.unregister(this.getName());
        super.dispose();
    }

    void updateNames(Locale locale)
    {
        var bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
        setLocale(locale);
        setTitle(bundle.getString("gameWindowTitle"));
    }
}
