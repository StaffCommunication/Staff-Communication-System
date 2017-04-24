
package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

public class StaffMember extends Thread
{
    //requests by every staff member / client are handled on a different thread
    private final Socket staffConection;
    //piped stream for IPC
    //data from another staff / client
    private PipedInputStream ipcStream;
    
    //client / staff socket output stream ==> send to client
    private OutputStream out;
    //client / staff socket input stream ==> receive from
    private InputStream in;
    
    //constructor
    public StaffMember(Socket sock, PipedOutputStream oS)
    {
        //initialize socket
        this.staffConection = sock;
        //initialize and connect streams
        try {
            ipcStream = new PipedInputStream(oS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //initialize socket streams 
    private void initStreams()
    {
        //input stream (read from it)
        try{
            this.in = this.staffConection.getInputStream();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        //output stream (write to it)
        try {
             this.out = this.staffConection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //override run()
    @Override
    public void run()
    {
        //start handler
        handle();
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
                byte[] msg = new byte[64*1024];//64kbs
                while(true)//forever
                {
                    //read data from the staffConnection's input stream
                    try {
                        if(in.read(msg) > 0)//some data read
                        {
                            //determine destination staffId
                            
                        }
                    } catch (IOException e) {
                        //continue receiving data
                        continue;
                    }//try-catch
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
                byte[] buff = new byte[64*1024];//64kbs
                while(true)//read data forever
                {
                    try {
                        if(ipcStream.read(buff) > 0)//data read
                        {
                            //forward data to remote client
                            try {
                                //write to clients output stream
                                out.write(buff,0,buff.length);
                            } catch (IOException e) {
                                //log error message
                                e.printStackTrace();
                            }
                        }
                        
                    } catch (IOException e) {
                        //log error message
                        e.printStackTrace();
                    }
                }//while
            }//run()
        };//thread 2
        
        //fire!!!
        inThread.start();
        outThread.start();
    }
    
}
