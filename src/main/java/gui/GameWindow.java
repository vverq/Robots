package gui;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends RestorableJInternalFrame
{
    private final GameVisualizer m_visualizer;
    private StatesKeeper m_keeper;

    GameWindow(String title, StatesKeeper keeper)
    {
        super(title, true, true, true, true);
        m_visualizer = new GameVisualizer();
        m_keeper = keeper;
        m_keeper.register(this, "GameWindow");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultLocale(JInternalFrame.getDefaultLocale());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void dispose() {
        setVisible(false);
        m_keeper.unregister(this);
    }
}
