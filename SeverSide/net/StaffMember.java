
package net;

import java.io.IOException;
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
        
    }
    
}
