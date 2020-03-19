package gui;

import javax.swing.*;


class ExitWindow extends JFrame
{
    private String[] options;
    private String exitDialogString;

    ExitWindow(String title, String exitDialogString, String[] options)
    {
        setTitle(title);
        this.exitDialogString = exitDialogString;
        this.options = options;
        setVisible(false);
        setLocationRelativeTo(null);
        pack();
    }

    boolean createExitDialogAndGetAnswer()
    {
        int answerID = JOptionPane
                .showOptionDialog(this, this.exitDialogString,
                        this.getTitle(), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, this.options,
                        this.options[0]);
        if (answerID == 0)
        {
            this.setVisible(false);
            return true;
        }
        return false;
    }
}
