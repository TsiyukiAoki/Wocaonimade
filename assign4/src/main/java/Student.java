import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

public abstract class Student {

    /**
     * A unique student id, e.g., 12213199.
     */
    private Integer id;

    /**
     * Student's name.
     */
    private String name;

    /**
     * Enrolled courses.
     * <p>
     * If the student is still learning this course, the grade should be null.
     * Otherwise, record the grade in the map's value.
     * If the grade is FAIL or F, we allow the student to re-enroll this course by clearing the existing grade,
     * otherwise, one cannot re-enroll a studied course.
     */
    private Map<Course, Grade> courses;

    /**
     * Constructor of {@link Student}.
     *
     * @param id   should be an 8-digit integer:
     *             Undergraduates' ids should start with 1.
     *             Postgraduates' ids should start with 3.
     * @param name should be a non-empty string.
     * @throws IllegalArgumentException if any of the above parameter is illegal.
     */
    protected Student(Integer id, String name) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    /**
     * Checks if this student satisfies all the graduating conditions.
     * <p>
     * Common graduating conditions:
     * <ol>
     *     <li>Enroll at least 3 courses</li>
     *     <li>All courses are passed, e.g. should not be F or FAIL</li>
     * </ol>
     * There are also different conditions for undergraduates and postgraduates, see their javadoc.
     * <p>
     * Hint: you are allowed to change this abstract method into non-abstract.
     *
     * @return {@code true} if one satisfies all conditions, otherwise {@code false}.
     */
    public abstract boolean canGraduate();

    /**
     * Tries to enroll the course, do some checks before enrolling. If any condition is violated, throw exceptions in the following priority.
     *
     * @param course course that the student wants to enroll.
     * @throws CourseAlreadyPassedException   if there's a passing grade. If there is a FAIL or F, it's allowed to enroll, and you need to clear the previous failing grade.
     * @throws CourseAlreadyEnrolledException if the student already enrolled the course in this semester (the grade is null).
     * @throws ConflictedTimeslotException    if any learning course (grade is null) has conflicting timeslots with this course.
     * @throws NoLeftCapacityException        if the course is already full.
     */
    public void enroll(Course course) throws CourseAlreadyPassedException, CourseAlreadyEnrolledException, ConflictedTimeslotException, NoLeftCapacityException {
        throw new NotImplementedException();
    }

    /**
     * Record the grade of a course that is current learning.
     *
     * @param course the course to be recorded.
     * @param grade  the grade of this course.
     * @throws CourseNotTakenException      if this student has not enrolled this course.
     * @throws CourseAlreadyPassedException if this course is already graded.
     * @throws InvalidGradeException        if a PASS_FAIL course is assigned a FIVE_LEVEL grade, vise versa.
     */
    public void recordGrade(Course course, Grade grade) throws CourseNotTakenException, CourseAlreadyPassedException, InvalidGradeException {
        throw new NotImplementedException();
    }

    /**
     * Calculate the GPA for this student.
     * <p>
     * For all FIVE_LEVEL grading courses taken by this student, GPA is the weighted average number of their grades:
     * GPA = sum(credit * gradePoint) / sum(credit)
     *
     * @return the GPA.
     */
    public double getGpa() {
        throw new NotImplementedException();
    }
}

class UndergraduateStudent extends Student {

    public UndergraduateStudent(Integer id, String name) {
        super(id, name);
        throw new NotImplementedException();
    }

    /**
     * Additional graduating conditions for undergraduate students:
     * <ol>
     *     <li>Have earned at least 8 credits</li>
     *     <li>GPA >= 3.0</li>
     * </ol>
     *
     * @return {@code true} if one satisfies all conditions, otherwise {@code false}.
     */
    @Override
    public boolean canGraduate() {
        throw new NotImplementedException();
    }
}

class PostgraduateStudent extends Student {

    public PostgraduateStudent(Integer id, String name) {
        super(id, name);
        throw new NotImplementedException();
    }

    /**
     * Additional graduating conditions for postgraduate students:
     * <ol>
     *     <li>Have earned at least 5 credits</li>
     *     <li>GPA >= 3.2</li>
     * </ol>
     *
     * @return {@code true} if one satisfies all conditions, otherwise {@code false}.
     */
    @Override
    public boolean canGraduate() {
        throw new NotImplementedException();
    }
}

enum Grade {

    /**
     * Grade for courses using PASS_FAIL grading schema.
     */
    PASS(null),

    /**
     * Grade for courses using PASS_FAIL grading schema.
     */
    FAIL(null),

    /**
     * Grade for courses using FIVE_LEVEL grading schema.
     */
    A(4.00),

    /**
     * Grade for courses using FIVE_LEVEL grading schema.
     */
    B(3.73),

    /**
     * Grade for courses using FIVE_LEVEL grading schema.
     */
    C(3.09),

    /**
     * Grade for courses using FIVE_LEVEL grading schema.
     */
    D(2.08),

    /**
     * Grade for courses using FIVE_LEVEL grading schema.
     */
    F(0.00),
    ;

    /**
     * Grade point of the grade level.
     */
    private final Double gradePoint;

    Grade(Double gradePoint) {
        this.gradePoint = gradePoint;
    }
}
