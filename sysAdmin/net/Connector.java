
/*
*define the network functionality here
*
*define how the data is send over the socket and how its handled
*sfter reception
*/

package sysadmin.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import sysadmin.net.msg.Message;


public class Connector {
    //socket
    private Socket socket;
    //server address
    private final String SERVER_IP;
    //server port
    private final short SERVER_PORT;

    
    //I/O streams
    private InputStream inStream;
    private OutputStream outStream;
    
    //status
    private boolean loggedIn;
    private boolean connected;
    
    //initialize
    public Connector()
    {
        //init ip
        SERVER_IP = "127.0.0.1";
        //init port
        SERVER_PORT = 8000;
        connected = false;
    }
    
    //connect to server
    public boolean connect()
    {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    //set socket options
    private void setSockOpt()
    {
        try {
            //set socket options 
            socket.setTcpNoDelay(true);
            socket.setSoLinger(true, 5);
        } catch (SocketException e) {
            System.out.println(e);
        }
    }
    
    //send data to server
    public boolean send(Message msg)
    {
        //build a string to send
        String buffer = msg.toString();
        //send data
        try
        {
            outStream.write(buffer.getBytes(),0,buffer.length());
        }
        catch(IOException e){
            return false;
        }
        return true;
    }
    
    //receive data from server
    public Message recv()
    {
        //wait for response from server
        byte[] res = new byte[8*1024];//8kb
        ByteArrayOutputStream outP = new ByteArrayOutputStream();
        
        //read at most 8kb at a time from socket, and write to the byte array stream
        while(true)
        {
            Arrays.fill(res,0,res.length,(byte)0);
            try
            {
                int n = inStream.read(res,0,res.length);
                if(n <= 0) break;
                
                //some data read into res
                //write the data to the byte array stream
                outP.write(res,0,n);
            }
            catch(IOException e){
                break;
            }
        }
        return new Message(outP.toString());
    }
    
    //is the user logged in ?
    public boolean isLoggedIn()
    {
        return loggedIn;
    }
    
    //release resources ==> socket and i/o streams
    public String release()
    {
        try
        {
            inStream.close();
            outStream.close();
            //close socket
            try
            {
                socket.close();
            }
            catch(IOException e)
            {
                return "Error closing socket";
            }
        }
        catch(IOException e)
        {
            return "Error closing input/output streams";
        }
        return "success";
    }
}
