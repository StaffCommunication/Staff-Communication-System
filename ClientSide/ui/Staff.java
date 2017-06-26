
package sysadmin.ui.menu.chat;


/*
* this class defines the attributes of a staff member
*/
public class Staff {
    
    private final String name//full name
            ,nId//national id
            ,wId//work id
            ,eAddr//email address
            ,DOB//date of birth
            ,gender//gender
            ,fac//faculty
            ,dep//department
            ,cPhone;//mobile no.
    
    //constructor
    public Staff(String n, String ni, String w, String ea, String dob, String g,
            String f, String d, String c)
    {
        name = n;
        nId = ni;
        wId = w;
        eAddr = ea;
        DOB = dob;
        gender = g;
        fac = f;
        dep = d;
        cPhone = c;
    }
    
    //getters
    public String getName()
    {
        return name;
    }
    
    public String getWID()
    {
        return wId;
    }
    
    public String getNID()
    {
        return nId;
    }
    
    public String getCPhone()
    {
        return cPhone;
    }
    
    public String getEAddr()
    {
        return eAddr;
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public String getFaculty()
    {
        return fac;
    }
    
    public String getDOB()
    {
        return DOB;
    }
    
    public String getDepartment()
    {
        return dep;
    }
}
