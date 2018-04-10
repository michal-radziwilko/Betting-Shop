package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start {
    public JPanel mainPanel;
    private JButton buttonLogin;
    private JButton buttonRegister;

    public Start() {
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = new JFrame("Logowanie");
                Login LoginFrame = new Login();
                loginFrame.setContentPane(LoginFrame.mainPanel);
                loginFrame.pack();
                loginFrame.setVisible(true);
                loginFrame.setLocationRelativeTo(null);
            }
        });
        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame registerFrame = new JFrame("Rejestracja");
                Register RegisterFrame = new Register();
                registerFrame.setContentPane(RegisterFrame.mainPanel);
                registerFrame.pack();
                registerFrame.setVisible(true);
                registerFrame.setLocationRelativeTo(null);
            }
        });
    }
}
