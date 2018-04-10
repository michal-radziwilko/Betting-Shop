package GUI;

import App.Uzytkownik;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Core {
    public JPanel mainPanel;
    private JTextField textFieldName;
    private JTextField textFieldMoney;
    private JButton buttonHistory;
    private JButton buttonAnte;
    private JButton buttonWithdraw;
    private JButton buttonPlay;
    private JButton buttonShow;
    private JButton buttonModify;
    Connection connection;
    ResultSet result;
    PreparedStatement statement;
    Uzytkownik uzytkownik;


    public Core(Uzytkownik uz) {
        this.uzytkownik = uz;
        Actualize(uz);
         try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
             connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        buttonModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int poziom_dostepu=-1;

                    try{
                    statement = connection.prepareStatement("select POZIOM_DOSTEPU\n" +
                            "from uprawnienia\n" +
                            "where\n" +
                            "id_uprawnienia =\n" +
                            "(\n" +
                            "Select id_uprawnienia\n" +
                            "from konto\n" +
                            "where\n" +
                            "id_konta=(SELECT ID_KONTA from KONTO where ID_UZYTKOWNIKA=?)\n" +
                            ")");

                    statement.setInt(1, uzytkownik.getId_uzytkownika());

                    statement.execute();
                    result = statement.getResultSet();

                    while (result.next()) {
                        poziom_dostepu = result.getInt("poziom_dostepu");
                    }

                    } finally {
                        //It's important to close the statement when you are done with it
                        statement.close();
                    }

                    if(poziom_dostepu==1)
                        System.out.println("Poziom dostepu uzytkownika");

                    else if (poziom_dostepu==2) {
                        System.out.println("Poziom dostepu moderatora");

                        JFrame modifyFrame = new JFrame("Modyfikacja");
                        Modify ModifyFrame = new Modify();
                        modifyFrame.setContentPane(ModifyFrame.mainPanel);
                        modifyFrame.pack();
                        modifyFrame.setVisible(true);
                        modifyFrame.setLocationRelativeTo(null);
                    }


                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame showBetsFrame = new JFrame("Przeglądanie zakładów");
                ShowBets ShowBetsFrame = new ShowBets();
                showBetsFrame.setContentPane(ShowBetsFrame.mainPanel);
                showBetsFrame.pack();
                showBetsFrame.setVisible(true);
                showBetsFrame.setLocationRelativeTo(null);
            }
        });
        buttonAnte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame anteFrame = new JFrame("Wpłata pieniędzy");
                Ante AnteFrame = new Ante(uzytkownik, Core.this);
                anteFrame.setContentPane(AnteFrame.mainPanel);
                anteFrame.pack();
                anteFrame.setVisible(true);
                anteFrame.setLocationRelativeTo(null);
            }
        });
        buttonWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame withdrawFrame = new JFrame("Wypłata pieniędzy");
                Withdraw WithdrawFrame = new Withdraw(uzytkownik, Core.this);
                withdrawFrame.setContentPane(WithdrawFrame.mainPanel);
                withdrawFrame.pack();
                withdrawFrame.setVisible(true);
                withdrawFrame.setLocationRelativeTo(null);
            }
        });
        buttonHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame historyFrame = new JFrame("Historia zakładów");
                History HistoryFrame = new History(uzytkownik);
                historyFrame.setContentPane(HistoryFrame.mainPanel);
                historyFrame.pack();
                historyFrame.setVisible(true);
                historyFrame.setLocationRelativeTo(null);
            }
        });
        buttonPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame betFrame = new JFrame("Obstawianie");
                Bet BetFrame = new Bet(uzytkownik);
                betFrame.setContentPane(BetFrame.mainPanel);
                betFrame.pack();
                betFrame.setVisible(true);
                betFrame.setLocationRelativeTo(null);
            }
        });
    }
    public void Actualize(Uzytkownik uz) {
        this.uzytkownik = uz;
        textFieldName.setText(uzytkownik.getImie() + " " + uzytkownik.getNazwisko());
        textFieldMoney.setText("Środki: " + uzytkownik.getSrodki());
    }
}
