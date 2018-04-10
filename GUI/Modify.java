package GUI;

import App.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Modify {
    private JButton buttonAdd;
    private JButton buttonDelete;
    private JButton buttonModify;
    public JPanel mainPanel;
    private JTable tableBets;
    public Connection connection;
    public ResultSet result, result1;
    public PreparedStatement statement, statement1;
    public DefaultTableModel model = new DefaultTableModel(new String[]{"id_zakladu","Dyscyplina","Druzyna 1", "Druzyna 2", "Data rozgrywki",
            "Kurs na drużynę 1", "Kurs na drużynę 2", "Kurs na remis","Wygrany"}, 0);


    public Modify() {
        refresh();
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame addChangeFrame = new JFrame("Dodaj zakład");
                AddChange AddChange = new AddChange("add", 0,"","","",null,0,0,0,0, Modify.this);
                addChangeFrame.setContentPane(AddChange.mainPanel);
                addChangeFrame.pack();
                addChangeFrame.setVisible(true);
                addChangeFrame.setLocationRelativeTo(null);
            }
        });
        buttonModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_zakladu = (int)tableBets.getValueAt(tableBets.getSelectedRow(),0);
                String nazwaDysc = tableBets.getValueAt(tableBets.getSelectedRow(),1).toString();
                String nazwaDr1 = tableBets.getValueAt(tableBets.getSelectedRow(),2).toString();
                String nazwaDr2 = tableBets.getValueAt(tableBets.getSelectedRow(),3).toString();
                Date data_zakonczenia = (Date)tableBets.getValueAt(tableBets.getSelectedRow(),4);
                float kurs1 = Float.parseFloat(tableBets.getValueAt(tableBets.getSelectedRow(),5).toString());
                float kurs2 = Float.parseFloat(tableBets.getValueAt(tableBets.getSelectedRow(),6).toString());
                float kurs3 = Float.parseFloat(tableBets.getValueAt(tableBets.getSelectedRow(),7).toString());
                int wygrany = (int)tableBets.getValueAt(tableBets.getSelectedRow(),8);

                JFrame addChangeFrame = new JFrame("Edytuj zakład");
                AddChange AddChange = new AddChange("modify", id_zakladu,nazwaDysc,nazwaDr1,nazwaDr2,data_zakonczenia,kurs1,kurs2,kurs3,wygrany, Modify.this);
                addChangeFrame.setContentPane(AddChange.mainPanel);
                addChangeFrame.pack();
                addChangeFrame.setVisible(true);
                addChangeFrame.setLocationRelativeTo(null);

            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id_zakladu = (int)tableBets.getValueAt(tableBets.getSelectedRow(),0);;

                try {
                    statement1 = connection.prepareStatement("delete from zaklad where id_zakladu = ?");

                    statement1.setInt(1, id_zakladu);
                    statement1.execute();

                    model.removeRow(tableBets.getSelectedRow());
                    tableBets.setModel(model);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


            }
            });

    }
    public void refresh(){
        tableBets.setModel(model);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            statement = connection.prepareStatement("SELECT z.* ,CASE WHEN (SELECT COUNT(*) FROM obstawienie o WHERE (o.id_zakladu = z.id_zakladu AND DATA_ZAKONCZENIA<(SELECT sysdate from dual))) > 0 THEN 1 ELSE 0 END AS child FROM   zaklad z");
            statement.execute();
            result = statement.getResultSet();
            model.setRowCount(0);
            while (result.next())
            {
                int child = result.getInt("child");
                boolean doesHaveAChild;
                if(child == 0)
                    doesHaveAChild=false;
                else
                    doesHaveAChild=true;

                if(!doesHaveAChild) {
                    int id_zakladu = result.getInt(1);
                    int id_dyscypliny = result.getInt(2);
                    float kurs1 = result.getFloat(3);
                    float kurs2 = result.getFloat(4);
                    float kurs3 = result.getFloat(5);
                    Date data_zakonczenia = result.getDate(7);
                    int id_druzyny1 = result.getInt(8);
                    int id_druzyny2 = result.getInt(9);
                    int wygrany = result.getInt("wygrany");

                    String nazwaDr1 = "", nazwaDr2 = "", nazwaDysc = "";
                    // Pobranie nazwy druzyny 1
                    statement1 = connection.prepareStatement("select nazwa from druzyny where id_druzyny =?");

                    statement1.setInt(1, id_druzyny1);

                    statement1.execute();
                    result1 = statement1.getResultSet();
                    while (result1.next()) {
                        nazwaDr1 = result1.getString("nazwa");
                    }

                    // Pobranie nazwy druzyny 2
                    statement1 = connection.prepareStatement("select nazwa from druzyny where id_druzyny =?");

                    statement1.setInt(1, id_druzyny2);

                    statement1.execute();
                    result1 = statement1.getResultSet();
                    while (result1.next()) {
                        nazwaDr2 = result1.getString("nazwa");
                    }

                    // Pobranie nazwy dyscypliny
                    statement1 = connection.prepareStatement("select nazwa from dyscypliny where ID_DYSCYPLINY =?");

                    statement1.setInt(1, id_dyscypliny);

                    statement1.execute();
                    result1 = statement1.getResultSet();
                    while (result1.next()) {
                        nazwaDysc = result1.getString("nazwa");
                    }

                    model.addRow(new Object[]{id_zakladu, nazwaDysc, nazwaDr1, nazwaDr2, data_zakonczenia, kurs1, kurs2, kurs3,wygrany});
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        tableBets.setModel(model);
    }

}

