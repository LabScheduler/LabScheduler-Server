package com.example.labschedulerserver.controller;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddRoomRequest;
import com.example.labschedulerserver.payload.response.DataResponse;
import com.example.labschedulerserver.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getAllRoom() {
        List<Room> rooms = roomService.getAllRoom();
        DataResponse<?> response = DataResponse.builder()
                .data(rooms)
                .success(true)
                .message("Get all rooms successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        DataResponse<Room> response = DataResponse.<Room>builder()
                .data(roomService.getRoomById(id))
                .success(true)
                .message("Get room by id successfully")
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/addNewRoom")
    public ResponseEntity<?> addNewRoom(@RequestBody AddRoomRequest addRoomRequest) {
        DataResponse<Room> response = DataResponse.<Room>builder()
                .data(roomService.addNewRoom(addRoomRequest))
                .success(true)
                .message("Add new room successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Integer id) {
        roomService.deleteRoomById(id);
        DataResponse<?> response = DataResponse.builder()
                .success(true)
                .message("Delete room successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateRoom/{id}")
    public ResponseEntity<?> updateRoomById(@PathVariable Integer id,@RequestBody Map<String, Object> payload) {
        DataResponse<?> response = DataResponse.builder()
                .success(true)
                .data(roomService.updateRoomById(id,payload))
                .message("Update room successfully")
                .build();
        return ResponseEntity.ok(response);
    }


}
