package events;
import java.sql.*;
public class MySqlConnect {
    
    static Connection  conn; 
    String sql;
    PreparedStatement pst=null;
    Statement stmt;
    ResultSet rs=null;
    
    public static Connection connectDB() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Events?useSSL=false","root","");
            
            if (conn == null) {
            System.out.println("Connection cannot be established");
        }
            else{
              System.out.println("Connection established"); 
            }
        return conn;    
            }
            catch(Exception e2){
                System.err.println(e2);
            }
        return null;
        }
}
