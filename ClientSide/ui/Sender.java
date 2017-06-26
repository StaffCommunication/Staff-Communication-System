
package sysadmin.net.workers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.Arrays;


import sysadmin.ui.UIManager;

/*
** this worker will send packets to the server
** this worker will have an output stream to the server
*/

public class Sender extends Thread
{
    //to server
    private final OutputStream toServer;
    //get data from other workers
    private final PipedInputStream ipcIn;
    
    //constructor
    public Sender(OutputStream socketStream)
    {
        //init
        toServer = socketStream;
        ipcIn = new PipedInputStream();
    }
    
    @Override
    public void run()
    {
        //connect to some piped out put stream
        UIManager.createPipe("sender", ipcIn);
        //receive and forward to server forever
        while(true)
        {
            //receive
            ByteArrayOutputStream data = recv();
            
            UIManager.free("sender");
            data.write(" END".getBytes(),0,4);
            //forward to server
            send(data.toByteArray());
        }
    }
    
    //send to server
    private void send(byte [] data)
    {
        try
        {
            toServer.write(data);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //receive data from other workers
    private ByteArrayOutputStream recv()
    {
        //8 kb
        byte[] buff = new byte[8*1024];
        String EOF;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        
        while(true)
        {
            //clear buffer
            Arrays.fill(buff, 0, buff.length, (byte)0);
            try
            {
                int n = ipcIn.read(buff, 0, buff.length);
                EOF = new String(buff, n - 3, 3);
                if(EOF.compareTo("END") == 0){
                    bStream.write(buff, 0, n - 4);
                    break;
                }//end of stream
                
                bStream.write(buff, 0, n);
            }
            catch(IOException e){
                e.printStackTrace();
                break;
            }
        }
        return bStream;
    }
}
