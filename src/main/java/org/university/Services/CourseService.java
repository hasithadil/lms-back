package org.university.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.university.Mappers.CourseMapper;
import org.university.Models.CourseModel;
import org.university.Models.LecturerModel;
import org.university.Repositories.CourseRepo;
import org.university.Repositories.LecturerRepo;
import org.university.dto.CourseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseService {
    @Inject
    CourseMapper courseMapper;

    @Inject
    CourseRepo courseRepo;

    @Inject
    LecturerRepo lecturerRepo;

    public List<CourseDTO> getAllCourses(){
        List<CourseModel> courses = courseRepo.listAll();

        return courses.stream().map(courseMapper::toDto).collect(Collectors.toList());
    }

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

    @Transactional
    public CourseDTO updateCourse(long id, CourseDTO dto){
        CourseModel course = courseRepo.findById(id);

        if(course == null){
            throw new NotFoundException("Course not found");
        }

        LecturerModel lecturer = lecturerRepo.findById(dto.getLecturerId());
        if(lecturer == null){
            throw new NotFoundException("Lecturer not found");
        } else {
            course.setLecturer(lecturer);
        }


        course.setName(dto.getName());
        course.setMaxStudent(dto.getMaxStudent());

        return courseMapper.toDto(course);
    }

    @Transactional
    public void deleteCourse(long id){
        CourseModel course = courseRepo.findById(id);

        if(course == null){
            throw new NotFoundException("Course not found");
        }

        courseRepo.delete(course);
    }
}
