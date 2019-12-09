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
    
    public Administrator(User user, ResultSet rs) throws SQLException{
    	this(rs.getInt("administratorId"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            rs.getString("jobTitle"),
            rs
       );
    }
    
    public Administrator(int administratorId, String firstName, String lastName, String jobTitle, ResultSet rs) throws SQLException {
		super(rs);
		
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