package com.googleform.ResponseService.Repository;

import com.googleform.ResponseService.Entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
}
