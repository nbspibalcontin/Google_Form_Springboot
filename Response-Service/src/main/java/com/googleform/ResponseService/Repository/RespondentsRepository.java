package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Respondents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespondentsRepository extends JpaRepository<Respondents, Long> {
}
