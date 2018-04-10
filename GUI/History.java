package GUI;

import App.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class History {
    public JPanel mainPanel;
    private JTable tableHistory;
    public DefaultTableModel model = new DefaultTableModel(new String[]{"Druzyna 1", "Druzyna 2", "Data rozgrywki", "Twój typ",
            "Wygrany"}, 0);
    Connection connection;
    ResultSet result, result1;
    PreparedStatement statement, statement1;

    History(Uzytkownik uzytkownik){
        tableHistory.setModel(model);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            int id_konta=-1;

            statement = connection.prepareStatement("SELECT id_konta from konto where id_uzytkownika = ?");

            statement.setInt(1, uzytkownik.getId_uzytkownika());

            statement.execute();
            result = statement.getResultSet();

            while(result.next()) {
                id_konta = result.getInt("id_konta");
            }

            statement = connection.prepareStatement("select * from obstawienie where id_serii in (select id_serii from seria where id_konta = ?)");

            statement.setInt(1, id_konta);

            statement.execute();
            result = statement.getResultSet();

            model.setRowCount(0);

            while(result.next()) {
                int id_obstawienia = result.getInt("id_obstawienia");
                int id_serii = result.getInt("id_serii");
                int id_zakladu = result.getInt("id_zakladu");
                Date data_utworzenia = result.getDate("data_utworzenia");
                Date data_rozliczenia = result.getDate("data_rozliczenia");
                int typ = result.getInt("typ");

                // Pobranie id druzyn, daty zakonczenia zakladu, wygranego
                int id_druzyny1=-1, id_druzyny2=-1;
                String nazwaDr1="";
                String nazwaDr2="";
                Date data_zakonczenia=null;
                int wygrany=-1;

                statement1 = connection.prepareStatement("select * from zaklad where id_zakladu =?");

                statement1.setInt(1, id_zakladu);

                statement1.execute();
                result1 = statement1.getResultSet();
                while(result1.next()) {
                    data_zakonczenia = result1.getDate("data_zakonczenia");
                    id_druzyny1 = result1.getInt("id_druzyny1");
                    id_druzyny2 = result1.getInt("id_druzyny2");
                    wygrany = result1.getInt("wygrany");
                }

                // Pobranie nazwy druzyny 1
                statement1 = connection.prepareStatement("select nazwa from druzyny where id_druzyny =?");

                statement1.setInt(1, id_druzyny1);

                statement1.execute();
                result1 = statement1.getResultSet();
                while(result1.next()) {
                    nazwaDr1 = result1.getString("nazwa");
                }

                // Pobranie nazwy druzyny 2
                statement1 = connection.prepareStatement("select nazwa from druzyny where id_druzyny =?");

                statement1.setInt(1, id_druzyny2);

                statement1.execute();
                result1 = statement1.getResultSet();
                while(result1.next()) {
                    nazwaDr2 = result1.getString("nazwa");
                }

                // wyświetlanie danych

                String wygranyNazwa = "";
                if(wygrany == 1)
                    wygranyNazwa=nazwaDr1;
                else if(wygrany==2)
                    wygranyNazwa=nazwaDr2;

                String typNazwa="";
                if(typ==1)
                    typNazwa=nazwaDr1;
                else if(typ==2)
                    typNazwa=nazwaDr2;
                else if(typ==3)
                    typNazwa="remis";

                model.addRow(new Object[]{nazwaDr1, nazwaDr2, data_zakonczenia, typNazwa, wygranyNazwa});
            }

            tableHistory.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
