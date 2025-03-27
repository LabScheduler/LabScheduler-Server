package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.Class.AddClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.repository.ClassRepository;
import com.example.labschedulerserver.repository.MajorRepository;
import com.example.labschedulerserver.service.ClassService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final MajorRepository majorRepository;

    @Override
    public List<Clazz> getAllClasses(){
        return classRepository.findAll();
    }

    @Override
    public Clazz getClassById(Long id) {
        return classRepository.findById(id).orElseThrow(()->new RuntimeException("Class not found"));
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
        Clazz clazz = classRepository.findById(id).orElseThrow(()->new RuntimeException("Class not found"));
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

    public Clazz getClassByName(String className){
        return classRepository.findByName(className).orElseThrow(() -> new RuntimeException("Room not found with name: " + className));
    };
}
