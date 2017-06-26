package loginui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreatedUsers {
    static ObservableList listDisplay =FXCollections.observableArrayList();
    static ListView users=new ListView(listDisplay);
    
    static Connection  conn; 
    static String sql;
    static PreparedStatement pst=null;
    static Statement stmt;
    static ResultSet rs=null;
    
    public static HBox displayAll(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Users?useSSL=false","root",""); 
            String query="select Name from logInfo";  
            pst=conn.prepareStatement(query);
            rs=pst.executeQuery();
              
            while(rs.next()){
              listDisplay.add(rs.getString("Name"));
            }
            pst.close();
            rs.close();
            }
            catch(Exception ex){
                Logger.getLogger(CreatedUsers.class.getName()).log(Level.SEVERE, null, ex);
                        
            }
        
     
        HBox hbox=new HBox(users);
        return hbox;
    }
    
}
