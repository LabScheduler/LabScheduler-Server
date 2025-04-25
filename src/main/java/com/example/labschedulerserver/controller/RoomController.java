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
        try{
            List<Room> rooms = roomService.getAllRoom();
            DataResponse response = DataResponse.builder()
                    .data(rooms)
                    .success(true)
                    .message("Get all rooms successfully")
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            DataResponse response = DataResponse.builder()
                    .data(null)
                    .success(false)
                    .message("Get all rooms failed")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try{
            DataResponse response = DataResponse.<Room>builder()
                    .data(roomService.getRoomById(id))
                    .success(true)
                    .message("Get room by id successfully")
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            DataResponse response = DataResponse.builder()
                    .data(null)
                    .success(false)
                    .message("Get room by id failed")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody AddRoomRequest addRoomRequest) {
        try{
            DataResponse response = DataResponse.<Room>builder()
                    .data(roomService.createRoom(addRoomRequest))
                    .success(true)
                    .message("Add new room successfully")
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            DataResponse response = DataResponse.builder()
                    .data(null)
                    .success(false)
                    .message("Add new room failed")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long id) {
        try{
            roomService.deleteRoomById(id);
            DataResponse response = DataResponse.builder()
                    .success(true)
                    .message("Delete room successfully")
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Delete room failed")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateRoomById(@PathVariable Long id,@RequestBody Map<String, Object> payload) {
        try{
            DataResponse response = DataResponse.builder()
                    .success(true)
                    .data(roomService.updateRoomById(id,payload))
                    .message("Update room successfully")
                    .build();
            return ResponseEntity.ok(response);
        }catch (Exception e){
            DataResponse response = DataResponse.builder()
                    .success(false)
                    .message("Update room failed")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }




}
