package com.googleform.ResponseService.Service;

import com.googleform.ResponseService.Dto.MessageResponse;
import com.googleform.ResponseService.Entity.Form;
import com.googleform.ResponseService.Entity.Questions;
import com.googleform.ResponseService.Entity.Respondents;
import com.googleform.ResponseService.Entity.Response;
import com.googleform.ResponseService.Exception.FormNotFoundException;
import com.googleform.ResponseService.Exception.RespondentAlreadyExistsException;
import com.googleform.ResponseService.Exception.ResponseNotFoundException;
import com.googleform.ResponseService.Repository.FormRepository;
import com.googleform.ResponseService.Repository.QuestionsRepository;
import com.googleform.ResponseService.Repository.RespondentsRepository;
import com.googleform.ResponseService.Repository.ResponseRepository;
import com.googleform.ResponseService.Request.QuestionRequest;
import com.googleform.ResponseService.Request.ResponseRequest;
import com.googleform.ResponseService.Request.UpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
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

    //Create Response
    public MessageResponse createResponses(ResponseRequest responseDto) {
        Form existingForm = getExistingForm(responseDto.getFormId());
        Respondents respondents = null;

        for (QuestionRequest questionRequest : responseDto.getResponses()) {
            Questions existingQuestion = getExistingQuestion(questionRequest.getQuestionId());

            if (respondents == null) {
                respondents = saveRespondents(responseDto.getEmail(), existingForm);
            }

            saveResponse(existingQuestion, questionRequest.getResponse(), respondents);
        }

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

    // Helper method to save Respondents information
    private Respondents saveRespondents(String email, Form existingForm) {
        // Check if a Respondents with the same email already exists
        Optional<Respondents> existingRespondent = respondentsRepository.findByEmail(email);

        if (existingRespondent.isPresent()) {
            throw new RespondentAlreadyExistsException("Respondent with email " + email + " already exists!");
        }

        Respondents respondents = new Respondents();
        respondents.setEmail(email);
        respondents.setForm(existingForm);

        return respondentsRepository.save(respondents);
    }

    // Helper method to save a new Response for a given Question
    private void saveResponse(Questions existingQuestion, String response, Respondents respondents) {
        Response newResponse = new Response();
        newResponse.setResponse(response);
        newResponse.setQuestions(existingQuestion);
        newResponse.setRespondents(respondents);

        responseRepository.save(newResponse);
    }

    //TODO Create update function for response
    public void updateResponsesByRespondentId(Long respondentId, List<UpdateRequest> updateResponseDTOs) {
        List<Response> responses = responseRepository.findByRespondentsId(respondentId);

        if (!responses.isEmpty()) {
            for (UpdateRequest updateResponseDTO : updateResponseDTOs) {
                Long responseId = updateResponseDTO.getResponseId();
                String updatedResponse = updateResponseDTO.getUpdatedResponse();

                Response response = responses.stream()
                        .filter(r -> r.getId().equals(responseId))
                        .findFirst()
                        .orElseThrow(() -> new ResponseNotFoundException("Response with ID " + responseId + " not found"));

                response.setResponse(updatedResponse);
            }

            responseRepository.saveAll(responses);
        } else {
            // Handle the case where no responses are found for the given respondentId
            throw new ResponseNotFoundException("Responses for Respondent ID " + respondentId + " not found");
        }
    }
}