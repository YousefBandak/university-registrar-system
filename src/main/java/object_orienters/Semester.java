package object_orienters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Semester {

    private String name; // name of the semester
    private String semesterName; // name + year
    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;
    private Set<Student> students;
    private Set<Teacher> teachers;
    private Set<Course> courses;
    private final long weeksNumber;
    private boolean isFall;
    private boolean isSpring;
    private boolean isSummer;

    /**
     * Constructs a new Semester with specified start and end dates.
     * Initializes the semester name, calculates its length in weeks, and sets flags
     * for
     * whether it's a Fall, Spring, or Summer semester.
     *
     * @param semesterStartDate The start date of the semester.
     * @param semesterEndDate   The end date of the semester.
     */

    public Semester(LocalDate semesterStartDate, LocalDate semesterEndDate) {

        this.semesterStartDate = semesterStartDate;
        this.semesterEndDate = semesterEndDate;
        this.name = giveName();
        this.semesterName = this.name + " - " + semesterStartDate.getYear();
        this.students = new HashSet<>();
        this.teachers = new HashSet<>();
        this.courses = new HashSet<>();
        this.isFall = this.name.equals("Fall");
        this.isSpring = this.name.equals("Spring");
        this.isSummer = this.name.equals("Summer");
        this.weeksNumber = calculateWeeksBetween(semesterStartDate, semesterEndDate);

    }

    /**
     * Registers a course in the semester, assigning a teacher and enrolling a list of students.
     * Ensures that the course is not already registered, that it does not have scheduling conflicts,
     * and that the assigned teacher and students meet the necessary criteria for registration.
     *
     * @param course The course to be registered in the semester.
     * @param lStudents The list of students attempting to enroll in the course.
     * @param teacher The teacher assigned to teach the course.
     *
     * Note: The method checks for duplicate course IDs, scheduling conflicts, teacher availability,
     * and student prerequisites before successful registration.
     */
    // TESTED SUCCESSFULLY
    public void registerInACourse(Course course, List<Student> lStudents, Teacher teacher) {
        boolean isNewCourse = courses.stream().noneMatch(e -> e.getCourseID().equalsIgnoreCase(course.getCourseID()));

        // Add new course to the semester if it doesn't already exist
        if (isNewCourse) {
            // Check for room conflict
            boolean roomConflict = courses.stream()
                    .flatMap(e -> e.getWeeklyMeetings().stream())
                    .anyMatch(wm -> course.getWeeklyMeetings().stream().anyMatch(wm2 -> wm2.hasRoomConflict(wm)));

            if (roomConflict) {
                System.out.println("Error registering " + course.getCourseName() + " because another course has conflict with room");
                return;
            }

            // Check if teacher is free
            if (!teacher.isFreeOn(course.getWeeklyMeetings())) {
                System.out.println("Error registering " + course.getCourseName() + " because teacher has conflict with course Weekly Meetings");
                return;
            }

            // Assign the teacher to the course and add the course to the teacher's registered courses
            course.setTeacher(teacher);
            teacher.getRegisteredCourses().add(course);
            this.teachers.add(teacher);

            // Add the course to the semester's course list
            this.courses.add(course);
        }

        // For existing courses, check if the same teacher is already assigned
        else if (course.getTeacher().isPresent() && !course.getTeacher().get().equals(teacher)) {
            System.out.println("Error registering " + course.getCourseName() + " because it already has a different teacher assigned.");
            return;
        }

        // Register the new list of students to the course
        for (Student student : lStudents) {
            if (!course.getEnrolledStudents().contains(student) &&
                    student.isFreeOn(course.getWeeklyMeetings()) &&
                    student.preRequisitesCheck(course) &&
                    !course.isFull()) {
                course.enrollStudent(student);
                student.addRegisteredCourse(course);
                System.out.println(student.getId() + " " + student.getName() + " registered in " + course.getCourseName());
                this.students.add(student);
            } else {
                System.out.println("Cannot register " + student.getName() + " in " + course.getCourseName());
            }
        }
    }


    /**
     * Unregisters a course from the semester, removing specific students and/or unassigning the teacher.
     * The course is removed from the semester if it no longer has any enrolled students or an assigned teacher.
     *
     * @param course The course from which students or the teacher will be unregistered.
     * @param studentsToUnregister The list of students to be unregistered from the course. Can be null if no students need to be unregistered.
     * @param unregisterTeacher A boolean indicating whether to unassign the teacher from the course.
     *
     * Note: The method updates course enrollment and teacher assignment accordingly and removes the course from the semester if it's left with no participants.
     */
    public void unregisterInACourse(Course course, List<Student> studentsToUnregister, boolean unregisterTeacher) {
        if (!courses.contains(course)) {
            System.out.println("Course with ID: " + course.getCourseID() + " is not registered in this semester.");
            return;
        }

        // Unenroll specified students from the course
        studentsToUnregister.stream()
                .filter(student -> course.getEnrolledStudents().contains(student))
                .forEach(student -> {
                    course.getEnrolledStudents().remove(student);
                    student.getRegisteredCourses().remove(course);
                    this.students.remove(student);
                    System.out.println("Student " + student.getId() + " " + student.getName() + " unregistered from " + course.getCourseName());
                });

        // Unassign the teacher from the course
        if (unregisterTeacher && course.getTeacher().isPresent()) {
            Teacher teacher = course.getTeacher().get();
            teacher.getRegisteredCourses().remove(course);
            this.teachers.remove(teacher);
            course.setTeacher(null);
            System.out.println("Teacher " + teacher.getName() + " is unassigned from course " + course.getCourseName());
        }

        // If there are no more students enrolled and the teacher is unassigned, remove the course from the semester
        if (course.getEnrolledStudents().isEmpty() && !course.getTeacher().isPresent()) {
            courses.remove(course);
            System.out.println("Course " + course.getCourseName() + " removed from the semester.");
        }
    }





    /**
     * Determines the name of the semester (Fall, Spring, or Summer) based on its
     * start date.
     *
     * @return The name of the semester.
     */
    public String giveName() {
        int startMonth = semesterStartDate.getMonthValue();
        if ((startMonth >= 9 && startMonth <= 12)) {
            // Fall semester (September to December)
            this.setName("Fall");
        } else if ((startMonth >= 1 && startMonth <= 6)) {
            // Spring semester (January to May)
            this.setName("Spring");
        } else if ((startMonth >= 6 && startMonth <= 8)) {
            // Summer semester (June to August)
            this.setName("Summer");
        }

        return name;
    }

    // HELPER NETHOD
    private void setName(String name) {
        this.name = name;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public long getWeeksNumber() {
        return weeksNumber;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Set<Course> getCourse() {
        return courses;
    }

    public LocalDate getSemesterStartDate() {
        return semesterStartDate;
    }

    public LocalDate getSemesterEndDate() {
        return semesterEndDate;
    }

    public boolean isFall() {
        return isFall;
    }

    public boolean isSpring() {
        return isSpring;
    }

    public boolean isSummer() {
        return isSummer;
    }

    /**
     * Calculates the number of weeks between two dates(Start date and End date).
     *
     * @param startDate The start date.
     * @param endDate   The end date.
     * @return The number of weeks between the two dates.
     */
    static long calculateWeeksBetween(LocalDate startDate, LocalDate endDate) {
        // Calculate the number of weeks in the Semester
        return ChronoUnit.WEEKS.between(startDate, endDate);
    }

    /**
     * Returns a string representation of the semester.
     * This representation includes the semester's name, start date, and end date,
     * providing a concise summary of the semester's duration and identity.
     *
     * @return A formatted string containing the semester's details.
     */
    @Override
    public String toString() {
        return "Semester: " + semesterName + "[from:" + semesterStartDate + ", to:" + semesterEndDate + "]";
    }

}
