
package sysadmin.ui.menu.chat;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/*
* its a VBox
*/

public class ChatArea extends VBox
{
    //keep track of the timestamp of the last text
    private long LAST_TEXT = 0;
    
    public ChatArea(String n)
    {
        getChildren().add(new Label(n));
        setAlignment(Pos.CENTER);
    }
    
    
    //add to the chat area
    public long addText(Node node)
    {
        LAST_TEXT = System.nanoTime();
        getChildren().add(node);
        return LAST_TEXT;
    }
}
