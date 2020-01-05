package com.apcsa.model;


import java.sql.*;
import com.apcsa.data.*;
import com.apcsa.model.*;
import java.util.*;
import java.util.ArrayList;
import com.apcsa.data.PowerSchool;

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
            user
       );
    }
    
    /**
     * Creates an instance of the administrator class
     * @param administratorId
     * @param firstName
     * @param lastName
     * @param jobTitle
     * @param user
     * @throws SQLException
     */
    
    public Administrator(int administratorId, String firstName, String lastName, String jobTitle, User user) throws SQLException {
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
    
    /**
     * Displays the faculty
     */
    
    public static void viewFaculty() {
    	try(Connection conn = PowerSchool.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_TEACHER_IDS_SQL)	) {
    		ArrayList<String> faculty = new ArrayList<String>();
    		try(ResultSet rs = stmt.executeQuery()) {
    			while(rs.next()) {
    				faculty.add(rs.getString("last_name") + ", " + rs.getString("first_name") + " / " + "\n");
    			}
    		}
    		System.out.println(faculty);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
}