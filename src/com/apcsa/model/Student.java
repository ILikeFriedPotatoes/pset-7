package com.apcsa.model;

import java.sql.Connection;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.controller.Utils;
import com.apcsa.data.PowerSchool;
import com.apcsa.model.User;

public class Student extends User {

    private int studentId;
    private int classRank;
    private int gradeLevel;
    private int graduationYear;
    private double gpa;
    private String firstName;
    private String lastName;

    public Student(User user, ResultSet rs) throws SQLException {
    	super(user);

    	this.studentId = rs.getInt("student_id");
    	this.classRank = rs.getInt("class_rank");
    	this.gradeLevel = rs.getInt("grade_level");
    	this.graduationYear = rs.getInt("graduation");
    	this.gpa = rs.getDouble("gpa");
    	this.firstName = rs.getString("first_name");
    	this.lastName = rs.getString("last_name");
	}

	public Student(ResultSet rs) throws SQLException {
		//user id, account type, username, password, last login
		super(rs.getInt("user_id"), rs.getString("account_type"), rs.getString("username"), rs.getString("auth"), rs.getString("last_login"));

		this.studentId = rs.getInt("student_id");
    this.classRank = rs.getInt("class_rank");
    this.gradeLevel = rs.getInt("grade_level");
    this.graduationYear = rs.getInt("graduation");
    this.gpa = rs.getDouble("gpa");
    this.firstName = rs.getString("first_name");
		this.lastName = rs.getString("last_name");
	}

    public String getFirstName() {
    	return firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public int getGraduation() {
		return this.graduationYear;
	}

	public int getGradeLevel() {
		return this.gradeLevel;
	}

}
