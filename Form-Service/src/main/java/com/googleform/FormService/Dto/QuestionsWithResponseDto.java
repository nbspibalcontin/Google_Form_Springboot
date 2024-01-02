package com.googleform.FormService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsWithResponseDto {
    private Long id;
    private String question;
    private List<ResponseDto> responses;
}
