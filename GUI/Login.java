package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

import App.Uzytkownik;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;


public class Login {
    public JPanel mainPanel;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField textFieldLogin;
    private JPasswordField passwordField;
    Connection connection;
    ResultSet result;
    PreparedStatement statement;

    public Login() {
        textFieldLogin.setText("");
        passwordField.setText("");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textFieldLogin.getText() != "" && passwordField.getText() != ""){
                    try {
                        String haslo = "", login = "", imie="", nazwisko ="", email="";
                        int id= -1;
                        statement = connection.prepareStatement("SELECT * from UZYTKOWNIK where login = ?");
                        statement.setString(1, textFieldLogin.getText());

                        statement.execute();
                        result = statement.getResultSet();

                        while(result.next()) {
                            haslo = result.getString("haslo");
                            login = result.getString("login");
                            imie = result.getString("imie");
                            nazwisko = result.getString("nazwisko");
                            email = result.getString("email");
                            id = result.getInt("id_uzytkownika");
                        }

                        char[] correctPassword = haslo.toCharArray();
                        char[] password = passwordField.getPassword();

                        if (Arrays.equals(password, correctPassword)) {
                            System.out.println("Password is correct");

                            Uzytkownik uzytkownik = new Uzytkownik(id,imie,nazwisko,email,login,haslo);
                            JFrame coreFrame = new JFrame("Zakład bukmacherski");
                            Core CoreFrame = new Core(uzytkownik);
                            coreFrame.setContentPane(CoreFrame.mainPanel);
                            //loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            coreFrame.pack();
                            //loginFrame.setSize(500, 300);
                            coreFrame.setVisible(true);
                            coreFrame.setLocationRelativeTo(null);
                        } else {
                            JFrame errorFrame = new JFrame("Błąd");
                            Error ErrorFrame = new Error("Niepoprawnie wprowadzone dane!");
                            errorFrame.setContentPane(ErrorFrame.mainPanel);
                            errorFrame.pack();
                            errorFrame.setVisible(true);
                            errorFrame.setLocationRelativeTo(null);
                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                else {
                    JFrame errorFrame = new JFrame("Błąd");
                    Error ErrorFrame = new Error("Niepoprawnie wprowadzone dane!");
                    errorFrame.setContentPane(ErrorFrame.mainPanel);
                    errorFrame.pack();
                    errorFrame.setVisible(true);
                    errorFrame.setLocationRelativeTo(null);
                }
            }
        });

        buttonRegister.addActionListener(new ActionListener() {
            // KOD NA KTÓRYM BAZUJE, TU ZNAJDUJE SIE BO GDZIES MUSI
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
