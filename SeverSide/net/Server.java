
package net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import javafx.application.Platform;


//import db.DBUtils;

public class Server {
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
    
    //keep a mapping of staffIds and passwords
    private HashMap<String,String> shadow;
    
    
    //constructor
    public Server(){
        //initialize server socket
        try {
            sSocket = new ServerSocket(PORT, 100);//a backlog of 100
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    //initialize credentials map
    public void initCredentials()
    {
        //read a password file
        try {
            //open file
            Scanner scnr = new Scanner(new File("shadow"));
            String line;
            
            while(scnr.hasNext())
            {
                //get a line
                line = scnr.nextLine();
            }
        } catch (FileNotFoundException e) {
        }
    }
    
    //server client connect requests forever
    public void serveForever()
    {
        //initialize ipcStreams
        //its a static variable
        Server.ipcStreams = new HashMap<>();
        
        //Socket object
        Socket socket;
        
        //handle client connect() requests forever
        while(true)
        {
            //each accept() call returns a Socket object
            try {
                //accept() client connect() request
                socket = sSocket.accept();
                
                //authenticate and register staff / client on the server
                authendicate(socket);
            } catch (IOException e) {
                //print error msg
                e.printStackTrace();
            }
        }
    }
    
    //authendicate client / staff
    private void authendicate(Socket socket)
    {
        //execute this on a new thread
        Platform.runLater(()->{
            //each thread has a unique output stream that other streams can write to
            PipedOutputStream ipc;
            //check whether client credentials are valid
            //read data
            try {
                InputStream sIn = socket.getInputStream();
                //create a buffer
                //authData holds a json string of the form
                //{"staffID":"id string","pwd":"staff member password"}
                byte [] authData = new byte[128];
            } catch (Exception e) {
            }
        });
        
    }
    
    //send data to a staff requests handler
    public static void forwardToClientHandler(String staffId, byte [] data)
    {
        //get a stream connected to a thread representing staffName
        //write to it
        try {
            ipcStreams.get(staffId).write(data);
        } catch (IOException e) {
            //writing to stream failed
            e.printStackTrace();
        }
    }
    
}
