
package sysadmin.net.workers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sysadmin.net.msg.Message;
import sysadmin.ui.UIManager;

/*
** receive data from server, forward to right handler
*/

public class Receiver extends Thread
{
    //in put stream, data from server
    private InputStream fromServer;

    public Receiver(InputStream in) {
        //init
        fromServer = in;
    }
    
    @Override
    public void run()
    {
        //receive a message, forward to right worker
        //"chats","events"
        while(true)
        {
            Message msg = recv();
            //send
            UIManager.sendToWorker(msg.getOwner(), msg);
        }
    }
    
    //receive message
    private Message recv()
    {
        //get data from socket's input stream
        byte[] buff = new byte[8*1024];
        String EOF;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        
        while(true)
        {
            try {
                //read data
                int n = fromServer.read(buff, 0, buff.length);
                EOF = new String(buff, n - 3, 3);
                if(EOF.compareTo("END") == 0){
                    bStream.write(buff, 0, n - 4);
                    break;
                }
                //append to byte array stream
                bStream.write(buff, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        //message
        return new Message(bStream.toString());
    }
}
