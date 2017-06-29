
package scsserver.net.msg;


public enum MessageType {
    //define message type constants
    SMS("sms"),//text message
    NOTF("notf"),//notification, news feed
    REG("reg"),//login data
    CONF_CONF("conf_conf"),//login confirmation
    CONF_ERROR("conf_error"),//log in failure
    REQ("req"),//query
    RES_RES("res_res"),//response from db query
    RES_ERROR("res_error"),//db 
    FILE("file"),//file packets
    SAVE_FILE("save_file"),//save this message, client disconnected
    SAVE_SMS("save_sms"),
    SAVE_EVENT("save_event"),
    EVENT("event"),
    UNKNOWN("unknown");
    
    
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
            case "conf_conf":
                return MessageType.CONF_CONF;
            case "conf_error":
                return MessageType.CONF_ERROR;
            case "notf":
                return MessageType.NOTF;
            case "file":
                return MessageType.FILE;
            case "res_error":
                return MessageType.RES_ERROR;
            case "res_res":
                return MessageType.RES_RES;
                default:
                    return MessageType.UNKNOWN;
        }
    }
}
