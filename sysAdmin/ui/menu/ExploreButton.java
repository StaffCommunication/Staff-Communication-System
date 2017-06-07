
package sysadmin.ui.menu;

import javafx.scene.control.ToggleButton;

/*
** to navigate the entire list of registered staff members,
** this toggle button will take you there
*/

public class ExploreButton extends ToggleButton
{
    
    //define constructor
    public ExploreButton()
    {
        //set button name
        setText("Explore");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
}
