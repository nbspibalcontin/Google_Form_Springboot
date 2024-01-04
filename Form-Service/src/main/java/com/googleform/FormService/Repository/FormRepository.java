package com.googleform.FormService.Repository;

import com.googleform.FormService.Entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findFormsByCode(String code);
    Optional<Form> findFormByCode(String code);
}
