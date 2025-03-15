package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.AddMajorRequest;

import java.util.List;
import java.util.Map;

public interface MajorService {
    List<Major> getAllMajors();

    Major getMajorById(Integer id);

    Major createNewMajor(AddMajorRequest request);

    void deleteMajorById(Integer id);

    Major updateMajor(Integer id, Map<String, Object> payload);
}
