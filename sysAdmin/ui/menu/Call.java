
package sysadmin.ui.menu;


import javafx.scene.control.ToggleButton;
import sysadmin.ui.menu.call.CallerWorker;

/*
** start an audio call, view calls log and view a list off all contacts
*/

public class Call extends ToggleButton
{
    
    private CallerWorker cw = new CallerWorker();
    
    //define constructor
    public Call()
    {
        //cw.setUp();
        //set button name
        setText("Call");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
    
    public CallerWorker getCallerPane()
    {
        return cw;
    }
}
