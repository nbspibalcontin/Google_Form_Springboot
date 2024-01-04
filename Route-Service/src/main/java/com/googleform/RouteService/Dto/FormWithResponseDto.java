package com.googleform.RouteService.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FormWithResponseDto {
    private Long id;
    private String title;
    private String code;
    private Long totalRespondent;
    private List<QuestionsWithResponseDto> questionsList;
}
