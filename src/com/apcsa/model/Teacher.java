package com.apcsa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.model.User;

public class Teacher extends User {

    private int teacherId;
    private int departmentId;
    public static int assignmentId = 1;
    private String firstName;
    private String lastName;
	private String departmentName;

    /**
     * Creates an instance of the Teacher class.
     *
     * @param user the User
     * @param rs a ResultSet of Teacher information
     */
    
    public Teacher(User user, ResultSet rs) throws SQLException {
    	super(user);
    	
    	this.teacherId = rs.getInt("teacher_id");
    	this.departmentId = rs.getInt("department_id");
    	this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
    }

    public Teacher(ResultSet rs) throws SQLException {
		super(rs.getInt("user_id"), rs.getString("account_type"), rs.getString("username"), rs.getString("auth"), rs.getString("last_login"));
		this.teacherId = rs.getInt("teacher_id");
		this.firstName = rs.getString("first_name");
		this.lastName = rs.getString("last_name");
        this.departmentName = rs.getString("title");
        this.departmentId = rs.getInt("department_id");
	}

	/**
     * @return teacherId
     */

    public int getTeacherId() {
        return teacherId;
    }

    /**
     * @return departmentId
     */

    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * @return firstName
     */

    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @return departmentName
     */
    
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @return lastName
     */

    public String getLastName() {
        return lastName;
    }

}
