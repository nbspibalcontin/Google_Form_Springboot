package com.googleform.ResponseService.Service;

import com.googleform.ResponseService.Dto.MessageResponse;
import com.googleform.ResponseService.Entity.Form;
import com.googleform.ResponseService.Entity.Questions;
import com.googleform.ResponseService.Entity.Respondents;
import com.googleform.ResponseService.Entity.Response;
import com.googleform.ResponseService.Exception.FormNotFoundException;
import com.googleform.ResponseService.Repository.FormRepository;
import com.googleform.ResponseService.Repository.QuestionsRepository;
import com.googleform.ResponseService.Repository.RespondentsRepository;
import com.googleform.ResponseService.Repository.ResponseRepository;
import com.googleform.ResponseService.Request.QuestionRequest;
import com.googleform.ResponseService.Request.ResponseRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class Response_Service {
    private final QuestionsRepository questionsRepository;
    private final ResponseRepository responseRepository;
    private final RespondentsRepository respondentsRepository;
    private final FormRepository formRepository;

    public Response_Service(QuestionsRepository questionsRepository, ResponseRepository responseRepository, RespondentsRepository respondentsRepository, FormRepository formRepository) {
        this.questionsRepository = questionsRepository;
        this.responseRepository = responseRepository;
        this.respondentsRepository = respondentsRepository;
        this.formRepository = formRepository;
    }


    public MessageResponse createResponses(ResponseRequest responseDto) {
        // Retrieve the existing Form based on the provided form ID
        Form existingForm = getExistingForm(responseDto.getFormId());

        // Iterate through each question response in the request
        for (QuestionRequest questionRequest : responseDto.getResponses()) {
            // Retrieve the existing Question based on the provided question ID
            Questions existingQuestion = getExistingQuestion(questionRequest.getId());

            // Check if a Response with the same ID already exists
            if (responseRepository.existsById(questionRequest.getId())) {
                // Handle the case where a response with the same ID already exists
                return new MessageResponse("Response with ID " + questionRequest.getId() + " already exists!");
            }

            // Save the new Response for the current question
            saveResponse(existingQuestion, questionRequest.getResponse());
        }

        // Save the Respondents information
        saveRespondents(responseDto.getEmail(), existingForm);

        // Return success message
        return new MessageResponse("Responses saved successfully!");
    }

    // Helper method to retrieve an existing Form based on ID
    private Form getExistingForm(Long formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new FormNotFoundException("Form not found with ID: " + formId));
    }

    // Helper method to retrieve an existing Question based on ID
    private Questions getExistingQuestion(Long questionId) {
        return questionsRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + questionId));
    }

    // Helper method to save a new Response for a given Question
    private void saveResponse(Questions existingQuestion, String response) {
        Response newResponse = new Response();
        newResponse.setResponse(response);
        newResponse.setQuestions(existingQuestion);

        responseRepository.save(newResponse);
    }

    // Helper method to save Respondents information
    private void saveRespondents(String email, Form existingForm) {
        Respondents respondents = new Respondents();
        respondents.setEmail(email);
        respondents.setForm(existingForm);

        respondentsRepository.save(respondents);
    }


}