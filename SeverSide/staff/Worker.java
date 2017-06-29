
package scsserver.staff;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;


import scsserver.net.Acceptor;
import scsserver.net.msg.Message;
import scsserver.net.msg.MessageType;
import scsserver.ui.UIManager;


public class Worker extends Thread
{
    //requests by every staff member / client are handled on a different thread
    private final SSLSocket socket;
    //piped stream for IPC
    //data from another staff / client
    private final PipedOutputStream outIpc;
    private final PipedInputStream inIpc;
    //client / staff socket output stream ==> send to client / staff member
    private OutputStream out;
    //input stream ==> data from client's / staff member's PC
    private InputStream in;
    
    //thread id ==> set to staff work id
    private String workerId;
    
    //status
    private boolean isConnected;
    
    
 
    //constructor
    public Worker(SSLSocket sock, PipedOutputStream oS)
    {
        //initialize socket
        socket = sock;
        outIpc = oS;
        //initialize and connect streams
        inIpc = new PipedInputStream();
        try {
            inIpc.connect(oS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //set up
    public boolean setUp(){
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            //failed to initialize stream
            return false;
        }
    }
    
    //authendicate to server
    public boolean authendicate()
    {
        Message msg = recvFromClient();
        //if its a REG message, request db to authendicate the client
        if(msg.getType() == MessageType.REG){
            //register a piped output stream for this thread
            Acceptor.add(outIpc, msg.getSrc());
            //forward to db worker
            Acceptor.forwardToClientHandler(msg);
        }
        else
            return false;
        
        //initialize worker id
        workerId = msg.getSrc();
        setName(workerId);//thread name
        
        //receive response from db
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        byte[] buff = new byte[16*1024];
        String EOF;
        while(true){
            Arrays.fill(buff,0,buff.length,(byte)0);
            try {
                //read data
                int n = inIpc.read(buff, 0, buff.length);
                EOF = new String(buff,n - 3,3);
                //end-of-stream
                if(EOF.compareTo("END") == 0){
                    bStream.write(buff, 0, n - 4);
                    break;
                }
                bStream.write(buff, 0, n);
            } catch (IOException e) {
                return false;
            }
        }
        
        msg = new Message(bStream.toString());
        //if Error, authentication failed, else succeeded
        boolean auth;
        if(msg.getType() == MessageType.CONF_CONF){
            auth = true;
            isConnected = true;
        }
        else
            auth = false;
        
        //send response to remote client
        try {
            bStream.write(" END".getBytes());
            out.write(bStream.toByteArray(), 0, bStream.size());
        } catch (IOException e) {
            System.err.println(e);
        }
        return auth;
    }
    
    //override run()
    @Override
    public void run()
    {
        //start handler
        if(setUp())
            if(authendicate())
                handle();
    }
    
    //worker id
    public String getWorkerId()
    {
        return workerId;
    }
    
    //handle client requests
    public void handle()
    {
        //create two threads, one for handling data from the client
        //the other for handling data from other clients / threads and sending to client
       
        //handle data from client / staff member's PC
        Thread inThread = new Thread()
        {
            @Override
            public void run()
            {
                Message message;
                while(isConnected)//forever
                {
                    message = recvFromClient();
                    if(message != null)
                    {
                        Acceptor.forwardToClientHandler(message);
                    }
                }//while
            }//run()
        };//thread 1
        
        
        //handle data from a thread representing another connected staff member / client
        //read from the ipcStream ==> interprocess communication (though its threads here)
        Thread outThread = new Thread()
        {
            @Override
            public void run()
            {
                Message msg;
                while(isConnected)//read data forever
                {
                    msg = recvFromWorker();
                    //forward data to remote client
                    byte[] buff = (msg.toString() + " END").getBytes();
                    try {
                        out.write(buff);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        };//thread 2
        
        //fire!!!
        inThread.start();
        outThread.start();
    }

    //receive data from client
    public Message recvFromClient()
    {
        byte[] res = new byte[16*1024];//8kb
        ByteArrayOutputStream outP = new ByteArrayOutputStream();
        String EOF;
        
        //read at most 8kb at a time from socket, and write to the byte array stream
        while(true)
        {
            //clear array
            Arrays.fill(res,0,res.length,(byte)0);
            try
            {
                int n = in.read(res,0,res.length);
                EOF = new String(res,n - 3, 3);
                
                //end-of-stream?
                if(EOF.compareTo("END") == 0){
                    outP.write(res, 0, n - 4);
                    break;
                }
                
                //some data read into res
                //write the data to the byte array stream
                outP.write(res,0,n);
            }
            catch(IOException e){
                //clean and exit
                clean();
                isConnected = false;
                return null;
            }
        }
        return new Message(outP.toString());
    }
    
    //receive from another thread
    public Message recvFromWorker()
    {
        ByteArrayOutputStream bArray = new ByteArrayOutputStream();
        String EOF;
        byte[] buff = new byte[16*1024];
        int n;
        
        while(true){
            //clear memory
            Arrays.fill(buff, 0, buff.length, (byte)0);
            try {
            n = inIpc.read(buff, 0, buff.length);
            EOF = new String(buff,n - 3, 3);
            //end-of-stream?
                if(EOF.compareTo("END") == 0){
                    bArray.write(buff, 0, n - 4);
                    break;
                }
            //write data to byte stream
            bArray.write(buff, 0, n);
            } catch (IOException e) {
                System.out.println(e);
                break;
            }
        }//while
        
        return new Message(bArray.toString());
    }
    
    //release thread resources
    public void clean()
    {
        //close streams
        try {
            inIpc.close();
            outIpc.close();
            in.close();
            out.close();
            //remove from global map
            Acceptor.remove(workerId);
            //close socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //log
        UIManager.log("[ " + workerId + " ] left");
    }
    
}//class Worker
