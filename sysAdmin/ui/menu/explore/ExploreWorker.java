
package sysadmin.ui.menu.explore;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sysadmin.net.msg.Base64Utils;
import sysadmin.net.msg.Message;
import sysadmin.net.msg.MessageType;
import sysadmin.ui.UIManager;
import sysadmin.ui.scenes.Home;

/*
** create an interface where the admin can create new accounts and view 
** current account information
*/


public class ExploreWorker extends TabPane implements Runnable
{
    //new
    private final New nwStaff;
    //interface to show a list of registered staff
    private final StafftList sList;
    
    //piped input stream
    private final PipedInputStream pIn;
    
    //submit button
    private final Button sub;
    
    //all staff members
    private static ArrayList<Staff> list;
    
    //is the list initialised?
    public static boolean isInit;
    
    public ExploreWorker(PipedInputStream in)
    {
        //initialize
        pIn = in;
        sub = new Button("submit");
        nwStaff = new New(sub);
        sList = new StafftList();
        //add
        getTabs().addAll(nwStaff,sList);
        getStyleClass().add("tab-pane");
        setTabMinWidth(100);
        setTabMinHeight(35);
        
        //start handler
        new Thread(this,"explore").start();
        
    }
    
    //create pipe
    private void popen()
    {
        UIManager.createPipe("explore", pIn);
    }
    //set up
    public void setUp()
    {
        //new
        nwStaff.setUp();
        nwStaff.show();
        
        //all
        sList.setUpSelector();
    }
    
    @Override
    public void run()
    {
        //create pipe
        popen();
        //initialize staff list
        ExploreWorker.initList(pIn);
        sList.setUpMain();
        //button event listener
        sub.setOnAction((e)->{
            createNew();
        });
        
        //this thread shouldnt die!
        while(true){}
    }
    
    //create a new staff member account
    //build an SQL query string, send it to server and wait for response
    private void createNew()
    {
        //get the sql query
        String pLoad = nwStaff.getQuery();
        //get the work id ==> to be used to create a workid:passwd pair
        String wid = nwStaff.getWorkId();
        
        //add account
        if(add(pLoad)){
            //hash using SHA256
            wid = DigestUtils.sha256Hex(wid);
            //query string
            StringBuffer qString = new StringBuffer();
            qString.append("INSERT IGNORE INTO passwd(acctype,workid,pwd) values(");
            qString.append("\"staff\",\"" + wid + "\",\"" + wid + "\")");

            //query data
            JSONObject qData = new JSONObject();
            qData.put("type", "INSERT");
            qData.put("query", qString.toString());
            
            //add log in data
            if(add(qData.toJSONString())){
                //successfully created new account
                System.out.println("created .....");
            }else
                System.out.println("log in data not added ....");
        }else{
            System.out.println("account not added ....");
        }
        nwStaff.reset();
    }
    
    //add account
    private boolean add(String pLoad)
    {
        pLoad = Base64Utils.encode(pLoad);
        Message msg = 
                new Message(MessageType.REQ, Home.getWorkId(), "db", pLoad, "explore");
        
        //forward query to sender thread
        UIManager.sendToWorker("sender", msg);
        
        //wait for response
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buff = new byte[8*1024];
        String EOF;
        
        //receive
        while(true){
            Arrays.fill(buff, 0, buff.length, (byte)0);
            try {
                int n = pIn.read(buff, 0, buff.length);
                EOF = new String(buff,n - 3, 3);
                if(EOF.compareTo("END") == 0){
                    //write to array stream and exit
                    stream.write(buff, 0, n - 4);
                    break;
                }
                //write to byte array stream
                stream.write(buff, 0, n);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }//while
        
        msg = new Message(stream.toString());

        if(msg.getType() == MessageType.RES_RES)
            return true;
        return false;
    }
    
    
    /******* static methods ******/
    public static ArrayList getList()
    {
        return list;
    }
    
    //initialize list
    //get data from the server
    private static void initList(PipedInputStream rcver)
    {
        list = new ArrayList<Staff>();
        //send query to server
        JSONObject pl = new JSONObject();
        //pack
        pl.put("type", "SELECT");
        pl.put("query", "SELECT * FROM staff WHERE workID != \"" + Home.getWorkId() + "\"");
        //forward to Sender worker
        UIManager.sendToWorker("sender", new Message(MessageType.REQ,
                Home.getWorkId(),"db",
                Base64Utils.encode(pl.toJSONString()), "explore"));
        
        //receive response
        JSONArray array = recv(rcver);
        
        //initialize list
        for(Object jo : array)
            list.add(createStaffObject((JSONObject)jo));
        
        ExploreWorker.isInit = true;
    }
    
    //build a Staff object from a json object
    private static Staff createStaffObject(JSONObject jO)
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
    
    private static JSONArray recv(PipedInputStream rcver)
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
                int n = rcver.read(buff, 0, buff.length);
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
}
