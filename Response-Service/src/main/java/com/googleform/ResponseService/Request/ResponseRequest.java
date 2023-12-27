package com.googleform.ResponseService.Request;

import com.googleform.ResponseService.Request.QuestionRequest;
import lombok.Data;

import java.util.List;

@Data
public class ResponseRequest {
    private Long formId;
    private String email;
    private List<QuestionRequest> responses;
}
