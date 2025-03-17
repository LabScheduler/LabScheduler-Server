package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.model.Subject;
import com.example.labschedulerserver.payload.request.AddSubjectRequest;
import com.example.labschedulerserver.repository.SubjectRepository;
import com.example.labschedulerserver.service.SubjectService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    @Override
    public List<Subject> getAllSubjects(){
        return subjectRepository.findAll();
    };
    @Override
    public Subject getSubjectById(int id){
        return subjectRepository.findById(id).orElseThrow(()->new RuntimeException("Subject not found"));
    };
    @Override
    public Subject addNewSubject(AddSubjectRequest request){
        Subject subject = Subject.builder()
                .code(request.getCode())
                .name(request.getName())
                .totalCredits(request.getTotalCredits())
                .totalTheoryPeriods(request.getTotalTheoryPeriods())
                .totalPracticePeriods(request.getTotalPracticePeriods())
                .build();
        subjectRepository.save(subject);
        return subject;
    };
    @Override
    public void deleteSubject(int id){
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new RuntimeException("Subject not found"));
        subjectRepository.delete(subject);
    };
    @Override
    public Subject updateSubject(int id, Map<String,Object> mp){
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new RuntimeException("Subject not found"));

        for (Map.Entry<String, Object> entry : mp.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                Field field = Room.class.getDeclaredField(ConvertFromJsonToTypeVariable.convert(ConvertFromJsonToTypeVariable.convert(key)));
                field.setAccessible(true);
                field.set(subject, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Field not found");
            }
        }
        subjectRepository.save(subject);
        return subject;
    };
    @Override
    public Subject getSubjectByName(String name){
        return subjectRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Subject not found with name: " + name));
    }

}
