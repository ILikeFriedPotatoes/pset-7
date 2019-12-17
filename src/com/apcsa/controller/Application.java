package com.apcsa.controller;

import java.util.Scanner;
import com.apcsa.data.PowerSchool;
import com.apcsa.model.User;

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
        String username = in.next();

        System.out.print("Password: ");
        String password = in.next();

        // if login is successful, update generic user to administrator, teacher, or student
        try {
            if (login(username, password)) {
                activeUser = activeUser.isAdministrator()
                    ? PowerSchool.getAdministrator(activeUser) : activeUser.isTeacher()
                    ? PowerSchool.getTeacher(activeUser) : activeUser.isStudent()
                    ? PowerSchool.getStudent(activeUser) : activeUser.isRoot()
                    ? activeUser : null;

                if (isFirstLogin() && !activeUser.isRoot()) {
                    // first-time users need to change their passwords from the default provided
                	changePassword(username, password);
                }
                // create and show the user interface
                //
                // remember, the interface will be difference depending on the type
                // of user that is logged in (root, administrator, teacher, student)
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
        //
        // prompt root user to enter username of user whose password needs to be reset
        //
        // ask root user to confirm intent to reset the password for that username
        //
        // if confirmed...
        //      call database method to reset password for username
        //      print success message
        //
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
    		PowerSchool.updatePassword(username, newPassword);
    	} else {
    		System.out.println("Please enter in a new password");
    		changePassword(username, hashedPassword);
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
        if(Utils.confirm(in, "Are you sure you want to logout? (y/n)")) {
        	activeUser = null;
        }
    }
    
    /**
     * Displays the interface for student users
     */
    
    private void showStudentUI() {
    	while(activeUser != null) {
    		switch(getStudentMenuSelection()) {
    			case VIEW_GRADES: break;
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
    
    /*
     * enum TeacherAction {
    	VIEW_ENROLLMENT, ADD_ASSIGNMENT, DELETE_ASSIGNMENT, ENTER_GRADE, PASSWORD,
    	LOGOUT
    }
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
    			case VIEW_FACULTY: break;
    			case VIEW_FACULTY_DEPARTMENT: break;
    			case VIEW_ENROLLMENT: break;
    			case VIEW_ENROLLMENT_GRADE: break;
    			case VIEW_ENROLLMENT_COURSE: break;
    			case PASSWORD: break;
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