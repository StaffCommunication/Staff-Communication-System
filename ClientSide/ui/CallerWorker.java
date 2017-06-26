
package sysclient.ui.menu.call;

import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import sysclient.net.Connector;

public class CallerWorker extends TabPane implements Runnable{
 public  Dialed dialed;
   public Missed missed;
   public Received received;
   public Connector connect;
    private HBox nav;
   
    public CallerWorker(){
       dialed = new Dialed();
       missed = new Missed();
       received = new Received();
      nav = new HBox(); // initialise  our HBox on the top section      
      }
    @Override
    public void run() {
    
    }
  public void setUp(){
      nav.getChildren().addAll();

      
      
  }
}
