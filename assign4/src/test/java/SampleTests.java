import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings({"java:S5845", "AssertBetweenInconvertibleTypes"})
class SampleTests {

    @BeforeAll
    static void checkRequiredFields() {
        Stream.of(Course.class, Timeslot.class, Student.class)
                .flatMap(cls -> Arrays.stream(cls.getDeclaredFields()))
                .map(Field::getModifiers)
                .forEach(mod ->
                        Assertions.assertTrue(
                                Modifier.isPrivate(mod) || Modifier.isProtected(mod),
                                "all fields should be private or protected"
                        )
                );

        Stream.of(DCourse.class, DTimeslot.class, DStudent.class)
                .forEach(cls -> {
                    val sup = cls.getGenericSuperclass();
                    val par = ((ParameterizedType) sup).getActualTypeArguments();
                    assert par.length == 1;
                    val act = (Class<?>) par[0];
                    for (val f : cls.getDeclaredFields()) {
                        try {
                            act.getDeclaredField(f.getName());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                            Assertions.fail("do not delete or rename any declared field");
                        }
                    }
                });
    }

    @Test
    @Order(1)
    void testCreateInstancesSimple() {
        Assertions.assertEquals(
                DCourse.builder()
                        .cid("CS110")
                        .name("I2JAVA")
                        .credit(3)
                        .gradingSchema(GradingSchema.FIVE_LEVEL)
                        .capacity(150)
                        .leftCapacity(150)
                        .timeslots(CollectionExt.asSet())
                        .build(),
                new Course("CS110", "I2JAVA", 3, GradingSchema.FIVE_LEVEL, 150)
        );

        Assertions.assertEquals(
                DTimeslot.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .start(Time.valueOf("14:00:00"))
                        .end(Time.valueOf("15:50:00"))
                        .build(),
                new Timeslot(DayOfWeek.MONDAY, Time.valueOf("14:00:00"), Time.valueOf("15:50:00"))
        );

        Assertions.assertEquals(
                DStudent.builder()
                        .id(12210000)
                        .name("Turing")
                        .courses(CollectionExt.asMap())
                        .build(),
                new UndergraduateStudent(12210000, "Turing")
        );

        Assertions.assertEquals(
                DStudent.builder()
                        .id(30010000)
                        .name("Tim")
                        .courses(CollectionExt.asMap())
                        .build(),
                new PostgraduateStudent(30010000, "Tim")
        );
    }

    @Test
    @Order(2)
    void testEnrollSimple() {
        val course = new Course("CS110", "I2JAVA", 3, GradingSchema.FIVE_LEVEL, 150);
        val student = new UndergraduateStudent(12210000, "Turing");

        Assertions.assertDoesNotThrow(
                () -> student.enroll(course),
                "student should successfully enrolled the course"
        );

        Assertions.assertThrows(
                CourseAlreadyEnrolledException.class,
                () -> student.enroll(course),
                "since this student already enrolled the course, it cannot enroll it again"
        );
    }

    @Test
    @Order(3)
    @SneakyThrows
    void testCalculateGPASimple() {
        val student = new UndergraduateStudent(12210000, "Turing");
        val course1 = new Course("CS110", "I2JAVA", 3, GradingSchema.FIVE_LEVEL, 150);
        val course2 = new Course("CS101", "I2CS", 2, GradingSchema.FIVE_LEVEL, 100);
        val course3 = new Course("CLE050", "JAPANESE", 2, GradingSchema.PASS_FAIL, 20);

        student.enroll(course1);
        student.enroll(course2);
        student.enroll(course3);

        student.recordGrade(course1, Grade.B);
        student.recordGrade(course2, Grade.A);
        student.recordGrade(course3, Grade.PASS);

        Assertions.assertEquals(
                (3 * 3.73 + 2 * 4.00) / (3 + 2), student.getGpa(), 1e-5,
                "GPA = (3 * 3.73 + 2 * 4.00) / (3 + 2), PASS_FAIL courses are not counted"
        );
    }

    @Test
    @Order(4)
    @SneakyThrows
    void testCanGraduateSimple() {
        val student = new UndergraduateStudent(12210000, "Turing");

        val course1 = new Course("CS110", "I2JAVA", 6, GradingSchema.FIVE_LEVEL, 150);
        val course2 = new Course("CS101", "I2CS", 6, GradingSchema.PASS_FAIL, 100);
        val course3 = new Course("CS102", "OLD JAVA", 1, GradingSchema.FIVE_LEVEL, 100);

        student.enroll(course1);
        student.enroll(course2);
        student.enroll(course3);

        student.recordGrade(course1, Grade.A);
        Assertions.assertFalse(student.canGraduate(), "the undergraduate student only earned 6 credits < 12");

        student.recordGrade(course2, Grade.PASS);
        student.recordGrade(course3, Grade.B);
        Assertions.assertTrue(student.canGraduate(), "now he earned 3 courses, 12 credits, with GPA >= 3.00");
    }
}
