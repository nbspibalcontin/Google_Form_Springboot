package com.googleform.ResponseService.Controller;

import com.googleform.ResponseService.Dto.MessageResponse;
import com.googleform.ResponseService.Exception.FormNotFoundException;
import com.googleform.ResponseService.Exception.RespondentAlreadyExistsException;
import com.googleform.ResponseService.Request.ResponseRequest;
import com.googleform.ResponseService.Service.Respondent_Service;
import com.googleform.ResponseService.Service.Response_Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/response")
public class ResponseController {
    private final Response_Service responseService;
    private final Respondent_Service respondentService;

    public ResponseController(Response_Service responseService, Respondent_Service respondentService) {
        this.responseService = responseService;
        this.respondentService = respondentService;
    }

    //Create Response for the questions
    @PostMapping("/question-response")
    public ResponseEntity<?> createResponse(@RequestBody ResponseRequest responseDto) {
        try {
            return ResponseEntity.ok(responseService.createResponses(responseDto));
        } catch (RespondentAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException | FormNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{respondentId}")
    public ResponseEntity<?> deleteResponse(@PathVariable Long respondentId) {
        try {
            respondentService.deleteRespondentsById(respondentId);
            return ResponseEntity.ok(new MessageResponse("Respondent Successfully Deleted!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
