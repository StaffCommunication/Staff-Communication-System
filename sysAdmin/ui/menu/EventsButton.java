
package sysadmin.ui.menu;


import javafx.scene.control.ToggleButton;

/*
** this button displays a pane showing all past events and an option
** to send event updates to all staff members
*/

public class EventsButton extends ToggleButton
{
    
    //define constructor
    public EventsButton()
    {
        //set button name
        setText("Events");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
}
