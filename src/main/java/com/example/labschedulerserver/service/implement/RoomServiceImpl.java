package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.model.Room;
import com.example.labschedulerserver.payload.request.AddRoomRequest;
import com.example.labschedulerserver.repository.RoomRepository;
import com.example.labschedulerserver.service.RoomService;
import com.example.labschedulerserver.ultils.ConvertFromJsonToTypeVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    public Room addNewRoom(AddRoomRequest addRoomRequest) {
        Room room = Room.builder()
                .name(addRoomRequest.getName())
                .capacity(addRoomRequest.getCapacity())
                .description(addRoomRequest.getDescription())
                .status(RoomStatus.AVAILABLE)
                .build();
        roomRepository.save(room);
        return room;
    }

    @Override
    public void deleteRoomById(Long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        roomRepository.delete(room);
    }

    @Override
    public Room updateRoomById(Long id, Map<String, Object> payload) {
        Room tmpRoom = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = Room.class.getDeclaredField(ConvertFromJsonToTypeVariable.convert(key));
                field.setAccessible(true);
                if (field.getType().isEnum()) {
                    Object enumValue = Enum.valueOf((Class<Enum>) field.getType(), value.toString());
                    field.set(tmpRoom, enumValue);
                } else {
                    field.set(tmpRoom, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Field not found");
            }
        }
        roomRepository.save(tmpRoom);
        return tmpRoom;
    }

    @Override
    public Room findByName(String name) {
        return roomRepository.findByName(name).orElseThrow(() -> new RuntimeException("Room not found with name: " + name));
    }
}
