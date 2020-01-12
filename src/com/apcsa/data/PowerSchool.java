package com.apcsa.data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.apcsa.controller.Utils;
import com.apcsa.model.Administrator;
import com.apcsa.model.Student;
import com.apcsa.model.Teacher;
import com.apcsa.model.User;

public class PowerSchool {

    private final static String PROTOCOL = "jdbc:sqlite:";
    private final static String DATABASE_URL = "data/powerschool.db";

    /**
     * Initializes the database if needed (or if requested).
     *
     * @param force whether or not to force-reset the database
     * @throws Exception
     */

    public static void initialize(boolean force) {
        if (force) {
            reset();    // force reset
        } else {
            boolean required = false;

            // check if all tables have been created and loaded in database

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(QueryUtils.SETUP_SQL)) {

                while (rs.next()) {
                    if (rs.getInt("names") != 9) {
                        required = true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // build database if needed

            if (required) {
                reset();
            }
        }
    }
    /**
     * Retrieves the user the root user is trying to reset the password for.
     */

    public static User resetUserPassword(String username) {
    	try (Connection conn = getConnection();
    	PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_USERS_FROM_USERNAME)) {
    		stmt.setString(1, username);
    		try(ResultSet rs = stmt.executeQuery()) {
    			return new User(rs);
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * Retrieves the User object associated with the requested login.
     *
     * @param username the username of the requested User
     * @param password the password of the requested User
     * @return the User object for valid logins; null for invalid logins
     */

    public static User login(String username, String password) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.LOGIN_SQL)) {

            stmt.setString(1, username);
            stmt.setString(2, Utils.getHash(password));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = new Timestamp(new Date().getTime());
                    int affected = PowerSchool.updateLastLogin(conn, username, ts);
                    if (affected != 1) {
                        System.err.println("Unable to update last login (affected rows: " + affected + ").");
                    }

                    return new User(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the administrator account associated with the user.
     *
     * @param user the user
     * @return the administrator account if it exists
     */

    public static User getAdministrator(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_ADMIN_SQL)) {

            stmt.setInt(1, user.getUserId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Administrator(user, rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Returns the teacher account associated with the user.
     *
     * @param user the user
     * @return the teacher account if it exists
     */

    public static User getTeacher(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_TEACHER_SQL)) {

            stmt.setInt(1, user.getUserId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Teacher(user, rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Returns the student account associated with the user.
     *
     * @param user the user
     * @return the student account if it exists
     */

    public static User getStudent(User user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_STUDENT_SQL)) {

            stmt.setInt(1, user.getUserId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(user, rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /*
     * Creates an arraylist of the teachers in the database.
     */

    public static ArrayList<Teacher> getFaculty() {
        ArrayList<Teacher> faculty = new ArrayList<Teacher>();
         try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_FACULTY);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    faculty.add(new Teacher(rs));
                }
            }
         } catch (SQLException e) {
             System.out.println(e);
         }

         return faculty;
     }

    
    /*
     * Creates an arraylist of students in the database.
     */
    
   public static ArrayList<Student> getStudents() {
       ArrayList<Student> students = new ArrayList<Student>();
       try (Connection conn = getConnection()) {
           PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_STUDENTS);
           try (ResultSet rs = stmt.executeQuery()) {
               while (rs.next()) {
                   students.add(new Student(rs));
               }
           }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return students;
   }
    
    /*
     * Establishes a connection to the database.
     *
     * @return a database Connection object
     * @throws SQLException
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(PROTOCOL + DATABASE_URL);
    }

    /*
     * Updates the last login time for the user.
     *
     * @param conn the current database connection
     * @param username the user's username
     * @param ts the current timestamp
     * @return the number of affected rows
     */

    private static int updateLastLogin(Connection conn, String username, Timestamp ts) {
    	try (PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_LAST_LOGIN_SQL)) {

            conn.setAutoCommit(false);
            stmt.setString(1, ts.toString());
            stmt.setString(2, username);

            if (stmt.executeUpdate() == 1) {
                conn.commit();

                return 1;
            } else {
                conn.rollback();

                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return -1;
        }
    }
    
    public static void resetLastLogin(String username) {
    	try(Connection conn = getConnection();
    		PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_LAST_LOGIN_SQL)) {
    		stmt.setString(1, "0000-00-00 00:00:00.000");
    		stmt.setString(2, username);
    		int affected = PowerSchool.resetLastLogin(conn, username);
    		if (affected != 1) {
                System.err.println("Unable to update last login (affected rows: " + affected + ").");
            }
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @param conn the current database connection
     * @param username the user's username
     * @return the number of affected rows
     */
    
    
    private static int resetLastLogin(Connection conn, String username) {
        try (PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_LAST_LOGIN_SQL)) {

            conn.setAutoCommit(false);
            stmt.setString(1, "0000-00-00 00:00:00.000");
            stmt.setString(2, username);

            if (stmt.executeUpdate() == 1) {
                conn.commit();

                return 1;
            } else {
                conn.rollback();

                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return -1;
        }
    }


    /*
     * Updates the password for the user.
     * 
     * @param conn the current database connection
     * @param username the user's username
     * @param hasehdPassword the password to update 
     */
    
    public static int updatePassword(String username, String hashedPassword) {
    	try (Connection conn = getConnection()) {
    	
    	PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_PASSWORD_SQL);
    	stmt.setString(1, hashedPassword);
    	stmt.setString(2, username);
    	conn.setAutoCommit(false);
    	if (stmt.executeUpdate() == 1) {
    		conn.commit();
    		
    		return 1;
    	}else {
    		conn.rollback();
    		
    		return -1;
    	}
        } catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    /*
     * Builds the database. Executes a SQL script from a configuration file to
     * create the tables, setup the primary and foreign keys, and load sample data.
     */

    private static void reset() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(new File("config/setup.sql")))) {

            String line;
            StringBuffer sql = new StringBuffer();

            // read the configuration file line-by-line to get SQL commands

            while ((line = br.readLine()) != null) {
                sql.append(line);
            }

            // execute SQL commands one-by-one

            for (String command : sql.toString().split(";")) {
                if (!command.strip().isEmpty()) {
                    stmt.executeUpdate(command);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Unable to load SQL configuration file.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: Unable to open and/or read SQL configuration file.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Unable to execute SQL script from configuration file.");
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Student> getStudentsByGradeWithUpdatedRank(int grade) {
        ArrayList <Student> students = new ArrayList<Student>();
        setRank(grade);
        students = getStudentsByGrade(grade);
        
        return students;
    }
    
    public static ArrayList<Student> getStudentsByGrade(int grade) {
        ArrayList<Student> students = new ArrayList<Student>();

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_STUDENTS_BY_GRADE_SQL(grade))) {
                while (rs.next()) {
                    students.add(new Student(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    private static void setRank(int grade) {
    	ArrayList<Student> students = new ArrayList<Student>(getStudentsByGrade(grade));
    	for (int i = 0; i < students.size(); i++) {
            Student comparing = students.get(i);
            int numOfBetterStudents = 0;

            if (comparing.getGPA() == -1.0) {
                students.get(i).setClassRank(0);
            } else {

                for (int j = 0; j < students.size(); j++) {
                    if (comparing.getGPA() < students.get(j).getGPA()) {
                        numOfBetterStudents++;
                    }
                }
                students.get(i).setClassRank(numOfBetterStudents+1);
            }
        }
    }

    public static ArrayList<Student> getStudentsByCourse(String courseNo) {
    	ArrayList<Student> students = new ArrayList<Student>();
        ArrayList<Integer> courseIds = new ArrayList<Integer>();
        ArrayList<Integer> studentIds = new ArrayList<Integer>();

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_COURSES_BY_COURSENO_SQL(courseNo))) {
                while (rs.next()) {
                    courseIds.add(rs.getInt("course_id"));
                }

                for (int courseId : courseIds) {
                    try (Connection conn2 = getConnection();
                        Statement stmt2 = conn2.createStatement()) {

                        try (ResultSet rs2 = stmt2.executeQuery(QueryUtils.COURSE_GRADES_BY_COURSEID_SQL(courseId))) {
                            while (rs2.next()) {
                                studentIds.add(rs2.getInt("student_id"));
                            }

                            for (int studentId : studentIds) {
                                try (Connection conn3 = getConnection();
                                    Statement stmt3 = conn3.createStatement()) {

                                    try (ResultSet rs3 = stmt3.executeQuery(QueryUtils.GET_STUDENT_BY_STUDENT_ID_SQL(studentId))) {
                                        while (rs3.next()) {
                                            students.add(new Student(rs3));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    public static ArrayList<String> getTeacherCourses(User user) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_TEACHER_COURSES_SQL(((Teacher) user).getTeacherId()))) {
                ArrayList<String> courseNos = new ArrayList<String>();
                while (rs.next()) {
                    courseNos.add(rs.getString("course_no"));
                }
                return courseNos;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static int getCourseIdFromCourseNo(String courseNo) {
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_COURSES_BY_COURSENO_SQL(courseNo))) {
                if (rs.next()) {
                    return rs.getInt("course_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static int createAssignment(int course_id, int marking_period, int is_midterm, int is_final, String title, int point_value) {
        int assignment_id = getNextAssignmentId(course_id);

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            if (stmt.executeUpdate(QueryUtils.CREATE_ASSIGNMENT_SQL(course_id, assignment_id, marking_period, is_midterm, is_final, title, point_value)) == 1) {
                System.out.println("\nSuccessfully created assignment.\n");
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
    private static int getNextAssignmentId(int course_id) {
        int assignmentId = 1;
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_ASSIGNMENTS_SQL)) {

            stmt.setInt(1, course_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assignmentId = rs.getInt("assignment_id") + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentId;
    }
    
    public static ArrayList<String> getTeacherAssignments(User user, String course_no, int marking_period, int is_midterm, int is_final) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_TEACHER_ASSIGNMENTS_SQL(((Teacher) user).getTeacherId(), course_no, marking_period, is_midterm, is_final))) {
                ArrayList<String> assignments = new ArrayList<String>();
                while (rs.next()) {
                    assignments.add(rs.getString("title"));
                }
                return assignments;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static ArrayList<Integer> getTeacherAssignmentPoints(User user, String course_no, int marking_period, int is_midterm, int is_final) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_TEACHER_ASSIGNMENTS_SQL(((Teacher) user).getTeacherId(), course_no, marking_period, is_midterm, is_final))) {
                ArrayList<Integer> assignmentsPts = new ArrayList<Integer>();
                while (rs.next()) {
                    assignmentsPts.add(rs.getInt("point_value"));
                }
                return assignmentsPts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static String getAssignmentName(String course_no, int assignment_id) {
        int course_id = getCourseIdFromCourseNo(course_no);
        String title = "";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM assignments a, courses c " +
            "WHERE a.course_id = c.course_id " +
            "AND c.course_id = ? " +
            "AND a.assignment_id = ? " +
            "ORDER BY a.assignment_id")) {

            stmt.setInt(1, course_id);
            stmt.setInt(2, assignment_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    title = rs.getString("title");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return title;
    }
    
    public static int deleteAssignment(int course_id, int assignment_id, String title) {
    	try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                Statement stmt2 = conn.createStatement()) {

                if (stmt.executeUpdate(QueryUtils.DELETE_ASSIGNMENT_SQL(course_id, assignment_id)) == 1) {
                    if (!getAssignmentsAssignmentGrades(course_id, assignment_id).isEmpty()) {
                        if (stmt2.executeUpdate(QueryUtils.DELETE_ASSIGNMENT_GRADES_SQL(course_id, assignment_id)) == 1) {
                            System.out.println("\nSuccessfully deleted " + title + ".\n");
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                    System.out.println("\nSuccessfully deleted " + title + ".\n");
                    return 0;
                } else {
                    return 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1;
        }
    
    public static ArrayList<Integer> getAssignmentsAssignmentGrades(int course_id, int assignment_id) {
        ArrayList<Integer> assignmentIds = new ArrayList<Integer>();
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery("SELECT * FROM assignment_grades WHERE course_id = " + course_id + " AND assignment_id = " + assignment_id)) {
                if (rs.next()) {
                    assignmentIds.add(rs.getInt("assignment_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentIds;
    }
    
    public static int getAssignmentId(String course_no, String title) {
        int course_id = getCourseIdFromCourseNo(course_no);
        int assignmentId = 0;
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(QueryUtils.GET_ASSIGNMENT_ID_FOR_ALTER_SQL)) {

            stmt.setInt(1, course_id);
            stmt.setString(2, title);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assignmentId = rs.getInt("assignment_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentId;
    }
    public static ArrayList<Student> getAssignmentStudents(String course_no) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(QueryUtils.GET_ASSIGNMENT_STUDENTS_SQL(course_no))) {
                ArrayList<Student> students = new ArrayList<Student>();
                while (rs.next()) {
                    students.add(new Student(rs));
                }
                return students;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    } 
}
