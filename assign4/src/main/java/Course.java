import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Set;

public class Course {

    /**
     * Course id, e.g., CS110.
     * <p>
     * You MUST use cid to identify courses. Hint: override equals & hashCode.
     * For example, Calculus-CH1 and Calculus-BI2 are both classes for MA101,
     * if a student already enrolled Calculus-CH1 in the current semester, he is not allowed to enroll in Calculus-BI2.
     */
    private String cid;

    /**
     * Course name, e.g., Introduce to Java Programming.
     */
    private String name;

    /**
     * Credits of this course.
     */
    private Integer credit;

    /**
     * Grading schema of this course.
     */
    private GradingSchema gradingSchema;

    /**
     * Course capacity.
     */
    private Integer capacity;

    /**
     * You should update the left capacity when enrolling students.
     */
    private Integer leftCapacity;

    /**
     * One course may have one or more timeslots.
     * e.g., a lecture in Monday's 10:20-12:10, and a lab in Tuesday's 14:00-15:50.
     */
    private Set<Timeslot> timeslots;

    /**
     * Constructor of {@link Course}.
     *
     * @param cid           should be a non-empty string.
     * @param name          should be a non-empty string.
     * @param credit        should be a positive integer.
     * @param gradingSchema should not be null.
     * @param capacity      should be a positive integer.
     * @throws IllegalArgumentException if any of the above parameter is illegal.
     */
    public Course(String cid, String name, Integer credit, GradingSchema gradingSchema, Integer capacity) throws IllegalArgumentException {
        throw new NotImplementedException();
    }

    /**
     * Record a timeslot for this course.
     *
     * @param timeslot the new timeslot to be checked and recorded.
     * @throws ConflictedTimeslotException if {@code timeslot} conflicts with any of existing timeslot.
     */
    public void addTimeslot(Timeslot timeslot) throws ConflictedTimeslotException {
        throw new NotImplementedException();
    }
}

enum GradingSchema {

    /**
     * The course's grade will be recorded into GPA.
     */
    FIVE_LEVEL,

    /**
     * The course's grade will not affect GPA.
     */
    PASS_FAIL,
}

class Timeslot {

    /**
     * Day of week.
     */
    private final DayOfWeek dayOfWeek;

    /**
     * Start time.
     */
    private final Time start;

    /**
     * End time.
     */
    private final Time end;

    /**
     * Constructor of {@link Timeslot}.
     *
     * @param dayOfWeek should not be null.
     * @param start     should not be null.
     * @param end       should not be null, and should after {@code start}.
     * @throws IllegalArgumentException if any of the above parameter is illegal.
     */
    public Timeslot(DayOfWeek dayOfWeek, Time start, Time end) throws IllegalArgumentException {
        throw new NotImplementedException();
    }
}
