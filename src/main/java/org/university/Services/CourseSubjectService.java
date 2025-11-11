package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.university.Mappers.CourseSubjectMapper;
import org.university.Mappers.SubjectMapper;
import org.university.Models.CourseModel;
import org.university.Models.Course_SubjectModel;
import org.university.Models.SubjectModel;
import org.university.Repositories.CourseRepo;
import org.university.Repositories.CourseSubjectRepo;
import org.university.Repositories.SubjectRepo;
import org.university.dto.CourseSubjectDTO;
import org.university.dto.SubjectDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseSubjectService {
    @Inject
    SubjectRepo subjectRepo;

    @Inject
    CourseRepo courseRepo;

    @Inject
    CourseSubjectRepo courseSubjectRepo;

    @Inject
    CourseSubjectMapper  courseSubjectMapper;

    @Inject
    SubjectMapper subjectMapper;

    @Transactional
    public CourseSubjectDTO addSubjectToCourse(CourseSubjectDTO dto){
        CourseModel course = courseRepo.findById(dto.getCourseId());
        if(course == null){
            throw  new NotFoundException("Course not found");
        }

        SubjectModel subject = subjectRepo.findById(dto.getSubjectId());
        if(subject == null){
            throw  new NotFoundException("Subject not found");
        }

        //Check duplicate (avoid adding same subject twice)
        Course_SubjectModel existing = courseSubjectRepo
                .find("courseId = ?1 and subjectId = ?2", course, subject)
                .firstResult();

        if(existing != null){
            throw new IllegalStateException("Subject already assigned to this course");
        }

        Course_SubjectModel newCourse = new Course_SubjectModel();
        newCourse.setCourseId(course);
        newCourse.setSubjectId(subject);

        courseSubjectRepo.persist(newCourse);

        return dto;
    }

    public List<SubjectDTO> getSubjectsByCourse(long courseId){
        var list = courseSubjectRepo.findByCourseId(courseId);

        return list.stream().map(cs -> subjectMapper.toDTO(cs.getSubjectId())).collect(Collectors.toList());
    }

    @Transactional
    public void removeSubjectFromCourse(long courseId, long subjectId){
        var record = courseSubjectRepo.find("courseId.courseId = ?1 and subjectId.subId = ?2", courseId, subjectId).firstResult();

        if(record == null){
            throw  new NotFoundException("Subject not found");
        }

        courseSubjectRepo.delete(record);
    }
}
