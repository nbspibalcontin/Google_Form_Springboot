package com.googleform.FormService.Service;

import com.googleform.FormService.Dto.FormDto;
import com.googleform.FormService.Dto.MessageResponse;
import com.googleform.FormService.Entity.Form;
import com.googleform.FormService.Entity.Questions;
import com.googleform.FormService.Exception.CodeNotFoundException;
import com.googleform.FormService.Exception.FormCreationException;
import com.googleform.FormService.Exception.FormNotFoundException;
import com.googleform.FormService.Repository.FormRepository;
import com.googleform.FormService.Repository.QuestionsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Create_Form {

    private final FormRepository formRepository;
    private final QuestionsRepository questionsRepository;

    private final ModelMapper modelMapper;

    public Create_Form(FormRepository formRepository, QuestionsRepository questionsRepository, ModelMapper modelMapper) {
        this.formRepository = formRepository;
        this.questionsRepository = questionsRepository;
        this.modelMapper = modelMapper;
    }

    //Create Form
    public MessageResponse create_Form(FormDto formDto) {
        try {
            String randomCode = generateRandomCode(15);

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
    private String generateRandomCode(int characterLimit) {
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomCode = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < characterLimit; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }

    //Find Form By Code
    public List<FormDto> getFormByCode(String code) {
        List<Form> forms = formRepository.findByCode(code);

        if (forms.isEmpty()) {
            throw new CodeNotFoundException("No Form found containing: " + code);
        }

        return forms.stream().map(this::convertToFormDto).collect(Collectors.toList());
    }

    //Convert Form to FormDto
    private FormDto convertToFormDto(Form form) {
        return modelMapper.map(form, FormDto.class);
    }

    //Delete Form
    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new FormNotFoundException("Form with ID " + id + " not found.");
        }
        formRepository.deleteById(id);
    }
}
