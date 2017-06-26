package loginui;

import java.sql.*;


public class MyConnect {
    static Connection  conn; 
    String sql;
    PreparedStatement pst=null;
    Statement stmt;
    ResultSet rs=null;

    public MyConnect() {
    }
    
    public static Connection connectDB() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/Users?useSSL=false","root","");  
            if (conn == null) {
            System.out.println("Connection cannot be established");
        }
        return conn;    
            }
            catch(Exception e2){
                System.err.println(e2);
            }
        return null;
        }
    }

                
       /*try{
       Class.forName("com.mysql.jdbc.Driver");
         conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/students","root","");
       System.out.println("Connected to database");
       
       stmt=conn.createStatement();
       sql="select * from studentInfo ";
       rs=stmt.executeQuery(sql);
       
       
       
       while(rs.next()){
           r=rs.getInt("Roll");
           nm=rs.getString("Name");
           dept=rs.getString("Dept");
           
           System.out.print("   Roll:"+r);
           System.out.print("  Name:"+nm);
           System.out.println("  Dept:"+dept);
       }
   }
                
    catch(Exception e)
    {
       e.printStackTrace();
    }*/
       
    

    
    
    

