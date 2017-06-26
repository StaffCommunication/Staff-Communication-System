
package sysclient.ui.menu.events;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.*;


public class History extends Tab{
    
    public History()
    {
        
    }
    public GridPane viewEvents()
    {
        GridPane grid =new GridPane();
        ColumnConstraints col1 =  new ColumnConstraints(50);
        grid.add(new Label("Event Title"), 0, 0);
        grid.add(new Label("Event Date"), 1, 0);
        grid.add(new Label("Venue"), 2, 0);
        grid.add(new Label("Details of Event"), 3, 0);
        grid.add(new Label("Reveived"), 4, 0);
        
        grid.getColumnConstraints().addAll(col1,col1,col1,col1,col1);
        
        RowConstraints row1=new RowConstraints(800);
        
        return grid;
    }
    
}
