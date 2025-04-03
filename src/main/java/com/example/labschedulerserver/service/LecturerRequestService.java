package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.LecturerRequestLog;
import com.example.labschedulerserver.payload.request.LecturerScheduleRequest;
import com.example.labschedulerserver.payload.request.ProcessRequest;
import com.example.labschedulerserver.payload.response.LecturerRequest.LecturerRequestResponse;

import java.util.List;

public interface LecturerRequestService {
    //lecturer create new schedule request
    LecturerRequestResponse createScheduleRequest(LecturerScheduleRequest request);
    
    //manager will get all pending requests
    List<LecturerRequestResponse> getAllPendingRequests();
    
    //manager will get all requests
    List<LecturerRequestResponse> getAllRequests();
    
    //get requests from lecturer
    List<LecturerRequestResponse> getRequestsByLecturerId(Long lecturerId);
    
    //get request by id
    LecturerRequestResponse getRequestById(Long requestId);

    //manager will process the request (approve/reject)
    LecturerRequestResponse processRequest(ProcessRequest request);
    
    //lecturer can cancel their request
    void cancelRequest(Long requestId);
}
