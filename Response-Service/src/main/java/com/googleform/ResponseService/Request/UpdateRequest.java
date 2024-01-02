package com.googleform.ResponseService.Request;

import lombok.Data;

@Data
public class UpdateRequest {
    private Long responseId;
    private String updatedResponse;
}
