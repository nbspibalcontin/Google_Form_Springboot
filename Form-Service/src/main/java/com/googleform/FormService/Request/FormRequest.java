package com.googleform.FormService.Request;

import lombok.Data;

import java.util.List;

@Data
public class FormRequest {
    private Long id;
    private String title;
    private List<QuestionsRequest> questionsList;
}
