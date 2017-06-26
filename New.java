
package sysclient.ui.menu.events;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;


public class New extends Tab
{
    private Button submit;
    private Event ev;
    

    public  New(Button sub)
    {
        ev = new Event();
        submit = sub;
    }
    
    public void setUp()
    {
        ev.addItems();
        VBox vb = new VBox(10, ev, submit);
    }
    
    public  JSONObject getEvent()
    {
        
        return null;
    }
    
}

    

    

    

   
    
    

    

    
    

