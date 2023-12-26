package com.googleform.ResponseService.Service;

import com.googleform.ResponseService.Dto.MessageResponse;
import com.googleform.ResponseService.Dto.ResponseDto;
import com.googleform.ResponseService.Entity.Questions;
import com.googleform.ResponseService.Entity.Response;
import com.googleform.ResponseService.Repository.QuestionsRepository;
import com.googleform.ResponseService.Repository.ResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class Response_Service {
    private final QuestionsRepository questionsRepository;
    private final ResponseRepository responseRepository;

    public Response_Service(QuestionsRepository questionsRepository, ResponseRepository responseRepository) {
        this.questionsRepository = questionsRepository;
        this.responseRepository = responseRepository;
    }


    public MessageResponse createResponse(ResponseDto responseDto) {
        // Assuming you already have the Questions object

        Questions existingQuestion = questionsRepository.findById(responseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Question not found with ID: " + responseDto.getId()));

        Response response = new Response();
        BeanUtils.copyProperties(responseDto, response, "id");
        response.setQuestions(existingQuestion);

        responseRepository.save(response);

        return new MessageResponse("Response saved successfully!");
    }


}