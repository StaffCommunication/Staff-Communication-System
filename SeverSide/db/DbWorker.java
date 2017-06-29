
package scsserver.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import scsserver.net.Acceptor;
import scsserver.net.msg.Base64Utils;
import scsserver.net.msg.Message;
import scsserver.net.msg.MessageType;

/*
* a thread to handle db queries
*/
public class DbWorker extends Thread 
{
    //a database connection
    DB db;
    //i/o streams
    final PipedInputStream in;
    
    //constructor
    public DbWorker(DB db,PipedOutputStream out)
    {
        this.db = db;
        //create a pipe
        in = new PipedInputStream();
        try
        {
            in.connect(out);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        //add pipe to global map
        Acceptor.add(out,"db");
    }
    
    //override run()
    @Override
    public void run()
    {
        //receive request, process it, return response
        while(true)
        {
            Message qs = recvQuery();
            
            if(qs != null)
            {
                //if REG message, authendicate client
                if(qs.getType() == MessageType.REG)
                    authendicate(qs.getPayload(), qs.getSrc(), qs.getOwner());
                
                else if(qs.getType() == MessageType.REQ)//db query
                {
                    JSONObject qData = parseQuery(Base64Utils.decode(qs.getPayload()));
                    switch((String)qData.get("type")){
                        case "UPDATE":
                        case "ALTER":
                        case "INSERT":
                            //call commit
                            commit((String)qData.get("query"), qs.getSrc(),qs.getOwner());
                            break;
                        case "SELECT":
                            select((String)qData.get("query"), qs.getSrc(), qs.getOwner());
                            break;
                    }//switch
                }//else if
                else if(qs.getType() == MessageType.SAVE_EVENT)
                    saveEvent(qs);
                else if(qs.getType() == MessageType.SAVE_FILE)
                    saveFile(qs);
                else if(qs.getType() == MessageType.SAVE_SMS)
                    saveSms(qs);
            }//if
        }
    }
    
    //receive a db query from a thread
    public Message recvQuery()
    {
        //read query
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        byte[] q = new byte[8*1024];//8 kb
        //end of stream
        String EOF;

        while(true)
        {
            //clear memory
            Arrays.fill(q, 0, q.length, (byte)0);
            try {
                int n = in.read(q, 0, q.length);
                EOF = new String(q,n - 3, 3);
                
                if(EOF.compareTo("END") == 0){
                    bStream.write(q, 0, n - 4);
                    break;
                }
                //write to byte stream
                bStream.write(q, 0, n);
            } catch (IOException e) {
                return null;
            }
        }
        
        return new Message(bStream.toString());
    }
    
    //send a SELECT query to db
    public void select(String qs, String src, String ownr)
    {
        //send query to database
        ResultSet rSet = db.query(qs);
        //response
        Message msg;
        if(rSet == null)//db query failed
        {
            msg = new Message(MessageType.RES_ERROR,"db",src,
                    Base64Utils.encode("Error"), ownr);
        }
        else{
            try {
                JSONArray jArray = new JSONArray();
                ResultSetMetaData rsmd = rSet.getMetaData();
                //column names
                ArrayList<String> clmns = new ArrayList<>();
                int count = rsmd.getColumnCount();
                
                //get all column names of all table columns returned
                for(int i = 1;i <= count; ++i)
                {
                    clmns.add(rsmd.getColumnName(i));
                }

                //build the json array
                while(rSet.next()){
                    JSONObject obj = new JSONObject();
                    
                    for(String clm : clmns){
                        //build a json object using the column names as the keys
                        obj.put(clm, rSet.getString(clm));
                    }
                    //add object to array
                    jArray.add(obj);
                }
                
                msg = new Message(MessageType.RES_RES, "db", src,
                        Base64Utils.encode(jArray.toJSONString()), ownr);
            } catch (SQLException e) {
                //do nothing
                System.out.println(e);
                msg = new Message(MessageType.RES_ERROR,"db",src,
                    Base64Utils.encode("Error"), ownr);
            }
        }
        //send back response
        Acceptor.forwardToClientHandler(msg);
    }
    
    //send either UPDATE, ALTER or INSERT query to db
    public void commit(String qs, String src, String ownr)
    {
        //response message
        Message msg;
        if(db.alter(qs))
        {
            //successfully execute()d either UPDATE,ALTER or INSERT
            msg = new Message(MessageType.RES_RES, "db", src,
                    Base64Utils.encode("Success"), ownr);
        }else{
            msg = new Message(MessageType.RES_ERROR, "db", src,
                    Base64Utils.encode("Error"), ownr);
        }
        //send back response
        Acceptor.forwardToClientHandler(msg);
    }
    
    
    //authenticate to server
    //the payload of an authendication message (REG) is a json object with the structure
    //{"workId":"user work id", "pwd":"password"}
    public void authendicate(String pLoad, String src, String ownr)
    {
        //the payload is always encoded using Base64 scheme
        pLoad = Base64Utils.decode(pLoad);
        JSONParser parser = new JSONParser();
        JSONObject creds = new JSONObject();//credentials
        boolean parsed = false;
        boolean loggedIn = false;
        try {
            creds = (JSONObject)parser.parse(pLoad);
            parsed = true;
        } catch (ParseException e) {
            //not parsed
            System.err.println(e);
        }
        
        //get password corresponding to the workId, match
        if(parsed){
            String id = (String)creds.get("workId");
            String pwd = (String)creds.get("pwd");
            String type = (String)creds.get("acctype");
            //get hash values
            id = DigestUtils.sha256Hex(id);
            pwd = DigestUtils.sha256Hex(pwd);
            
            //query db
            ResultSet rs = db.query("SELECT acctype,pwd FROM passwd WHERE workid=\""+id+"\"");
            
            try{
                if(rs != null && rs.next()){
                    //compare
                    if(pwd.compareTo(rs.getString("pwd")) == 0 && 
                            type.compareTo(rs.getString("acctype")) == 0){
                        loggedIn = true;
                    }
                }
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        
        //if logged in, confirm to client else send error message
        Message msg;
        if(loggedIn){
            msg = new Message(MessageType.CONF_CONF, "db", src, 
                    Base64Utils.encode("Log in successful"), ownr);
        }
        else{
            msg = new Message(MessageType.CONF_ERROR, "db", src, 
                    Base64Utils.encode("Log in failed"), ownr);
        }
        //send back response
        Acceptor.forwardToClientHandler(msg);
    }
    
    
    
    //process db query string, determine whether is a SELECT
    //, UPDATE, ALTER or INSERT query
    public JSONObject parseQuery(String qs)
    {
        //the query string is a json string
        JSONParser p = new JSONParser();
        try {
            JSONObject jo = (JSONObject) p.parse(qs);
            return jo;
        } catch (ParseException e) {
            return null;
        }
    }
    
    //save file to filesystem, keep a record on the db
    public void saveSms(Message msg)
    {
        String m = Base64Utils.decode(msg.getPayload());
        String q = "INSERT IGNORE INTO msg(src, dest, delivered, type,message) "
                + "values(\"" + msg.getSrc() + "\",\"" + msg.getDest() 
                + "\",\" 0,\"sms\"," + m + "\"";
        db.alter(q);
    }
    
    //save a file
    public void saveFile(Message msg)
    {
        
    }
    
    public void saveEvent(Message msg)
    {
        String eJString = Base64Utils.decode(msg.getPayload());
        JSONObject event;
        //parse
        JSONParser p = new JSONParser();
        try {
            
        } catch (Exception e) {
        }
    }
}//class DbWorker
