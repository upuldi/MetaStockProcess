import java.sql.*;


/**
 * Created by IntelliJ IDEA.
 * User: udoluweera
 * Date: 4/28/11
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class JDBCTEST {

    public static void main(String[] args) {
      System.out.println("Inserting values in Mysql database table!");
      Connection con = null;
      String url = "jdbc:mysql://localhost:3306/";
      String db = "CSEDAILYDATA";
      String driver = "com.mysql.jdbc.Driver";
      try{
        Class.forName(driver);
        con = DriverManager.getConnection(url + db, "root", "root");
        try{
          Statement st = con.createStatement();
          int val = st.executeUpdate("INSERT CSEDAILYDATA.RT_DATA VALUES("+1+","+"'BFL'"+")");
          System.out.println("1 row affected");
        }
        catch (SQLException s){
          System.out.println("SQL statement is not executed!");
          s.printStackTrace();
        }
      }
      catch (Exception e){
        e.printStackTrace();
      }
    }

}
