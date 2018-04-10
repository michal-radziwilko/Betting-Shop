package App;

import java.sql.*;

public class Uzytkownik {
    public Uzytkownik(int id_uzytkownika, String imie, String nazwisko, String email, String login, String haslo) {
        this.id_uzytkownika = id_uzytkownika;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.login = login;
        this.haslo = haslo;


        Connection connection = null;
        ResultSet result;
        PreparedStatement statement;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@<DATABASE ADDRESS>","<DATABASE LOGIN>","<DATABASE PASSWORD");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            try {
                statement = connection.prepareStatement("SELECT srodki from konto where ID_UZYTKOWNIKA=?");

                statement.setInt(1, getId_uzytkownika());

                statement.execute();

                result = statement.getResultSet();

                while (result.next()) {
                    srodki = Float.toString(result.getFloat("srodki"));
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
        }
    }


    private final int id_uzytkownika;
    private String imie;
    private String nazwisko;
    private String email;
    private String login;
    private String haslo;
    private String srodki;

    public int getId_uzytkownika() {
        return id_uzytkownika;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getHaslo() {
        return haslo;
    }

    public String getSrodki() {
        return srodki;
    }
    public void setSrodki(String srodki) {this.srodki = srodki;}
}
