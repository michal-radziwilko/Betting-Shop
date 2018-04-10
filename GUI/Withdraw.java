package GUI;

import App.Uzytkownik;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Withdraw {
    private JTextField textFieldWithdraw;
    private JButton buttonConfirm;
    public JPanel mainPanel;
    Connection connection;
    ResultSet result;
    PreparedStatement statement;

    Withdraw(Uzytkownik uzytkownik, Core core) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    float srodki = -1;
                    statement = connection.prepareStatement("SELECT srodki from konto where ID_UZYTKOWNIKA=?");

                    statement.setInt(1, uzytkownik.getId_uzytkownika());

                    statement.execute();

                    result = statement.getResultSet();

                    while (result.next()) {
                        srodki = result.getFloat("srodki");
                        System.out.println("środki przed wypłaceniem z konta: " + srodki);
                        srodki = srodki - Float.parseFloat(textFieldWithdraw.getText());
                    }

                    if (srodki >= 0) {


                        statement = connection.prepareStatement("update konto set srodki=? where ID_UZYTKOWNIKA=?");

                        statement.setFloat(1, srodki);
                        statement.setInt(2, uzytkownik.getId_uzytkownika());

                        statement.execute();

                        //Sprawdzenie poprawnosci dzialania
                        statement = connection.prepareStatement("SELECT srodki from konto where ID_UZYTKOWNIKA=?");

                        statement.setInt(1, uzytkownik.getId_uzytkownika());

                        statement.execute();

                        result = statement.getResultSet();

                        while (result.next()) {
                            System.out.println("środki po wyplaceniu z konta: " + result.getFloat("srodki"));
                            uzytkownik.setSrodki(result.getString("srodki"));
                        }
                    } else
                        System.out.println("Nie ma wystarczających środków na koncie!");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                core.Actualize(uzytkownik);
            }
        });
    }
}
