package com.apcsa.data;

public class QueryUtils {

    /////// QUERY CONSTANTS ///////////////////////////////////////////////////////////////

    /*
     * Determines if the default tables were correctly loaded.
     */

    public static final String SETUP_SQL =
        "SELECT COUNT(name) AS names FROM sqlite_master " +
            "WHERE type = 'table' " +
        "AND name NOT LIKE 'sqlite_%'";

    /*
     * Updates the last login timestamp each time a user logs into the system.
     */

    public static final String LOGIN_SQL =
        "SELECT * FROM users " +
            "WHERE username = ?" +
        "AND auth = ?";

    /*
     * Updates the last login timestamp each time a user logs into the system.
     */

    public static final String UPDATE_LAST_LOGIN_SQL =
        "UPDATE users " +
            "SET last_login = ? " +
        "WHERE username = ?";

    /*
     * Updates the password of a user.
     */

    public static final String UPDATE_PASSWORD_SQL =
    		"UPDATE users "
    		+ "SET auth = ? " +
    		"WHERE username = ?";

    /*
     * Retrieves an administrator associated with a user account.
     */

    public static final String GET_ADMIN_SQL =
        "SELECT * FROM administrators " +
            "WHERE user_id = ?";

    /*
     * Retrieves a teacher associated with a user account.
     */

    public static final String GET_TEACHER_SQL =
        "SELECT * FROM teachers " +
            "WHERE user_id = ?";

    public static String GET_TEACHER_FROM_ID = 
    "SELECT * FROM TEACHERS " +
    	"WHERE teacher_id = ?";
    
    public static String GET_DEPARTMENT_BY_ID = 
    "SELECT * FROM DEPARTMENTS " +
    	"WHERE DEPARTMENT_ID = ?";
    
    public static String GET_ENROLLMENT_SQL = 
    "SELECT * FROM STUDENTS";
    
    public static String GET_USERS_FROM_USERNAME = 
    "SELECT * FROM USERS " +
    "WHERE username = ?";
    
    public static String GET_ENROLLMENT_FROM_ID =
    "SELECT * FROM STUDENTS " + 
    	"WHERE STUDENT_ID = ?";
    
    public static String GET_TEACHER_BY_DEPARTMENT_SQL =
    "SELECT * FROM TEACHERS " + 
    	"WHERE DEPARTMENT_ID = ?";
    
    public static final String GET_DEPARTMENTS_SQL =
    "SELECT * FROM DEPARTMENTS";
    
    public static final String GET_TEACHER_IDS_SQL =
    "SELECT * FROM TEACHERS";
    
    public static final String GET_ENROLLMENT_BY_GRADE =
    "SELECT * FROM STUDENTS " +
    	"WHERE GRADE_LEVEL = ?";
    
    public static final String GET_ENROLLMENT_WITH_COURSE_NO = 
    "SELECT * FROM COURSES " +
    	"WHERE COURSE_NO = ?";
    
    public static final String GET_ASSIGNMENT_GRADES_FROM_STUDENT_ID_AND_COURSE_ID =
    "SELECT * FROM ASSIGNMENT_GRADES " +
    	"WHERE COURSE_ID = ?" + 
    	"AND STUDENT_ID = ?";
    
    public static final String GET_COURSE_GRADES_FROM_COURSE_IDS =
    "SELECT * FROM COURSE_GRADES " +
    	"WHERE COURSE_ID = ?";
    
    public static final String GET_COURSES_FROM_STUDENT_ID =
    "SELECT * FROM COURSE_GRADES " +
    	"WHERE STUDENT_ID = ?";
    
    public static final String GET_STUDENT_ID_FROM_USER_ID =
    "SELECT * FROM STUDENTS " +
    	"WHERE USER_ID = ?";
    
    public static final String GET_USERS_WITH_USERNAME = 
    "SELECT * FROM USERS " +
    	"WHERE USERNAME = ?";
    
    public static final String GET_COURSES_FROM_COURSE_ID = 
    "SELECT * FROM COURSES " +
    	"WHERE COURSE_ID = ?";
    
    public static final String TEACHER_VIEW_ENROLLMENT = 
    "SELECT STUDENTS.first_name, STUDENTS.last_name, COURSE_GRADES.grade FROM COURSE_GRADES " +
    	"INNER JOIN STUDENTS ON COURSE_GRADES.STUDENT_ID=STUDENTS.STUDENT_ID"
    	+ " WHERE COURSE_ID = ?";

    public static final String GET_TEACHER_ID_FROM_USER_ID =
    	"SELECT TEACHERS.TEACHER_ID FROM TEACHERS "
    	+ "WHERE USER_ID = ?";
    
    public static final String GET_COURSE_TITLES = 
    "SELECT COURSES.COURSE_NO, COURSES.COURSE_ID FROM COURSES "
    + "WHERE TEACHER_ID = ?";
    		
    public static final String GET_COURSES_FROM_TEACHER_ID = 
    "SELECT * FROM COURSES " +
    	"WHERE TEACHER_ID = ?";
    
    public static final String GET_COURSE_GRADES_FROM_STUDENT_ID = 
    "SELECT * FROM COURSE_GRADES " +
    	"WHERE STUDENT_ID = ?";
    
    public static final String ADD_ASSIGNMENTS = 
    "INSERT INTO ASSIGNMENTS " +
    "VALUES(?, ?, ?, ?, ?, ?, ?)";
    /*
     * Retrieves a student associated with a user account.
     */

    public static final String GET_STUDENT_SQL =
        "SELECT * FROM students " +
            "WHERE user_id = ?";

    public static String GET_FACULTY =
        "SELECT users.user_id, account_type, username, auth, last_login, teacher_id, first_name, last_name, title, teachers.department_id " +
            "FROM teachers " +
            "INNER JOIN users ON teachers.user_id=users.user_id " +
            "INNER JOIN departments " +
            "ON teachers.department_id=departments.department_id";

    public static String GET_STUDENTS = 
    "SELECT last_name, first_name, graduation, student_id, class_rank, grade_level, gpa, users.user_id, account_type, username, auth, last_login " +
        "FROM students INNER JOIN users ON users.user_id = students.user_id ORDER BY last_name";

    public static String GET_DEPARTMENTS =
        "SELECT '[' || department_id || '] ' || title || '.' \"Phrase\" FROM departments ORDER BY department_id;";

    public static String GET_FACULTY_BY_DEPT =
    "SELECT last_name || ', ' || first_name || ' / ' || departments.title \"Phrase\" FROM teachers " +
        "INNER JOIN departments ON teachers.department_id=departments.department_id WHERE departments.department_id = ? ORDER BY last_name";

    public static String GET_STUDENT_ENROLLMENT_BY_COURSE_NO =
    "SELECT DISTINCT grade, course_no, course_grades.student_id \"STUDENT_ID\", course_grades.course_id, first_name, last_name, graduation FROM courses " +
        "INNER JOIN course_grades ON courses.course_id = course_grades.course_id " +
        "INNER JOIN students ON students.student_id = course_grades.student_id WHERE course_no = ?";

    public static String GET_STUDENT_COURSES =
    "SELECT courses.title, grade, courses.course_no FROM course_grades " +
        "INNER JOIN courses ON course_grades.course_id = courses.course_id " +
        "INNER JOIN students ON students.student_id = course_grades.student_id " +
        "WHERE students.student_id = ?";

    public static String GET_ASSIGNMENT_GRADES_BY_COURSE_STUDENT =
    "SELECT * FROM course_grades " +
        "INNER JOIN courses ON course_grades.course_id = courses.course_id " +
        "INNER JOIN students ON students.student_id = course_grades.student_id " +
        "WHERE students.student_id = ?";

    public static String GET_TEACHER_COURSES =
        "SELECT * FROM teachers INNER JOIN courses ON teachers.teacher_id = courses.teacher_id WHERE teachers.teacher_id = ?";

    public static String GET_STUDENT_ENROLLMENT_BY_COURSE_ID =
    "SELECT * FROM students LEFT OUTER JOIN course_grades ON students.student_ID = course_grades.student_id INNER JOIN courses ON courses.course_id = course_grades.course_id OUTER LEFT JOIN users ON users.user_id = students.student_id WHERE courses.course_id = ?";

    public static String GET_STUDENTS_BY_GRADE_SQL(int grade) {
        return "SELECT * FROM students " +
        "WHERE grade_level = " + String.valueOf(grade) + " " +
        "ORDER BY student_id";
    }
    
    public static String GET_COURSES_BY_COURSENO_SQL(String courseNo) {
        return "SELECT * FROM courses " + 
        "WHERE course_no = \"" + courseNo + "\"";
    }
    
    public static String COURSE_GRADES_BY_COURSEID_SQL(int courseId) {
        return "SELECT * FROM course_grades " + 
        "WHERE course_id = " + String.valueOf(courseId);
    }
    
    public static String GET_STUDENT_BY_STUDENT_ID_SQL(int studentId) {
        return "SELECT * FROM students " + 
        "WHERE student_id = " + String.valueOf(studentId) + " " +
        "ORDER BY last_name, first_name";
    }
    
    public static String GET_TEACHER_COURSES_SQL(int teacher_id) {
        return "SELECT * FROM courses c, teachers t " +
        "WHERE c.teacher_id = t.teacher_id " +
        "AND t.teacher_id = " + teacher_id + " " +
        "ORDER BY title";
    }
    
    public static String CREATE_ASSIGNMENT_SQL(
        int course_id, int assignment_id, int marking_period,
        int is_midterm, int is_final, String title, int point_value) {

        return "INSERT INTO assignments " +
        "(course_id, assignment_id, marking_period, is_midterm, is_final, title, point_value) " +
        "VALUES (" + course_id + ", " + assignment_id + ", " + marking_period + ", " +
                    is_midterm + ", " + is_final + ", \"" + title + "\", " + point_value + ")";
    }
    
    public static final String GET_ASSIGNMENTS_SQL =
        "SELECT * FROM assignments a, courses c " +
        "WHERE a.course_id = c.course_id " +
        "AND c.course_id = ? " +
        "ORDER BY a.assignment_id";
    
    public static String GET_TEACHER_ASSIGNMENTS_SQL(int teacher_id, String course_no, int marking_period, int is_midterm, int is_final) {
        return "SELECT * FROM assignments a, courses c, teachers t " +
        "WHERE c.teacher_id = t.teacher_id AND c.course_id = a.course_id " +
        "AND t.teacher_id = " + teacher_id + " " +
        "AND a.course_id = " + PowerSchool.getCourseIdFromCourseNo(course_no) + " " +
        "AND a.marking_period = " + marking_period + " " +
        "AND a.is_midterm = " + is_midterm + " " +
        "AND a.is_final = " + is_final + " " +
        "ORDER BY a.assignment_id";
    }
    
    public static String DELETE_ASSIGNMENT_SQL(int course_id, int assignment_id) {
        return "DELETE FROM assignments " +
        "WHERE course_id = " + course_id + " " +
        "AND assignment_id = " + assignment_id;
    }
    
    public static String DELETE_ASSIGNMENT_GRADES_SQL(int course_id, int assignment_id) {
        return "DELETE FROM assignment_grades " +
        "WHERE course_id = " + course_id + " " +
        "AND assignment_id = " + assignment_id;
    }
    
    public static final String GET_ASSIGNMENT_ID_FOR_ALTER_SQL =
        "SELECT * FROM assignments a, courses c " +
        "WHERE a.course_id = c.course_id " +
        "AND c.course_id = ? " +
        "AND a.title = ? " +
        "ORDER BY a.assignment_id";
    
}


