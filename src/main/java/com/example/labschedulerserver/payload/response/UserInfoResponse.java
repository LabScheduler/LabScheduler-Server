package com.example.labschedulerserver.payload.response;

import com.example.labschedulerserver.common.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private Integer id;
    private String email;
    private String role;
    private AccountStatus status;
    private Object accountInfo;
}
