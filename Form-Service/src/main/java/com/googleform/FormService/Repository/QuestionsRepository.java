package com.googleform.FormService.Repository;

import com.googleform.FormService.Entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
