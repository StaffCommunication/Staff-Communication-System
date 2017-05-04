
package sysadmin.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import sysadmin.net.Connector;
import sysadmin.ui.scenes.Home;
import sysadmin.ui.scenes.LogIn;


public class UIManager {
    
    //scenes
    private LogIn logInScene;
    private Home homeScene;
    //stage
    private Stage stage;
    
    //server connection
    private Connector connector;
    
    
    //constructor
    public UIManager(Stage st, Connector c)
    {
        stage = st;
        //init connector
        connector = c;
    }
    
    //set scene
    private void set(Scene sc)
    {
        stage.setScene(sc);
    }
    
    //set up log in scene
    public void setUpScenes()
    {
        //create grid pane
        GridPane gp = new GridPane();
        gp.setMaxSize(300, 300);
        //summit button
        final Button submit = new Button("GO");
        //init log in scene
        logInScene = new LogIn(gp,submit);
        //set login scene
        logInScene.setUp();
        set(logInScene);
        stage.setTitle("[ Log in ]");
        
        //home scene
        BorderPane bp = new BorderPane();
        homeScene = new Home(bp);
        
        //set button click listener
        submit.setOnAction((e)->{
            //try and log in
            set(homeScene);
            stage.setTitle("[ Egerton536 ]");
        });
    }
    
    //set visible
    public void setVisible()
    {
        stage.show();
    }
}
