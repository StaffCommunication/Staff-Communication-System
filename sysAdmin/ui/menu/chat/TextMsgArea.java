
package sysadmin.ui.menu.chat;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import sysadmin.net.msg.Base64Utils;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.scenes.Home;

/*
* Type a text here, the just click "SEND"
*/
public class TextMsgArea extends HBox
{
    //actual text area
    private final TextArea tArea;
    //send button
    private final Button send;
    
    
    public TextMsgArea(Button butt)
    {
        send = butt;
        tArea = new javafx.scene.control.TextArea();
        style();
    }
    
    private void style()
    {
        tArea.setEditable(true);
        tArea.setPromptText("Type a message");
        tArea.setPrefRowCount(1);
    }
    
    //set up interface
    public void setup()
    {
        getChildren().addAll(tArea, send);
        getStyleClass().add("text-field");
        setSpacing(10);
        setPadding(new Insets(15));
    }
    
    public Message getMsg()
    {
        if(tArea.getText().length() == 0)
            return null;
        return new Message(MessageType.SMS,Home.getWorkId(),
                ChatPane.getDest(),Base64Utils.encode(tArea.getText()),"chat");
    }
    
    public void clear()
    {
        tArea.clear();
    }
}
