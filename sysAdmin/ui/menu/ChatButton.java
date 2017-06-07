
package sysadmin.ui.menu;

import java.io.PipedOutputStream;
import javafx.scene.control.ToggleButton;

import sysadmin.ui.workers.ChatWorker;

//a ChatWork is a pane and a thread at the same time
public class ChatButton extends ToggleButton
{
    //a reference to the chat pane
    private final ChatWorker chatWorker;

    //constructor
    public ChatButton(PipedOutputStream pO)
    {
        chatWorker = new ChatWorker(pO);
        //set button name
        setText("Chat");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
    
    //get the chat pane
    public ChatWorker getChatPane()
    {
        return chatWorker;
    }
}
