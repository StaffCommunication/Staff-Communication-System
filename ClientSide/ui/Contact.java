
package sysadmin.ui.menu.chat;


import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


/*
** give a contact attributes
*/
public class Contact 
        extends ToggleButton
            implements Comparable<Contact>
{
    private final Staff bioData;
    //refence to chat area
    private final ChatArea chatArea;
    
    //profile picture
    private Circle dp;
    
    //time the last msg was received
    private int tstamp;
    //constructor
    public Contact(Staff st, String dpLoc)
    {
        //init
        bioData = st;
        dp = new Circle(15, new ImagePattern
        (new Image(dpLoc)));
        
        chatArea = new ChatArea(st.getName());
        //set button text to full name
        setText(bioData.getName());
        setGraphic(dp);
        //add to a css class
        getStyleClass().add("contact");
        
        setAlignment(Pos.CENTER_LEFT);
    }
    
    //get fn
    public String getFullName()
    {
        return bioData.getName();
    }
    
    //get the worker id
    public String getWorkerId()
    {
        return bioData.getWID();
    }
    
    //get the chat area pane
    public ChatArea getChatArea()
    {
        return chatArea;
    }
    
    @Override
    public int compareTo(Contact c)
    {
        return tstamp - c.getTstamp();
    }
    
    public int getTstamp()
    {
        return tstamp;
    }
    
    public void setTstamp(int ts)
    {
        tstamp = ts;
    }
}
