package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.LecturerRequest;
import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;

import java.util.List;

public interface LecturerRequestService {
    //Create a schedule request
    LecturerRequest createScheduleRequest(LecturerScheduleRequest request);
    
    //Get all pending manager schedule requests
    List<LecturerRequest> getAllPendingRequests();
    
    //Get all manager request
    List<LecturerRequest> getAllRequests();
    
    //Get schedule requests by lecturer ID
    List<LecturerRequest> getRequestsByLecturerId(Long lecturerId);
    
    //Get schedule request by ID
    LecturerRequest getRequestById(Long requestId);
    
    //Get request logs by request ID
    LecturerRequestLog getRequestLog(Long requestId);

    //Process a schedule request (approve or reject)
    LecturerRequestLog processRequest(ProcessRequest request);
    
    //Cancel a pending request
    void cancelRequest(Long requestId);
}
