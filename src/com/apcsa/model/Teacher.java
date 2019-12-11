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
    
    

}