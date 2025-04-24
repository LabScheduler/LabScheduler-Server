package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Clazz;
import com.example.labschedulerserver.payload.request.Class.AddClassRequest;
import com.example.labschedulerserver.payload.request.Class.UpdateClassRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.StudentClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/class")

public class ClassController {
    private final StudentClassService studentClassService;
    @GetMapping
    public ResponseEntity<DataResponse> getClasses() {
        List<Clazz> classes = studentClassService.getAllClasses();
        DataResponse response = DataResponse.builder()
                .data(classes)
                .success(true)
                .message("Get all classes successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(studentClassService.getClassById(id))
                .success(true)
                .message("Get class by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createNewClass(@RequestBody AddClassRequest addClassRequest) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(studentClassService.addNewClass(addClassRequest))
                .success(true)
                .message("Add new class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        studentClassService.deleteClass(id);
        DataResponse response = DataResponse.builder()
                .success(true)
                .message("Delete class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateRoomById(@PathVariable Long id,@RequestBody UpdateClassRequest updateClassRequest) {
        DataResponse response = DataResponse.builder()
                .success(true)
                .data(studentClassService.updateClass(id,updateClassRequest))
                .message("Update class successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> findClassByName(@PathVariable String name) {
        DataResponse response = DataResponse.<Clazz>builder()
                .data(studentClassService.getClassByName(name))
                .success(true)
                .message("Get class by name successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter/{majorId}")
    public ResponseEntity<?> filterClassByMajor(@PathVariable Long majorId) {
        DataResponse response = DataResponse.<List<Clazz>>builder()
                .data(studentClassService.getAllClassesByMajorId(majorId))
                .success(true)
                .message("Get class by major successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
