
package scsserver.net;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.HashMap;
import javafx.application.Platform;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;


import scsserver.db.DB;
import scsserver.db.DbWorker;
import scsserver.net.msg.Message;
import scsserver.net.msg.MessageType;
import scsserver.staff.Worker;
import scsserver.ui.UIManager;


//import db.DBUtils;

public class Acceptor extends Thread {
    //server port
    private final int PORT = 8888;
    //secure socket
    private SSLServerSocket sSocket;
    
    //associate staff names with output streams
    //each staff is represented by a thread
    //to forward data from staff A to B, send the data to thread handling
    //requests for staff B
    //this thread will then forward the data to staff B's PC
    private static HashMap<String,PipedOutputStream> ipcStreams;
    
    //a database connection
    private final DB dbCon;
    //ciphers to use
    private static String[] ciphers = 
    {"TLS_DH_anon_WITH_AES_128_CBC_SHA","TLS_RSA_WITH_AES_128_CBC_SHA",
    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA","TLS_DHE_DSS_WITH_AES_128_CBC_SHA"};
    
    //keep track of the last error encountered
    private static String lastError;
    
    
    //constructor
    public Acceptor(DB connection){
        
        //initialize database connection
        dbCon = connection;
    }
    
    //handle client connect requests
    @Override
    public void run()
    {
        //initialize ipcStreams
        //its a static variable
        Acceptor.ipcStreams = new HashMap<>();
        
        //start datadase worker
        startDbHandler();
        serveForever();
    }
    
    //connect to database
    public boolean connectToDB()
    {
        //if connected to db, initialize client authentication details
        UIManager.log("creating a database connection");
        if(dbCon.connect())
        {
            return true;
        }
        return false;
    }
    
    //listening port
    public int getPort()
    {
        return PORT;
    }
    
    //start server
    public boolean startServer()
    {
        try
        {
            //all SSLServerSocket class constructors are protected
            //we use a factory abstract class to create a secure server socket object
            SSLServerSocketFactory f = createFactory();
            //bind() socket and set to listening
            sSocket = (SSLServerSocket) f.createServerSocket(PORT, 100);
            //set ciphers to use
            sSocket.setEnabledCipherSuites(Acceptor.ciphers);
            //if we made it this far, our server is running
            
            //log
            UIManager.log("server started, listening on port " + getPort());
            return true;
        }
        catch(IOException e){
            Acceptor.setLastError(e.getMessage());
            return false;
        }
    }
    
    //get secure sockets factory
    private SSLServerSocketFactory createFactory()
    {
        return (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    }
    
    //start db queries handler
    private void startDbHandler()
    {
        //log
        Platform.runLater(()->{
            UIManager.log("starting database requests handler");
        });

        //create handler
        DbWorker dw = new DbWorker(dbCon, new PipedOutputStream());
        //start worker
        dw.start();
    }
    
    //server client connect requests forever
    public void serveForever()
    {
        
        Platform.runLater(()->{
            UIManager.log("server ready to accept client connection requests");
            UIManager.log("------------------------------------------------------\n");
        });
        
        //client socket
        SSLSocket cSocket;
        
        //handle client connect() requests forever
        while(true)
        {
            //each accept() call returns a Socket object
            try{
                //accept() client connect() request
                cSocket = (SSLSocket) sSocket.accept();

                UIManager.log("received connection from [ " + 
                        cSocket.getInetAddress().getHostAddress() + " ," +
                        cSocket.getPort() + " ]");
                //start handler
                startNewWorker(cSocket);
            } catch (IOException e) {
                //set last error
                Acceptor.setLastError(e.getMessage());
                //continue accepting client connect() requests
            }
        }
    }

    //release resources
    public void release()
    {
        try {
            sSocket.close();
            Platform.runLater(()->{
                UIManager.log("shutting down server");
            });
        } catch (Exception e) {
            Platform.runLater(()->{
                UIManager.log("error shutting down server");
            });
        }
        
    }
    
    //start new worker thread
    private void startNewWorker(SSLSocket sock)
    {
        //create worker 
        Worker worker = new Worker(sock,new PipedOutputStream());
        //start worker
        worker.start();
    }
    
    //send data to a staff requests handler
    //this method will be called from other threads
    public static void forwardToClientHandler(Message message)
    {
        //get a stream connected to a thread representing staffName
        //write to it
        PipedOutputStream stream = ipcStreams.get(message.getDest());
        //is the client still connected?
        if(stream == null)
        {
            //change type
            if(message.getType() == MessageType.FILE)
                message.setType(MessageType.SAVE_FILE);
            else if(message.getType() == MessageType.SMS)
                message.setType(MessageType.SAVE_SMS);
            else if(message.getType() == MessageType.EVENT)
                message.setType(MessageType.SAVE_EVENT);
            
            toDB(message);
        }
        else{
            if(message.getDest().compareTo("db") == 0)
                //to db worker
                toDB(message);
            else
                toWorker(message);
        }
    }
    
    public synchronized static void toDB(Message msg)
    {
        PipedOutputStream st = ipcStreams.get("db");
        synchronized(st)
        {
            try {
                wait(st);
                st.write((msg.toString() + " END").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized static void toWorker(Message msg)
    {
        PipedOutputStream st = ipcStreams.get(msg.getDest());
        synchronized(st)
        {
            try {
                wait(st);
                st.write((msg.toString() + " END").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void wait(PipedOutputStream st)
    {
        synchronized(st)
        {
            try {
                st.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void free(String pName)
    {
        PipedOutputStream st = ipcStreams.get(pName);
        if(st != null)
            st.notifyAll();
    }
    //add to ipcstream map
    public synchronized static void add(PipedOutputStream ps, String id)
    {
         ipcStreams.put(id,ps);
    }
    
    //remove
    public synchronized static void remove(String id)
    {
        ipcStreams.remove(id);
    }
    
    //reset / set the last error message
    //am using the posix error handling concept
    //posix system apps keep track of the last error code by resetting the variable 'errno'
    //defined in errno.h
    private static void setLastError(String err)
    {
        lastError = err;
    }
    
    //get the error message of the last error encountered
    public static String getLastError()
    {
        return lastError;
    }
}
