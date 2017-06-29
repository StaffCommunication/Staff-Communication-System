
package sysadmin.ui.menu.explore;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
** list all staff members
*/


public class StafftList extends Tab
{
    //main pane
    private final BorderPane mPane;
    //center pane == modifiable
    private final ScrollPane cPane;
    
    //staff list
    
    
    //selector
    private final ComboBox<String> selector;
    
    public StafftList(){
        //init
        mPane = new BorderPane();
        cPane = new ScrollPane();
        selector = new ComboBox<>();
        
        //style classes
        mPane.getStyleClass().add("contact-list-main");
        cPane.getStyleClass().add("contact-list-center");
        
        setText("Explore");
        getStyleClass().add("h-tabs");
        setClosable(false);
        
        //set content of this tab
        setContent(mPane);
    }
    
    public void setUpSelector()
    {
        selector.getItems().addAll("all","department","faculty");
        selector.setValue("all");
        
        //init to all
        all();
        selector.setOnAction((e)->{
            setSelected(selector.getValue());
        });
    }
    
    //set selected
    public void setSelected(String slcted)
    {
        switch(slcted)
        {
            case "all":
                all();
                break;
            case "department":
                sortByDep();
                break;
            case "faculty":
                sortByFac();
                break;
        }
    }
    
    public void setUpMain()
    {
        //container for the selector
        HBox cont = new HBox();
        //style container
        cont.setPrefHeight(100);
        cont.setAlignment(Pos.CENTER_RIGHT);
        cont.setSpacing(20);
        cont.getChildren().addAll(new Label("Sort by : "), selector);
        
        Platform.runLater(()->{
            mPane.setTop(cont);
            mPane.setCenter(cPane);
        });
    }
    
    //print all
    private void all()
    {
        //table
        GridPane gp = new GridPane();
        //column headers
        Label n = new Label("Name"), ni = new Label("National ID"),
                wi = new Label("Work ID"), cp = new Label("Cellphone"),
                ea = new Label("E-mail"), g = new Label("Gender"),
                dob = new Label("D.O.B"), dep = new Label("Department"),
                fac = new Label("Faculty");
        
        n.getStyleClass().add("table-col-hrd");
        ni.getStyleClass().add("table-col-hrd");
        wi.getStyleClass().add("table-col-hrd");
        cp.getStyleClass().add("table-col-hrd");
        ea.getStyleClass().add("table-col-hrd");
        g.getStyleClass().add("table-col-hrd");
        dep.getStyleClass().add("table-col-hrd");
        dob.getStyleClass().add("table-col-hrd");
        fac.getStyleClass().add("table-col-hrd");
        
        gp.addRow(0, n, wi, ni, g, dob, cp, ea, dep, fac);
        int i = 0;
        ArrayList<Staff> lst = new ArrayList<>(ExploreWorker.getList());
        for(Staff st : lst)
        {
            gp.addRow(++i, new Label(st.getName()), new Label(st.getWID()),
                    new Label(st.getNID()), new Label(st.getGender()),
                    new Label(st.getDOB()), new Label(st.getCPhone()),
                    new Label(st.getEAddr()), new Label(st.getDepartment()),
                    new Label(st.getFaculty()));
        }
        
        gp.setVgap(15);
        gp.setHgap(20);
        gp.setStyle("-fx-padding : 20px;"
                + "-fx-background-color : inherit;"
                + "-fx-font-family : \"Courier\";"
                + "-fx-font-size : 12px;");       
        Label lab = new Label("All");
        VBox cont = new VBox(20, lab, gp);
        lab.getStyleClass().add("labl");
        cont.setPadding(new Insets(20));
        
        //set content of the main scroll pane
        Platform.runLater(()->{
            cPane.setContent(cont);
        });
    }
    
    //sort by department
    public void sortByDep()
    {
        //get a copy of the staff list
        ArrayList<Staff> lst = new ArrayList<>(ExploreWorker.getList());
        
        //main container
        VBox mCont = new VBox(10);
        //loop through the entire list, sort by dep
        for(int i = 0;i < lst.size(); ++i)
        {
            //get the department name
            String dep = lst.get(i).getDepartment();
            ArrayList<Staff> nL = getStaffByDep(dep, lst);
            //build table
            //table view object
            
            GridPane table = buildDepGrid(nL);
            Label lab = new Label(dep + " department");
            VBox cont = new VBox(20, lab, table);
            lab.getStyleClass().add("labl");
            lab.setMinWidth(100);
            cont.setPadding(new Insets(20));
            //add to main container
            mCont.getChildren().add(cont);
            
            //remove sub list
            lst.removeAll(nL);
        }
        
        //set content of the main scroll pane
        //mCont.setAlignment(Pos.CENTER);
        Platform.runLater(()->{
            cPane.setContent(mCont);
        });
    }
    
    //get a list of staff from a certain dep
    private ArrayList<Staff> getStaffByDep(String dep, ArrayList<Staff> lst)
    {
        ArrayList<Staff> nList = new ArrayList<>();
        
        for(Staff s : lst)
        {
            if(s.getDepartment().compareTo(dep) == 0)
            {
                nList.add(s);
            }
        }
        return nList;
    }
    
    //sort by faculty
    public void sortByFac()
    {
        //get a copy of the staff list
        ArrayList<Staff> lst = new ArrayList<>(ExploreWorker.getList());
        
        //main container
        VBox mCont = new VBox(10);
        //loop through the entire list, sort by dep
        for(int i = 0;i < lst.size(); ++i)
        {
            //get the department name
            String fac = lst.get(i).getFaculty();
            ArrayList<Staff> nL = getStaffByFac(fac, lst);
            //build table
            GridPane table = buildFacGrid(nL);

            Label lab = new Label("Faculty of " + fac);
            VBox cont = new VBox(8, lab, table);
            lab.getStyleClass().add("labl");
            cont.setPadding(new Insets(15));
            //add to main container
            mCont.getChildren().add(cont);
            
            //remove sub list
            lst.removeAll(nL);
        }
        
        //set content of the main scroll pane
        //mCont.setAlignment(Pos.CENTER);
        Platform.runLater(()->{
            cPane.setContent(mCont);
        });
    }

    //get a list of staff from a certain fac
    private ArrayList<Staff> getStaffByFac(String fac, ArrayList<Staff> lst)
    {
        ArrayList<Staff> nList = new ArrayList<>();
        
        for(Staff s : lst)
        {
            if(s.getFaculty().compareTo(fac) == 0)
            {
                nList.add(s);
            }
        }
        return nList;
    }
    
    
    private GridPane buildDepGrid(ArrayList<Staff> lst)
    {
        GridPane gp = new GridPane();
        
        //column headers
        Label n = new Label("Name"), ni = new Label("National ID"),
                wi = new Label("Work ID"), cp = new Label("Cellphone"),
                ea = new Label("E-mail"), g = new Label("Gender"),
                dob = new Label("D.O.B"), fac = new Label("Faculty");
        
        n.getStyleClass().add("table-col-hrd");
        ni.getStyleClass().add("table-col-hrd");
        wi.getStyleClass().add("table-col-hrd");
        cp.getStyleClass().add("table-col-hrd");
        ea.getStyleClass().add("table-col-hrd");
        g.getStyleClass().add("table-col-hrd");
        fac.getStyleClass().add("table-col-hrd");
        dob.getStyleClass().add("table-col-hrd");
        
        gp.addRow(0, n, wi, ni, g, dob, cp, ea, fac);
        int i = 0;
        for(Staff st : lst)
        {
            gp.addRow(++i, new Label(st.getName()), new Label(st.getWID()),
                    new Label(st.getNID()), new Label(st.getGender()),
                    new Label(st.getDOB()), new Label(st.getCPhone()),
                    new Label(st.getEAddr()), new Label(st.getFaculty()));
        }
        
        gp.setVgap(15);
        gp.setHgap(20);
        gp.setStyle("-fx-padding : 20px;"
                + "-fx-background-color : inherit;"
                + "-fx-font-family : \"Courier\";"
                + "-fx-font-size : 12px;");
        return gp;
    }
    
    private GridPane buildFacGrid(ArrayList<Staff> lst)
    {
        GridPane gp = new GridPane();
        
        //column headers
        Label n = new Label("Name"), ni = new Label("National ID"),
                wi = new Label("Work ID"), cp = new Label("Cellphone"),
                ea = new Label("E-mail"), g = new Label("Gender"),
                dob = new Label("D.O.B"), dep = new Label("Department");
        
        n.getStyleClass().add("table-col-hrd");
        ni.getStyleClass().add("table-col-hrd");
        wi.getStyleClass().add("table-col-hrd");
        cp.getStyleClass().add("table-col-hrd");
        ea.getStyleClass().add("table-col-hrd");
        g.getStyleClass().add("table-col-hrd");
        dep.getStyleClass().add("table-col-hrd");
        dob.getStyleClass().add("table-col-hrd");
        
        gp.addRow(0, n, wi, ni, g, dob, cp, ea, dep);
        int i = 0;
        for(Staff st : lst)
        {
            gp.addRow(++i, new Label(st.getName()), new Label(st.getWID()),
                    new Label(st.getNID()), new Label(st.getGender()),
                    new Label(st.getDOB()), new Label(st.getCPhone()),
                    new Label(st.getEAddr()), new Label(st.getDepartment()));
        }
        
        gp.setVgap(15);
        gp.setHgap(20);
        gp.setStyle("-fx-padding : 20px;"
                + "-fx-background-color : inherit;"
                + "-fx-font-family : \"Courier\";"
                + "-fx-font-size : 12px;");
        return gp;
    }
}
