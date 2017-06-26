/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messangerclient;
import messangerclient.ClientManager;
import messangerclient.LoginFrame;

public class Main {

   
    public static void main(String[] args) {
          ClientManager clientManager=new ClientManager();
        LoginFrame loginFrame=new LoginFrame(clientManager);
        loginFrame.setVisible(true);
       
    }
    
}
