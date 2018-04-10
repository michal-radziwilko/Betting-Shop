package GUI;

import App.Uzytkownik;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ante {
    private JTextField textFieldAnte;
    private JButton buttonConfirm;
    public JPanel mainPanel;
    Connection connection;
    ResultSet result;
    PreparedStatement statement;

    Ante(Uzytkownik uzytkownik, Core core){

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
                    float srodki=-1;
                    try{
                    statement = connection.prepareStatement("SELECT srodki from konto where ID_UZYTKOWNIKA=?");

                    statement.setInt(1, uzytkownik.getId_uzytkownika());

                    statement.execute();

                    result = statement.getResultSet();

                    while(result.next()) {
                      srodki = result.getFloat("srodki");
                      System.out.println("środki przed doładowaniem konta: "+srodki);
                      srodki = srodki + Float.parseFloat(textFieldAnte.getText());
                    }
                    } finally {
                        //It's important to close the statement when you are done with it
                        statement.close();
                    }

                    try{
                    statement = connection.prepareStatement("update konto set srodki=? where ID_UZYTKOWNIKA=?");

                    statement.setFloat(1, srodki);
                    statement.setInt(2,uzytkownik.getId_uzytkownika());

                    statement.execute();
                    } finally {
                        //It's important to close the statement when you are done with it
                        statement.close();
                    }
                    //Sprawdzenie poprawnosci dzialania
                    try{
                    statement = connection.prepareStatement("SELECT srodki from konto where ID_UZYTKOWNIKA=?");

                    statement.setInt(1, uzytkownik.getId_uzytkownika());

                    statement.execute();

                    result = statement.getResultSet();

                    while(result.next()) {
                        System.out.println("środki po doładowaniu konta: "+ result.getFloat("srodki"));
                        uzytkownik.setSrodki(result.getString("srodki"));
                    }
                    } finally {
                        //It's important to close the statement when you are done with it
                        statement.close();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                core.Actualize(uzytkownik);
            }

        });

    }
}
