
/*
*define the network functionality here
*
*define how the data is send over the socket and how its handled
*sfter reception
*/

package sysadmin.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


//Connector is a thread
public class Connector
{
    //secure server socket
    private SSLSocket socket;
    //server address
    private final String SERVER_IP;
    //server port
    private final short SERVER_PORT;


    //cipher algorithms to use
    private static String[] ciphers = 
    {"TLS_DH_anon_WITH_AES_128_CBC_SHA","TLS_RSA_WITH_AES_128_CBC_SHA",
    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA","TLS_DHE_DSS_WITH_AES_128_CBC_SHA"};
    
    //initialize
    public Connector()
    {
        //init ip
        SERVER_IP = "192.168.43.132";
        //init port
        SERVER_PORT = 8888;
    }
    
    
    //create a TLS socket factory
    private SSLSocketFactory createFactory()
    {
        return (SSLSocketFactory) SSLSocketFactory.getDefault();
    }
    
    
    //connect to server
    public boolean connect()
    {
        //create a secure client side socket 
        //first get a sockets factory
        
        SSLSocketFactory sFactory = createFactory();
        try
        {
            //get a secure socket from the factory, connected to the SSL-enabled
            //server socket
            socket = (SSLSocket) sFactory.createSocket(SERVER_IP,SERVER_PORT);
            //if we made it this far, we have an SSL-enabled socket connected to the server
            //set algorithms to use
            socket.setEnabledCipherSuites(Connector.ciphers);
            //connected, return true
            return true;
        }
        catch(IOException e){
            
             //set last error
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
    
    //get socket input stream
    public InputStream getInputStream()
    {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }
    
    //get socket output stream
    public OutputStream getOutputStream()
    {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            return null;
        }
    }

    // release resources ==> socket and i/o streams
    
    public String release()
    {
        try
        {
             socket.close();
        }
        catch(IOException e)
        {
            return "Error closing socket";
        }
        return "success";
    }
}
