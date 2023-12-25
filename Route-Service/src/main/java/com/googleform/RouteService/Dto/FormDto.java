package com.googleform.RouteService.Dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDto {
    private int id;
    private String title;
    private List<QuestionsDto> questionsList;
}
