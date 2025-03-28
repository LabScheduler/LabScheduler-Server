package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.AddMajorRequest;

import java.util.List;
import java.util.Map;

public interface MajorService {
    List<Major> getAllMajors();

    Major getMajorById(Long id);

    Major createMajor(AddMajorRequest request);

    void deleteMajor(Long id);

    Major updateMajor(Long id, Map<String, Object> payload);
}
