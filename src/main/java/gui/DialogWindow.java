package gui;

import javax.swing.*;


class DialogWindow extends JFrame
{
    private String[] options;
    private String dialogString;

    DialogWindow(String title, String dialogString, String[] options)
    {
        setTitle(title);
        this.dialogString = dialogString;
        this.options = options;
        setVisible(false);
        setLocationRelativeTo(null);
        pack();
    }

    boolean createDialogAndGetAnswer()
    {
        int answerID = JOptionPane
                .showOptionDialog(this, this.dialogString,
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
