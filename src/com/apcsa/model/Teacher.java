package com.apcsa.model;

import com.apcsa.model.User;

public class Teacher extends User {

    public Teacher(int userId, String accountType, String username, String password, String lastLogin) {
		super(userId, accountType, username, password, lastLogin);
		// TODO Auto-generated constructor stub
	}
	private int teacherId;
    private int departmentId;
    private String firstName;
    private String lastName;
    
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