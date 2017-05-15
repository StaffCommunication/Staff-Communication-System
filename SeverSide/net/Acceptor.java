
package scsserver.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import scsserver.db.DB;
import scsserver.db.DbWorker;
import scsserver.net.msg.Message;
import scsserver.staff.Worker;


//import db.DBUtils;

public class Acceptor {
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
    
    //keep a mapping of staffIds and passwords ==> encrypted using SHA512
    private final  HashMap<String,String> shadow;
    
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
        //init shadow
        shadow = new HashMap<>();
    }
    
    //connect to database
    private boolean connectToDB()
    {
        //if connected to db, initialize client authentication details
        if(dbCon.connect())
        {
            return initCredentials();
        }
        return false;
    }
    
    //start server
    public boolean start()
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
            
            //connect to db
            return connectToDB();
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
    
    //initialize staff accounts' authentication details
    private boolean initCredentials()
    {
        //initialize staff credentials using the table scs.shadow
        ResultSet cred = dbCon.query("SELECT * FROM shadow");
        
        if(cred != null)
        {
            while(true)
            {
                try {
                    if(!cred.next()) break;
                    shadow.put(cred.getString(1),cred.getString(2));
                } catch (SQLException e) {
                     //set last error
                     Acceptor.setLastError(e.getMessage());
                     break;
                }
            }//while
            //credentials initialized
            return true;
        }//if
        return false;
    }
    
    //start db queries handler
    private void startDbHandler()
    {
        //create an output stream
        PipedOutputStream toDb = new PipedOutputStream();
        //create handler
        DbWorker dw = new DbWorker(dbCon, toDb);
        //add out put stream to map
        Acceptor.add(toDb, "db");
        
        //start worker
        dw.start();
    }
    
    //server client connect requests forever
    public void serveForever()
    {
        //initialize ipcStreams
        //its a static variable
        Acceptor.ipcStreams = new HashMap<>();
        
        //client socket
        SSLSocket cSocket;
        
        //handle client connect() requests forever
        while(true)
        {
            //each accept() call returns a Socket object
            try{
                //accept() client connect() request
                cSocket = (SSLSocket) sSocket.accept();
                
                //authenticate and register staff / client on the server
                String wId = authendicate(cSocket.getInputStream());
                if(wId != null)
                {
                    //create a client handler
                    startNewWorker(cSocket, wId);
                }
            } catch (IOException e) {
                //set last error
                Acceptor.setLastError(e.getMessage());
                //continue accepting client connect() requests
            }
        }
    }
    
    //authendicate client / staff / admin
    //return work id
    private String authendicate(InputStream in)
    {
        //get credentials from user
        byte[] cred = new byte[1024];
        try 
        {
            in.read(cred, 0, cred.length);
            Message msg = new Message(new String(cred));
            
            //get the pay load
            if(inShadow(msg.getPayload()))
            {
                return msg.getSrc();
            }
        } catch (Exception e) {
            //set last error
            Acceptor.setLastError(e.getMessage());
        }
        return null;
    }
    
    //check whether client credentials are in the shadow db table
    //after starting the server, the hashmap shadow is initialized to the contents
    //of the table scs.shadow
    //we will use this hashmap to auth client
    private boolean inShadow(String jsonString)
    {
        //create json object
        JSONParser p = new JSONParser();
        boolean found = false;//match found
        try {
            JSONObject jo = (JSONObject) p.parse(jsonString);
            //keys and values of shadow are hashed using SHA2
            String nmHash = DigestUtils.sha256Hex(jo.get("user").toString());
            String pwdHash = DigestUtils.sha256Hex(jo.get("pwd").toString());
            
            if(shadow.containsKey(nmHash))
            {
                //compare passwords
                if(shadow.get(nmHash).compareTo(pwdHash) == 0)
                {
                    found = true;
                }
            }
        } catch (ParseException e) {
            Acceptor.setLastError(e.getMessage());
        }
        return found;
    }
    
    //start new worker thread
    private void startNewWorker(SSLSocket sock, String id)
    {
        //create a piped output stream
        PipedOutputStream ps = new PipedOutputStream();
        //add it to map
        Acceptor.add(ps, id);
        
        //create worker 
        Worker worker = new Worker(sock, ps, id);
        //start worker
        worker.start();
    }
    
    //send data to a staff requests handler
    //this method will be called from other threads
    public static void forwardToClientHandler(Message message)
    {
        //get a stream connected to a thread representing staffName
        //write to it
        try {
            ipcStreams.get(message.getDest()).write(
                    message.toString().getBytes(),0,message.toString().length());
        } catch (IOException e) {
             //set last error
            Acceptor.setLastError(e.getMessage());
        }
    }
    
    //add to ipcstream map
    private static void add(PipedOutputStream ps, String id)
    {
         ipcStreams.put(id,ps);
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
