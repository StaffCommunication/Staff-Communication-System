
package sysclient.ui.menu.events;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import sysclient.net.msg.Message;

public class EventsView extends TabPane implements  Runnable{
    
    private New nw;
    private History hst;
    
    public EventsView()
    {
        Button sub = new Button("Submit");
        nw = new New(sub);
        nw.setUp();
        
        
        sub.setOnAction((e)->{
            
        });
    }

    @Override
    public void run()
    {
        
    }
    
    public void submitEvent(Event eve)
    {
        
    }
    
    public void send(Message msg)
    {
        
    }
          
}
