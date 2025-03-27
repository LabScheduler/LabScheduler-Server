package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddRoomRequest;

import java.util.List;
import java.util.Map;

public interface RoomService {
    public List<Room> getAllRoom();
    public Room getRoomById(Long id);
    public Room addNewRoom(AddRoomRequest addRoomRequest);
    public void deleteRoomById(Long id);
    public Room updateRoomById(Long id, Map<String,Object> payload);
}
