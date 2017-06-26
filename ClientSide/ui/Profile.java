
package sysadmin.ui.menu.account;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import sysadmin.ui.menu.chat.Staff;

/*
* button to display user bio data
*/

public class Profile extends ToggleButton
{
    //Staff object
    private final Staff staff;
    
    //grid
    private final GridPane gp;
    
    //constructor
    public Profile(Staff st)
    {
        super("Profile");
        staff = st;
        gp = new GridPane();
        
        getStyleClass().add("profile-toggle");
    }
    
    //set up grid pane
    public void setUpGrid()
    {
        gp.add(new Label("Name"), 0, 0);
        gp.add(new Label(staff.getName()), 1, 0);
        gp.add(new Label("Work ID"), 0, 1);
        gp.add(new Label(staff.getWID()), 1, 1);
        gp.add(new Label("National ID"), 0, 2);
        gp.add(new Label(staff.getNID()), 1, 2);
        gp.add(new Label("Gender"), 0, 3);
        gp.add(new Label(staff.getGender()), 1, 3);
        gp.add(new Label("Cell Phone"), 0, 4);
        gp.add(new Label(staff.getCPhone()), 1, 4);
        gp.add(new Label("E-mail Address"), 0, 5);
        gp.add(new Label(staff.getEAddr()), 1, 5);
        gp.add(new Label("D.O.B"), 0, 6);
        gp.add(new Label(staff.getDOB()), 1, 6);
        gp.add(new Label("Department"), 0, 7);
        gp.add(new Label(staff.getDepartment()), 1, 7);
        gp.add(new Label("Faculty"), 0, 8);
        gp.add(new Label(staff.getFaculty()), 1, 8);
        
        //style
        gp.setVgap(15);
        gp.setHgap(25);
        gp.setAlignment(Pos.CENTER);
        
        gp.setStyle("-fx-background-color : silver;"
                + "-fx-font-family : \"Lucida\";"
                + "-fx-padding : 20px 0px 0px 20px;"
                + "-fx-font-size : 13px;"
                + "-fx-text-fill : #333;");
    }
    
    public GridPane getGrid()
    {
        return gp;
    }
    
}
