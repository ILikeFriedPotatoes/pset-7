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
     * Displays the departments
     */
    
    public static ArrayList<String> displayDepartments() {
    	try(Connection conn = PowerSchool.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_DEPARTMENTS_SQL);	) {
    		ArrayList<String> departments = new ArrayList<String>();
    		try(ResultSet rs = stmt.executeQuery()) {
    			while(rs.next()) {
    				departments.add(rs.getString("title"));
    			}
    			for(int i = 0; i < departments.size(); i++) {
    				System.out.println("[" + (i + 1) + "] " + departments.get(i));
    			}
    			System.out.println("\n" + "::: " + "\n");
    			return departments;
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static void getTeacherByDepartments(int departmentId) {
    	try(Connection conn = PowerSchool.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_TEACHER_BY_DEPARTMENT_SQL);) {
    		stmt.setInt(1, departmentId);
    		ArrayList<Integer> facultyIds = new ArrayList<Integer>();
    		ArrayList<String> facultyList = new ArrayList<String>();
    		try(ResultSet rs = stmt.executeQuery()) {
    			while(rs.next()) {
    				facultyIds.add(rs.getInt("teacher_id"));
    			}
    		}
    		for(int i = 0; i < facultyIds.size(); i++) {
    			String teacher = "";
    			teacher += (PowerSchool.teacherLastName(facultyIds.get(i)) + ", ");
    			teacher += (PowerSchool.teacherFirstName(facultyIds.get(i)) + " / ");
    			teacher += (PowerSchool.teacherDepartment(departmentId));
    			facultyList.add(teacher);
    		}
    		Collections.sort(facultyList);
    		for(int i = 0; i < facultyList.size(); i++) {
    			System.out.print((i + 1) + ". " + facultyList.get(i) + "\n");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Displays the faculty
     */
    
    public static void viewFaculty() {
    	try(Connection conn = PowerSchool.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_TEACHER_IDS_SQL);) {
    		ArrayList<Integer> facultyIds = new ArrayList<Integer>();
    		ArrayList<String> facultyList = new ArrayList<String>();
    		try(ResultSet rs = stmt.executeQuery()) {
    			while(rs.next()) {
    				facultyIds.add(rs.getInt("teacher_id"));
    			}
    		}
    		for(int i = 0; i < facultyIds.size(); i++) {
    			String teacher = "";
    			teacher += (PowerSchool.teacherLastName(facultyIds.get(i)) + ", ");
    			teacher += (PowerSchool.teacherFirstName(facultyIds.get(i)) + " / ");
    			int departmentId = PowerSchool.teacherDepartmentId(facultyIds.get(i));
    			teacher += (PowerSchool.teacherDepartment(departmentId));
    			facultyList.add(teacher);
    		}
    		Collections.sort(facultyList);
    		for(int i = 0; i < facultyList.size(); i++) {
    			System.out.print((i + 1) + ". " + facultyList.get(i) + "\n");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
}