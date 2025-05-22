package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.ClassType;
import com.example.labschedulerserver.common.StudentOnClassStatus;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.*;
import com.example.labschedulerserver.payload.request.Class.CreateClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.payload.response.Class.ClassMapper;
import com.example.labschedulerserver.payload.response.Class.ClassResponse;
import com.example.labschedulerserver.payload.response.User.StudentResponse;
import com.example.labschedulerserver.payload.response.User.UserMapper;
import com.example.labschedulerserver.repository.*;
import com.example.labschedulerserver.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {
    private final MajorRepository majorRepository;
    private final ClassRepository classRepository;
    private final SpecializationRepository specializationRepository;
    private final StudentOnClassRepository studentOnClassRepository;
    private final StudentAccountRepository studentAccountRepository;

    @Override
    public ClassResponse createClass(CreateClassRequest request) {
        Clazz clazz = Clazz.builder()
                .name(request.getName())
                .major(majorRepository.findById(request.getMajorId()).orElseThrow(() -> new ResourceNotFoundException("Major not found")))
                .classType(ClassType.valueOf(request.getClassType().toUpperCase()))
                .specialization(request.getSpecializationId() != null ? specializationRepository.findById(request.getSpecializationId()).orElseThrow(() -> new ResourceNotFoundException("Specialization not found")) : null)
                .build();
        return ClassMapper.toClassResponse(classRepository.save(clazz));
    }

    @Override
    public List<ClassResponse> getAllClasses(String classType) {
        if (classType == null || classType.isEmpty()) {
            return classRepository.findAll().stream().map(ClassMapper::toClassResponse).toList();
        }
        return classRepository.findAll().stream().filter(clazz -> clazz.getClassType() == ClassType.valueOf(classType.toUpperCase())).map(ClassMapper::toClassResponse).toList();
    }


    @Override
    public ClassResponse getClassById(Long id) {
        return ClassMapper.toClassResponse(classRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class not found")));
    }

    @Override
    public List<StudentResponse> getStudentsInClass(Long classId) {
        Clazz clazz = classRepository.findById(classId).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        if (clazz.getStudentOnClasses() != null) {
            return clazz.getStudentOnClasses().stream()
                    .map(studentOnClass -> {
                        StudentAccount student = studentOnClass.getStudents();
                        return (StudentResponse) UserMapper.mapUserToResponse(student.getAccount(), student);
                    })
                    .toList();
        }
        throw new ResourceNotFoundException("Students not found in class with id: " + classId);
    }

    @Override
    public ClassResponse updateClass(Long id, UpdateClassRequest request) {
        Clazz clazz = classRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        clazz.setName(request.getName());

        if (clazz.getClassType() == ClassType.MAJOR) {
            clazz.setMajor(majorRepository.findById(request.getMajorId()).orElseThrow(() -> new ResourceNotFoundException("Major not found")));
        } else if (clazz.getClassType() == ClassType.SPECIALIZATION) {
            clazz.setMajor(majorRepository.findById(request.getMajorId()).orElseThrow(() -> new ResourceNotFoundException("Major not found")));

            Specialization specialization = specializationRepository.findById(request.getSpecializationId()).orElseThrow(() -> new ResourceNotFoundException("Specialization not found"));
            if (specialization.getMajor() == clazz.getMajor()) {
                clazz.setSpecialization(specialization);
            } else {
                throw new ResourceNotFoundException("Specialization not found in this major");
            }
        }

        return ClassMapper.toClassResponse(classRepository.save(clazz));
    }

    @Override
    public void deleteClass(Long id) {
        Clazz clazz = classRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        classRepository.delete(clazz);
    }

    @Override
    public void deleteStudentFromClass(Long classId, Long studentId) {
        Clazz clazz = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        List<StudentOnClass> studentOnClass = clazz.getStudentOnClasses().stream()
                .filter(studentOnClass1 -> studentOnClass1.getStudents().getAccountId().equals(studentId))
                .toList();


        studentOnClassRepository.deleteAll(studentOnClass);
    }

    @Override
    public void addStudentToClass(Long classId, List<Long> studentIds) {
        Clazz clazz = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        List<StudentOnClass> studentOnClasses = studentIds.stream()
                .map(studentId -> {
                    StudentAccount student = studentAccountRepository.findById(studentId)
                            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                    return StudentOnClass.builder()
                            .id(new StudentOnClassId(studentId, classId))
                            .students(student)
                            .clazz(clazz)
                            .status(StudentOnClassStatus.ENROLLED)
                            .build();
                })
                .toList();

        studentOnClassRepository.saveAll(studentOnClasses);
    }

}
