package com.googleform.ResponseService.Controller;

import com.googleform.ResponseService.Dto.ResponseDto;
import com.googleform.ResponseService.Service.Response_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/response")
public class ResponseController {
    private final Response_Service responseService;

    public ResponseController(Response_Service responseService) {
        this.responseService = responseService;
    }

    @PostMapping("/question-response")
    public ResponseEntity<?> createResponse(@RequestBody ResponseDto responseDto){
        try {
            return ResponseEntity.ok(responseService.createResponse(responseDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
