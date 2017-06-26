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
public interface ClientListListener {
    void addToList(String userName);
    void removeFromList(String userName);
}
