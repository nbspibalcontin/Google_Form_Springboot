package com.googleform.ResponseService.Service;

import com.googleform.ResponseService.Exception.RespondentNotFoundException;
import com.googleform.ResponseService.Repository.RespondentsRepository;
import com.googleform.ResponseService.Repository.ResponseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class Respondent_Service {
    private final RespondentsRepository respondentsRepository;
    private final ResponseRepository responseRepository;

    public Respondent_Service(RespondentsRepository respondentsRepository, ResponseRepository responseRepository) {
        this.respondentsRepository = respondentsRepository;
        this.responseRepository = responseRepository;
    }

    //Delete
    public void deleteRespondentsById(Long respondentId) {
        if (!responseRepository.existsByRespondentsId(respondentId)) {
            throw new RespondentNotFoundException("Respondent with ID " + respondentId + " not found.");
        }
        responseRepository.deleteByResponseId(respondentId);
        respondentsRepository.deleteByRespondentsId(respondentId);
    }

}
