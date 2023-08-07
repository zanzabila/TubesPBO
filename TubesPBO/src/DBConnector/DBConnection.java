package DBConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private Connection connection;
    private Statement statement;
    private ResultSet rs;
    
    public DBConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restoran", "root", "");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.print(ex);
        }
    }
    
    public ResultSet getData(String SQLString){
        try {
            rs = statement.executeQuery(SQLString);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return rs;
    }
     
    public void query (String SQLString){
        try {
            statement.execute(SQLString);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}