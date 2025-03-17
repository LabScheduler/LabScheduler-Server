package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddClassRequest;
import com.example.labschedulerserver.payload.request.AddRoomRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")

public class ClassController {
    private final ClassService classService;
    @GetMapping
    public ResponseEntity<DataResponse> getClasses() {
        List<Clazz> classes = classService.getAllClasses();
        DataResponse response = DataResponse.builder()
                .data(classes)
                .success(true)
                .message("Get all classes successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(classService.getClassById(id))
                .success(true)
                .message("Get class by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> createNewClass(@RequestBody AddClassRequest addClassRequest) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(classService.addNewClass(addClassRequest))
                .success(true)
                .message("Add new class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Integer id) {
        classService.deleteClass(id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateRoomById(@PathVariable Integer id,@RequestBody Map<String, Object> mp) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(classService.updateClass(id,mp))
                .message("Update class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> findClassByName(@PathVariable String name) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(classService.getClassByName(name))
                .success(true)
                .message("Get class by name successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
