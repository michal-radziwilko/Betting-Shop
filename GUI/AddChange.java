package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddChange {
    public JButton buttonConfirm;
    private JTextField textFieldBet1;
    private JTextField textFieldBet2;
    private JTextField textFieldBet3;
    private JTextField textFieldStartDate;
    private JTextField textFieldEndDate;
    private JTextField textFieldWinner;
    private JTextField textFieldTeam1;
    private JTextField textFieldTeam2;
    private JTextField textFieldSport;
    public JPanel mainPanel;
    Connection connection;
    ResultSet result;
    PreparedStatement statement;


    public AddChange(String type, int id_zakladu_, String nazwaDysc_, String nazwaDr1_, String nazwaDr2_, Date data_zakonczenia_, float kurs1_, float kurs2_, float kurs3_, int wygrany_, Modify modify) {

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

        // Dodawanie zakładu
        if (type == "add"){
            System.out.println("DODAWANIE");

                    try {
                        int id_zakladu=-2, id_dyscypliny=-1, id_druzyny1=-1,id_druzyny2=-1;

                        // Znajdowanie id_zakladu pod jakim dodać nowy zakład
                        try{
                            statement = connection.prepareStatement("select max(id_zakladu) as id_zakladu from ZAKLAD");

                            statement.execute();
                            result = statement.getResultSet();

                            while (result.next()) {
                                id_zakladu = result.getInt("id_zakladu");
                                id_zakladu++;
                            }
                        } finally {
                            //It's important to close the statement when you are done with it
                            statement.close();
                        }

                        // Znajdowanie id_dyscypliny o podanej nazwie
                        try{
                            statement = connection.prepareStatement("select id_dyscypliny from DYSCYPLINY where NAZWA =?");
                            statement.setString(1, textFieldSport.getText());

                            statement.execute();
                            result = statement.getResultSet();

                            while (result.next()) {
                                id_dyscypliny = result.getInt("id_dyscypliny");
                            }
                        } finally {
                            //It's important to close the statement when you are done with it
                            statement.close();
                        }

                        // Znajdowanie id_druzyny1 o podanej nazwie
                        try{
                            statement = connection.prepareStatement("select id_druzyny from druzyny where NAZWA =?");
                            statement.setString(1, textFieldTeam1.getText());

                            statement.execute();
                            result = statement.getResultSet();

                            while (result.next()) {
                                id_druzyny1 = result.getInt("id_druzyny");
                            }
                        } finally {
                            //It's important to close the statement when you are done with it
                            statement.close();
                        }

                        // Znajdowanie id_druzyny2 o podanej nazwie
                        try{
                            statement = connection.prepareStatement("select id_druzyny from druzyny where NAZWA =?");
                            statement.setString(1, textFieldTeam2.getText());

                            statement.execute();
                            result = statement.getResultSet();

                            while (result.next()) {
                                id_druzyny2 = result.getInt("id_druzyny");
                            }
                        } finally {
                            //It's important to close the statement when you are done with it
                            statement.close();
                        }

                        // Wstawianie nowego zakładu
                        try{
                            statement = connection.prepareStatement("insert into zaklad(ID_ZAKLADU, ID_DYSCYPLINY, kurs1, kurs2, kurs3, data_rozpoczecia, data_zakonczenia, id_druzyny1, id_druzyny2)" +
                                    "values(?,?,?,?,?,?,?,?,?)");

                            statement.setInt(1, id_zakladu);
                            statement.setInt(2, id_dyscypliny);
                            statement.setFloat(3, Float.parseFloat(textFieldBet1.getText()));
                            statement.setFloat(4, Float.parseFloat(textFieldBet2.getText()));
                            statement.setFloat(5, Float.parseFloat(textFieldBet3.getText()));
                            statement.setString(6, textFieldStartDate.getText());
                            statement.setString(7, textFieldEndDate.getText());
                            statement.setInt(8, id_druzyny1);
                            statement.setInt(9, id_druzyny2);

                            statement.execute();
                        } finally {
                            //It's important to close the statement when you are done with it
                            statement.close();
                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    modify.refresh();
                }

        // Modyfikacja zakładu
        if (type == "modify") {

            int id_zakladu = id_zakladu_;
            String nazwaDysc=nazwaDysc_;
            String nazwaDr1=nazwaDr1_;
            String nazwaDr2=nazwaDr2_;
            Date data_zakonczenia=data_zakonczenia_;
            float kurs1=kurs1_;
            float kurs2=kurs2_;
            float kurs3 = kurs3_;
            int wygrany=wygrany_;

            System.out.println("MODYFIKACJA");
            int id_dyscypliny = -1;
            int id_druzyny1 = -1;
            int id_druzyny2 = -1;
            boolean CzyNowaData = false;
            String nowaData = "";

            if (!textFieldSport.getText().equals(""))
                nazwaDysc = textFieldSport.getText();

            if (!textFieldBet1.getText().equals(""))
                kurs1 = Float.parseFloat(textFieldBet1.getText());

            if (!textFieldBet2.getText().equals(""))
                kurs2 = Float.parseFloat(textFieldBet2.getText());

            if (!textFieldBet3.getText().equals(""))
                kurs3 = Float.parseFloat(textFieldBet3.getText());

            if (!textFieldEndDate.getText().equals("")) {
                CzyNowaData = true;
                nowaData = textFieldEndDate.getText();
            }

            if (!textFieldWinner.getText().equals(""))
                wygrany = Integer.parseInt(textFieldWinner.getText());

            if (!textFieldTeam1.getText().equals(""))
                nazwaDr1 = textFieldTeam1.getText();

            if (!textFieldTeam2.getText().equals(""))
                nazwaDr2 = textFieldTeam2.getText();




            try {
                statement = connection.prepareStatement("select id_druzyny from DRUZYNY where nazwa =?");
                statement.setString(1, nazwaDr1);

                statement.execute();

                result = statement.getResultSet();
                while (result.next()) {
                    id_druzyny1 = result.getInt("id_druzyny");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                statement = connection.prepareStatement("select id_druzyny from DRUZYNY where nazwa =?");
                statement.setString(1, nazwaDr2);

                statement.execute();

                result = statement.getResultSet();
                while (result.next()) {
                    id_druzyny2 = result.getInt("id_druzyny");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                statement = connection.prepareStatement("select id_dyscypliny from dyscypliny where nazwa =?");
                statement.setString(1, nazwaDysc);

                statement.execute();

                result = statement.getResultSet();
                while (result.next()) {
                    id_dyscypliny = result.getInt("id_dyscypliny");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if (!CzyNowaData) {
                try {
                    statement = connection.prepareStatement("update zaklad set id_dyscypliny=?, id_druzyny1=?, id_druzyny2=?, data_zakonczenia=?, kurs1=?, kurs2=?, kurs3=?, wygrany=? where id_zakladu = ?");
                    statement.setInt(1, id_dyscypliny);
                    statement.setInt(2, id_druzyny1);
                    statement.setInt(3, id_druzyny2);
                    statement.setDate(4, data_zakonczenia);
                    statement.setFloat(5, kurs1);
                    statement.setFloat(6, kurs2);
                    statement.setFloat(7, kurs3);
                    statement.setInt(8, wygrany);
                    statement.setInt(9, id_zakladu);

                    System.out.println("przed update");
                    System.out.println("Dane do modyfikacji: id_z: "+id_zakladu+" id_dr1: "+id_druzyny1+" kurs1: "+kurs1);

                    statement.execute();

                    System.out.println("Po update kurs1: " + kurs1);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            if(CzyNowaData)
            {
                try {
                    statement = connection.prepareStatement("update zaklad set id_dyscypliny=?, id_druzyny1=?, id_druzyny2=?, data_zakonczenia=?, kurs1=?, kurs2=?, kurs3=?, wygrany=? where id_zakladu = ?");
                    statement.setInt(1, id_dyscypliny);
                    statement.setInt(2, id_druzyny1);
                    statement.setInt(3, id_druzyny2);
                    statement.setString(4, nowaData);
                    statement.setFloat(5, kurs1);
                    statement.setFloat(6, kurs2);
                    statement.setFloat(7, kurs3);
                    statement.setInt(8, wygrany);
                    statement.setInt(9, id_zakladu);

                    System.out.println("przed update");
                    statement.execute();

                    System.out.println("Po update kurs1: " + kurs1);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            modify.refresh();
        }
            }
        });

    }

}
