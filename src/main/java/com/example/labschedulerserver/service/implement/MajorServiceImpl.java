package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.AddMajorRequest;
import com.example.labschedulerserver.repository.DepartmentRepository;
import com.example.labschedulerserver.repository.MajorRepository;
import com.example.labschedulerserver.service.MajorService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    @Override
    public Major getMajorById(Long id) {
        return majorRepository.findById(id).orElseThrow(()-> new RuntimeException("Major not found with id: " + id));
    }

    @Override
    public Major createNewMajor(AddMajorRequest request) {
        Major major = majorRepository.findMajorByName(request.getName());
        if(major != null){
            throw new RuntimeException("Major already exist");
        }
        return Major.builder()
                .name(request.getName())
                .code(request.getCode())
                .department(departmentRepository.findById(request.getDepartmentId()).orElseThrow(()-> new RuntimeException("Department not found")))
                .build();
    }

    @Override
    public void deleteMajorById(Long id) {
        if(!majorRepository.existsById(id)){
            throw new RuntimeException("Major not found with id: " + id);
        }
        majorRepository.deleteById(id);
    }

    @Override
    public Major updateMajor(Long id, Map<String, Object> payload) {
        Major major = majorRepository.findById(id).orElseThrow(()-> new RuntimeException("Major not found with id: " + id));

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                Field field = Major.class.getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
                field.setAccessible(true);
                field.set(major, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Field not found");
            }
        }
        majorRepository.save(major);
        return major;
    }
}
