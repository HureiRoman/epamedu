package edu.epam.model;

import edu.epam.annotations.Column;

/**
 * Created by fastforward on 08/07/15.
 */
public class StudentVisiting {
    @Column("lesson_id")
    private Integer lessonId;
    @Column("student_id")
    private Integer studentId;
    @Column("is_present")
    private boolean isPresent;

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }
}
