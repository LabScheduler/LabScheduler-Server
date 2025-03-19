package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Major;
import com.example.labschedulerserver.payload.request.AddMajorRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/major")
@RequiredArgsConstructor
public class MajorController {
    private final MajorService majorService;

    @GetMapping
    public ResponseEntity<?> getAllMajors(){
        List<Major> majors = majorService.getAllMajors();
        DataResponse response = DataResponse.builder()
                .data(majors)
                .success(true)
                .message("Get all majors successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateMajor(@PathVariable Long id,@RequestBody Map<String,Object> payload){
        Major major = majorService.updateMajor(id,payload);
        DataResponse response = DataResponse.builder()
                .data(major)
                .success(true)
                .message("Update major successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMajor(@PathVariable Long id){
        majorService.deleteMajorById(id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete major successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewMajor(@RequestBody AddMajorRequest request){
        Major newMajor = majorService.createNewMajor(request);
        DataResponse response = DataResponse.builder()
                .data(newMajor)
                .success(true)
                .message("Create new major successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
