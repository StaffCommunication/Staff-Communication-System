
package sysadmin.ui;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import sysadmin.net.Connector;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.scenes.Home;
import sysadmin.ui.scenes.LogIn;



public class UIManager {
    
    //scenes
    private LogIn logInScene;
    private Home homeScene;
    //stage
    private final Stage stage;
    
    //keep a map of ipcs ==> inter-process communication out put streams
    //data written on these streams will be read at the other end from an
    //in put stream connected to the out put stream 
    private final static HashMap<String,PipedOutputStream> ipcIO 
            = new HashMap<>();
    
    
    //constructor
    public UIManager(Stage st)
    {
        stage = st;
    }
    
    //set scene
    private void set(Scene sc)
    {
        stage.setScene(sc);
    }
    
    //set up log in scene
    public void setUpScenes(Connector c)
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
        homeScene = new Home(new SplitPane());
        
        //set button click listener
        submit.setOnAction((e)->{
            Message msg = logInScene.logIn();
            //try and log in
            if(msg.getType() == MessageType.CONF_CONF){
                //load home pane
                set(homeScene);
                //set work id
                Home.setWorkId(logInScene.getWorkId());
                //set title
                stage.setTitle("[ egerton536 ]");
                //set up home scene
                homeScene.setUp();
            }
        });
    }
    
    //set visible
    public void setVisible()
    {
        stage.show();
    }
    
    
    //add piped output stream to map
    public synchronized static void addStream(String id, PipedOutputStream stream)
    {
        ipcIO.put(id, stream);
    }
    
    //remove stream from map
    public synchronized static void removeStream(String id)
    {
        ipcIO.remove(id);
    }
    
    //connect streams
    public synchronized static boolean createPipe(String id, PipedInputStream in)
    {
        try {
            //connect piped io streams
            ipcIO.get(id).connect(in);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    //forward message to appropriate worker
    public synchronized static void sendToWorker(String pName,Message msg)
    {
        //forward to worker
        String data = msg.toString() + " END";
        PipedOutputStream stream = ipcIO.get(pName);
        try
        {
            wait(stream);
            stream.write(data.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    //keep a stream busy
    public synchronized static void wait(PipedOutputStream stream)
    {
        try {
            synchronized(stream)
            {
                stream.wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //free stream
    public synchronized static void free(String pName)
    {
        PipedOutputStream st = ipcIO.get(pName);
        synchronized(st)
        {
            st.notifyAll();
        }
    }
}
