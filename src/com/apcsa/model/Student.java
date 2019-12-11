package com.apcsa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.model.User;

public class Student extends User {

	public Student(int userId, String accountType, String username, String password, String lastLogin) {
		super(userId, accountType, username, password, lastLogin);
		// TODO Auto-generated constructor stub
	}

	private int studentId;
    private int classRank;
    private int gradeLevel;
    private int graduationYear;
    private double gpa;
    private String firstName;
    private String lastName;

    private int studentId() {
		return studentId;
    };
    private int classRank() {
    	return classRank;
    };
    private int gradeLevel() {
    	return gradeLevel;
    };
    private int graduationYear() {
    	return graduationYear;
    };
    private double gpa() {
    	return gpa;
    };
    private String firstName() {
    	return firstName;
    };
    private String lastName() {
    	return lastName;
    };

}
