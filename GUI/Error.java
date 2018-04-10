package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Error {
    private JButton buttonCancel;
    private JButton buttonConfirm;
    public JPanel mainPanel;
    private JLabel text;

    public Error(String t) {
        text.setText(t);
    }
}
