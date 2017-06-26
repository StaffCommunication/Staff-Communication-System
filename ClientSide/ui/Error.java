package loginui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Error {
    
    public  static void display(){
        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(300);
        window.setMinHeight(100);
        
        Label label=new Label("Invalid username or password");
        
        StackPane layout=new StackPane(label);
        Scene scene=new Scene(layout);
        
        window.setScene(scene);
        window.showAndWait();
        
        
    }
    public  static void emptyPassword(){
        Alert fail=new Alert(Alert.AlertType.INFORMATION);
                    fail.setHeaderText("failure");
                    fail.setContentText("User and password fields cannot be empty");
                    fail.showAndWait();
        
        
    }
    
}
