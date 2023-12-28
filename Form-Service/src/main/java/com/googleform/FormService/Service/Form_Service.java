package com.googleform.FormService.Service;

import com.googleform.FormService.Dto.*;
import com.googleform.FormService.Exception.QuestionNotFoundException;
import com.googleform.FormService.Request.FormRequest;
import com.googleform.FormService.Entity.Form;
import com.googleform.FormService.Entity.Questions;
import com.googleform.FormService.Exception.CodeNotFoundException;
import com.googleform.FormService.Exception.FormCreationException;
import com.googleform.FormService.Exception.FormNotFoundException;
import com.googleform.FormService.Repository.FormRepository;
import com.googleform.FormService.Repository.QuestionsRepository;
import com.googleform.FormService.Request.QuestionsRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Form_Service {

    private final FormRepository formRepository;
    private final QuestionsRepository questionsRepository;

    private final ModelMapper modelMapper;

    public Form_Service(FormRepository formRepository, QuestionsRepository questionsRepository, ModelMapper modelMapper) {
        this.formRepository = formRepository;
        this.questionsRepository = questionsRepository;
        this.modelMapper = modelMapper;
    }

    //Create Form
    public MessageResponse create_Form(FormRequest formDto) {
        try {
            String randomCode = generateRandomCode();

            Form form = new Form();
            form.setTitle(formDto.getTitle());
            form.setCode(randomCode);

            // Create Questions entities
            List<Questions> questionsList = formDto.getQuestionsList().stream().map(questionRequest -> {
                Questions questions = new Questions();
                BeanUtils.copyProperties(questionRequest, questions);
                questions.setForm(form);
                return questions;
            }).collect(Collectors.toList());

            form.setQuestionsList(questionsList);

            formRepository.save(form);
            questionsRepository.saveAll(questionsList);


            return new MessageResponse("Form Create Successfully.");
        } catch (Exception e) {
            throw new FormCreationException("Form Creation Failed: " + e.getMessage());
        }
    }

    //Generate the Code for Form
    private String generateRandomCode() {
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomCode = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }

    //Find All Form
    public List<FormDto> findAllForms(){
        List<Form> formList = formRepository.findAll();
        return formList.stream().map(this::convertToFormDto).collect(Collectors.toList());
    }

    //Find All FormWithQuestionsAndResponse
    public List<FormWithResponseDto> FormWithQuestionsAndResponse(){
        List<Form> formList = formRepository.findAll();
        return formList.stream().map(this::convertToFormWithQuestionsAndResponseDto).collect(Collectors.toList());
    }

    //Find Form By Code
    public List<FormRequest> getFormByCode(String code) {
        List<Form> forms = formRepository.findByCode(code);

        if (forms.isEmpty()) {
            throw new CodeNotFoundException("No Form found containing: " + code);
        }

        return forms.stream().map(this::convertToFormRequest).collect(Collectors.toList());
    }


    //Find Form with questions
    public Optional<FormDto> getFormByIdWithQuestions(Long id) {
        Optional<Form> formOptional = formRepository.findById(id);

        if (formOptional.isEmpty()) {
            throw new FormNotFoundException("No Form found with Id: " + id);
        }

        Form form = formOptional.get();
        List<QuestionsDto> questionsList = extractQuestionsWithId(form.getQuestionsList());

        // Use ModelMapper to map Form to FormWithQuestions
        FormDto formWithQuestions = modelMapper.map(form, FormDto.class);
        formWithQuestions.setQuestionsList(questionsList);

        return Optional.of(formWithQuestions);
    }

    //Find Form with questions and Response
    public Optional<FormWithResponseDto> getFormByIdWithQuestionsAndResponse(Long id) {
        Optional<Form> formOptional = formRepository.findById(id);

        if (formOptional.isEmpty()) {
            throw new FormNotFoundException("No Form found with Id: " + id);
        }

        Form form = formOptional.get();
        List<QuestionsWithResponseDto> questionsList = extractQuestionsWithResponse(form.getQuestionsList());

        // Use ModelMapper to map Form to FormWithQuestions

        FormWithResponseDto formWithQuestionsAndResponse = modelMapper.map(form, FormWithResponseDto.class);
        formWithQuestionsAndResponse.setQuestionsList(questionsList);

        return Optional.of(formWithQuestionsAndResponse);
    }

    //Delete Form
    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new FormNotFoundException("Form with ID " + id + " not found.");
        }
        formRepository.deleteById(id);
    }

    //Update Question
    public void updateForm(Long id, FormRequest formRequest) {
        Optional<Form> formOptional = formRepository.findById(id);

        if (formOptional.isEmpty()) {
            throw new FormNotFoundException("No Form found with Id: " + id);
        }

        validateQuestionIdsExistence(formRequest);

        Form existingForm = formOptional.get();

        // Update the existing form with the new form data
        modelMapper.map(formRequest, existingForm);

        // Update the existing questions with the new question data
        List<Questions> updatedQuestions = updateQuestions(existingForm.getQuestionsList(), formRequest.getQuestionsList());
        existingForm.setQuestionsList(updatedQuestions);

        // Save the updated form
        Form updatedForm = formRepository.save(existingForm);

        // Map the updated form to FormWithQuestions
        FormDto formWithQuestions = modelMapper.map(updatedForm, FormDto.class);
        formWithQuestions.setQuestionsList(extractQuestionsWithId(updatedForm.getQuestionsList()));
    }

    public void validateQuestionIdsExistence(FormRequest formRequest) {
        List<QuestionsRequest> questionsList = formRequest.getQuestionsList();

        for (QuestionsRequest questionRequest : questionsList) {
            Long questionId = questionRequest.getId();
            if (questionId != null && !questionsRepository.existsById(questionId)) {
                throw new QuestionNotFoundException("No Question found with ID: " + questionId);
            }
        }
    }
    private List<Questions> updateQuestions(List<Questions> existingQuestions, List<QuestionsRequest> updateQuestionRequests) {
        Set<Long> existingQuestionIds = existingQuestions.stream().map(Questions::getId).collect(Collectors.toSet());

        for (QuestionsRequest updateQuestionRequest : updateQuestionRequests) {
            if (!existingQuestionIds.contains(updateQuestionRequest.getId())) {
                throw new QuestionNotFoundException("Question not found with ID: " + updateQuestionRequest.getId());
            }
        }

        for (int i = 0; i < existingQuestions.size() && i < updateQuestionRequests.size(); i++) {
            Questions existingQuestion = existingQuestions.get(i);
            QuestionsRequest updateQuestionRequest = updateQuestionRequests.get(i);

            // Update the existing question with the new question data
            modelMapper.map(updateQuestionRequest, existingQuestion);
        }

        return existingQuestions;
    }

    //Mapping Question and QuestionDto
    private List<QuestionsDto> extractQuestionsWithId(List<Questions> questionsList) {
        return questionsList.stream()
                .map(question -> modelMapper.map(question, QuestionsDto.class))
                .collect(Collectors.toList());
    }

    private List<QuestionsWithResponseDto> extractQuestionsWithResponse(List<Questions> questionsList) {
        return questionsList.stream()
                .map(question -> modelMapper.map(question, QuestionsWithResponseDto.class))
                .collect(Collectors.toList());
    }

    //Convert Form to FormDto
    private FormDto convertToFormDto(Form form) {
        return modelMapper.map(form, FormDto.class);
    }

    //Convert Form to FormWithQuestionsAndResponseDto
    private FormWithResponseDto convertToFormWithQuestionsAndResponseDto(Form form) {
        return modelMapper.map(form, FormWithResponseDto.class);
    }

    //Convert Form to FormRequest
    private FormRequest convertToFormRequest(Form form) {
        return modelMapper.map(form, FormRequest.class);
    }

}
