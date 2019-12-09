package com.apcsa.model;

import com.apcsa.model.User;

public class Administrator extends User {

    private int administratorId;
    private String firstName;
    private String lastName;
    private String jobTitle;
    
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