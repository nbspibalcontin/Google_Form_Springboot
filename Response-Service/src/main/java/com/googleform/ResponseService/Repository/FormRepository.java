package com.googleform.ResponseService.Repository;


import com.googleform.ResponseService.Entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {

}
