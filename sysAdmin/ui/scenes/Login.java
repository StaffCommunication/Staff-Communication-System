
/*
*I will use different scenes for log in and home
*by home, i mean the interface displayed after the admin logs in
*home will have diff interfaces for creating new a/c , dropping a/c
*updating a/c, viewing reports from staff, sending updates to staff ...
*/

package sysadmin.ui.scenes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.Arrays;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.json.simple.JSONObject;


import sysadmin.net.msg.Base64Utils;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.UIManager;


//the login scene has a grid pane
//the grid pane has two fields: username and password
//a 'LOG IN' button is also provided

public class LogIn extends Scene {
    
    //grid
    private GridPane grid;
    //user name and password textfields
    private final TextField adminID;
    private final PasswordField pwd;
    //log in button
    private final Button submit;
    
    //in put stream
    private PipedInputStream in;
    
    
    public LogIn(GridPane g,Button b)
    {
        //use base class to set size and layout manager
        super(g,1000,600);
        //initialize
        grid = g;
        in = new PipedInputStream();
        adminID = new TextField();
        pwd = new PasswordField();
        submit = b;
        //css id
        submit.setId("submit");
        
        //connect streams
        UIManager.createPipe("app", in);
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
        pwd.getStyleClass().add("text-field");
        adminID.getStyleClass().add("text-field");
       
    }
    
    //login to server
    public Message logIn()
    {
        JSONObject jo = new JSONObject();
        //send log in data to the server
        jo.put("user", adminID.getText());
        jo.put("pwd", pwd.getText());
        
        //log in data
        Message msg = new Message(MessageType.REG,adminID.getText(),
                "db",Base64Utils.encode(jo.toJSONString()));
        
        //send to Sender worker
        UIManager.sendToWorker("sender", msg);
        
        //wait for response
        ByteArrayOutputStream response = recvRes();

        //clear inputs
        adminID.clear();
        pwd.clear();
        //wait to server response
        //return connector.recv();
        return new Message(response.toString());
    }
    
    //receive response
    private ByteArrayOutputStream recvRes()
    {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        byte[] buff = new byte[8*1024];//8kb
        
        while(true)
        {
            Arrays.fill(buff,0,buff.length,(byte)0);
            try {
                 int n = in.read(buff, 0, buff.length);
                 if(n <= 0) break;
                 //write to byte stream
                 bStream.write(buff, 0, n);
            } catch (IOException e) {
                //do nothing
            }
        }
        return bStream;
    }
    
}
