package org.university.Models;

// this is for the composite primary key

import java.io.Serializable;
import java.util.Objects;

public class CourseSubjectId implements Serializable {

    public long courseId;
    public long subjectId;

    public CourseSubjectId() {}

    public CourseSubjectId(long courseId, long subjectId) {
        this.courseId = courseId;
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseSubjectId)) return false;
        CourseSubjectId that = (CourseSubjectId) o;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, subjectId);
    }
}
