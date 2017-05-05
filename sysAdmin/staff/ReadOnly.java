
/*
*At times the admin would like to view the list of all staff members, in a read
*only mode. This class makes it possible
*/

package sysadmin.staff;

import javafx.scene.control.Label;

/*
*display only
*/

public class ReadOnly
{
    //full name of the staff member
    private final Label fName;
    //the national id of the staff member
    private final Label nationalID;
    //work id
    private final Label workID;
    //cellphone no.
    private final Label cellPhone;
    //email address
    private final Label eAddress;
    //gender
    private final Label gender;
    //faculty
    private final Label faculty;
    //department
    private final Label department;
    
    //constructor
    public ReadOnly(String fn, String ni, String wi,
            String cp, String ea, String g, String f, String d)
    {
        //initialize all staff member details
        fName = new Label(fn);
        cellPhone = new Label(cp);
        department = new Label(d);
        faculty = new Label(f);
        eAddress = new Label(ea);
        nationalID = new Label(ni);
        workID = new Label(wi);
        gender = new Label(g);

    }
    
    public void setName(String n)
    {
        fName.setText(n);
    }
    
    public void setDepartment(String d)
    {
        department.setText(d);
    }
    
    public void setFaculty(String f)
    {
        faculty.setText(f);
    }
    
    public void setGender(String g)
    {
        gender.setText(g);
    }
    
    public void setAddress(String ea)
    {
        eAddress.setText(ea);
    }
    
    public void setWorkID(String wi)
    {
        workID.setText(wi);
    }
    
    public void setNationalID(String ni)
    {
        nationalID.setText(ni);
    }
    
    //getters
    public Label getName()
    {
        return fName;
    }
    
    public Label getCellPhone()
    {
        return cellPhone;
    }
    
    public Label getWorkId()
    {
        return workID;
    }
    
    public Label getNationalId()
    {
        return nationalID;
    }
    
    public Label getAddress()
    {
        return eAddress;
    }
    
    public Label getFacultyLabel()
    {
        return faculty;
    }
    
    public Label getDepartment()
    {
        return department;
    }
    
    public Label getGender()
    {
        return gender;
    }
    

}//end
