package com.googleform.FormService.Request;

import lombok.Data;

import java.util.List;

@Data
public class FormRequest {
    private String title;
    private List<QuestionsRequest> questionsList;
}
