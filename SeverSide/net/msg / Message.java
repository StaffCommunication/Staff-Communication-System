
package scsserver.net.msg;

//message class

import java.util.Base64;
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
            src = jo.get("src").toString();
            dest = jo.get("dest").toString();
            //use type id (a string) to get message type
            mType = MessageType.messageType((String)jo.get("type"));
            payload = jo.get("payload").toString();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
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
    
    //get source user name
    public String getSrc()
    {
        return src;
    }
    
    //get the name of the destination client
    public String getDest()
    {
        return dest;
    }
    
    //get the payload
    public String getPayload()
    {
        return payload;
    }
    
    //payload is encoded using base 64 scheme
    //decode and return the actual data
    public byte [] getData()
    {
        Base64.Decoder dec = Base64.getDecoder();
        return dec.decode(payload);
    }
    
    //get the message type
    public MessageType getType()
    {
        return mType;
    }
}
