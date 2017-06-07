
/*
*DB.java
*Database class, wraps a database connection
*/
package scsserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    
    //database connection object
    private Connection dbCon;
    //db type, host, port, api and database name to connect to
    private final  String db;
    //server db account user name
    private final String accName;
    //server db account passwd
    private final String accPwd;
    
    //constructor
    public DB()
    {
        //initialize
        db = "jdbc:mysql://localhost:3306/scs";
        //name
        accName = new String("server");
        //password
        accPwd = new String("egerton536");
    }
    
    //connect to db
    public boolean connect()
    {
        //try connecting to database
        try 
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch(ClassNotFoundException e1)
            {
                //return false;
                e1.printStackTrace();
            }
            dbCon = DriverManager.getConnection(db, accName,accPwd);
        } catch (SQLException e2)
        {
            //return false, connection failed
            return false;
        }
        return true;
    }
    
    //query db ==> SELECT
    public ResultSet query(String qString)
    {
        try {
            Statement st = dbCon.createStatement();
            //create a result set object and return it
            return st.executeQuery(qString);
        } catch (Exception e) {
            return null;
        }
    }
    
    //insert, update, modify
    public boolean alter(String qString)
    {
        try {
            PreparedStatement ps = dbCon.prepareStatement(qString);
            try {
                //execute sql statement
                int status = ps.executeUpdate();
                if(status >= 0)//query not executed successfully
                    return true;
                return false;
            } catch (SQLException e) {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
