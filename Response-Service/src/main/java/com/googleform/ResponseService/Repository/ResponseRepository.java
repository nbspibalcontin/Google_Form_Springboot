package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Respondents;
import com.googleform.ResponseService.Entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    boolean existsByRespondentsId(Long respondentsId);
    @Modifying
    @Query(value = "DELETE FROM response WHERE respondents_id = :respondentsId", nativeQuery = true)
    void deleteByResponseId(@Param("respondentsId") Long respondentsId);
    List<Response> findByRespondentsId(Long responseId);
}
