package com.googleform.FormService.Controller;

import com.googleform.FormService.Dto.FormDto;
import com.googleform.FormService.Dto.MessageResponse;
import com.googleform.FormService.Exception.CodeNotFoundException;
import com.googleform.FormService.Exception.FormNotFoundException;
import com.googleform.FormService.Service.Create_Form;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@RequestMapping("api/form")
@CrossOrigin(origins="*")
public class FormController {
    private final Create_Form form_service;

    public FormController(Create_Form createForm) {
        this.form_service = createForm;
    }

    //Create Form
    @PostMapping("/create")
    public ResponseEntity<?> createForm(@RequestBody FormDto formDto) {
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

    //Delete Form By Id
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable Long id) {
        try {
            form_service.deleteForm(id);
            return ResponseEntity.ok("Delete Successfully.");
        } catch (FormNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
