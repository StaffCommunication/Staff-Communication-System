package loginui;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.sql.*;


public class Table{
    
    public static TableView <Person>events=new TableView();
    //public static  ObservableList<Person> data=FXCollections.observableArrayList();
    static HBox hb=new HBox();
    
    static Connection  conn; 
    static String sql;
    static PreparedStatement pst=null;
    static Statement stmt=null;
    static ResultSet rs=null;
    
    public static StackPane table1(){
        events.setMaxWidth(500);
        
        TableColumn event = new TableColumn("Event");
        event.setMinWidth(200);
        event.setCellValueFactory(new PropertyValueFactory<>("Event"));
 
        TableColumn venue = new TableColumn("Venue");
        venue.setMinWidth(200);
        venue.setCellValueFactory(new PropertyValueFactory<>("Venue"));
        
        TableColumn date = new TableColumn("Date");
        date.setMinWidth(200);
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        events.getColumns().addAll(event,venue,date);
        
        
            try{
              MyConnect.connectDB();
              String query="select * from EventTable";
              pst=MyConnect.conn.prepareStatement(query);
              rs=pst.executeQuery();
              while(rs.next()){
                  ObservableList<Person> data=FXCollections.observableArrayList();
                  data.remove(data);
                  //for(int)
                  data.add(new Person (
                          //rs.getMetaData().ge
                    rs.getString("Events"),
                    rs.getString("Venue"),
                    rs.getString("Date")
                  ));
                  
                events.setItems(data);
                
                
              }
              pst.close();
              rs.close();
            }
            catch(Exception e2){
                System.err.println(e2);
            }
       
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(events);
        StackPane stack =new StackPane();
        stack.getChildren().add(vbox);        
        return stack;
    }
    
    
}
