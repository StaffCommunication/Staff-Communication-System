
/*
*DBUtils.java
*Database class, wraps a database connection
*/
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    
    private static String db = "jdbc:mysql://localhost:3306/StaffComSystem";
    
    //connect to db
    public static Connection connect(String user, String pwd)
    {
        //try connecting to database
        Connection dbCon;
        try 
        {
            try
            {
                Class.forName(db);
            }
            catch(ClassNotFoundException e1)
            {
                return null;
            }
            dbCon = DriverManager.getConnection(db, user, pwd);
        } catch (SQLException e2)
        {
            //return false, connection failed
            return null;
        }
        return dbCon;
    }
    
    //query db
}
