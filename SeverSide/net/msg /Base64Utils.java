
package scsserver.net.msg;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

//encode and decode payload using Base64 encoding scheme
public class Base64Utils {
    
    //encode : string --> string
    public static String encode(String st)
    {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(st.getBytes());
    }
    
    //decode : string --> string
    public static String decode(String st)
    {
        Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(st));
    }
    
    //encode : byte[] --> string
    public static String encode(byte [] data)
    {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
    
    //decode : string --> byte[]
    //the variable diff is not used anywhere
    //i have used it to differentiate between the two overloaded decode() methods
    public static byte[] decode(String data,int diff)
    {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(data);
    }
}
