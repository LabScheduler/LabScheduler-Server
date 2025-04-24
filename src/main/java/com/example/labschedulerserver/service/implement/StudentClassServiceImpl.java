package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.model.StudentAccount;
import com.example.labschedulerserver.payload.request.Class.AddClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.repository.ClassRepository;
import com.example.labschedulerserver.repository.MajorRepository;
import com.example.labschedulerserver.repository.StudentAccountRepository;
import com.example.labschedulerserver.service.StudentClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentClassServiceImpl implements StudentClassService {
    private final ClassRepository classRepository;
    private final MajorRepository majorRepository;
    private final StudentAccountRepository studentAccountRepository;


    @Override
    public List<Clazz> getAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public Clazz getClassById(Long id) {
        return classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
    }

    @Override
    public Clazz addNewClass(AddClassRequest addClassRequest) {
        // Tìm Major theo ID
        Major major = majorRepository.findById(addClassRequest.getMajorId())
                .orElseThrow(() -> new RuntimeException("Major not found"));

        Clazz clazz = Clazz.builder()
                .name(addClassRequest.getName())
                .major(major)
                .build();

        return classRepository.save(clazz);
    }

    @Override
    public void deleteClass(Long id) {
        Clazz clazz = classRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));
        classRepository.delete(clazz);
    }

    @Override
    public Clazz updateClass(Long id, UpdateClassRequest request) {
        Clazz clazz = classRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Major not found"));

        clazz.setMajor(major);
        clazz.setName(request.getName());
        return classRepository.save(clazz);
    }

    public Clazz getClassByName(String className) {
        return classRepository.findByName(className).orElseThrow(() -> new RuntimeException("Room not found with name: " + className));
    }

    @Override
    public List<Clazz> getAllClassesByMajorId(Long majorId) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Major not found"));
        return major.getClasses();
    }

    @Override
    @Transactional
    public void addClassToStudent(Long studentId, Long classId) {
        StudentAccount studentAccount = studentAccountRepository.findById(studentId).orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + studentId));

        Clazz clazz = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

        if (studentAccount.getClasses() == null) {
            studentAccount.setClasses(new ArrayList<>());
        }

        if(!studentAccount.getClasses().contains(clazz)){
            studentAccount.getClasses().add(clazz);
            studentAccountRepository.save(studentAccount);
        } else {
            throw new BadRequestException("Student already belongs to this class");
        }
    }

}
