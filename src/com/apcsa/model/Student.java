package com.apcsa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apcsa.model.User;

public class Student extends User {

    private int studentId;
    private int classRank;
    private int gradeLevel;
    private int graduationYear;
    private double gpa;
    private String firstName;
    private String lastName;

    public Student (User user, ResultSet rs) throws SQLException {
    	this(rs.getInt("student_id"),
    		 rs.getInt("class_rank"),
    		 rs.getInt("grade_level"),
    		 rs.getInt("graduation"),
    		 rs.getDouble("gpa"),
    		 rs.getString("first_name"),
    		 rs.getString("last_name"),
    		 user
    	);
    }

    public Student(int studentId, int classRank, int gradeLevel, int graduationYear, double gpa, String firstName, String lastName,
			User user) throws SQLException {
		super(user.getUserId(), user.getAccountType(), user.getUsername(), user.getPassword(), user.getLastLogin());
		this.studentId = studentId;
		this.classRank = classRank;
		this.gradeLevel = gradeLevel;
		this.graduationYear = graduationYear;
		this.gpa = gpa;
		this.firstName = firstName;
		this.lastName = lastName;

	}

    public int getStudentId() {
        return studentId;
    }

    public int getClassRank() {
        return classRank;
    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public double getGPA() {
        return gpa;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
