package com.example.labschedulerserver.repository;

import com.example.labschedulerserver.common.RoomStatus;
import com.example.labschedulerserver.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByName(String name);

    Optional<Room> findAllByStatus(RoomStatus status);
}
