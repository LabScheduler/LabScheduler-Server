package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddClassRequest;
import com.example.labschedulerserver.repository.ClassRepository;
import com.example.labschedulerserver.repository.MajorRepository;
import com.example.labschedulerserver.service.ClassService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

        // Tạo đối tượng Clazz và gán Major
        Clazz clazz = Clazz.builder()
                .name(addClassRequest.getName())
                .major(major) // Gán đối tượng Major thay vì Integer
                .build();

        // Lưu vào database
        return classRepository.save(clazz);
    }

    @Override
    public void deleteClass(Long id) {
        Clazz clazz = classRepository.findById(id).orElseThrow(()->new RuntimeException("Class not found"));
        classRepository.delete(clazz);
    }

    @Override
    public Clazz updateClass(Long id, Map<String,Object> mp){
        Clazz clazz = classRepository.findById(id).orElseThrow(()->new RuntimeException("Class not found"));

        for (Map.Entry<String, Object> entry : mp.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                Field field = Clazz.class.getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
                field.setAccessible(true);
                field.set(clazz, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Field not found");
            }
        }
        classRepository.save(clazz);
        return clazz;
    };

    public Clazz getClassByName(String className){
        return classRepository.findByName(className).orElseThrow(() -> new RuntimeException("Room not found with name: " + className));
    };
}
