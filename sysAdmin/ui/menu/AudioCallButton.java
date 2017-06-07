
package sysadmin.ui.menu;


import javafx.scene.control.ToggleButton;

/*
** start an audio call, view calls log and view a list off all contacts
*/

public class AudioCallButton extends ToggleButton
{
    
    
    //define constructor
    public AudioCallButton()
    {
        //set button name
        setText("Audio Call");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
}
