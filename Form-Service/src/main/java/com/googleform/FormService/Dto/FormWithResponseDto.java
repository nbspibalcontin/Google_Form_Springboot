package com.googleform.FormService.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FormWithResponseDto {
    private Long id;
    private String title;
    private String code;
    private List<QuestionsWithResponseDto> questionsList;
}
