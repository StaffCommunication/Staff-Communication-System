
package sysadmin.ui.menu;

import java.io.PipedInputStream;
import javafx.scene.control.ToggleButton;

import sysadmin.ui.menu.chat.ChatWorker;

//a ChatWork is a pane and a thread at the same time
public class Chat extends ToggleButton
{
    //a reference to the chat pane
    private final ChatWorker chatWorker;

    //constructor
    public Chat()
    {
        chatWorker = new ChatWorker(new PipedInputStream());
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
