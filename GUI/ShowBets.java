package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ShowBets extends JScrollPane {

    public JPanel mainPanel;
    public Connection connection;
    public ResultSet result, result1;
    public PreparedStatement statement, statement1;
    public DefaultTableModel model = new DefaultTableModel(new String[]{"Dyscyplina","Druzyna 1", "Druzyna 2", "Data rozgrywki",
            "Kurs na drużynę 1", "Kurs na drużynę 2", "Kurs na remis"}, 0);
    public JTable tableBets;
    public JScrollPane scrollPane;

    ShowBets(){
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
            statement = connection.prepareStatement("SELECT ID_ZAKLADU, ID_DYSCYPLINY, KURS1, KURS2, KURS3," +
                    " DATA_ROZPOCZECIA, DATA_ZAKONCZENIA, ID_DRUZYNY1, ID_DRUZYNY2, WYGRANY FROM ZAKLAD where DATA_ZAKONCZENIA<(SELECT sysdate from dual)");
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

                model.addRow(new Object[]{nazwaDysc,nazwaDr1,nazwaDr2,data_zakonczenia,kurs1,kurs2,kurs3});
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        tableBets.setModel(model);
    }
    void funkcja(){
        tableBets.getValueAt(tableBets.getSelectedRow(),tableBets.getSelectedColumn());
    }
}
