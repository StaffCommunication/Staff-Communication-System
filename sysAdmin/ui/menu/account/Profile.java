
package sysadmin.ui.menu;

import javafx.scene.control.ToggleButton;
import sysadmin.ui.menu.account.AccountWorker;

/*
* user account
*/

public class Account extends ToggleButton
{
    //account worker
    private AccountWorker aPane;
    
    //define constructor
    public Account()
    {
        aPane = new AccountWorker();
        //set button name
        setText("Account");
        //setAlignment(Pos.CENTER);
        getStyleClass().add("menu-toggle");
    }
    
    public AccountWorker getAccountPane()
    {
        return aPane;
    }
}
