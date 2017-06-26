
package sysadmin.ui.menu.chat;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import sysadmin.net.msg.Message;

/*
** a border pane to hold the ChatArea, header and footer (TextMsgArea)
** 
*/

public class ChatPane extends BorderPane
{
    //header
    private static Label hdr;
    //destination od a message
    private static String dest;
    //chat messages area
    private TextMsgArea tMsg;

    //constructor
    public ChatPane(TextMsgArea tm)
    {
        ChatPane.hdr = new Label();
        tMsg = tm;
        setTop(hdr);
        setBottom(tMsg);
        
        tMsg.setup();
    }
    
    //change chat area
    public void setChatArea(ChatArea ca)
    {
        setCenter(ca);
    }
    
    public static void setHeader(String hder)
    {
        hdr.setText(hder);
    }
    
    //get message
    public Message getMsg()
    {
        return tMsg.getMsg();
    }
    
    //set source
    public static void setDest(String d)
    {
        dest = d;
    }
    
    //get the header, this is the name
    public static String getDest()
    {
        return dest;
    }
    
}
