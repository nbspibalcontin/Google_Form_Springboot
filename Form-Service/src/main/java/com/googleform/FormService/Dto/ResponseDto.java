package com.googleform.FormService.Dto;

import lombok.Data;

@Data
public class ResponseDto {
    private Long id;
    private String response;
    private Long respondentId;
    private String respondentEmail;
}
