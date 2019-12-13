package com.apcsa.controller;

import java.util.Scanner;
import com.apcsa.data.PowerSchool;
import com.apcsa.model.User;

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
            System.out.print("\nUsername: ");
            String username = in.next();

            System.out.print("Password: ");
            String password = in.next();

            // if login is successful, update generic user to administrator, teacher, or student

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
     * Displays the interface for student users
     */
    
    private void studentInterface() {
    	System.out.println("\nLogin to an account");
    	System.out.println("View course grades.");
    	System.out.println("View assignment grades by course.");
    	System.out.println("Change password.");
    	System.out.println("Logout.");
    }
    
    /**
     *  Displays the interface for teacher users
     */
    
    private void teacherInterface() {
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
    
    private void administratorInterface() {
    	System.out.println("\nLogin to an account");
    	System.out.println("View faculty");
    	System.out.println("View faculty by department");
    	System.out.println("View enrollment");
    	System.out.println("View enrollment by grade");
    	System.out.println("View enrollment by course");
    	System.out.println("Change password");
    	System.out.println("Logout of an account");
    	
    }
    
    /**
     * Displays the interface for root users
     */
    
    private void rootUserInterface() {
    	System.out.println("\nHello, again, Root!");
    	System.out.println("Reset password foranother account");
    	System.out.println("Factory reset database");
    	System.out.println("Logout of an account");
    	System.out.println("Shutdown application");
    	
    }
    
    /**
     * Decides the type of account being logged into
     */

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