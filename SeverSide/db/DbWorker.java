
package scsserver.db;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import scsserver.net.Acceptor;
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
    PipedInputStream in;
    
    //constructor
    public DbWorker(DB db, PipedOutputStream out)
    {
        this.db = db;
        //create a pipe
        try
        {
            in = new PipedInputStream(out);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //override run()
    @Override
    public void run()
    {
        //receive request, process it, return response
        while(true)
        {
            Message qs = getDbQuery();          
            //
            if(qs != null)
            {
                //get payload / query infor
                //getData decodes the payload accordingly
                String qString = new String(qs.getData());
                //get type
                JSONObject query = parseQuery(qString);
                switch(query.get("query").toString())
                {
                    case "UPDATE":
                    case "INSERT":
                    case "ALTER":
                        if(commit(query.get("string").toString()))
                        {
                            //committed successfully
                            onSuccess(new Message(MessageType.ACK,
                                    qs.getDest(), qs.getSrc(),"Success"));
                        }
                        else
                            onError(new Message(MessageType.ERROR,
                                    qs.getDest(), qs.getSrc(),"Error"));
                        break;
                    case "SELECT":
                        ResultSet rs = select(query.get("string").toString());
                        if(rs == null)//error
                        {
                            //send error message to client
                            onError(new Message(MessageType.ERROR, qs.getDest(), qs.getSrc(),"Error"));
                        }
                        else//send results to client
                            reply(rs, qs.getDest(),qs.getSrc());
                }
            }
        }
    }
    
    //receive a db query from a thread
    private Message getDbQuery()
    {
        //read query
        byte[] q = new byte[8*1024];//8 kb
        
        try {
            in.read(q, 0, q.length);
            return new Message(new String(q));
        } catch (IOException e) {
            return null;
        }
    }
    
    //send a SELECT query to db
    private ResultSet select(String qs)
    {
        //get type
        return db.query(qs);
    }
    
    //send either UPDATE, ALTER or INSERT query to db
    private boolean commit(String qs)
    {
        return db.alter(qs);
    }
    
    //process db query string, determine whether is a SELECT
    //, UPDATE, ALTER or INSERT query
    private JSONObject parseQuery(String qs)
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
    
    //error
    private void onError(Message msg)
    {
        Acceptor.forwardToClientHandler(msg);
    }
    
    //success
    private void onSuccess(Message msg)
    {
        Acceptor.forwardToClientHandler(msg);
    }
    
    //reply, in case of a SELECT query
    private void reply(ResultSet rs, String s, String d)
    {
        //column names
        ArrayList<String> clmns = null;
        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int n = rsmd.getColumnCount();
            //create list
            clmns = new ArrayList<>();
            for(int i = 0;i < n;++i){
                clmns.add(rsmd.getCatalogName(i+1));
            }
            
            //create a json object of row no : row values (another json object)
            JSONObject jo = new JSONObject();//the payload
            int i = 1;
            while(rs.next())
            {
                jo.put(Integer.toString(i), row(rs, clmns));
                ++i;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    //json string representing the contents of a row
    private String row(ResultSet rs, ArrayList<String> cols)
    {
        //json object
        JSONObject jo = new JSONObject();
        for(String col : cols)
        {
            try {
                jo.put(col, rs.getString(col));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jo.toJSONString();//a json string
    }
}
