package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddRoomRequest;

import java.util.List;
import java.util.Map;

public interface RoomService {
    public List<Room> getAllRoom();
    public Room getRoomById(Integer id);
    public Room addNewRoom(AddRoomRequest addRoomRequest);
    public void deleteRoomById(Integer id);
    public Room updateRoomById(Integer id, Map<String,Object> payload);
}
