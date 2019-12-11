package com.apcsa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.model.User;

public class Teacher extends User {

    private int teacherId;
    private int departmentId;
    private String firstName;
    private String lastName;

    public Teacher (User user, ResultSet rs) throws SQLException {
    	this(rs.getInt("teacher_id"),
             rs.getInt("department_id"),
             rs.getString("first_name"),
             rs.getString("last_name"),
             user
        );
    }


    public Teacher(int teacherId, int departmentId, String firstName, String lastName,  User user) {
    	super(user.getUserId(), user.getAccountType(), user.getUsername(), user.getPassword(), user.getLastLogin());
		this.teacherId = teacherId;
		this.departmentId = departmentId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
    
    public int getTeacherId() {
        return teacherId;
    }

    
    public int getDepartmentId() {
        return departmentId;
    }
    
    
    public String getFirstName() {
        return firstName;
    }
    
    
    public String getLastName() {
        return lastName;
    }

}