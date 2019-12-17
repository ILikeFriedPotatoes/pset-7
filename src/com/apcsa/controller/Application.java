package com.apcsa.controller;

import java.util.Scanner;
import com.apcsa.data.PowerSchool;
import com.apcsa.model.User;

/**
 * 
 * @author jwang
 * @author mman
 */

/**
 * 
 * Creates an enum for the root user to
 *
 */

enum RootAction { PASSWORD, DATABASE, LOGOUT, SHUTDOWN }
enum AdminAction { VIEW_FACULTY, VIEW_FACULTY_DEPARTMENT, VIEW_ENROLLMENT,
	VIEW_ENROLLMENT_GRADE, VIEW_ENROLLMENT_COURSE, PASSWORD, LOGOUT}

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
                }

                // create and show the user interface
                //
                // remember, the interface will be difference depending on the type
                // of user that is logged in (root, administrator, teacher, student)
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
     * Resets the database to its factory settings.
     */

    private void factoryReset() {
        //
        // ask root user to confirm intent to reset the database
        //
        // if confirmed...
        //      call database initialize method with parameter of true
        //      print success message
        //
    }
    
    /*
     * Logs out of the application.
     */

    private void logout() {
        //
        System.out.print("Are you sure you want to logout? (y/n)");
        //
        // if confirmed...
        //      set activeUser to null
        //
    }
    
    /**
     * Displays the interface for student users
     */
    
    private void showStudentUI() {
    	System.out.println("\nLogin to an account");
    	System.out.println("View course grades.");
    	System.out.println("View assignment grades by course.");
    	System.out.println("Change password.");
    	System.out.println("Logout.");
    }
    
    private int getStudentMenuSelection() {
    	
    	return 1;
    }
    
    /**
     *  Displays the interface for teacher users
     */
    
    private void showTeacherUI() {
    	System.out.println("\nLogin to an account");
    	System.out.println("View enrollment by course.");
    	System.out.println("Add assignment.");
    	System.out.println("Delete assignment.");
    	System.out.println("Enter grade for assignment.");
    	System.out.println("Change password.");
    	System.out.println("Logout.");
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
    			case : break;
    			default: System.out.println("\nInvalid selection."); break;
    		}
    	}
    }
    
    private AdminAction getAdminMenuSelection() {
    	System.out.println("[1] View faculty.");
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
     * Decides the type of account being logged into
     *
     */
    //This is a function @jwang. I'm not sure if we need this, so it's fine if you delete it
    private void accountType() {
    	
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