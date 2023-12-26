package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Questions,Long> {
}
