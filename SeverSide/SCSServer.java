
package scsserver;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scsserver.db.DB;
import scsserver.ui.UIManager;

public class SCSServer extends Application {
    
    @Override
    public void start(Stage stage) {
        
        //create a DB object
        DB dbConnection = new DB();
        //create a layout manager
        BorderPane rootP = new BorderPane();
        
        //create a scene
        UIManager uiManager = new UIManager(rootP, dbConnection);
        uiManager.setUp();
 
        stage.setScene(uiManager);
        stage.setTitle("SCSServer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
