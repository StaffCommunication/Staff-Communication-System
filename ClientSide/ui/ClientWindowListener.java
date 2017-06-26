/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messangerclient;

/**
 *
 * @author Don
 */
public interface ClientWindowListener {
     public void openWindow(String message);
    public void closeWindow(String message);
    public void fileStatus(String filesStatus);
}
