package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Register {
    public JPanel mainPanel;
    private JTextField textFieldName;
    private JTextField textFieldSurname;
    private JTextField textFieldEmail;
    private JTextField textFieldLogin;
    private JPasswordField passwordField;
    private JButton buttonRegister;
    public Connection connection;
    public ResultSet result;
    public PreparedStatement statement;

    public Register() {
        textFieldName.setText("");
        textFieldSurname.setText("");
        textFieldEmail.setText("");
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
        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldName.getText() != "" && textFieldSurname.getText() != "" && textFieldLogin.getText() != "" && textFieldEmail.getText() != "" && passwordField.getText() != "") {
                    if (textFieldEmail.getText().contains("@")) {
                        if (passwordField.getText().length() > 7 && passwordField.getText().matches(".*\\d.*")) {

                            int id_uzytkownika =-1, id_konta=-1;

                            try {

                                statement = connection.prepareStatement("select max(id_uzytkownika) as id_uzytkownika from UZYTKOWNIK");

                                statement.execute();
                                result = statement.getResultSet();

                                while (result.next()) {
                                    id_uzytkownika = result.getInt("id_uzytkownika");
                                    id_uzytkownika++;
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                            try {

                                String haslo = String.valueOf(passwordField.getPassword());

                                statement = connection.prepareStatement("insert into uzytkownik(id_uzytkownika, imie, nazwisko, email, login, haslo)" +
                                        "values(?,?,?,?,?,?)");

                                statement.setInt(1, id_uzytkownika);
                                statement.setString(2, textFieldName.getText());
                                statement.setString(3, textFieldSurname.getText());
                                statement.setString(4, textFieldEmail.getText());
                                statement.setString(5, textFieldLogin.getText());
                                statement.setString(6, haslo);

                                statement.execute();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                            try {

                                statement = connection.prepareStatement("select max(id_konta) as id_konta from konto");

                                statement.execute();
                                result = statement.getResultSet();

                                while (result.next()) {
                                    id_konta = result.getInt("id_konta");
                                    id_konta++;
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                            try {
                                statement = connection.prepareStatement("insert into konto(id_konta, id_uzytkownika, id_uprawnienia, srodki)" +
                                        "values(?,?,?,?)");

                                statement.setInt(1, id_konta);
                                statement.setInt(2, id_uzytkownika);
                                statement.setInt(3, 1);
                                statement.setInt(4,0);

                                statement.execute();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                        }
                        else{
                            JFrame errorFrame = new JFrame("Błąd");
                            Error ErrorFrame = new Error("Niepoprawnie wprowadzone dane!\n Pamiętaj, że hasło musi mieć conajmniej 8 znaków długości oraz zawierać conajmniej jedną cyfrę.\n Natomiast email zawierać znak @.");
                            errorFrame.setContentPane(ErrorFrame.mainPanel);
                            errorFrame.pack();
                            errorFrame.setVisible(true);
                            errorFrame.setLocationRelativeTo(null);
                        }
                    }
                    else{
                        JFrame errorFrame = new JFrame("Błąd");
                        Error ErrorFrame = new Error("Niepoprawnie wprowadzone dane!\n Pamiętaj, że hasło musi mieć conajmniej 8 znaków długości oraz zawierać conajmniej jedną cyfrę.\n Natomiast email zawierać znak @.");
                        errorFrame.setContentPane(ErrorFrame.mainPanel);
                        errorFrame.pack();
                        errorFrame.setVisible(true);
                        errorFrame.setLocationRelativeTo(null);
                    }
                }
                else {
                    JFrame errorFrame = new JFrame("Błąd");
                    Error ErrorFrame = new Error("Niepoprawnie wprowadzone dane!\n Pamiętaj, że hasło musi mieć conajmniej 8 znaków długości oraz zawierać conajmniej jedną cyfrę.\n Natomiast email zawierać znak @.");
                    errorFrame.setContentPane(ErrorFrame.mainPanel);
                    errorFrame.pack();
                    errorFrame.setVisible(true);
                    errorFrame.setLocationRelativeTo(null);
                }
            }
        });
    }
}
