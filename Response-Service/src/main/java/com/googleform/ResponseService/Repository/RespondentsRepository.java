package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Respondents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RespondentsRepository extends JpaRepository<Respondents, Long> {
   Optional<Respondents> findByEmail(String email);
   boolean existsById(Long id);
   @Modifying
   @Query(value = "DELETE FROM respondents WHERE id = :respondentsId", nativeQuery = true)
   void deleteByRespondentsId(@Param("respondentsId") Long respondentsId);
}
