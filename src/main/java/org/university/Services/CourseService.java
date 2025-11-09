package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.university.Mappers.CourseMapper;
import org.university.Models.CourseModel;
import org.university.Repositories.CourseRepo;
import org.university.Repositories.LecturerRepo;
import org.university.dto.CourseDTO;

@ApplicationScoped
public class CourseService {
    @Inject
    CourseMapper courseMapper;

    @Inject
    CourseRepo courseRepo;

    @Inject
    LecturerRepo lecturerRepo;

    @Transactional
    public CourseDTO createCourse(CourseDTO dto){

        var lecturer = lecturerRepo.findById(dto.getLecturerId());

        if(lecturer == null){
            throw new NotFoundException("Lecturer not found");
        }

        CourseModel course = courseMapper.toModel(dto);
        course.setLecturer(lecturer);

        courseRepo.persist(course);

        return courseMapper.toDto(course);

    }
}
