package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;

public class Andmebaas {

    private Connection connection;
    private Statement statement;
    private ResultSet result;


    public Andmebaas(){
        try {

            Class.forName("com.mysql.jdbc.Driver");
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Pood","root","");
                statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println(e);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Puudub ühendus andmebaasiga");
        }
    }

    public ObservableList<DataItem> tagastaAndmebaas(){
        ObservableList<DataItem> koguInfo = FXCollections.observableArrayList ();
        try {
            String query = "SELECT * FROM Jalatsid";
            result = statement.executeQuery(query);
            while (result.next()){
                koguInfo.add(new DataItem(result.getString("Firma"),result.getString("Model"),result.getString("Suurus"),result.getString("Hind")));
            }
        }catch(Exception e) {System.out.println("Ei saa lugeda andmebaasi");}

        return koguInfo;
    }


    public void näitaAndmebaas() {
        try {
            String query = "SELECT * FROM Jalatsid";
            result = statement.executeQuery(query);

            ArrayList<String> firmad = new ArrayList<>();
            ArrayList<String> mudelid = new ArrayList<>();
            ArrayList<String> suurused = new ArrayList<>();
            ArrayList<String> hinnad = new ArrayList<>();

            //Leiab vormistamiseks kõige suuremad sõnede pikkused
            int[] suurimadPikkused = {5,5,6,2};
            while(result.next()) {
                firmad.add(result.getString("Firma"));
                mudelid.add(result.getString("Model"));
                suurused.add(result.getString("Suurus"));
                hinnad.add(result.getString("Hind") + " €");
                suurimadPikkused[0] = Math.max(suurimadPikkused[0],firmad.get(firmad.size()-1).length());
                suurimadPikkused[1] = Math.max(suurimadPikkused[1],mudelid.get(mudelid.size()-1).length());
                suurimadPikkused[2] = Math.max(suurimadPikkused[2],suurused.get(suurused.size()-1).length());
                suurimadPikkused[3] = Math.max(suurimadPikkused[3],hinnad.get(hinnad.size()-1).length());
            }

            //Prindib tabeli ülemise osa võttes teadmiseks iga veeru pikima sõne pikkuse
            System.out.println("Firma"+repeat(" ",suurimadPikkused[0]-5)+"|Model"+repeat(" ",suurimadPikkused[1]-5)+"|Suurus"+repeat(" ",suurimadPikkused[2]-6)+"|Hind"+repeat(" ",suurimadPikkused[3]-4)+"|");
            System.out.println(repeat("-",suurimadPikkused[0])+"+"+repeat("-",suurimadPikkused[1])+"+"+repeat("-",suurimadPikkused[2])+"+"+repeat("-",suurimadPikkused[3])+"+");

            //Prindib tabeli sisu
            int i = 0;
            for(String f :firmad) {
                System.out.print(firmad.get(i)+repeat(" ",suurimadPikkused[0]-firmad.get(i).length())+"|");
                System.out.print(mudelid.get(i)+repeat(" ",suurimadPikkused[1]-mudelid.get(i).length())+"|");
                System.out.print(suurused.get(i)+repeat(" ",suurimadPikkused[2]-suurused.get(i).length())+"|");
                System.out.print(hinnad.get(i)+repeat(" ",suurimadPikkused[3]-hinnad.get(i).length())+"|");
                System.out.println();
                i++;
            }
            System.out.println();
        }catch(Exception e) {System.out.println("Ei saa lugeda andmebaasi");}

    }

    String repeat (String sis,int korda) {
        //Eesmärgiliselt sama mis pythonis sõne*int
        StringBuilder s = new StringBuilder();
        for(;korda > 0;korda--) {
            s.append(sis);
        }
        return s.toString();
    }

    int AndmebaasiLaius() {
        //Hetkel see funktsioon kasutuses pole aga sellega saaks tulevikus tabeli printimise paindlikumaks - saaks kasutada tabeleid millel rohkem kui 4 veergu
        try {
            String query = "SELECT * FROM Jalatsid";
            result = statement.executeQuery(query);
            ResultSetMetaData metaData = result.getMetaData();
            return metaData.getColumnCount();
        }catch(Exception e) {System.out.println("Ei saa lugeda andmebaasi");}
        return 0;
    }

    boolean lisaAndmebaasi(String[] uus_toode) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Jalatsid (Firma, Model, Suurus, Hind) values (?,?,?,?)");
            preparedStatement.setString(1, uus_toode[0]);
            preparedStatement.setString(2, uus_toode[1]);
            preparedStatement.setString(3, uus_toode[2]);
            preparedStatement.setString(4, uus_toode[3]);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Ei saanud lisada andmebaasi uut toodet");
        }
        return false;
    }

    int leiaIndeks(String[] toode){

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Jalatsid WHERE Firma = ? AND  Model = ? AND Suurus = ? AND Hind = ?");
            preparedStatement.setString(1, toode[0]);
            preparedStatement.setString(2, toode[1]);
            preparedStatement.setString(3, toode[2]);
            preparedStatement.setString(4, toode[3]);
            result = preparedStatement.executeQuery();
            if(result.next())
                return Integer.valueOf(result.getString("id"));
        } catch (SQLException e) {
            System.out.println("Ei leidnud sellist toodet");
        }
        return -1;
    }

    boolean kustutaAndmebaasisit(int indeks) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Jalatsid WHERE id = ?");
            preparedStatement.setString(1, String.valueOf(indeks));
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Ei saanud lisada andmebaasi uut toodet");
        }
        return false;

    }

    boolean muudaAndmebaas(int indeks, String[] uus) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Jalatsid SET Firma=?,Model=?,Suurus=?,Hind=? WHERE id=?");
            preparedStatement.setString(1, uus[0]);
            preparedStatement.setString(2, uus[1]);
            preparedStatement.setString(3, uus[2]);
            preparedStatement.setString(4, uus[3]);
            preparedStatement.setInt(5, indeks);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Ei saanud uuendada");
        }
        return false;
    }
}
