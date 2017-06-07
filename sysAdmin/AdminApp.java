
package sysadmin;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import sysadmin.net.Connector;
import sysadmin.net.workers.Receiver;
import sysadmin.net.workers.Sender;
import sysadmin.ui.UIManager;


public class AdminApp extends Application
{
    @Override
    public void start(Stage primaryStage) {
        //connect to server first
        Connector conn = new Connector();
        UIManager uiManager;
        
        //worker threads
        Sender sndr;
        Receiver rcvr;
        OutputStream out = null;
        InputStream in = null;
        
        if(conn.connect()){
            setUpStreams();
            //start threads
            out = conn.getOutputStream();
            in = conn.getInputStream();
            
            if(out != null){
                //start sender
                sndr = new Sender(out);
                sndr.start();
            }
            
            if(in != null){
                //start receiver
                rcvr = new Receiver(in);
                rcvr.start();
            }
            //create ui manager
            uiManager = new UIManager(primaryStage);
            //set up scenes
            uiManager.setUpScenes(conn);
            //show
            uiManager.setVisible();
        }
        else{
            Alert alrt = new Alert(Alert.AlertType.ERROR);
            alrt.setTitle("-- Error --");
            alrt.setHeaderText("Network error");
            alrt.setContentText("Could not connect to server");
            alrt.showAndWait();
        }
    }
    
    
    //create and add piped out put streams to global map
    private static void setUpStreams()
    {
        //chat worker out put stream
        UIManager.addStream("chat", new PipedOutputStream());
        //general application out put stream, will be used
        //by the Receiver worker to forward log in confirmation data received
        //from the server
        UIManager.addStream("app", new PipedOutputStream());
        //events worker out put stream
        //UIManager.addStream("events", new PipedOutputStream());
        //Sender worker out put stream
        UIManager.addStream("sender", new PipedOutputStream());
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
