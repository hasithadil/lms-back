package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Models.*;
import org.university.Repositories.*;
import org.university.dto.CourseProgressDTO;
import org.university.dto.StudentOverallProgressDTO;
import org.university.dto.SubjectProgressDTO;
import org.university.dto.TopicProgressDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudentProgresService {
    @Inject
    TopicRepo topicRepo;

    @Inject
    StudentTopicProgressRepo studentTopicProgressRepo;

    @Inject
    StudentRepo studentRepo;

    @Inject
    EnrollmentRepo enrollmentRepo;

    @Inject
    CourseSubjectRepo courseSubjectRepo;

    @Transactional
    public TopicProgressDTO markTopicProgress(Long studentId,TopicProgressDTO dto) {
        StudentModel student = studentRepo.findById(studentId);
        if (student == null) {
            throw new NotFoundException("Student not found");
        }

        TopicModel topic = topicRepo.findById(dto.getTopicId());
        if (topic == null) {
            throw new NotFoundException("Topic not found");
        }

        // Check if student is enrolled in a course that has this subject
        boolean isEnrolled = isStudentEnrolledInSubject(dto.getStudentId(), topic.getSubject().getSubId());
        if (!isEnrolled) {
            throw new IllegalStateException("Student is not enrolled in any course containing this subject");
        }

        // find or create progress model
        StudentTopicProgressModel progress = studentTopicProgressRepo
                .findByStudentAndTopic(dto.getStudentId(), dto.getTopicId())
                .orElseGet(() -> {
                    StudentTopicProgressModel newProgress = new StudentTopicProgressModel();
                    newProgress.setStudent(student);
                    newProgress.setTopic(topic);

                    return newProgress;
                });

        progress.setIsCompleted(true);

        studentTopicProgressRepo.persist(progress);

        return convertProgressToDto(progress);
    }

    public SubjectProgressDTO getStudentProgressInSubject(Long studentId,Long subjectId) {
        StudentModel student = studentRepo.findById(studentId);
        if (student == null) {
            throw new NotFoundException("Student not found");
        }

        SubjectModel subject = topicRepo.findById(subjectId) != null
                ? topicRepo.findById(subjectId).getSubject()
                : null;

        // Get all topics in this subject
        List<TopicModel> allTopics = topicRepo.findBySubjectId(subjectId);

        List<StudentTopicProgressModel> studentProgresses = studentTopicProgressRepo.findByStudentAndSubject(studentId, subjectId);

        SubjectProgressDTO dto = new SubjectProgressDTO();
        dto.setSubjectId(subjectId);
        dto.setSubjectName(allTopics.isEmpty() ? "" : allTopics.get(0).getSubject().getSubjectName());
        dto.setTotalTopics(allTopics.size());

        List<TopicProgressDTO> topicProgressList = new ArrayList<>();
        int completedCount = 0;

        for (TopicModel topic : allTopics) {
            TopicProgressDTO topicDto = new TopicProgressDTO();
            topicDto.setTopicId(topic.getTopicId());
            topicDto.setTopicName(topic.getTopicName());

            StudentTopicProgressModel progress = studentProgresses.stream()
                    .filter(p -> p.getTopic().getTopicId().equals(topic.getTopicId()))
                    .findFirst()
                    .orElse(null);

            if (progress != null) {
                topicDto.setProgressId(progress.getProgressId());
                topicDto.setIsCompleted(progress.getIsCompleted());

                if (progress.getIsCompleted()) {
                    completedCount++;
                }
            } else {
                topicDto.setIsCompleted(false);
            }
            topicProgressList.add(topicDto);
        }

        dto.setCompletedTopics(completedCount);
        dto.setTopics(topicProgressList);

        dto.setCompletionPercentage(allTopics.isEmpty() ? 0.0
                : (completedCount * 100.0) / allTopics.size());

        return dto;
    }

    public CourseProgressDTO getStudentProgressInCourse(Long studentId,Long courseId) {
        EnrollmentModel enrollment = enrollmentRepo
                .find("student.s_id = ?1 AND course.courseId = ?2", studentId, courseId)
                .firstResult();

        if (enrollment == null) {
            throw new NotFoundException("Student not enrolled in this course");
        }

        List<Course_SubjectModel> courseSubjects = courseSubjectRepo.findByCourseId(courseId);

        CourseProgressDTO courseDTO = new CourseProgressDTO();
        courseDTO.setCourseId(courseId);
        courseDTO.setCourseName(enrollment.getCourse().getName());
        courseDTO.setTotalSubjects(courseSubjects.size());

        int totalTopicsInCourse = 0;
        int totalCompletedTopics = 0;
        List<SubjectProgressDTO> subjectsProgress = new ArrayList<>();

        for (Course_SubjectModel cs : courseSubjects) {
            Long subjectId = cs.getSubjectId().getSubId();
            SubjectProgressDTO subjectProgress = getStudentProgressInSubject(studentId, subjectId);

            totalTopicsInCourse += subjectProgress.getTotalTopics();
            totalCompletedTopics += subjectProgress.getCompletedTopics();

            subjectsProgress.add(subjectProgress);
        }

        courseDTO.setTotalTopics(totalTopicsInCourse);
        courseDTO.setCompletedTopics(totalCompletedTopics);
        courseDTO.setSubjectsProgress(subjectsProgress);
        courseDTO.setOverallCompletionPercentage(totalTopicsInCourse == 0 ? 0.0
                : (totalCompletedTopics * 100.0) / totalTopicsInCourse);

        return courseDTO;
    }

    public StudentOverallProgressDTO getStudentOverallProgress(Long studentId) {
        StudentModel student = studentRepo.findById(studentId);
        if (student == null) {
            throw new NotFoundException("Student not found");
        }

        List<EnrollmentModel> enrollments = enrollmentRepo
                .find("student.s_id", studentId)
                .list();

        StudentOverallProgressDTO overallDTO = new StudentOverallProgressDTO();
        overallDTO.setStudentId(studentId);
        overallDTO.setStudentName(student.getFirstName() + " " + student.getLastName());
        overallDTO.setTotalEnrolledCourses(enrollments.size());

        List<CourseProgressDTO> coursesProgress = enrollments.stream()
                .map(e -> getStudentProgressInCourse(studentId, e.getCourse().getCourseId()))
                .collect(Collectors.toList());

        overallDTO.setCoursesProgress(coursesProgress);

        return overallDTO;
    }

    public boolean isStudentEnrolledInSubject(Long studentId, Long subjectId) {
        List<EnrollmentModel> enrollments = enrollmentRepo
                .find("student.s_id", studentId)
                .list();

        if (enrollments == null) {
            throw new NotFoundException("not enrolled student");
        }

        for (EnrollmentModel enrollment : enrollments) {
            Long courseId = enrollment.getCourse().getCourseId();
            List<Course_SubjectModel> courseSubjects = courseSubjectRepo.findByCourseId(courseId);

            boolean hasSubject = courseSubjects.stream()
                    .anyMatch(cs -> cs.getSubjectId().getSubId().equals(subjectId));

            if (hasSubject) {
                return true;
            }
        }
        return false;
    }

    public TopicProgressDTO convertProgressToDto(StudentTopicProgressModel progress) {
        TopicProgressDTO dto = new TopicProgressDTO();
        dto.setProgressId(progress.getProgressId());
        dto.setStudentId(progress.getStudent().getS_id());
        dto.setTopicId(progress.getTopic().getTopicId());
        dto.setTopicName(progress.getTopic().getTopicName());
        dto.setIsCompleted(progress.getIsCompleted());

        return dto;
    }
}
