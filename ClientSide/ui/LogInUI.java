
package loginui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.stage.Stage;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LogInUI extends Application {
    ResultSet rs1;
    Connection  conn; 
    String sql;
    PreparedStatement pst=null;
    Statement stmt;
    ResultSet rs=null;
    int r;
    String u,p;
    Stage stage;
    @Override
    public void start(Stage primaryStage) {
        stage=primaryStage;
        GridPane grid = new GridPane();
        Label username =new Label("Username");
        TextField textuser=new TextField();
        textuser.setPromptText("Username");
        
        //password label
        Label password=new Label("Password");
        PasswordField textpass=new PasswordField();
        textpass.setPromptText("Password");
        Button login=new Button("Sign");
        
        Label displayError=new Label();
        RowConstraints row=new RowConstraints(20);
        grid.getRowConstraints().addAll(row,row,row);
        //gridpane2.setGridLinesVisible(true);
        ColumnConstraints col1=new ColumnConstraints(80);
        ColumnConstraints col2=new ColumnConstraints(150);
        ColumnConstraints col3=new ColumnConstraints(200);
        grid.getColumnConstraints().addAll(col1,col2,col3);
        grid.setVgap(20);
        grid.add(displayError, 0, 0,3,1);
        grid.add(username, 0, 1);
        grid.add(textuser, 1, 1);
        grid.add(password, 0, 2);
        grid.add(textpass, 1, 2);
        grid.add(login, 1, 3);
        grid.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(grid, 500, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Communication System");
        primaryStage.show();
        
       // login.setStyle("-fx-background-color: cornflowerblue;-fx-padding: 5");
        
        //open main scene
        StackPane stack=new StackPane();
        Scene scene2=new Scene(stack,800,600);
        
            login.setOnAction(new EventHandler<ActionEvent>() {   
            @Override
            public void handle(javafx.event.ActionEvent event) {
                try{
                MyConnect.connectDB();
                String sql="select * from logInfo where Name = ? and password = ? ";
                pst=MyConnect.conn.prepareStatement(sql);
                pst.setString(1, textuser.getText());
                pst.setString(2, textpass.getText());
                rs=pst.executeQuery();
                
                if(textuser.getText().equals("") && textpass.getText().equals("")){
                    //Error.emptyPassword();
                    displayError.setText("Username and password fields empty");
                    return;
                } 
                else if(textuser.getText().equals("")){
                    displayError.setText("Username field empty");
                    return;
                }
                else if(textpass.getText().equals("")){
                    displayError.setText("Password field empty");
                    return;
                }
                else if((!textuser.getText().equals("") || !textpass.getText().equals("")) && rs.next()){
                    
                   MainUI open = new MainUI();
                   stack.getChildren().add(open);
                   primaryStage.setScene(scene2);
                    
                }
                else if((!textuser.getText().equals("") || !textpass.getText().equals("")) && !rs.next()){
                    Error.display();
                }
                
        
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    
                }finally{
                    try {
                        pst.close();
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(LogInUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
         }
                }
            
        });       
       
}
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
