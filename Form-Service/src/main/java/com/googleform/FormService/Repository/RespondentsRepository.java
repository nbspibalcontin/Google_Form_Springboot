package com.googleform.FormService.Repository;


import com.googleform.FormService.Entity.Respondents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespondentsRepository extends JpaRepository<Respondents, Long> {
    Long countByFormId(Long formId);
}
