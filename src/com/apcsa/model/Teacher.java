package com.apcsa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.apcsa.controller.Utils;
import com.apcsa.data.PowerSchool;
import com.apcsa.data.QueryUtils;
import com.apcsa.model.User;

public class Teacher extends User {

    private int teacherId;
    private int departmentId;
    public static int assignmentId = 1;
    private String firstName;
    private String lastName;
	private String departmentName;

    /**
     * Creates an instance of the Teacher class.
     *
     * @param user the User
     * @param rs a ResultSet of Teacher information
     */
    
    public Teacher(User user, ResultSet rs) throws SQLException {
    	super(user);
    	
    	this.teacherId = rs.getInt("teacher_id");
    	this.departmentId = rs.getInt("department_id");
    	this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
    }

    public Teacher(ResultSet rs) throws SQLException {
		super(rs.getInt("user_id"), rs.getString("account_type"), rs.getString("username"), rs.getString("auth"), rs.getString("last_login"));
		this.teacherId = rs.getInt("teacher_id");
		this.firstName = rs.getString("first_name");
		this.lastName = rs.getString("last_name");
        this.departmentName = rs.getString("title");
        this.departmentId = rs.getInt("department_id");
	}

	/**
     * @return teacherId
     */

    public int getTeacherId() {
        return teacherId;
    }

    /**
     * @return departmentId
     */

    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * @return firstName
     */

    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @return departmentName
     */
    
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @return lastName
     */

    public String getLastName() {
        return lastName;
    }
    
    public static void viewStudentEnrollment(int userId, Scanner in) {

    	int teacherId = 0;
    	ArrayList<String> courseNumbers = new ArrayList<String>();
    	ArrayList<Integer> courseIds = new ArrayList<Integer>();
    	ArrayList<String> studentFirstName = new ArrayList<String>();
    	ArrayList<String> studentLastName = new ArrayList<String>();
    	ArrayList<Integer> studentGrade = new ArrayList<Integer>();
    	try (Connection conn = PowerSchool.getConnection();
    		PreparedStatement getCoursesFromCourseId = conn.prepareStatement(QueryUtils.TEACHER_VIEW_ENROLLMENT);
    		PreparedStatement getTeacherId = conn.prepareStatement(QueryUtils.GET_TEACHER_ID_FROM_USER_ID );
    		PreparedStatement getCourseTitles = conn.prepareStatement(QueryUtils.GET_COURSE_TITLES);) {
    		
    		getTeacherId.setInt(1, userId);
    		try(ResultSet teacherIdRs = getTeacherId.executeQuery()) {
    			teacherId = teacherIdRs.getInt("teacher_id");
    		}
    		getCourseTitles.setInt(1, teacherId);
    		try(ResultSet courseTitles = getCourseTitles.executeQuery()) {
    			while(courseTitles.next()) {
    				courseNumbers.add(courseTitles.getString("course_no"));
    				courseIds.add(courseTitles.getInt("course_id"));
    			}
    		}
    		System.out.println();
    		for(int i = 0; i < courseNumbers.size(); i ++) {
    			System.out.println("[" + (i+1) + "] " + courseNumbers.get(i));
    		}
    		System.out.print("\n::: ");
    		int selectionNumber = in.nextInt();
    		in.nextLine();
    		if(selectionNumber > courseNumbers.size() || selectionNumber < 0) {
    			System.out.println("Invalid selection.");
    		}
    		getCoursesFromCourseId.setInt(1, courseIds.get(selectionNumber - 1));
    		try(ResultSet courses = getCoursesFromCourseId.executeQuery()) {
    			while(courses.next()) {
    				studentFirstName.add(courses.getString("first_Name"));
    				studentLastName.add(courses.getString("last_name"));
    				studentGrade.add(courses.getInt("grade"));
    			}
    		}
    		System.out.println();
    		ArrayList<String> finalArrayList = new ArrayList<String>();
    		for(int i = 0; i < studentFirstName.size(); i++) {
    			finalArrayList.add(studentLastName.get(i) + ", "
    			 + studentFirstName.get(i) + " / ");
	    		if(studentGrade.get(i) == 0) {
	    			finalArrayList.set(i, finalArrayList.get(i) + "--");
	    		} else {
	    			finalArrayList.set(i, finalArrayList.get(i) + Integer.toString(studentGrade.get(i)));
    			}
    		}
    		Collections.sort(finalArrayList);
    		for(int i = 0; i < finalArrayList.size(); i++) {
    			System.out.println((i+1) + ". " + finalArrayList.get(i));
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void addAssignment(User user, Scanner in) {
        String courseNo = getCourseSelection(in, user);

        if (courseNo.isEmpty()) {
            System.out.println("\nYou don't teach any courses.\n");
        } else {
            int mpSelection = getMPSelection(in);

            System.out.print("\nAssignment Title: ");
            String title = in.nextLine();
            System.out.print("Point Value: ");
            int point_value = Utils.getInt(in, 0);
            while (point_value < 1 || point_value > 100) {
                System.out.println("\nPoint value must be between 1 and 100.\n");
                System.out.print("Point Value: ");
                point_value = Utils.getInt(in, 0);
            }

            int course_id = PowerSchool.getCourseIdFromCourseNo(courseNo);
            int marking_period = -1; int is_midterm = -1; int is_final = -1;

            switch (mpSelection) {
                case 1:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 2:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 3:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 4:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 5:
                    marking_period = 0;
                    is_midterm = 1;
                    is_final = 0;
                    break;
                case 6:
                    marking_period = 0;
                    is_midterm = 0;
                    is_final = 1;
                    break;
            }

            if (Utils.confirm(in, "Are you sure you want to create this assignment? (y/n) ")){
                if (PowerSchool.createAssignment(course_id, marking_period, is_midterm, is_final, title, point_value) == 1) {
                    System.out.println("\nThere was an error.\n");
                }
            } else {
                System.out.println();
            }
        }
    }
    
    private static String getCourseSelection(Scanner in, User user) {
        System.out.println("\nChoose a course.\n");
        ArrayList<String> courses = PowerSchool.getTeacherCourses(user);
        int numCourses = 0;
        if (courses.isEmpty()) {
            System.out.println("You teach no courses.");
            return null;
        }
        for (int i = 1; i <= courses.size(); i++) {
            System.out.println("[" + i + "]" + " " + courses.get(i-1));
            numCourses = i;
        }
        System.out.print("\n");
        int selection;
        do {
            System.out.print("::: ");
            selection = Utils.getInt(in, -1);
        } while (selection < 0 || selection > numCourses);
        return courses.get(selection - 1);
    }
    
    private static int getMPSelection(Scanner in) {
        System.out.println("\nChoose a marking period or exam status.\n");
        System.out.println("[1] MP1 assignment.");
        System.out.println("[2] MP2 assignment.");
        System.out.println("[3] MP3 assignment.");
        System.out.println("[4] MP4 assignment.");
        System.out.println("[5] Midterm exam.");
        System.out.println("[6] Final exam.");
        System.out.print("\n");

        int mpSelection;
        do {
            System.out.print("::: ");
            mpSelection = Utils.getInt(in, -1);
        } while (mpSelection < 1 || mpSelection > 6);

        return mpSelection;
    }
    
    public static void deleteAssignment(User user, Scanner in) {
        String courseNo = getCourseSelection(in, user);

        if (courseNo.isEmpty()) {
            System.out.println("\nYou don't teach any courses.\n");
        } else {
            int mpSelection = getMPSelection(in);
            int course_id = PowerSchool.getCourseIdFromCourseNo(courseNo);
            int marking_period = -1; int is_midterm = -1; int is_final = -1;
            switch (mpSelection) {
                case 1:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 2:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 3:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 4:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 5:
                    marking_period = 0;
                    is_midterm = 1;
                    is_final = 0;
                    break;
                case 6:
                    marking_period = 0;
                    is_midterm = 0;
                    is_final = 1;
                    break;
            }
            int asgnmt_id = getAssignment(user, in, courseNo, marking_period, is_midterm, is_final);
            if (asgnmt_id == 0) {
                System.out.println();
            } else {
                String title = PowerSchool.getAssignmentName(courseNo, asgnmt_id);

                if (Utils.confirm(in, "Are you sure you want to delete this assignment? (y/n) ")){
                    if (PowerSchool.deleteAssignment(course_id, asgnmt_id, title) == 1) {
                        System.out.println("\nThere was an error.\n");
                    }
                } else {
                    System.out.println();
                }
            }
        }
    }
    
    public static int getAssignment(User user, Scanner in, String course_no, int marking_period, int is_midterm, int is_final) {
        ArrayList<String> assignments = PowerSchool.getTeacherAssignments(user, course_no, marking_period, is_midterm, is_final);
        ArrayList<Integer> points = PowerSchool.getTeacherAssignmentPoints(user, course_no, marking_period, is_midterm, is_final);
        int count = 0;

        if (assignments.isEmpty()) {
            return 0;
        }

        System.out.println("Choose an assignment.\n");
        for (int i = 1; i <= assignments.size(); i++) {
            System.out.println("[" + i + "]" + " " + assignments.get(i-1) + " (" + points.get(i-1) + " pts)");
            count++;
        }
        System.out.print("\n");

        int selection = 0;
        do {
            System.out.print("::: ");
            selection = Utils.getInt(in, 0);
        } while (selection < 0 || selection > count);

        return PowerSchool.getAssignmentId(course_no, assignments.get(selection-1));
    }
    
    public static void enterGrade(User user, Scanner in) {
        String course_no = getCourseSelection(in, user);

        if (course_no.isEmpty()) {
            System.out.println("\nYou don't teach any courses.\n");
        } else {
            int mpSelection = getMPSelection(in);
            int course_id = PowerSchool.getCourseIdFromCourseNo(course_no);
            int marking_period = -1; int is_midterm = -1; int is_final = -1;
            switch (mpSelection) {
                case 1:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 2:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 3:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 4:
                    marking_period = mpSelection;
                    is_midterm = 0;
                    is_final = 0;
                    break;
                case 5:
                    marking_period = 0;
                    is_midterm = 1;
                    is_final = 0;
                    break;
                case 6:
                    marking_period = 0;
                    is_midterm = 0;
                    is_final = 1;
                    break;
            }
            int assignment_id = getAssignment(user, in, course_no, marking_period, is_midterm, is_final);
            if (assignment_id == 0) {
                System.out.println("\nYou have no assignments here.\n");
            } else {
                String title = PowerSchool.getAssignmentName(course_no, assignment_id);

                Student student = getStudent(in, course_no);
    
                System.out.println("\nAssignment: " + title + " (" + PowerSchool.getAssignmentPoints(course_no, assignment_id) + " pts)");
                System.out.println("Student: " + student.getName());
                int grade = PowerSchool.getStudentGradeInCourse(PowerSchool.getCourseIdFromCourseNo(course_no), student.getStudentId());
                System.out.println("Current Grade: " + (grade == -1 ? "--" : grade)); // TODO NEED TO PROGRAM
                System.out.print("\n");
    
                int newGrade;
                do {
                    System.out.print("New Grade: ");
                    newGrade = Utils.getInt(in, -1);
                } while (newGrade < 0 || newGrade > PowerSchool.getAssignmentPoints(course_no, assignment_id));
    
                if (Utils.confirm(in, "\nAre you sure you want to enter this grade? (y/n) ")){
                    if (PowerSchool.enterGrade(course_id, assignment_id, student.getStudentId(), newGrade, PowerSchool.getAssignmentPoints(course_no, assignment_id)) == 1) {
                        System.out.println("\nThere was an error.\n");
                    }
                } else {
                    System.out.println();
                }
            }
        }
    }
    
    public static int enterGrade(int course_id, int assignment_id, int student_id, int points_earned, int points_possible) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             Statement stmt2 = conn.createStatement()) {
            
            try (ResultSet rs = stmt2.executeQuery(
                "SELECT * FROM assignment_grades WHERE course_id = " + course_id +
                " AND student_id = " + student_id + " AND assignment_id = " + assignment_id)) {
                if (rs.next()) {
                    System.out.println("\nA grade already exists for this assignment and student.\n");
                    return 0;
                }
            }

            if (stmt.executeUpdate(QueryUtils.ENTER_GRADE_SQL(course_id, assignment_id, student_id, points_earned, points_possible)) == 1) {
                updateGpaAndClassRank(student_id);
                updateCourseGrades(student_id, course_id);
                System.out.println("\nSuccessfully entered grade.\n");
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static Student getStudent(Scanner in, String course_no) {
        System.out.println("\nChoose a student.\n");
        ArrayList<Student> students = PowerSchool.getAssignmentStudents(course_no);

        for (int i = 1; i <= students.size(); i++) {
            System.out.println("[" + i + "] " + students.get(i-1).getName());
        }
        System.out.print("\n");

        int selection;
        do {
            System.out.print("::: ");
            selection = Utils.getInt(in, 0);
        } while (selection < 1 || selection > students.size());

        return students.get(selection-1);
    }
    
}
