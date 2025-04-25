package com.example.labschedulerserver.payload.response.Class;

import com.example.labschedulerserver.model.Clazz;

public class ClassMapper {
    public static ClassResponse toClassResponse(Clazz clazz) {
        return ClassResponse.builder()
                .id(clazz.getId())
                .name(clazz.getName())
                .major(clazz.getMajor().getName())
                .type(clazz.getClassType().toString())
                .specialization(clazz.getSpecialization() != null ? clazz.getSpecialization().getName() : "")
                .numberOfStudents(clazz.getStudentOnClasses() != null ? clazz.getStudentOnClasses().size() : 0)
                .build();
    }
}
