package com.apcsa.controller;

import java.util.ArrayList;
import java.util.Scanner;
import com.apcsa.data.PowerSchool;
import com.apcsa.data.QueryUtils;
import com.apcsa.model.Administrator;
import com.apcsa.model.User;
import com.apcsa.model.Student;
import java.sql.*;

/**
 * 
 * @title Power School
 * @author jwang
 * @author mman
 */

/**
 * 
 * Creates an enum for the root user, admin, student, and teacher to help
 * with user interface.
 *
 */

enum RootAction { 
	PASSWORD, DATABASE, LOGOUT, SHUTDOWN
}
enum AdminAction { 
	VIEW_FACULTY, VIEW_FACULTY_DEPARTMENT, VIEW_ENROLLMENT, VIEW_ENROLLMENT_GRADE,
	VIEW_ENROLLMENT_COURSE, PASSWORD, LOGOUT
}
enum StudentAction {
	VIEW_GRADES, VIEW_GRADES_COURSE, PASSWORD, LOGOUT
}
enum TeacherAction {
	VIEW_ENROLLMENT, ADD_ASSIGNMENT, DELETE_ASSIGNMENT, ENTER_GRADE, PASSWORD,
	LOGOUT
}

public class Application {

    private Scanner in;
    private User activeUser;
    private String username;
    private String password;

    /**
     * Creates an instance of the Application class, which is responsible for interacting
     * with the user via the command line interface.
     */

    public Application() {
        this.in = new Scanner(System.in);

        try {
            PowerSchool.initialize(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the PowerSchool application.
     */

    public void startup() {
        System.out.println("PowerSchool -- now for students, teachers, and school administrators!");

        // continuously prompt for login credentials and attempt to login

        while (true) {
            showLoginUI();
        }
    }
    
    /**
     * Displays the login screen
     */
    private void showLoginUI() {
    	System.out.print("\nUsername: ");
        this.username = in.next();

        System.out.print("Password: ");
        this.password = in.next();

        // if login is successful, update generic user to administrator, teacher, or student
        try {
            if (login(this.username, this.password)) {
                activeUser = activeUser.isAdministrator()
                    ? PowerSchool.getAdministrator(activeUser) : activeUser.isTeacher()
                    ? PowerSchool.getTeacher(activeUser) : activeUser.isStudent()
                    ? PowerSchool.getStudent(activeUser) : activeUser.isRoot()
                    ? activeUser : null;

                if (isFirstLogin() && !activeUser.isRoot()) {
                	changePassword(this.username, this.password);
                }
                createAndShowUI();
            } else {
                System.out.println("\nInvalid username and/or password.");
            }
        } catch (Exception e) {
        	shutdown(e);
        }
    }

    /**
     * Logs in with the provided credentials.
     *
     * @param username the username for the requested account
     * @param password the password for the requested account
     * @return true if the credentials were valid; false otherwise
     */

    public boolean login(String username, String password) {
        activeUser = PowerSchool.login(username, password);

        return activeUser != null;
    }
    
    /**
     * Displays the user interface specific to the user
     */
    
    public void createAndShowUI() {
    	System.out.println("\nHello, again, " + activeUser.getFirstName() + "!");
    	
    	if(activeUser.isRoot()) {
    		showRootUI();
    	} else if(activeUser.isAdministrator()) {
    		showAdministratorUI();
    	} else if(activeUser.isTeacher()) {
    		showTeacherUI();
    	} else if(activeUser.isStudent()) {
    		showStudentUI();
    	}
    }
    
    /**
     * Displays an interface for root users.
     */

    private void showRootUI() {
        while (activeUser != null) {
            switch (getRootMenuSelection()) {
                case PASSWORD: resetPassword(); break;
                case DATABASE: factoryReset(); break;
                case LOGOUT: logout(); break;
                case SHUTDOWN: shutdown(); break;
                default: System.out.println("\nInvalid selection."); break;
            }
        }
    }
    
    /*
     * Retrieves a root user's menu selection.
     * 
     * @return the menu selection
     */
    
    private RootAction getRootMenuSelection() {
    	System.out.println("\n[1] Reset user password.");
        System.out.println("[2] Factory reset database.");
        System.out.println("[3] Logout.");
        System.out.println("[4] Shutdown.");
        System.out.print("\n::: ");
        
        switch (Utils.getInt(in, -1)) {
            case 1: return RootAction.PASSWORD;
            case 2: return RootAction.DATABASE;
            case 3: return RootAction.LOGOUT;
            case 4: return RootAction.SHUTDOWN;
            default: return null;
        }
    }
    
    /*
     * Shuts down the application after encountering an error.
     * 
     * @param e the error that initiated the shutdown sequence
     */
    
    private void shutdown(Exception e) {
    	if (in != null) {
    	     in.close();
        }
        
        System.out.println("Encountered unrecoverable error. Shutting down...\n");
        System.out.println(e.getMessage());
                
        System.out.println("\nGoodbye!");
        System.exit(0);
    }
    
    /*
     * Releases all resources and kills the application.
     */

    private void shutdown() {        
        System.out.println();
            
        if (Utils.confirm(in, "Are you sure? (y/n) ")) {
            if (in != null) {
                in.close();
            }
            
            System.out.println("\nGoodbye!");
            System.exit(0);
        }
    }
    
    /*
     * Allows a root user to reset another user's password.
     */

    private void resetPassword() {
        System.out.println("\nUsername: ");
        String username = in.nextLine();
        User forgetfulUser = PowerSchool.resetUserPassword(username);
        if(Utils.confirm(in, "\nAre you sure you want to reset the password for " + username + "?")) {
        	PowerSchool.updatePassword(username, Utils.getHash(username));
        	PowerSchool.resetLastLogin(username);
        	System.out.println("Succesfully reset password for " + username + ".");
        }

    }
    
    /*
     * Asks a user to change his or her password
     */
    private void changePassword(String username, String hashedPassword) {
    	System.out.println("\nEnter new password: ");
    	String newPassword = Utils.getHash(in.next());
    	String oldPassword = activeUser.getPassword();
    	if(!(newPassword.equals(oldPassword))) {
    		System.out.println("Successfully changed password.");
    		activeUser.setPassword(newPassword);
    		PowerSchool.updatePassword(this.username, newPassword);
    	} else {
    		System.out.println("Please enter in a new password");
    		changePassword(this.username, hashedPassword);
    	}
    	
    }
    
    private void updatePassword(String username, String hashedPassword) {
    	System.out.println("\nEnter current password: ");
    	String oldPassword = Utils.getHash(in.next());
    	if(oldPassword.equals(hashedPassword)) {
    		System.out.println("Enter new password.");
    		String newPassword = Utils.getHash(in.next());
    		activeUser.setPassword(newPassword);
    		PowerSchool.updatePassword(username, newPassword);
    	}
    }
    
    /*
     * Resets the database to its factory settings.
     */

    private void factoryReset() {
        // ask root user to confirm intent to reset the database
    	if(Utils.confirm(in, "\nAre you sure you want to reset all settings and data? (y/n)\n")) {
    		// call database initialize method with parameter of true
    		PowerSchool.initialize(true);
            // print success message
    		System.out.println("Successfully reset database");
    	} 
    }
    
    /*
     * Logs out of the application.
     */

    private void logout() {
        if(Utils.confirm(in, "\nAre you sure you want to logout? (y/n) ")) {
        	activeUser = null;
        }
    }
    
    /**
     * Displays the interface for student users
     */
    
    private void showStudentUI() {
    	while(activeUser != null) {
    		switch(getStudentMenuSelection()) {
    			case VIEW_GRADES: viewGrades(); break;
    			case VIEW_GRADES_COURSE: break;
    			case PASSWORD: break;
    			case LOGOUT: logout(); break;
    			default: System.out.println("\nInvalid selection."); break;
    		}
    	}
    }
    
    private StudentAction getStudentMenuSelection() {
    		System.out.println("\n[1] View course grades.");
    		System.out.println("[2] View assignment grades by course.");
    		System.out.println("[3] Change password.");
    		System.out.println("[4] Logout");
    		System.out.print("\n::: ");
    		
    		switch(Utils.getInt(in, -1)) {
	    		case 1: return StudentAction.VIEW_GRADES;
	    		case 2: return StudentAction.VIEW_GRADES_COURSE;
	    		case 3: return StudentAction.PASSWORD;
	    		case 4: return StudentAction.LOGOUT;
	    		default: return null;
    		}
    }
    
    private void viewGrades() {
    	int userId = activeUser.getUserId();
    	int studentId;
    	try(Connection conn = PowerSchool.getConnection();
    	PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_STUDENT_ID_FROM_USER_ID);
    	PreparedStatement stmt2 = conn.prepareStatement(QueryUtils.GET_COURSES_FROM_STUDENT_ID);
    	PreparedStatement stmt3 = conn.prepareStatement(QueryUtils.GET_COURSES_FROM_COURSE_ID);) {
    		stmt.setInt(1, userId);
    		try(ResultSet rs = stmt.executeQuery()) {
    			studentId = rs.getInt("student_id");
    		}
    		ArrayList<Integer> studentIds = new ArrayList<Integer>();
    		ArrayList<String> studentCourses = new ArrayList<String>();
    		ArrayList<Float> studentGrades = new ArrayList<Float>();
    		stmt2.setInt(1, studentId);
    		try(ResultSet rs2 = stmt2.executeQuery()) {
    			while(rs2.next()) {
    				studentIds.add(rs2.getInt("course_id"));
    			}
    		}
    		try(ResultSet rs4 = stmt2.executeQuery()) {
    			while(rs4.next()) {
    				studentGrades.add(rs4.getFloat("grade"));
    			}
    		}
    		for(int i = 0; i < studentIds.size(); i++) {
    			stmt3.setInt(1, studentIds.get(i));
    			try(ResultSet rs3 = stmt3.executeQuery()) {
    				while(rs3.next()) {
    					studentCourses.add(rs3.getString("title"));
    				}
    			}
    		}
    		System.out.println("");
    		for(int i = 0; i < studentCourses.size(); i++) {
    			System.out.print((i+1) + ". " + studentCourses.get(i) + " / ");
    			if(studentGrades.get(i) == 0) {
    				System.out.println("--");
    			} else {
    				System.out.println(studentGrades.get(i));
    			}
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     *  Displays the interface for teacher users
     */
 
    private void showTeacherUI() {
    	while(activeUser!= null) {
    		switch(getTeacherMenuSelection()) {
	    		case VIEW_ENROLLMENT: break;
	    		case ADD_ASSIGNMENT: break;
	    		case DELETE_ASSIGNMENT: break;
	    		case ENTER_GRADE: break;
	    		case PASSWORD: break;
	    		case LOGOUT: logout(); break;
	    		default: System.out.println("\nInvalid selection."); break;
    		}
    	}
    }
    
    /*
     * Determines the user interface for teachers
     */
 
    private TeacherAction getTeacherMenuSelection() {
    	System.out.println("\n[1] View enrollment by course");
    	System.out.println("[2] Add assignment");
    	System.out.println("[3] Delete assignment");
    	System.out.println("[4] Enter grade");
    	System.out.println("[5] Change password");
    	System.out.println("[6] Logout");
    	System.out.print("\n::: ");
    	
    	switch(Utils.getInt(in, -1)) {
	    	case 1: return TeacherAction.VIEW_ENROLLMENT;
	    	case 2: return TeacherAction.ADD_ASSIGNMENT;
	    	case 3: return TeacherAction.DELETE_ASSIGNMENT;
	    	case 4: return TeacherAction.ENTER_GRADE;
	    	case 5: return TeacherAction.PASSWORD;
	    	case 6: return TeacherAction.LOGOUT;
    		default: return null;
    	}
    }
    
    /**
     * Displays the interface for administrators
     */
    
    private void showAdministratorUI() {
    	while(activeUser != null) {
    		switch(getAdminMenuSelection()) {
    			case VIEW_FACULTY: Administrator.viewFaculty(); break;
    			case VIEW_FACULTY_DEPARTMENT: viewDepartment(); break;
    			case VIEW_ENROLLMENT: Administrator.viewEnrollment(); break;
    			case VIEW_ENROLLMENT_GRADE: viewEnrollmentGrade(); break;
    			case VIEW_ENROLLMENT_COURSE: viewEnrollmentCourse(); break;
    			case PASSWORD: changePassword(this.username, this.password); break;
    			case LOGOUT: logout(); break;
    			default: System.out.println("\nInvalid selection."); break;
    		}
    	}
    }
    
    private AdminAction getAdminMenuSelection() {
    	System.out.println("\n[1] View faculty.");
    	System.out.println("[2] View faculty by department.");
    	System.out.println("[3] View student enrollment.");
    	System.out.println("[4] View student enrollment by grade.");
    	System.out.println("[5] View student enrollment by course.");
    	System.out.println("[6] Change password.");
        System.out.println("[7] Logout.");
        System.out.print("\n::: ");
    	
    	switch (Utils.getInt(in, -1)) {
    		case 1: return AdminAction.VIEW_FACULTY;
    		case 2: return AdminAction.VIEW_FACULTY_DEPARTMENT;
    		case 3: return AdminAction.VIEW_ENROLLMENT;
    		case 4: return AdminAction.VIEW_ENROLLMENT_GRADE;
    		case 5: return AdminAction.VIEW_ENROLLMENT_COURSE;
    		case 6: return AdminAction.PASSWORD;
    		case 7: return AdminAction.LOGOUT;
    		default: return null; 
    	}
    }
    
    private void viewEnrollmentCourse() {
    	System.out.println("\nCourse No.: ");
    	String courseNumber = in.nextLine();
    	Administrator.viewCourseNumber(courseNumber);
    }
    
    private void viewEnrollmentGrade() {
    	System.out.println("\nChoose a grade level.\n");
    	System.out.println("[1] Freshman.");
    	System.out.println("[2] Sophomore.");
    	System.out.println("[3] Junior.");
    	System.out.println("[4] Senior.\n");
		int grade = in.nextInt();
		in.nextLine();
		System.out.println();
		grade = (grade == 4) ? 12: (grade == 3) ? 11 :
		(grade == 2) ? 10 : (grade == 1) ? 9 : -1;
		if(grade == -1) {
			System.out.println("\nPlease enter a valid option (1-4).");
			viewEnrollmentGrade();
		}
    	Administrator.viewEnrollmentGrade(grade);
    }
    
    private void viewDepartment() {
    	ArrayList<String> department = Administrator.displayDepartments();
    	int departmentId = in.nextInt();
    	if(departmentId < 0 || departmentId > department.size()) {
    		System.out.println("\nPlease enter a valid department number.\n");
    		viewDepartment();
    	}
    	Administrator.getTeacherByDepartments(departmentId);
    }
    
    /**
     * Determines whether or not the user has logged in before.
     *
     * @return true if the user has never logged in; false otherwise
     */

    public boolean isFirstLogin() {
        return activeUser.getLastLogin().equals("0000-00-00 00:00:00.000");
    }

    /////// MAIN METHOD ///////////////////////////////////////////////////////////////////

    /*
     * Starts the PowerSchool application.
     *
     * @param args unused command line argument list
     */

    public static void main(String[] args) {
        Application app = new Application();

        app.startup();
    }
}