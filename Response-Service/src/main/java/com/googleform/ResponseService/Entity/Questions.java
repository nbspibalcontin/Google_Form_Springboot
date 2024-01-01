package com.googleform.ResponseService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;
    @OneToMany(mappedBy = "questions", cascade = CascadeType.ALL)
    private List<Response> responses = new ArrayList<>();
}
