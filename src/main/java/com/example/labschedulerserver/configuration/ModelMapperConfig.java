package com.example.labschedulerserver.configuration;

import com.example.labschedulerserver.model.Course;
import com.example.labschedulerserver.payload.response.CourseResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        modelMapper.typeMap(Course.class, CourseResponse.class)
                .addMapping(course ->course.getSubject().getName(), CourseResponse::setSubject)
                .addMapping(course -> course.getClazz().getName(), CourseResponse::setClazz)
                .addMapping(course -> course.getSemester().getName(), CourseResponse::setSemester)
                .addMapping(course -> course.getLecturerAccount().getFullName(), CourseResponse::setLecturer)
                .addMapping(course -> course.getId(), CourseResponse::setId);


        return modelMapper;
    }
}
