package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Respondents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespondentsRepository extends JpaRepository<Respondents, Long> {
   Optional<Respondents> findByEmail(String email);
}
