
package sysadmin.ui.menu.chat;

import javafx.geometry.Insets;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


/*
* its a VBox
*/

public class ChatArea extends VBox
{
    
    public ChatArea(String n)
    {
        getChildren().add(new Label(n));
        //setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(10);
    }
    
    
    //add to the chat area
    public long addText(Node node)
    {
        getChildren().add(node);
        return System.currentTimeMillis();
    }
}
