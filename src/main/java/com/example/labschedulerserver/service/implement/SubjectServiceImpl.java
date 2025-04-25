package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.exception.BadRequestException;
import com.example.labschedulerserver.exception.FieldNotFoundException;
import com.example.labschedulerserver.exception.ResourceNotFoundException;
import com.example.labschedulerserver.model.Subject;
import com.example.labschedulerserver.payload.request.AddSubjectRequest;
import com.example.labschedulerserver.repository.SubjectRepository;
import com.example.labschedulerserver.service.SubjectService;
import com.example.labschedulerserver.utils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
    public Subject getSubjectById(Long id){
        return subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject not found with id: "+ id));
    }

    @Override
    public Subject createSubject(AddSubjectRequest request){
        if(subjectRepository.findByCode(request.getCode()).isPresent()){
            throw new RuntimeException("Subject already exists with code: "+ request.getCode());
        }
        Subject subject = Subject.builder()
                .code(request.getCode())
                .name(request.getName())
                .totalCredits(request.getTotalCredits())
                .totalTheoryPeriods(request.getTotalTheoryPeriods())
                .totalPracticePeriods(request.getTotalPracticePeriods())
                .totalExercisePeriods(request.getTotalExercisePeriods())
                .totalSelfStudyPeriods(request.getTotalSelfStudyPeriods())
                .build();
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Long id){
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject not found"));
        try{
            subjectRepository.delete(subject);
        }catch (Exception e){
            throw new BadRequestException("Can not delete subject");
        }
    }
    @Override
    public Subject updateSubject(Long id, Map<String,Object> mp){
        Subject subject = subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject not found"));

        for (Map.Entry<String, Object> entry : mp.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = Subject.class.getDeclaredField(ConvertFromJsonToTypeVariable.convert(ConvertFromJsonToTypeVariable.convert(key)));
                field.setAccessible(true);
                field.set(subject, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new FieldNotFoundException("Field not found");
            }
        }
        subjectRepository.save(subject);
        return subject;
    };
    @Override
    public Subject getSubjectByName(String name){
        return subjectRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with name: " + name));
    }

}
