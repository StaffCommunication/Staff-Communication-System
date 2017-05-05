
package sysadmin.net.msg;

/*
*define message types
*/

public enum MessageType {
    //define message type constants
    SMS("sms"),//text message
    NOTF("notf"),//notification, news feed
    REG("reg"),//login data
    CONF("conf"),//login confirmation
    REQ("req"),//query
    RES("res"),//response from db query
    ERROR("error"),//login failure
    FILE("file"),//file packets
    VIDEO("v_frane"),//video frame
    AUDIO("a_frame");//audio frame
    
    
    private final String id;
    
    MessageType(String id)
    {
        this.id = id;
    }
    
    //get the type id
    public String id()
    {
        return id;
    }
    
    //a factory method
    public static MessageType messageType(String t)
    {
        switch(t)
        {
            case "sms":
                return MessageType.SMS;
            case "reg":
                return MessageType.REG;
            case "req":
                return MessageType.REQ;
            case "conf":
                return MessageType.CONF;
            case "notf":
                return MessageType.NOTF;
            case "file":
                return MessageType.FILE;
            case "error":
                return MessageType.ERROR;
            case "v_frame":
                return MessageType.VIDEO;
            case "a_frame":
                return MessageType.AUDIO;
            default:
                return MessageType.ERROR;
        }
    }
}
