
package sysadmin;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import sysadmin.net.Connector;
import sysadmin.ui.UIManager;


public class AdminApp extends Application
{
    @Override
    public void start(Stage primaryStage) {
        //connect to server first
        Connector conn = new Connector();
        UIManager uiManager;
        
        if(!conn.connect()){
            Alert alrt = new Alert(Alert.AlertType.ERROR);
            alrt.setTitle("-- Error --");
            alrt.setHeaderText("Network error");
            alrt.setContentText("Could not connect to server");
            alrt.showAndWait();
        }
        else{
            //create ui manager
            uiManager = new UIManager(primaryStage);
            //set up scenes
            uiManager.setUpScenes(conn);
            //show
            uiManager.setVisible();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
