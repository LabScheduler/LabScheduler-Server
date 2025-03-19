package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.AddMajorRequest;

import java.util.List;
import java.util.Map;

public interface MajorService {
    List<Major> getAllMajors();

    Major getMajorById(Long id);

    Major createNewMajor(AddMajorRequest request);

    void deleteMajorById(Long id);

    Major updateMajor(Long id, Map<String, Object> payload);
}
