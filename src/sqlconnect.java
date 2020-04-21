import java.sql.*;
import javax.swing.*;
import java.sql.DriverManager;
public class sqlconnect {
    
    
    
    public static Connection ConnectDB(){
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            //Class.forName("org.sqlite.JDBC");
            String url = "jdbc:mysql://localhost/computer_shop";
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/connect","root","root");
           Connection conn = DriverManager.getConnection(url, "root", "123456");
            //JOptionPane.showMessageDialog(null, "Connection Established");
            return conn;
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            
        }
        return null;
    }
}
