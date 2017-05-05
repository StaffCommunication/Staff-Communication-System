
package sysadmin.net.msg;

//message class

import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//define the structure of any message send over the wire

public class Message {
    //message type
    private MessageType mType;
    //message source
    private String src;
    //message destination
    private String dest;
    //payload / msg data
    private String payload;
    
    //message constructor one
    public Message(
            MessageType t, String s, String d, String p)
    {
        //initialize
        src = s;
        mType = t;
        dest = d;
        payload = p;
    }
    
    //cunstructor two
    public Message(String buffer)
    {
        //parse json string
        JSONObject jo = new JSONObject();
        JSONParser parser = new JSONParser();
        
        //parse
        try
        {
            jo = (JSONObject) parser.parse(buffer);
            //initialize member variables
            src = (String)jo.get("src");
            dest = (String)jo.get("dest");
            //use type id (a string) to get message type
            mType = MessageType.messageType((String)jo.get("type"));
            payload = (String)jo.get("payload");
        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }
    
    //parse string from server, create a list of json objects
    public ArrayList<JSONObject> jsonStringsToList()
    {
        //declare and initialize a list
        ArrayList<JSONObject> jObjects = new ArrayList<>();
        //use string as an input stream
        Scanner scr = new Scanner(payload);
        //declare a json parser
        JSONParser parser = new JSONParser();
        
        while(scr.hasNextLine())
        {
            //the json objects are delimited by '\n'
            try
            {
                jObjects.add((JSONObject)parser.parse(scr.nextLine()));
            }
            catch(ParseException e){
                continue;
            }
        }
        return jObjects;
    }
    
    //convert Message to json string
    @Override 
    public String toString()
    {
        //create a json object
        JSONObject jObj = new JSONObject();
        //put
        jObj.put("src",src);
        jObj.put("dest", dest);
        jObj.put("type", mType.id());
        jObj.put("payload", payload);
        
        //return the json string
        return jObj.toJSONString();
    }
}
