package events;

import java.util.logging.Logger;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import java.sql.*;
import java.util.logging.Level;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
public class Events extends Application {
    Button submit=new Button("Submit");
    Label eventName;
    Label eventDate;
    Label eventVenue;
    Label eventDescribe;
    TextField nameField;
    TextField dateField;
    TextField venueField;
    TextArea describeField;
    TextField options;
    TextField faculty;
    TextField department;
    
    Menu mainMenu;
    MenuButton mainButton;
    static Connection  conn; 
    static String sql;
    static PreparedStatement pst=null;
    static Statement stmt=null;
    static ResultSet rs=null;
    
    ComboBox<Object> distribute,faculties,departments;
    ObservableList<Object> facList = FXCollections.observableArrayList("Science", "Medicine", "Education");
    ObservableList<Object> DepList = FXCollections.observableArrayList("Computer science", "Mathematics", "Biochemistry");
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        TabPane tabs=new TabPane();
        Tab newTab=new Tab();
        newTab.setText("New");
        newTab.setContent(createEvent());
        
        Tab exploreTab=new Tab();
        exploreTab.setText("Explore");
        exploreTab.setContent(viewEvents());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(newTab,exploreTab);
        Scene scene=new Scene(tabs,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createEvent(){
        eventName=new Label("Event Name");
        eventDate=new Label("Date");
        eventVenue=new Label("Venue");
        eventDescribe=new Label("Description");
        nameField=new TextField();
        dateField=new TextField();
        venueField=new TextField();
        describeField=new TextArea();
        describeField.setPrefRowCount(5);
        Label send=new Label("Distribute To");
        options = new TextField();
        faculty = new TextField();
        department = new TextField();
        
       mainOptions();
        
        GridPane grid=new GridPane();
        ColumnConstraints col=new ColumnConstraints(100);
        ColumnConstraints col2=new ColumnConstraints(250);
        grid.add(eventName, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(eventDate, 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(eventVenue, 0, 2);
        grid.add(venueField, 1, 2);
        grid.add(eventDescribe, 0, 3);
        grid.add(describeField, 1, 3);
        grid.add(send, 0, 4);
        grid.add(mainButton, 1, 4);
        //grid.add(faculty, 2, 4);
        grid.add(submit, 1, 5);
        
        grid.getColumnConstraints().addAll(col,col2);
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(20);
        
         MySqlConnect.connectDB();
        submit.setOnAction((e)->{ 
           
            addListener();
        });
        
        
        return grid;
    }
    public GridPane viewEvents()
    {
        GridPane grid =new GridPane();
        ColumnConstraints col1 =  new ColumnConstraints(100);
        grid.add(new Label("Event   Title"), 0, 0);
        grid.add(new Label("Event Date"), 1, 0);
        grid.add(new Label("Venue"), 2, 0);
        grid.add(new Label("Details of Event"), 3, 0);
        grid.add(new Label("Reveived"), 4, 0);
        grid.setHgap(30);
        
        grid.getColumnConstraints().addAll(col1,col1,col1,col1,col1);
        
        RowConstraints row1=new RowConstraints(800);
        
        return grid;
    }
    public void addListener(){
    try{
             sql="INSERT INTO  eventsdetails(EventName,EventDate,EventVenue,EventDescription) "
                      + "values(?,?,?,?)";
               //stmt = this.conn.createStatement();
               //stmt.executeUpdate("insert into eventsdetails(EventName,EventDate,EventVenue,EventDescription ) values ('" + nameField+ "','"+dateField+ "','"+venueField+ "',,"+describeField + "')");
              pst = conn.prepareStatement(sql);
              
              String name=nameField.getText();
              String date=dateField.getText();
              String venue=venueField.getText();
              String des=describeField.getText();
              
              pst.setString(1,name);
              pst.setString(2,date);
              pst.setString(3,venue);
              pst.setString(4,des);
              
              pst.execute();
              
              int i = pst.executeUpdate();
              if(i == 0){
                    Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, "Update failed");
              }else{
                  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                  alert.initModality(Modality.APPLICATION_MODAL);
                  
                  alert.setContentText("Successfully added");
              }
              
              
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            catch(Exception e2){
                System.err.println(e2);
            }
    }
    public void mainOptions(){
       
        
        Menu contMenu=new Menu("Contacts");
        Menu facultyMenu=new Menu("Faculty");
        Menu departmentMenu=new Menu("Department");
        
        MenuItem science=new MenuItem("Science");
        MenuItem edu=new MenuItem("Education & Community Studies");
        MenuItem med=new MenuItem("Health Sciences");
        MenuItem law=new MenuItem("law");
        MenuItem eng=new MenuItem("Engineering & Technology");
        MenuItem vet=new MenuItem("Veterinary Medicine $Surgery");
        MenuItem env=new MenuItem("Environment & Resource Development");
        MenuItem agri=new MenuItem("Agriculture");
        MenuItem arts=new MenuItem("Arts & Social Sciences");
        MenuItem comm=new MenuItem("Commerce");
        
        
        facultyMenu.getItems().addAll(science,edu,med,law,eng,vet,env,agri,arts,comm);
        
        MenuItem cs=new MenuItem("Computer Science");
        MenuItem as=new MenuItem("Animal Science");
        MenuItem ag=new MenuItem("Agricultural Economics");
        MenuItem bioc=new MenuItem("Boichemistry");
        MenuItem rs=new MenuItem("Religious Study");
        MenuItem mth=new MenuItem("mathematics");
        
        departmentMenu.getItems().addAll(cs,as,ag,bioc,rs,mth);
       
        
        
        mainButton = new MenuButton();
        mainButton.setPrefWidth(300);
        mainButton.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        mainButton.setAlignment(Pos.CENTER);
        mainButton.getItems().addAll(facultyMenu,departmentMenu,contMenu);

        
      
        
    }
}
    