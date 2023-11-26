package object_orienters;

import java.time.Duration;
import java.util.List;

public class Courses {
    // **REMOVE ALL COMMENTS AFTER CREATING NEEDED CLASSES (Teacher && Department)**
    // private Teacher teacher;
    private String courseType;
    // private Department courseDepartment;
    private String courseName;
    private int creditHours;
    private List<Courses> prerequisites;
    private List<WeeklyMeetings> weeklyMeetings;

    /*
     * public Courses(Teacher teacher, String courseType, Department
     * courseDepartment, String courseName, int creditHours,
     * List<Courses> prerequisites, List<WeeklyMeetings> weeklyMeetings) {
     * this.teacher = teacher;
     * this.courseType = courseType;
     * this.courseDepartment = courseDepartment;
     * this.courseName = courseName;
     * this.creditHours = creditHours;
     * this.prerequisites = prerequisites;
     * this.weeklyMeetings = weeklyMeetings;
     * }
     */

    private class WeeklyMeetings {
        private List<String> day; // CHANGED THIS TO A LIST 
        private Duration duration; // CHANGED THIS FROM PERIOD INTO DURATION
        private String room;

        public WeeklyMeetings(List<String> day, Duration duration, String room) {
            this.day = day;
            this.duration = duration;
            this.room = room;
        }

        public List<String> getDay() {
            return day;
        }

        public void setDay(List<String> day) {
            this.day = day;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

    }

    /*
     * public Teacher getTeacher() {
     * return teacher;
     * }
     * 
     * public void setTeacher(Teacher teacher) {
     * this.teacher = teacher;
     * }
     */

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    /*
     * public Department getCourseDepartment() {
     * return courseDepartment;
     * }
     * 
     * public void setCourseDepartment(Department courseDepartment) {
     * this.courseDepartment = courseDepartment;
     * }
     */

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public List<Courses> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Courses> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public List<WeeklyMeetings> getWeeklyMeetings() {
        return weeklyMeetings;
    }

    public void setWeeklyMeetings(List<WeeklyMeetings> weeklyMeetings) {
        this.weeklyMeetings = weeklyMeetings;
    }
}