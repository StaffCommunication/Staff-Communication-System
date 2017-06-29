
package sysadmin.ui.menu.account;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sysadmin.net.msg.Base64Utils;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.UIManager;
import sysadmin.ui.menu.explore.Staff;
import sysadmin.ui.scenes.Home;


public class AccountWorker extends VBox implements 
        Runnable
{
    //profile picture
    private final Circle crcl;
    //account details
    private Staff accData;
    
    //a log out button
    private final Button button;
    
    //splitpane
    private final SplitPane sp;
    //menu
    private final VBox menu;
    private final BorderPane bp;
    
    //in put stream
    private final PipedInputStream pi;

    //constructor
    public AccountWorker()
    {
        button = new Button("Log out");
        sp = new SplitPane();
        crcl = new Circle(50);
        menu = new VBox();
        bp = new BorderPane();
        pi = new PipedInputStream();
        
        menu.setId("acc-infor-menu");
        crcl.setFill(new ImagePattern(
                new Image("sysadmin/resources/pictures/one.png")));
        
        crcl.setSmooth(true);
        setPadding(new Insets(40));
        
        menu.setMinWidth(200);
        menu.maxWidthProperty().bind(sp.widthProperty().multiply(0.0));
        //start handler
        new Thread(this,"account").start();
    }
    
    //set up UI
    public void setUp()
    {
        //vbox
        VBox vb = new VBox(crcl,new Label(accData.getName()), button);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        
        button.setId("logout-button");
        vb.setStyle("-fx-text-fill : #888;"
                + "-fx-font-size : 20px;"
                + "-fx-padding : 10px;");
        sp.setStyle("-fx-background-color : silver;");
        //splitpane
        sp.getItems().addAll(menu, bp);
        sp.setId("user-infor-sp");
        
        //menu
        Button pcB = new Button("Save");
        PasswordChanger pc = new PasswordChanger(pcB);
        Profile pf = new Profile(accData);
        pf.setUpGrid();
        pc.setUpGrid();
        
        menu.getChildren().addAll(pf,pc);
        menu.setSpacing(20);
        
        menu.setStyle("-fx-background-color : silver;");
        
        this.getChildren().addAll(vb, sp);
        
        //listener
        pcB.setOnAction((e)->{
            changePwd();
        });
        
        addListeners(pc, pf);
    }
    
    //change password
    private void changePwd()
    {
        
    }
    
    //add event listeners
    private void addListeners(PasswordChanger pc, Profile pf)
    {
        pc.setOnAction((e)->{
            bp.setCenter(pc.getPCPane());
        });
        
        pf.setOnAction((e)->{
            bp.setCenter(pf.getGrid());
        });
        
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(pc, pf);
        bp.setCenter(pf.getGrid());
        
        tg.selectedToggleProperty().addListener(this::changed);
        pf.setSelected(true);
    }
    
    //changed toggle
    public void changed(ObservableValue<? extends Toggle> observable,
            Toggle oB, Toggle nB)
    {
        if(nB != null)
            ((ToggleButton)nB).setStyle("-fx-border-color : #004040;");
        if(oB != null)
            ((ToggleButton)oB).setStyle("-fx-border-color : silver;");
    }
    
    //override run()
    @Override
    public void run()
    {
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
        }
        init();
        
        Platform.runLater(()->{
            setUp();
        });
        
        //keep thread alive
        while(true){}
    }
    
    //initialize Staff details
    private void init()
    {
        //connect piped streams
        UIManager.createPipe("account", pi);
        JSONObject qo = new JSONObject();
        Message msg;
        qo.put("type", "SELECT");
        qo.put("query", "SELECT * FROM staff WHERE workID=\"root\"");
        //encode
        String q = Base64Utils.encode(qo.toJSONString());
        //send request
        msg = new Message(MessageType.REQ, Home.getWorkId(), "db", q, "account");
        UIManager.sendToWorker("sender",msg);
        //get response
        
        msg = recv();
        //payload
        accData = parse(msg.getPayload());
    }
    
    private Staff parse(String pLoad)
    {
        String n, wi, ni, dob, g, d, f, cp, ea;
        //parse payload
        JSONArray ja = null;
        JSONParser p = new JSONParser();
        pLoad = Base64Utils.decode(pLoad);
        try {
            ja = (JSONArray) p.parse(pLoad);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        JSONObject jo = (JSONObject)ja.get(0);
        n = (String) jo.get("name");
        wi = (String) jo.get("workID");
        ni = (String) jo.get("nationalID");
        dob = (String) jo.get("dob");
        g = (String) jo.get("gender");
        d = (String) jo.get("department");
        f = (String) jo.get("faculty");
        cp = (String) jo.get("phoneNo");
        ea = (String) jo.get("e_mail");
        
        return new Staff(n, ni, wi, ea, dob, g, f, d, cp);
    }
    
    //receive data
    private Message recv()
    {
        byte[] buff = new byte[16*1024];
        ByteArrayOutputStream st = new ByteArrayOutputStream();
        String EOF;
        
        while(true)
        {
            Arrays.fill(buff, 0, buff.length,(byte)0);
            try {
                int n = pi.read(buff, 0, buff.length);
                EOF = new String(buff, n - 3, 3);
                if(EOF.compareTo("END") == 0)
                {
                    st.write(buff, 0, n - 4);
                    break;
                }
                st.write(buff, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Message(st.toString());
    }
    
    //log out
    public void logOut()
    {
        
    }
}
