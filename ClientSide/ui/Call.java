
package sysadmin.ui.menu;


import javafx.scene.control.ToggleButton;

/*
** start an audio call, view calls log and view a list off all contacts
*/

public class Call extends ToggleButton
{
    
    
    //define constructor
    public Call()
    {
        //set button name
        setText("Call");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
}
