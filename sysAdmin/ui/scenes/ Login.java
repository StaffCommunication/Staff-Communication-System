
/*
*I will use different scenes for log in and home
*by home, i mean the interface displayed after the admin logs in
*home will have diff interfaces for creating new a/c , dropping a/c
*updating a/c, viewing reports from staff, sending updates to staff ...
*/

package sysadmin.ui.scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.json.simple.JSONObject;

import sysadmin.net.Connector;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;


//the login scene has a grid pane
//the grid pane has two fields: username and password
//a 'GO' button is also provided

public class LogIn extends Scene {
    
    //grid
    private GridPane grid;
    //user name and password textfields
    private final TextField adminID;
    private final PasswordField pwd;
    //log in button
    private final Button submit;
    //connector
    private Connector connector;
    
    public LogIn(GridPane g, Button b, Connector c)
    {
        //use base class to set size and layout manager
        super(g,600,600);
        //initialize grid
        grid = g;
        //initialize connector
        connector = c;
        //initialize button, user name and pwd attr
        adminID = new TextField();
        pwd = new PasswordField();
        submit = b;
        //css id
        submit.setId("submit");
    }
    
    //set up log in ui
    public void setUp()
    {
        //user
        grid.add(new Label("ADMIN ID"), 0, 0);
        grid.add(adminID, 1, 0);
        //pwd
        grid.add(new Label("PASSWORD"), 0, 1);
        grid.add(pwd, 1, 1);
        //button
        grid.add(submit, 1, 4);
        
        //set styles
        this.getStylesheets().add("sysadmin/ui/css/login.css");
        grid.setId("login-grid");
        
        //adminID.setPromptText("admin user id");
       
    }
    
    //login to server
    public Message logIn()
    {
        JSONObject jo = new JSONObject();
        //send log in data to the server
        jo.put("user", adminID.getText());
        jo.put("pwd", pwd.getText());
        
        //send the data to server ==> message is of type REG
        connector.send(
                new Message(MessageType.REG,adminID.getText(),"server",jo.toJSONString()));
        
        //wait to server response
        return connector.recv();
    }
    
}
