package com.example.labschedulerserver.service;

import com.example.labschedulerserver.common.RequestStatus;
import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.model.Schedule;

import java.util.List;

public interface LecturerRequestService {
    //Create a schedule request
    LecturerRequest createScheduleRequest(LecturerRequest request);
    
    //Get all pending manager schedule requests
    List<LecturerRequest> getAllPendingRequests();
    
    //Get all manager request
    List<LecturerRequest> getAllRequests();
    
    //Get schedule requests by lecturer ID
    List<LecturerRequest> getRequestsByLecturerId(Long lecturerId);
    
    //Get schedule request by ID
    LecturerRequest getRequestById(Long requestId);
    
    //Process a schedule request (approve or reject)
    LecturerRequestLog processRequest(Long requestId, Long managerId, RequestStatus status);
    
    //Get request logs by request ID
    LecturerRequestLog getRequestLogByRequestId(Long requestId);
    
    //Cancel a pending request
    void cancelRequest(Long requestId);
    
    //Schedule operations that result from approved requests
    Schedule addScheduleFromRequest(LecturerRequest request);
    Schedule rescheduleFromRequest(LecturerRequest request);
}
