/*
*staff member details
*full name, staffID, faculty, department
*/
package net;

public class BioData {
    //full name
    private String name;
    //ID
    private String ID;
    //faculty
    private String faculty;
    //department
    private String department;
    
    //constructor
    public BioData(String id)
    {
        //initialize
        this.ID = id;
        this.name = "";
        this.department = "";
        this.faculty = "";
    }
    
    //getters
    public String getID()
    {
        return this.ID;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getFaculty()
    {
        return this.faculty;
    }
    
    public String getDepartment()
    {
        return department;
    }
    
    //setters
    public void setName(String n)
    {
        this.name = n;
    }
    
    public void setID(String id)
    {
        this.ID = id;
    }
    
    public void setDepartment(String d)
    {
        this.department = d;
    }
    
    public void setFaculty(String f)
    {
        this.faculty = f;
    }
    
    //set all
    public void setAll(String n, String f, String d)
    {
        //set
        this.name = n;
        this.department = d;
        this.faculty = f;
    }
}
