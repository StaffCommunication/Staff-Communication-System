
package sysadmin.ui.menu.chat;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sysadmin.net.msg.Base64Utils;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.UIManager;
import sysadmin.ui.scenes.Home;
//import sysadmin.ui.menu.explore.ExploreWorker;



public class ChatWorker extends SplitPane implements Runnable
{
    //create a toggle group for all contacts
    
    //a vbox
    private VBox contacts;
    //main chat pane
    private final ChatPane bPane;
    //input stream
    private final PipedInputStream pIn;
    //handle
    private Thread handle;
    
    //send button
    private Button send;
    private ArrayList<Staff> cList;
    
    public ChatWorker(PipedInputStream in)
    {
        //initialize
        contacts = new VBox();
        send = new Button("Send");
        bPane = new ChatPane(new TextMsgArea(send));
        pIn = in;
        
        //start thread
        handle = new Thread(this,"chat");
        handle.start();
        //set threads name
        setOrientation(Orientation.HORIZONTAL);
        //css id
        setId("chat-interface");
        contacts.setId("contacts-list");
        
        contacts.setPadding(new Insets(10));
        
        send.setOnAction((e)->{
            Message msg = bPane.getMsg();
            if(msg != null)
                //send
                send(msg);
        });
    }
    
    //create a pipe
    private void popen()
    {
        UIManager.createPipe("chat", pIn);
    }
    //set up ui
    public void setUp()
    {
        contacts.setMaxWidth(200);
        contacts.setMinWidth(200);
        contacts.maxWidthProperty().bind(widthProperty().multiply(0.0));
    }
    
    @Override
    public void run()
    {
        //init staff details
        setUp();
        popen();
        init();
        
        ScrollPane sp = new ScrollPane(contacts);
        sp.setMaxWidth(200);
        sp.setMinWidth(200);
        sp.maxWidthProperty().bind(widthProperty().multiply(0.0));
        
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Platform.runLater(()->{
            getItems().addAll(sp,bPane);
        });
    }
    
    
    public ArrayList<Staff> getList()
    {
        ArrayList<Staff> list = new ArrayList<>();
        JSONObject qry = new JSONObject();
        qry.put("type", "SELECT");
        qry.put("query", "SELECT * FROM staff");
        
        UIManager.sendToWorker("sender",new Message(MessageType.REQ,
                Home.getWorkId(), "db", Base64Utils.encode(qry.toJSONString()),
                "chat"));
         //receive response
        JSONArray array = recv();
        
        //initialize list
        if(array == null)
            return null;
        for(Object jo : array)
            list.add(createStaffObject((JSONObject)jo));
        return list;
    }
    
    //build a Staff object from a json object
    private Staff createStaffObject(JSONObject jO)
    {
        String n, c, w, ni, e, f, d, dob, g;
        
        //init to null
        n = c = w = ni = e = f = d = dob = g = null;
        
        for(Object key : jO.keySet())
        {
            switch((String)key)
            {
                case "nationalID":
                    ni = (String)jO.get(key);
                    break;
                case "name":
                    n = (String)jO.get(key);
                    break;
                case "gender":
                    g = (String)jO.get(key);
                    break;
                case "e_mail":
                    e = (String)jO.get(key);
                    break;
                case "workID":
                    w = (String)jO.get(key);
                    break;
                case "phoneNo":
                    c = (String)jO.get(key);
                    break;
                case "faculty":
                    f = (String)jO.get(key);
                    break;
                case "department":
                    d = (String)jO.get(key);
                    break;
                case "dob":
                    dob = (String)jO.get(key);
                    break;
            }
        }
        return new Staff(n, ni, w, e, dob, g, f, d, c);
    }
    public JSONArray recv()
    {
        //receive
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buff = new byte[16*1024];//16kb
        String EOF;
        JSONParser parser = new JSONParser();
        
        while(true)
        {
            //clear memory
            Arrays.fill(buff, 0,buff.length,(byte)0);
            try {
                //read
                int n = pIn.read(buff, 0, buff.length);
                EOF = new String(buff,n - 3, 3);
                if(EOF.compareTo("END") == 0){
                    stream.write(buff, 0, n - 4);
                    break;
                }
                stream.write(buff, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        //parse
        JSONArray arr;
        Message rcvdMsg = new Message(stream.toString());
        String jsonArrString = rcvdMsg.getPayload();
        jsonArrString = Base64Utils.decode(jsonArrString);
        
        try
        {
            //decode payload and parse
            arr = (JSONArray)parser.parse(jsonArrString);
        }catch(ParseException e){
            return null;
        }
        return arr;  
    }
    //initialize contacts
    private void init()
    {
        cList = getList();
        ToggleGroup tg = new ToggleGroup();
        for(Staff st : cList)
        {
            Contact c = new Contact(st, "sysadmin/resources/pictures/dp.JPG");
            contacts.getChildren().add(c);
            tg.getToggles().add(c);
            
            //set listener
            c.setOnAction((e)->{
                bPane.setChatArea(c.getChatArea());
                bPane.setHeader(c.getFullName());
                ChatPane.setDest(c.getWorkerId());
            });
        }
        
        tg.selectedToggleProperty().addListener(this::changed);
        Contact cc = (Contact)tg.getToggles().get(0);
        cc.setSelected(true);
        
        bPane.setChatArea(new ChatArea(cc.getFullName()));
        bPane.setHeader(cc.getFullName());
    }
    
    //new contact clicked
    private void changed(ObservableValue<? extends Toggle> observable,
            Toggle oB, Toggle nB)
    {
        if(nB != null){
            if(oB != null)
            {//change style
                ((ToggleButton)nB).setStyle("-fx-background-color: #004040; "
                            + "-fx-text-fill : silver");
                ((ToggleButton)oB).setStyle("-fx-background-color: silver; "
                            + "-fx-text-fill : #004040;");
                //System.out.println("here ***1***");
            }
            else{
                ((ToggleButton)nB).setStyle("-fx-background-color: #004040; "
                        + "-fx-text-fill : silver");
                //System.out.println("here 1***");
            }
        }/*else{
            ((ToggleButton)oB).setStyle("-fx-background-color: inherit; "
                        + "-fx-text-fill : silver");*/
            //System.out.println("here 2 ***");
        
    }
    
    
    //send message
    public void send(Message msg)
    {
        //UIManager.sendToWorker("chat", msg);
        System.out.println(msg);
    }
}
