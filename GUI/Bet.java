package GUI;

import App.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Bet {
    public JPanel mainPanel;
    private JTable tableBets;
    private JTextField textFieldAnte;
    private JTable tableSeries;
    private JRadioButton RadioButton1;
    private JRadioButton RadioButton2;
    private JRadioButton RadioButtonDraw;
    private JButton ButtonAdd;
    private JButton buttonDelete;
    private JButton ButtonConfirm;
    private JTextField textFieldWin;
    private JTextField textFieldBet;
    private int once = 1;

    Connection connection;
    ResultSet result, result1;
    PreparedStatement statement, statement1;
    DefaultTableModel model = new DefaultTableModel(new String[]{"id_zakladu","Dyscyplina","Druzyna 1", "Druzyna 2", "Data rozgrywki",
            "Kurs na drużynę 1", "Kurs na drużynę 2", "Kurs na remis"}, 0);
    DefaultTableModel model1 = new DefaultTableModel(new String[]{"id_zakladu","Dyscyplina","Druzyna 1", "Druzyna 2", "Data rozgrywki",
            "Kurs na drużynę 1", "Kurs na drużynę 2", "Kurs na remis", "Twój typ"}, 0);

    Bet(Uzytkownik uzytkownik){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        RadioButton1.setSelected(true);
        showBets();

        tableSeries.setModel(model1);

        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dyscyplina,druzyna1,druzyna2,nazwaTypu="";
                float kurs1,kurs2,kurs3;
                Date data_zakonczenia;
                int id_zakladu, id_serii=-1,typ=-1;



                id_zakladu = (int)tableBets.getValueAt(tableBets.getSelectedRow(),0);
                dyscyplina = tableBets.getValueAt(tableBets.getSelectedRow(),1).toString();
                druzyna1 = tableBets.getValueAt(tableBets.getSelectedRow(),2).toString();
                druzyna2 = tableBets.getValueAt(tableBets.getSelectedRow(),3).toString();
                data_zakonczenia = (Date)tableBets.getValueAt(tableBets.getSelectedRow(),4);
                kurs1 = (float)tableBets.getValueAt(tableBets.getSelectedRow(),5);
                kurs2 = (float)tableBets.getValueAt(tableBets.getSelectedRow(),6);
                kurs3 = (float)tableBets.getValueAt(tableBets.getSelectedRow(),7);

                if(RadioButton1.isSelected()){
                    typ=1;
                    nazwaTypu=druzyna1;
                }
                else if(RadioButton2.isSelected()){
                    typ=2;
                    nazwaTypu=druzyna2;
                }
                else if(RadioButtonDraw.isSelected()){
                    typ=3;
                    nazwaTypu="remis";
                }

                if(typ == 1 || typ ==2 || typ==3) {
                    model1.addRow(new Object[]{id_zakladu, dyscyplina, druzyna1, druzyna2, data_zakonczenia, kurs1, kurs2, kurs3, nazwaTypu});
                    tableSeries.setModel(model1);
                }
                else
                    System.out.println("Aby dodać obstawienie najpierw wybierz swój typ!");


            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model1.removeRow(tableSeries.getSelectedRow());
                tableSeries.setModel(model1);
            }
        });

        ButtonConfirm.addActionListener(new ActionListener() {
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
                        System.out.println("środki przed obstawieniem zakladu: " + srodki);
                        srodki = srodki - Float.parseFloat(textFieldAnte.getText());
                    }

                    if (srodki >= Float.parseFloat(textFieldAnte.getText())) {

                        int rowCount = model1.getRowCount();
                        int id_obstawienia=-1,id_serii=-1, id_konta=-1, id_zakladu=-1,typ=-1;
                        float kurs1=-1,kurs2=-1,kurs3=-1, kurs_calkowity=-1;
                        Date data_utworzenia=null;
                        Date data_rozliczenia=null;
                        String nazwaTypu = "", nazwaDr1,nazwaDr2;

                            // odczytanie obecnej daty bazy danych
                            System.out.println("Zaczynam dodawać obstawienia");
                            try{
                                statement = connection.prepareStatement("SELECT sysdate from dual");
                                statement.execute();
                                result = statement.getResultSet();

                                while (result.next())
                                {
                                    data_utworzenia=result.getDate("sysdate");
                                }
                            } finally {
                                //connection.close();
                            }

                            // Ustalenie id_serii
                            try{
                                statement = connection.prepareStatement("select max(id_serii) as id_serii from seria");
                                statement.execute();
                                result = statement.getResultSet();

                                while (result.next())
                                {
                                    id_serii=result.getInt("id_serii");
                                    id_serii++;
                                }
                            } finally {
                                //connection.close();
                            }

                            for (int i = rowCount - 1; i >= 0; i--) {
                                // Ustalenie id_obstawienia
                                System.out.println("Wszedłem do pętli rowCount = "+rowCount+" i = "+rowCount);

                                try {
                                    statement = connection.prepareStatement("select max(id_obstawienia) as id_obstawienia from obstawienie");
                                    statement.execute();
                                    result = statement.getResultSet();


                                    while (result.next())
                                    {
                                        id_obstawienia=result.getInt("id_obstawienia");
                                        id_obstawienia++;
                                    }
                                } finally {
                                    //connection.close();
                                }
                                id_zakladu = (int)tableSeries.getValueAt(i,0);
                                nazwaDr1 = tableSeries.getValueAt(i,2).toString();
                                nazwaDr2 = tableSeries.getValueAt(i,3).toString();
                                data_rozliczenia = (Date)tableSeries.getValueAt(i,4);
                                kurs1 = (Float)tableSeries.getValueAt(i,5);
                                kurs2 = (Float)tableSeries.getValueAt(i,6);
                                kurs3 = (Float)tableSeries.getValueAt(i,7);
                                nazwaTypu = tableSeries.getValueAt(i,8).toString();

                                System.out.println("druzyna1: "+nazwaDr1+" druzyna2: "+nazwaDr2);

                                if(nazwaTypu==nazwaDr1)
                                    typ=1;
                                else if(nazwaTypu==nazwaDr2)
                                    typ=2;
                                else if(nazwaTypu=="remis")
                                    typ=3;

                                // Do obliczeń kursu całkowitego serii
                                if(typ==1)
                                    kurs_calkowity+=kurs1;
                                else if(typ==2)
                                    kurs_calkowity+=kurs2;
                                else if(typ==3)
                                    kurs_calkowity+=kurs3;

                                if(once == 1) {
                                    once = 0;
                                    kurs_calkowity /= (float) rowCount;

                                    try {
                                        statement = connection.prepareStatement("select id_konta from konto where id_uzytkownika = ?");
                                        statement.setInt(1, uzytkownik.getId_uzytkownika());
                                        statement.execute();
                                        result = statement.getResultSet();

                                        while (result.next()) {
                                            id_konta = result.getInt("id_konta");
                                        }
                                    } finally {
                                        //connection.close();
                                    }

                                    try {
                                        statement = connection.prepareStatement("insert into seria" +
                                                " values (?,?,?,?)");
                                        statement.setInt(1, id_serii);
                                        statement.setFloat(2, kurs_calkowity);
                                        statement.setFloat(3, Float.parseFloat(textFieldAnte.getText()));
                                        statement.setInt(4, id_konta);

                                        statement.executeUpdate();
                                    } finally {
                                        //connection.close();
                                    }
                                }

                                System.out.println("Jestem przed dodaniem obstawienia");
                                // Dodanie obstawienia
                                try{
                                    statement = connection.prepareStatement("insert into obstawienie" +
                                            " values (?,?,?,?,?,?)");
                                    statement.setInt(1, id_obstawienia);
                                    statement.setInt(2, id_serii);
                                    statement.setInt(3, id_zakladu);
                                    statement.setDate(4, data_utworzenia);
                                    statement.setDate(5, data_rozliczenia);
                                    statement.setInt(6, typ);

                                    statement.executeUpdate();
                                } finally {
                                    //connection.close();
                                }
                                System.out.println("Dodałem obstawienie");

                            }



                            for (int i = rowCount - 1; i >= 0; i--) {
                                model1.removeRow(i);
                            }
                            tableSeries.setModel(model1);


                        // Aktualizacja stanu konta
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

                        }
                    } else
                        System.out.println("Nie ma wystarczających środków na koncie!");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        RadioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadioButton2.setSelected(false);
                RadioButtonDraw.setSelected(false);
            }
        });
        RadioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadioButton1.setSelected(false);
                RadioButtonDraw.setSelected(false);
            }
        });
        RadioButtonDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadioButton1.setSelected(false);
                RadioButton2.setSelected(false);
            }
        });
    }

    void funkcja(){

        tableBets.getValueAt(tableBets.getSelectedRow(),tableBets.getSelectedColumn());
    }

    void showBets()
    {
        tableBets.setModel(model);

        try {
            statement = connection.prepareStatement("SELECT ID_ZAKLADU, ID_DYSCYPLINY, KURS1, KURS2, KURS3," +
                    " DATA_ROZPOCZECIA, DATA_ZAKONCZENIA, ID_DRUZYNY1, ID_DRUZYNY2, WYGRANY FROM ZAKLAD where DATA_ZAKONCZENIA<(SELECT sysdate from dual )AND id_dyscypliny=2");
            statement.execute();
            result = statement.getResultSet();
            model.setRowCount(0);
            while (result.next())
            {
                int id_zakladu= result.getInt(1);
                int id_dyscypliny= result.getInt(2);
                float kurs1= result.getFloat(3);
                float kurs2= result.getFloat(4);
                float kurs3= result.getFloat(5);
                Date data_zakonczenia= result.getDate(7);
                int id_druzyny1= result.getInt(8);
                int id_druzyny2= result.getInt(9);

                String nazwaDr1="", nazwaDr2="", nazwaDysc="";
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

                // Pobranie nazwy dyscypliny
                statement1 = connection.prepareStatement("select nazwa from dyscypliny where ID_DYSCYPLINY =?");

                statement1.setInt(1, id_dyscypliny);

                statement1.execute();
                result1 = statement1.getResultSet();
                while(result1.next()) {
                    nazwaDysc = result1.getString("nazwa");
                }

                model.addRow(new Object[]{id_zakladu,nazwaDysc,nazwaDr1,nazwaDr2,data_zakonczenia,kurs1,kurs2,kurs3});
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        tableBets.setModel(model);
    }
}
