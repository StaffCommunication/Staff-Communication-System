
package scsserver.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


import scsserver.db.DB;
import scsserver.net.Acceptor;


public class UIManager extends Scene {
    
    //default layout manager
    private final BorderPane rootPane;
    
    //acceptor
    private Acceptor acceptor;
    
    //logs
    private static TextArea logs = new TextArea();
    
    
    //constructor
    public UIManager(BorderPane pane, DB db)
    {
        super(pane, 800, 600);
        rootPane = pane;
        acceptor = new Acceptor(db);
    }
    
    //display start page
    public void setUp()
    {
        Button start = new Button("START");
        
        rootPane.setCenter(logs);
        rootPane.setTop(start);
        start.setAlignment(Pos.CENTER_RIGHT);
        
        UIManager.logs.setEditable(false);
        
        start.setOnAction(
                (e)->
                {
                    UIManager.log("starting server...");
                    if(acceptor.startServer())
                    {
                        //connect to database and initialize credentials map
                        if(acceptor.connectToDB())
                        {
                            //start handler
                            acceptor.start();
                        }
                        else
                            UIManager.log("failed to connect to database");
                    }
                    else
                        UIManager.log("could not start server : " + Acceptor.getLastError());
                }
        );
    }
    
    //clean
    public void clean()
    {
        acceptor.release();
    }
    
    //display all server activities on the logs pane
    public synchronized static void log(String logMsg)
    {
        logs.appendText("## " + logMsg + "\n");
    }
}
