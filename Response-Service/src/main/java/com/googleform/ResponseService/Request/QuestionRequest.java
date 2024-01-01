package com.googleform.ResponseService.Request;

import lombok.Data;

@Data
public class QuestionRequest {
    private Long questionId;
    private String response;
}
