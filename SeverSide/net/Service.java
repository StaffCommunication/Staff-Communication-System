
package net;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Platform;

import db.DBUtils;

public class Service {
    //server port
    private final int PORT = 8000;
    //secure socket
    private ServerSocket sSocket;
    
    //associate staff names with output streams
    //each staff is represented by a thread
    //to forward data from staff A to B, send the data to thread handling
    //requests for staff B
    //this thread will then forward the data to staff B's PC
    private static HashMap<String,PipedOutputStream> ipcStreams;
    
    
    //constructor
    public Service(){
        //initialize SSL server socket
        try {
            sSocket = new ServerSocket(PORT, 100);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    //server client connect requests forever
    public void serverForever()
    {
        //initialize ipcStreams
        //its a static variable
        Service.ipcStreams = new HashMap<>();
        
        //Socket object
        Socket socket;
        //each thread has a unique output stream that other streams can write
        //to
        PipedOutputStream ipc;
        
        //handle client connect() requests forever
        while(true)
        {
            //each accept() call returns a Socket object
            try {
                //accept() client connect() request
                socket = sSocket.accept();
                
                Platform.runLater(()->{
                    //create a StaffMember variable using a different thread
                    
                });
            } catch (IOException e) {
                //print error msg
                e.printStackTrace();
            }
        }
    }
    
    //authendicate client / staff
    private void authendicate()
    {
        
    }
    
    //send data to a staff requests handler
    public static void threadsUtil(String staffName, byte [] data)
    {
        
    }
    
}
