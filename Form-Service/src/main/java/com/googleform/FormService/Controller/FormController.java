package com.googleform.FormService.Controller;

import com.googleform.FormService.Exception.QuestionNotFoundException;
import com.googleform.FormService.Request.FormRequest;
import com.googleform.FormService.Dto.MessageResponse;
import com.googleform.FormService.Exception.CodeNotFoundException;
import com.googleform.FormService.Exception.FormNotFoundException;
import com.googleform.FormService.Service.Form_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/form")
@CrossOrigin(origins="*")
public class FormController {
    private final Form_Service form_service;

    public FormController(Form_Service createForm) {
        this.form_service = createForm;
    }

    //Create Form
    @PostMapping("/create")
    public ResponseEntity<?> createForm(@RequestBody FormRequest formDto) {
        try {
            MessageResponse response = form_service.create_Form(formDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Get Form by Code
    @GetMapping("/code/{code}")
    public ResponseEntity<?> findFromByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(form_service.getFormByCode(code));
        } catch (CodeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Find Form with questions by Id
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> findFromById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(form_service.getFormByIdWithQuestions(id));
        } catch (FormNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Find All Form
    @GetMapping("/allForms")
    public ResponseEntity<?> findAllForms(){
        try {
            return ResponseEntity.ok(form_service.findAllForms());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Find All Form With Response
    @GetMapping("/allForms/response")
    public ResponseEntity<?> findAllFormsWithResponse(){
        try {
            return ResponseEntity.ok(form_service.FormWithQuestionsAndResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    //Find Form with questions and Response by Id
    @GetMapping("/questionAndResponse/{id}")
    public ResponseEntity<?> findFromWithQuestionAndResponseById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(form_service.getFormByIdWithQuestionsAndResponse(id));
        } catch (FormNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Delete Form By Id
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable Long id) {
        try {
            form_service.deleteForm(id);
            return ResponseEntity.ok(new MessageResponse("Delete Successfully!"));
        } catch (FormNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    //Update Form title and questions
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateFormAndQuestions(@PathVariable Long id,@RequestBody FormRequest formRequest) {
        try {
            form_service.updateForm(id, formRequest);
            return ResponseEntity.ok(new MessageResponse("Update Successfully!"));
        } catch (QuestionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
