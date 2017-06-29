
package sysadmin.ui.menu.chat.file;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;


public class FileSender 
        extends HBox 
            implements Runnable
{
    
    private ProgressBar pb;
    private Label lab;
    private Button send;
    
    public FileSender()
    {
        pb = new ProgressBar();
        
    }
    
    @Override
    public void run()
    {
        
    }
}
