package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends RestorableJInternalFrame implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private StatesKeeper m_keeper;

    LogWindow(LogWindowSource logSource, String title, StatesKeeper keeper)
    {
        super(title, true, true, true, true);
        m_logSource = logSource;
        m_keeper = keeper;
        m_keeper.register(this, "LogWindow");
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        pack();
        updateLogContent();
    }

    public void dispose()
    {
        m_logSource.unregisterListener(this);
        m_keeper.unregister(this.getName());
        setVisible(false);
        super.dispose();
    }

    public void updateNames(Locale locale)
    {
        var bundle = ResourceBundle.getBundle("MainApplicationFrameBundle", locale);
        setLocale(locale);
        setTitle(bundle.getString("logWindowTitle"));
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
