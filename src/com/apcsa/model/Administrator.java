package com.apcsa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.model.User;

public class Administrator extends User {

	private int administratorId;
    private String firstName;
    private String lastName;
    private String jobTitle;
    
    /**
     * Creates an instance of the administrator class
     * @param User the user
     * @param rs the set of information for the user
     */
    
    public Administrator (User user, ResultSet rs) throws SQLException{
    	this(rs.getInt("administrator_Id"),
            rs.getString("first_Name"),
            rs.getString("last_Name"),
            rs.getString("job_Title"),
            rs
       );
    }
    
    public Administrator(int administratorId, String firstName, String lastName, String jobTitle, ResultSet rs) throws SQLException {
    	super(user.getUserId(), user.getAccountType(), user.getUsername(), user.getPassword(), user.getLastLogin());
		this.administratorId = administratorId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobTitle = jobTitle;
		
	}

	/**
     * @return administratorId
     */
    
    public int getAdministratorId() {
    	return administratorId;
    }
    
    /**
     * @return firstName
     */
    
    public String getFirstName() {
    	return firstName;
    }
    
    /**
     * @return lastName
     */

    public String getLastName() {
    	return lastName;
    }
    
    /**
     * @return jobTitle
     */
    
    public String getJobTitle() {
    	return jobTitle;
    }
    
}