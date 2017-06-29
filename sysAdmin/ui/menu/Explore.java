
package sysadmin.ui.menu;


import java.io.PipedInputStream;
import javafx.scene.control.ToggleButton;
import sysadmin.ui.menu.explore.ExploreWorker;


/*
** to navigate the entire list of registered staff members,
** this toggle button will take you there
*/

public class Explore extends ToggleButton
{
    private ExploreWorker xPane;
    //define constructor
    public Explore()
    {
        xPane = new ExploreWorker(new PipedInputStream());
        xPane.setUp();
        //set button name
        setText("Explore");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
    
    public ExploreWorker getExplorePane()
    {
        return xPane;
    }
}
